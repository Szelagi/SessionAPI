package pl.szelagi.event.player.initialize;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.szelagi.event.BaseEvent;
import pl.szelagi.event.EventListener;

import java.util.Collection;

public class PlayerChangeEvent<T extends EventListener> extends BaseEvent<T> {
	private final @NotNull Player player;
	private final @NotNull Collection<Player> otherSessionPlayers;
	private final @NotNull Collection<Player> allSessionPlayers;
	private final @NotNull InvokeType invokeType;

	public PlayerChangeEvent(Class<T> listenerClass, @NotNull Player player, @NotNull Collection<Player> otherSessionPlayers, @NotNull Collection<Player> allSessionPlayers, @NotNull InvokeType invokeType) {
		super(listenerClass);
		this.player = player;
		this.otherSessionPlayers = otherSessionPlayers;
		this.allSessionPlayers = allSessionPlayers;
		this.invokeType = invokeType;
	}

	public @NotNull Player getPlayer() {
		return player;
	}

	public @NotNull Collection<Player> getOtherSessionPlayers() {
		return otherSessionPlayers;
	}

	public @NotNull Collection<Player> getAllSessionPlayers() {
		return allSessionPlayers;
	}

	public @NotNull InvokeType getInvokeType() {
		return invokeType;
	}
}
