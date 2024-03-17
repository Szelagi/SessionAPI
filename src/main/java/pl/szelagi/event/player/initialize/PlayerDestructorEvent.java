package pl.szelagi.event.player.initialize;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.szelagi.event.player.initialize.listener.PlayerQuitListener;

import java.util.Collection;

public class PlayerDestructorEvent extends PlayerChangeEvent<PlayerQuitListener> {
	public PlayerDestructorEvent(@NotNull Player player, @NotNull Collection<Player> otherSessionPlayers, @NotNull Collection<Player> allSessionPlayers, @NotNull InvokeType invokeType) {
		super(PlayerQuitListener.class, player, otherSessionPlayers, allSessionPlayers, invokeType);
	}
}
