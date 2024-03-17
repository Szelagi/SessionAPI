package pl.szelagi.event.component;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.szelagi.component.BaseComponent;
import pl.szelagi.event.EventListener;
import pl.szelagi.event.component.listener.ComponentDestructorListener;

import java.util.Collection;

public class ComponentDestructorEvent extends ComponentChangeEvent {
	public ComponentDestructorEvent(@Nullable BaseComponent parent, @NotNull Collection<Player> currentPlayers) {
		super(parent, currentPlayers);
	}

	@Override
	public Class<? extends EventListener> getListenerClazz() {
		return ComponentDestructorListener.class;
	}
}