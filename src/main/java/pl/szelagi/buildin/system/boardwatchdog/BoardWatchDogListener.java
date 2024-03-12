package pl.szelagi.buildin.system.boardwatchdog;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import pl.szelagi.manager.ControllerManager;
import pl.szelagi.manager.SessionManager;
import pl.szelagi.util.timespigot.Time;


public class BoardWatchDogListener implements Listener {
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        var session = SessionManager.getSession(event.getPlayer());
        if (session == null) return;
        BoardWatchDogController controller = ControllerManager.getFirstController(session, c -> c instanceof BoardWatchDogController);
        if (controller == null) return;
        controller.getProcess().runControlledTaskLater(controller::stopWhenExitSpace, Time.Ticks(1));
    }
}
