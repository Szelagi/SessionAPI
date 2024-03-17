package pl.szelagi.event.player.initialize;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.szelagi.event.EventListener;
import pl.szelagi.event.component.listener.ComponentDestructorListener;

import java.util.Collection;

public class PlayerDestructorEvent extends PlayerChangeEvent {
	public PlayerDestructorEvent(@NotNull Player player, @NotNull Collection<Player> otherSessionPlayers, @NotNull Collection<Player> allSessionPlayers, @NotNull InvokeType invokeType) {
		super(player, otherSessionPlayers, allSessionPlayers, invokeType);
	}

	@Override
	public Class<? extends EventListener> getListenerClazz() {
		return ComponentDestructorListener.class;
	}
}
