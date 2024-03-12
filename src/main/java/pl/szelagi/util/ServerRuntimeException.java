package pl.szelagi.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class ServerRuntimeException extends RuntimeException {
    private static void showSchematicError(String error) {
        String errorMessage = "§4[ServerRuntimeException] §f" + error;
        for (Player player : Bukkit.getServer().getOnlinePlayers())
            if (player.isOp())
                player.sendMessage(errorMessage);
        Bukkit.getServer().getConsoleSender().sendMessage(errorMessage);
    }
    public ServerRuntimeException(String name) {
        super(name);
        var t = Arrays.stream(this.getStackTrace()).toList();
        var first = t.get(0);
        showSchematicError(this.getClass().getSimpleName() + ": §c§n" + this.getMessage() + "§7 (" + first + ")");
    }
}