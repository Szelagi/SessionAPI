package pl.szelagi.event.player.initialize;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class PlayerConstructorEvent extends PlayerChangeEvent {
	public PlayerConstructorEvent(@NotNull Player player, @NotNull Collection<Player> otherSessionPlayers, @NotNull Collection<Player> allSessionPlayers, @NotNull InvokeType invokeType) {
		super(player, otherSessionPlayers, allSessionPlayers, invokeType);
	}
}
