package pl.szelagi.event.player.initialize;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.szelagi.event.player.initialize.listener.PlayerJoinListener;

import java.util.Collection;

public class PlayerConstructorEvent extends PlayerChangeEvent<PlayerJoinListener> {
	public PlayerConstructorEvent(@NotNull Player player, @NotNull Collection<Player> otherSessionPlayers, @NotNull Collection<Player> allSessionPlayers, @NotNull InvokeType invokeType) {
		super(PlayerJoinListener.class, player, otherSessionPlayers, allSessionPlayers, invokeType);
	}
}
