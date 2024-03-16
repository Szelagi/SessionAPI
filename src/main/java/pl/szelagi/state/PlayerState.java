package pl.szelagi.state;

import org.bukkit.entity.Player;

import java.io.Serializable;

public abstract class PlayerState implements Serializable {
    private transient final Player player;
    public PlayerState(Player player) {
        this.player = player;
    }
    public Player getPlayer() {
        return player;
    }

}
