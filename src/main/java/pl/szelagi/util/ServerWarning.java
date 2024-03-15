package pl.szelagi.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ServerWarning {
    private static void showWaring(String error) {
        String errorMessage = "§6[Warning] §f" + error;
        for (Player player : Bukkit.getServer().getOnlinePlayers())
            if (player.isOp())
                player.sendMessage(errorMessage);
        Bukkit.getServer().getConsoleSender().sendMessage(errorMessage);
    }
    public ServerWarning(String message) {
        showWaring(message);
    }
}