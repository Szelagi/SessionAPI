package pl.szelagi.buildin.system.sessionsafecontrolplayers;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import pl.szelagi.bukkitadapted.LocationAdapted;
import pl.szelagi.state.PlayerState;

import java.io.Serializable;

public class SessionSafeControlPlayerState extends PlayerState implements Serializable {
    private GameMode gameMode;
    private LocationAdapted locationAdapted;

    public SessionSafeControlPlayerState(Player player) {
        super(player);
    }

    public void save() {
        this.gameMode = getPlayer().getGameMode();
        this.locationAdapted = new LocationAdapted(getPlayer().getLocation());
    }

    public void load(Player player) {
        player.setGameMode(gameMode);
        player.teleport(locationAdapted.getLocation());
    }

}
