package pl.szelagi.event.player.recovery;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.szelagi.component.constructor.PlayerDestructorLambdas;
import pl.szelagi.event.BaseEvent;

public class PlayerRecoveryEvent extends BaseEvent {
	private final @NotNull Player forPlayer;
	private final @NotNull PlayerDestructorLambdas lambdas = new PlayerDestructorLambdas();

	public PlayerRecoveryEvent(@NotNull Player forPlayer) {
		this.forPlayer = forPlayer;
	}

	public @NotNull Player getForPlayer() {
		return forPlayer;
	}

	public @NotNull PlayerDestructorLambdas getLambdas() {
		return lambdas;
	}
}
