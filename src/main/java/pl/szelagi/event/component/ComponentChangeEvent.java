package pl.szelagi.event.component;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.szelagi.component.BaseComponent;
import pl.szelagi.event.BaseEvent;

import java.util.Collection;

public abstract class ComponentChangeEvent extends BaseEvent {
	private final @NotNull BaseComponent component;
	private final @Nullable BaseComponent parentComponent;
	private final @NotNull Collection<Player> currentPlayers;

	public ComponentChangeEvent(@NotNull BaseComponent component, @NotNull Collection<Player> currentPlayers) {
		this.component = component;
		if (component.getParentProcess() != null) {
			this.parentComponent = component
					.getParentProcess()
					.getComponent();
		} else {
			this.parentComponent = null;
		}
		this.currentPlayers = currentPlayers;
	}

	public @NotNull BaseComponent getComponent() {
		return component;
	}

	public @Nullable BaseComponent getParentComponent() {
		return parentComponent;
	}

	public @NotNull Collection<Player> getCurrentPlayers() {
		return currentPlayers;
	}
}
