package pl.szelagi.event.component;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.szelagi.component.BaseComponent;
import pl.szelagi.event.component.listener.ComponentDestructorListener;

import java.util.Collection;

public class ComponentDestructorEvent extends ComponentChangeEvent<ComponentDestructorListener> {
	public ComponentDestructorEvent(@Nullable BaseComponent parent, @NotNull Collection<Player> currentPlayers) {
		super(ComponentDestructorListener.class, parent, currentPlayers);
	}
}