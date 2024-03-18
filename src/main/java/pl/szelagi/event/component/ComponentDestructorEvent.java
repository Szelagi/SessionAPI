package pl.szelagi.event.component;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.szelagi.component.BaseComponent;

import java.util.Collection;

public class ComponentDestructorEvent extends ComponentChangeEvent {
	public ComponentDestructorEvent(@NotNull BaseComponent component, @NotNull Collection<Player> currentPlayers) {
		super(component, currentPlayers);
	}
}