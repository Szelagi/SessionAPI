package pl.szelagi.component.session.exception.player.initialize;

import org.bukkit.entity.Player;

public class PlayerIsNotAliveException extends PlayerInitializeException {
    public PlayerIsNotAliveException() {
        super("player in not alive");
    }

    public static void check(Player p) throws PlayerIsNotAliveException{
        if (p.getHealth() <= 0) throw new PlayerIsNotAliveException();
    }
}