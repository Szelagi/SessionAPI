package pl.szelagi.buildin.system.sessionsafecontrolplayers;

import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import pl.szelagi.component.ISessionComponent;
import pl.szelagi.component.controller.Controller;
import pl.szelagi.event.player.initialize.PlayerConstructorEvent;
import pl.szelagi.event.player.initialize.PlayerDestructorEvent;
import pl.szelagi.event.player.recovery.PlayerRecoveryEvent;
import pl.szelagi.state.PlayerContainer;

import java.util.ArrayList;
import java.util.List;

public class SessionSafeControlPlayers extends Controller {
	private static final ArrayList<PotionEffectType> NEGATIVE_POTION_TYPES = new ArrayList<>(List.of(PotionEffectType.POISON, PotionEffectType.WITHER, PotionEffectType.WEAKNESS, PotionEffectType.DARKNESS, PotionEffectType.BLINDNESS, PotionEffectType.SLOW, PotionEffectType.SLOW_DIGGING, PotionEffectType.LEVITATION, PotionEffectType.CONFUSION, PotionEffectType.BAD_OMEN, PotionEffectType.HUNGER, PotionEffectType.GLOWING));
	private final PlayerContainer<SessionSafeControlPlayerState> stateContainer = new PlayerContainer<>(SessionSafeControlPlayerState::new);

	public SessionSafeControlPlayers(ISessionComponent sessionComponent) {
		super(sessionComponent);
	}

	@Override
	public void playerConstructor(PlayerConstructorEvent event) {
		super.playerConstructor(event);
		stateContainer.get(event.getPlayer())
		              .save();
	}

	@Override
	public void playerDestructor(PlayerDestructorEvent event) {
		super.playerDestructor(event);
		var player = event.getPlayer();
		var state = stateContainer.get(player);
		player.setFallDistance(0);
		player.setFireTicks(0);
		for (var potionType : NEGATIVE_POTION_TYPES)
			player.removePotionEffect(potionType);
		state.load(player);
	}

	@Override
	public void playerDestructorRecovery(PlayerRecoveryEvent event) {
		super.playerDestructorRecovery(event);
		var state = stateContainer.get(event.getForPlayer());
		event.getLambdas().add(player -> {
			player.setFallDistance(0);
			player.setFireTicks(0);
			for (var potionType : NEGATIVE_POTION_TYPES)
				player.removePotionEffect(potionType);
			state.load(player);
		});
	}

	@Override
	public @NotNull String getName() {
		return "SystemSafeControlController";
	}
}
