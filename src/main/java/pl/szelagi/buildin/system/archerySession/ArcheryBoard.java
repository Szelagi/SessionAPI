package pl.szelagi.buildin.system.archerySession;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.WeatherType;
import org.bukkit.entity.Skeleton;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import pl.szelagi.buildin.controller.BarTimerController;
import pl.szelagi.buildin.controller.OtherGameMode.OtherGameMode;
import pl.szelagi.buildin.controller.entity.SpawnEntity;
import pl.szelagi.buildin.controller.entity.Wave;
import pl.szelagi.buildin.controller.environment.CustomPlayerEnvironment;
import pl.szelagi.buildin.controller.environment.TimeType;
import pl.szelagi.buildin.controller.environment.WorldEnvironment;
import pl.szelagi.buildin.controller.interaction.NoPlaceBreakExcept;
import pl.szelagi.buildin.controller.interaction.NoPvP;
import pl.szelagi.buildin.controller.interaction.ProtectItemFrame;
import pl.szelagi.buildin.controller.life.Life;
import pl.szelagi.buildin.lobby.Lobby;
import pl.szelagi.component.ComponentStatus;
import pl.szelagi.component.board.Board;
import pl.szelagi.component.session.Session;
import pl.szelagi.component.session.cause.WinCause;
import pl.szelagi.event.component.ComponentConstructorEvent;
import pl.szelagi.util.timespigot.Time;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ArcheryBoard extends Board {
	private Lobby lobby;
	private Location lobbyLocation;
	private Location spawnLocation;
	private List<Location> mobLocations;
	private int round = 0;
	private BarTimerController barTimer;
	private BarTimerController breakTimer;
	private Wave wave;
	private Life life;

	public ArcheryBoard(Session session) {
		super(session);
	}

	@Override
	public void componentConstructor(ComponentConstructorEvent event) {
		super.componentConstructor(event);

		lobbyLocation = getTagResolve()
				.query("lobby")
				.getFirstLocation();

		spawnLocation = getTagResolve()
				.query("spawn")
				.getFirstLocation();

		mobLocations = getTagResolve()
				.query("mob").toLocations();

		this.lobby = new Lobby(this, Time.Seconds(15), lobbyLocation, 10, 1);
		this.lobby.getFinalizeEvent()
		          .bind(this::gameStart);
		this.lobby.start();

		life = new Life(this, 5, Time.Seconds(30));
		life.start();

		new CustomPlayerEnvironment(this, WeatherType.CLEAR, TimeType.MIDNIGHT).start();
		new NoPlaceBreakExcept(this)
				.setBreakFlag(Material.FIRE, true)
				.setBreakFlag(Material.COBWEB, true)
				.setPlaceFlag(Material.COBWEB, true)
				.setBreakFlag(Material.GRASS, true)
				.setBreakFlag(Material.TALL_GRASS, true)
				.start();
		new ProtectItemFrame(this).start();
		new WorldEnvironment(this)
				.setBlockBurn(false)
				.setFireSpread(false)
				.setExplosionDestroy(false)
				.start();
		new NoPvP(this).start();
		new OtherGameMode(this, GameMode.SURVIVAL).start();
	}

	private void gameStart() {
		for (var player : getSession().getPlayers()) {
			player.teleport(spawnLocation);

			var eq = player.getInventory();

			eq.addItem(new ItemStack(Material.IRON_SWORD));
			eq.addItem(new ItemStack(Material.CROSSBOW));
			eq.addItem(new ItemStack(Material.ARROW, 12));
			eq.addItem(new ItemStack(Material.PUMPKIN_PIE, 12));
		}
		roundStart();
	}

	private void roundStart() {
		barTimer = new BarTimerController(this, roundTime(round), "Runda " + (round + 1) + " -- pozostały czas %.2fs", this::gameEnd);
		barTimer.start();

		var sound = Sound.sound(Key.key("ambient.cave"), Sound.Source.AMBIENT, 0.5f, 0.8f);
		getSession().getPlayers()
		            .forEach(player -> {
			            var a = Audience.audience(player);
			            a.playSound(sound);
		            });

		wave = new Wave(this, mobLocations, generate(round));
		wave.finish.bind(this::waitStart);
		wave.start();
	}

	private void waitStart() {
		life.respawnAll();

		var sound = Sound.sound(Key.key("entity.experience_orb.pickup"), Sound.Source.AMBIENT, 1f, 1f);
		getSession().getPlayers()
		            .forEach(player -> {
			            var a = Audience.audience(player);
			            a.playSound(sound);
			            player.sendMessage("§aRound WIN!");
			            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 8 * 20, 1));
			            player.removePotionEffect(PotionEffectType.POISON);
			            player.removePotionEffect(PotionEffectType.SLOW);
			            player.removePotionEffect(PotionEffectType.WEAKNESS);
			            player.setFireTicks(0);

			            var bucket = new ItemStack(Material.BUCKET);
			            var milk = new ItemStack(Material.MILK_BUCKET);
			            var eq = player.getInventory();
			            while (eq.containsAtLeast(bucket, 1)) {
				            eq.removeItem(bucket);
				            eq.addItem(milk);
			            }
		            });

		if (barTimer != null && barTimer.status() == ComponentStatus.RUNNING)
			barTimer.stop();
		round++;

		var moreTime = round % 8 == 7;
		var time = moreTime ? Time.Seconds(60) : Time.Seconds(8);

		breakTimer = new BarTimerController(this, time, "Przerwa %.2fs", this::roundStart);
		breakTimer.start();
	}

	private void gameEnd() {
		getSession().stop(new WinCause("xd"));
	}

	private static Consumer<Skeleton> generator = (skeleton -> {
		final var variants = 2;
		var random = (int) (Math.random() * variants);
		switch (random) {
			case 0: {
				var eq = skeleton.getEquipment();
				eq.setItemInMainHand(new ItemStack(Material.IRON_SWORD));
				break;
			}
			case 1: {
				break;
			}
		}
	});

	private static int mobCount(int level) {
		return (int) Math.floor(level / 3d) + 1;
	}

	private static Time roundTime(int level) {
		var mobs = mobCount(level);
		return Time.Seconds(20 + mobs * 2);
	}

	private static List<SpawnEntity<?>> generate(int level) {
		var count = mobCount(level);
		var mobs = new ArrayList<SpawnEntity<?>>();
		for (int i = 0; i < count; i++) {
			mobs.add(SpawnEntity.make(Skeleton.class, generator));
		}
		return mobs;
	}

	@Override
	public @NotNull String getName() {
		return "SystemArcheryBoard";
	}
}
