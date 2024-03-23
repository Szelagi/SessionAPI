package pl.szelagi.event.player.canchange;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.szelagi.event.player.canchange.type.JoinType;

import java.util.List;

public class PlayerCanJoinEvent extends PlayerCanChangeEvent {
	private final JoinType type;

	public PlayerCanJoinEvent(@NotNull Player player, @NotNull List<Player> currentPlayers, JoinType type) {
		super(player, currentPlayers, type);
		this.type = type;
	}

	public JoinType getType() {
		return type;
	}
}
