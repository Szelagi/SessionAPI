package pl.szelagi.buildin.system.sessionwatchdog;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import pl.szelagi.component.session.cause.ExceptionCause;
import pl.szelagi.manager.ControllerManager;
import pl.szelagi.manager.SessionManager;
import pl.szelagi.util.timespigot.Time;
import pl.szelagi.world.SessionWorldManager;

public class SessionWatchDogListener implements Listener {
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {

        var session = SessionManager.getSession(event.getPlayer());
        if (session == null) return;
        SessionWatchDogController controller = ControllerManager.getFirstController(session, c -> c instanceof SessionWatchDogController);
        if (controller == null) return;

        controller.getProcess().runControlledTaskLater(() -> {
            if (event.getPlayer().getWorld().getName().equals(SessionWorldManager.getSessionWorld().getName())) return;
            controller.getSession().stop(new ExceptionCause("player illegal teleportation " + event.getPlayer().getWorld().getName() + " must be in " + SessionWorldManager.getSessionWorld().getName()));
        }, Time.Ticks(10));
    }
}
