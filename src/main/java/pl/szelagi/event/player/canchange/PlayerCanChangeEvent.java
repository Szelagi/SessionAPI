package pl.szelagi.event.player.canchange;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.szelagi.cancelable.CancelCause;
import pl.szelagi.cancelable.CancelNotCancelableException;
import pl.szelagi.cancelable.Cancelable;
import pl.szelagi.event.BaseEvent;
import pl.szelagi.event.EventListener;

import java.util.Collection;

public abstract class PlayerCanChangeEvent<T extends EventListener> extends BaseEvent<T> implements Cancelable {
	private final @NotNull Player player;
	private final @NotNull Collection<Player> currentPlayers;
	private final @NotNull Cancelable cancelable;
	private @Nullable CancelCause cancelCause;
	private boolean isCanceled;

	public PlayerCanChangeEvent(@NotNull Class<T> listenerClass, @NotNull Player player, @NotNull Collection<Player> currentPlayers, @NotNull Cancelable cancelable, @Nullable CancelCause cancelCause, boolean isCanceled) {
		super(listenerClass);
		this.player = player;
		this.currentPlayers = currentPlayers;
		this.cancelable = cancelable;
		this.cancelCause = cancelCause;
		this.isCanceled = isCanceled;
	}

	public @NotNull Player getPlayer() {
		return player;
	}

	public @NotNull Collection<Player> getCurrentPlayers() {
		return currentPlayers;
	}

	public @Nullable CancelCause getCancelCause() {
		return cancelCause;
	}

	@Override
	public void setCanceled(CancelCause cause) throws CancelNotCancelableException {
		if (!isCancelable())
			throw new CancelNotCancelableException("");
		isCanceled = true;
		cancelCause = cause;
	}

	public boolean isCanceled() {
		return isCanceled;
	}

	@Override
	public boolean isCancelable() {
		return cancelable.isCancelable();
	}
}
