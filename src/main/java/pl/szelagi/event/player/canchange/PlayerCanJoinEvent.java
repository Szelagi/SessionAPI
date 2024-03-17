package pl.szelagi.event.player.canchange;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.szelagi.cancelable.CancelCause;
import pl.szelagi.cancelable.Cancelable;
import pl.szelagi.event.player.canchange.listener.PlayerCanJoinListener;
import pl.szelagi.event.player.canchange.type.JoinType;

import java.util.Collection;

public class PlayerCanJoinEvent extends PlayerCanChangeEvent<PlayerCanJoinListener> {
	private final JoinType type;

	public PlayerCanJoinEvent(@NotNull Player player, @NotNull Collection<Player> currentPlayers, @NotNull Cancelable cancelable, @Nullable CancelCause cancelCause, boolean isCanceled, JoinType type) {
		super(PlayerCanJoinListener.class, player, currentPlayers, cancelable, cancelCause, isCanceled);
		this.type = type;
	}

	public JoinType getType() {
		return type;
	}
}
