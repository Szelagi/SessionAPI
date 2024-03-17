package pl.szelagi.event.component;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.szelagi.component.BaseComponent;
import pl.szelagi.event.component.listener.ComponentConstructorListener;

import java.util.Collection;

public class ComponentConstructorEvent extends ComponentChangeEvent<ComponentConstructorListener> {
	public ComponentConstructorEvent(@Nullable BaseComponent parent, @NotNull Collection<Player> currentPlayers) {
		super(ComponentConstructorListener.class, parent, currentPlayers);
	}
}
