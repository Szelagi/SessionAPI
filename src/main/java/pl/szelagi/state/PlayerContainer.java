package pl.szelagi.state;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.function.Function;

public class PlayerContainer<T extends PlayerState> extends Container<Player, T> implements Serializable {
	public PlayerContainer(@NotNull Function<Player, T> creator) {
		super(creator);
	}
}
