package pl.szelagi.state;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.HashMap;

public class PlayerStateContainer<T extends PlayerState> implements Serializable {
    private final InstanceCreator<T> creator;
    private final HashMap<Player, T> map = new HashMap<>();

    public PlayerStateContainer(InstanceCreator<T> creator) {
        this.creator = creator;
    }

    @NotNull
    public T get(Player player) {
        T record = map.get(player);
        if (record == null) {
            record = creator.get(player);
            map.put(player, record);
        };
        return record;
    }

    public void remove(Player player) {
        map.remove(player);
    }


}
