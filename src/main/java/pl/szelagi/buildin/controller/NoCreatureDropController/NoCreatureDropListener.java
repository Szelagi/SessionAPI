package pl.szelagi.buildin.controller.NoCreatureDropController;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import pl.szelagi.manager.BoardManager;
import pl.szelagi.manager.ControllerManager;

public class NoCreatureDropListener implements Listener {
	@EventHandler(ignoreCancelled = true)
	public void onEntityDeath(EntityDeathEvent event) {
		var dungeon = BoardManager.getSession(event.getEntity());
		if (dungeon == null)
			return;
		var controller = ControllerManager.getFirstController(dungeon, NoCreatureDropController.class);
		if (controller == null)
			return;
		event.getDrops().clear();
		event.setDroppedExp(0);
	}
}
