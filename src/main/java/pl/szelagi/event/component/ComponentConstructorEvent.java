package pl.szelagi.event.component;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.szelagi.component.BaseComponent;

import java.util.List;

public class ComponentConstructorEvent extends ComponentChangeEvent {
	public ComponentConstructorEvent(@NotNull BaseComponent component, @NotNull List<Player> currentPlayers) {
		super(component, currentPlayers);
	}
}
