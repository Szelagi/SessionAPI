package pl.szelagi.buildin.lobby;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Location;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.Nullable;
import pl.szelagi.cancelable.CancelCause;
import pl.szelagi.component.ISessionComponent;
import pl.szelagi.component.controller.Controller;
import pl.szelagi.event.component.ComponentConstructorEvent;
import pl.szelagi.event.player.canchange.PlayerCanJoinEvent;
import pl.szelagi.event.player.initialize.PlayerConstructorEvent;
import pl.szelagi.event.player.initialize.PlayerDestructorEvent;
import pl.szelagi.util.event.MultiParamEvent;
import pl.szelagi.util.timespigot.Time;

public class Lobby extends Controller {
	private static final Sound BREAK_SOUND = Sound.sound(Key.key("block" + ".note_block.bit"), Sound.Source.AMBIENT, 1f, .5f);
	private boolean isLobby = true;
	private final Location lobby;
	private final int maxSlots;
	private final int minSlots;
	private final Time waitTime;
	private final MultiParamEvent<Runnable> finalizeEvent = new MultiParamEvent<>();
	private MessageTimer messageTimer;

	public Lobby(ISessionComponent sessionComponent, Time waitTime, Location lobby, int maxSlots, int minSlots) {
		super(sessionComponent);
		this.lobby = lobby;
		this.maxSlots = maxSlots;
		this.minSlots = minSlots;
		this.waitTime = waitTime;
	}

	private void registerMessage(int timeBefore, char color) {
		String plural = timeBefore <= 1 ? "" : "s";
		var message = "§eThe game starts in §" + color + timeBefore + " §esecond" + plural + "!";
		messageTimer.registerMessageIfSecNotBusy(Time.Seconds(timeBefore), message);
	}

	@Override
	public void componentConstructor(ComponentConstructorEvent event) {
		super.componentConstructor(event);
		messageTimer = new MessageTimer(this, this.waitTime);

		// initialize messages
		registerMessage(waitTime.toSeconds(), 'e');
		registerMessage(30, '6');
		registerMessage(20, '6');
		registerMessage(10, '6');
		for (int i = 1; i <= 5; i++)
			registerMessage(i, 'c');

		messageTimer.setBreakCountMessage("§cWe don't have enough players! Start cancelled.");
		messageTimer.getFinalizeEvent()
		            .bind(this::lobbyFinalize);
	}

	@Override
	public void playerCanJoin(PlayerCanJoinEvent event) {
		super.playerCanJoin(event);
		if (maxSlots > event.getCurrentPlayers()
		                    .size())
			return;
		event.setCanceled(new CancelCause(this, "lobby is full"));
	}

	@Override
	public void playerConstructor(PlayerConstructorEvent event) {
		super.playerConstructor(event);
		var player = event.getPlayer();
		if (!isLobby)
			return;
		player.teleport(lobby);
		broadcast("§e" + player.getName() + " has joined " + playerStateMessage() + "!");
		if (!isCounting() && getSession().getPlayerCount() >= minSlots)
			startCountDown();
	}

	@Override
	public void playerDestructor(PlayerDestructorEvent event) {
		super.playerDestructor(event);
		var player = event.getPlayer();
		if (!isLobby)
			return;
		for (var p : getSession().getPlayers())
			p.sendMessage("§e" + player.getName() + " has left " + playerStateMessage() + "!");

		if (isCounting() && getSession().getPlayerCount() < minSlots)
			stopCountDown();
	}

	private String playerStateMessage() {
		return "§e(§b" + getSession().getPlayerCount() + "§e/§b" + maxSlots + "§e)";
	}

	public boolean isLobby() {
		return isLobby;
	}

	public void startCountDown() {
		if (isCounting())
			return;
		messageTimer.start();
	}

	public void stopCountDown() {
		if (!isCounting())
			return;
		messageTimer.stop();
		broadcastSound(BREAK_SOUND);
	}

	public boolean isCounting() {
		return messageTimer.isCounting();
	}

	public MultiParamEvent<Runnable> getFinalizeEvent() {
		return messageTimer.getFinalizeEvent();
	}

	private void broadcast(String message) {
		for (var player : getSession().getPlayers())
			player.sendMessage(message);
	}

	@Nullable
	@Override
	public Listener getListener() {
		return new LobbyListener();
	}

	private void lobbyFinalize() {
		isLobby = false;
		finalizeEvent.call(Runnable::run);
	}

	private void broadcastSound(Sound sound) {
		for (var player : getSession().getPlayers())
			Audience.audience(player)
			        .playSound(sound);
	}
}
