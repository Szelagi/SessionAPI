package pl.szelagi.state;

import org.bukkit.entity.Player;

public abstract class PlayerState {
    private transient final Player player;
    public PlayerState(Player player) {
        this.player = player;
    }
    public Player getPlayer() {
        return player;
    }

}
