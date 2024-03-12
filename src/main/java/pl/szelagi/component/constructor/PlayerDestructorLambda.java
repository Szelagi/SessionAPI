package pl.szelagi.component.constructor;

import org.bukkit.entity.Player;

import java.io.Serializable;

public interface PlayerDestructorLambda extends Serializable {
    void run(Player player, UninitializedType type);
}
