package pl.szelagi.state;

import org.bukkit.entity.Player;

public interface InstanceCreator<T> {
    T get(Player player);
}