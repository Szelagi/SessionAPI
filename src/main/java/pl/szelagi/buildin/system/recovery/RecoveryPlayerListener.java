package pl.szelagi.buildin.system.recovery;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldSaveEvent;
import pl.szelagi.manager.ControllerManager;
import pl.szelagi.manager.SessionManager;


public class RecoveryPlayerListener implements Listener {
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onWorldSave(WorldSaveEvent event) {
        var sessions = SessionManager.getSessions();
        for (var session : sessions) {
            RecoveryPlayerController controller = ControllerManager
                    .getFirstController(session, c -> c instanceof RecoveryPlayerController);
            if (controller == null) continue;
            controller.save();
        }
    }
}
