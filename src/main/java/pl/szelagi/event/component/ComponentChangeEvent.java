package pl.szelagi.event.component;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.szelagi.component.BaseComponent;
import pl.szelagi.event.BaseEvent;
import pl.szelagi.event.EventListener;

import java.util.Collection;

public abstract class ComponentChangeEvent<T extends EventListener> extends BaseEvent<T> {
	private final @Nullable BaseComponent parent;
	private final @NotNull Collection<Player> currentPlayers;

	public ComponentChangeEvent(Class<T> listenerClass, @Nullable BaseComponent parent, @NotNull Collection<Player> currentPlayers) {
		super(listenerClass);
		this.parent = parent;
		this.currentPlayers = currentPlayers;
	}

	public @Nullable BaseComponent getParent() {
		return parent;
	}

	public @NotNull Collection<Player> getCurrentPlayers() {
		return currentPlayers;
	}
}
