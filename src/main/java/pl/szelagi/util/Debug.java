package pl.szelagi.util;

import org.bukkit.Bukkit;
import pl.szelagi.component.ISessionComponent;
import pl.szelagi.component.board.Board;
import pl.szelagi.component.controller.Controller;
import pl.szelagi.component.session.Session;

public class Debug {
    public static void send(String message) {
        for (var p : Bukkit.getServer().getOnlinePlayers()) {
            if (!p.isOp()) continue;
            p.sendMessage("§8D: §f" + message);
        }
    }

    public static void send(ISessionComponent component, String message) {
        String prefix = "";
        if (component instanceof Controller) prefix = "§3[C]";
        if (component instanceof Board) prefix = "§5[B]";
        if (component instanceof Session) prefix = "§6[S]";
        send(prefix + " §n" + component.getName() + "§f "  + message);
    }
}
