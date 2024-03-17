package pl.szelagi.state;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface InstanceCreator<T> {
	@NotNull T get(Player player);
}