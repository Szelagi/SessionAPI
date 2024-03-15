package pl.szelagi.state.manual;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.szelagi.state.InstanceCreator;
import pl.szelagi.state.PlayerState;

import java.util.HashMap;

public class PlayerManualContainer<T extends PlayerState> {
    private final HashMap<Player, T> map = new HashMap<>();

    @NotNull
    public T create(@NotNull Player player, @NotNull InstanceCreator<T> creator) throws ManualContainerException {
        if (map.containsKey(player)) throw new ManualContainerException("player " + player + " multi initialization");
        map.put(player, creator.get(player));
        return get(player);
    }
    @NotNull
    public T get(@NotNull Player player) throws ManualContainerException {
        var record = map.get(player);
        if (record == null) throw new ManualContainerException("player " + player + " is not initialized");
        return record;
    }

    @NotNull
    public T remove(@NotNull Player player) throws ManualContainerException {
        var record = map.remove(player);
        if (record == null) throw new ManualContainerException("remove not exists player " + player);
        return record;
    }

    public boolean isExists(@NotNull Player player) {
        return map.containsKey(player);
    }

}
