package pl.szelagi.manager;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import pl.szelagi.component.session.Session;
import pl.szelagi.component.session.event.SessionStartEvent;
import pl.szelagi.component.session.event.SessionStopEvent;
import pl.szelagi.manager.compare.CompareSession;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;

public class SessionManager {
    private static final HashMap<Player, Session> PLAYER_SESSION_HASH_MAP = new HashMap<>();
    public static void addRelation(Player p, Session d) {
        PLAYER_SESSION_HASH_MAP.put(p, d);
    }
    public static void removeRelation(Player p) {
        PLAYER_SESSION_HASH_MAP.remove(p);
    }
    public static boolean isDuringDungeon(Player p) {
        return PLAYER_SESSION_HASH_MAP.containsKey(p);
    }
    @Nullable
    public static Session getSession(Player p) {
        return PLAYER_SESSION_HASH_MAP.get(p);
    }
    @Nullable
    public static Session getSession(Player p, CompareSession c) {
        var res = PLAYER_SESSION_HASH_MAP.get(p);
        if (res == null) return null;
        if (!c.compare(res)) return null;
        return res;
    }

    @NotNull
    public static ArrayList<Session> getSessions() {
        return new ArrayList<>(PLAYER_SESSION_HASH_MAP.values());
    }

    public static void initialize(JavaPlugin p) {
        class ManagerListener implements Listener {
            @EventHandler(ignoreCancelled = true)
            public void onDungeonStart(SessionStartEvent event) {

            }

            @EventHandler(ignoreCancelled = true)
            public void onDungeonStop(SessionStopEvent event) {

            }

        }

        p.getServer().getPluginManager().registerEvents(new ManagerListener(), p);
    }
}
