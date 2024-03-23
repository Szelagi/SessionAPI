package pl.szelagi.event.player.canchange;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.szelagi.event.player.canchange.type.QuitType;

import java.util.List;

public class PlayerCanQuitEvent extends PlayerCanChangeEvent {
	private final QuitType type;

	public PlayerCanQuitEvent(@NotNull Player player, @NotNull List<Player> currentPlayers, QuitType type) {
		super(player, currentPlayers, type);
		this.type = type;
	}

	public QuitType getType() {
		return type;
	}
}
