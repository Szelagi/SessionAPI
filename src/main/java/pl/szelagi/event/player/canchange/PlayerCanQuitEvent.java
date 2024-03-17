package pl.szelagi.event.player.canchange;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.szelagi.cancelable.CancelCause;
import pl.szelagi.cancelable.Cancelable;
import pl.szelagi.event.player.canchange.listener.PlayerCanQuitListener;
import pl.szelagi.event.player.canchange.type.QuitType;

import java.util.Collection;

public class PlayerCanQuitEvent extends PlayerCanChangeEvent<PlayerCanQuitListener> {
	private final QuitType type;

	public PlayerCanQuitEvent(@NotNull Player player, @NotNull Collection<Player> currentPlayers, @NotNull Cancelable cancelable, @Nullable CancelCause cancelCause, boolean isCanceled, QuitType type) {
		super(PlayerCanQuitListener.class, player, currentPlayers, cancelable, cancelCause, isCanceled);
		this.type = type;
	}

	public QuitType getType() {
		return type;
	}
}
