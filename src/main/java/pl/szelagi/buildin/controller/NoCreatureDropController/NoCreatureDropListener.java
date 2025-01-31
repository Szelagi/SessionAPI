/*
 * SessionAPI - A framework for game containerization on Minecraft servers
 * Copyright (C) 2025 Szelagi (https://github.com/Szelagi/SessionAPI)
 * Licensed under the GNU General Public License v3.0.
 * For more details, visit <https://www.gnu.org/licenses/>.
 */

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
