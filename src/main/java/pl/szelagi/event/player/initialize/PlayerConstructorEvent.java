package pl.szelagi.event.player.initialize;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.szelagi.event.EventListener;
import pl.szelagi.event.component.listener.ComponentConstructorListener;

import java.util.Collection;

public class PlayerConstructorEvent extends PlayerChangeEvent {
	public PlayerConstructorEvent(@NotNull Player player, @NotNull Collection<Player> otherSessionPlayers, @NotNull Collection<Player> allSessionPlayers, @NotNull InvokeType invokeType) {
		super(player, otherSessionPlayers, allSessionPlayers, invokeType);
	}

	@Override
	public Class<? extends EventListener> getListenerClazz() {
		return ComponentConstructorListener.class;
	}
}
