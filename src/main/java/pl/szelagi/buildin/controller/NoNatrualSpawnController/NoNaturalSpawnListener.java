package pl.szelagi.buildin.controller.NoNatrualSpawnController;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import pl.szelagi.manager.BoardManager;
import pl.szelagi.manager.ControllerManager;


public class NoNaturalSpawnListener implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (event.getSpawnReason() != CreatureSpawnEvent.SpawnReason.NATURAL) return;
        var dungeon = BoardManager.getSession(event.getEntity().getLocation());
        if (dungeon == null) return;
        NoNaturalSpawnController controller = ControllerManager
                .getFirstController(dungeon, c -> c instanceof NoNaturalSpawnController);
        if (controller == null) return;
        event.setCancelled(true);
    }
}
