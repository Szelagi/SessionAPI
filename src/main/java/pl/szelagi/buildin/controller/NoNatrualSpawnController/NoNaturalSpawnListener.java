/*
 * SessionAPI - A framework for game containerization on Minecraft servers
 * Copyright (C) 2025 Szelagi (https://github.com/Szelagi/SessionAPI)
 * Licensed under the GNU General Public License v3.0.
 * For more details, visit <https://www.gnu.org/licenses/>.
 */

package pl.szelagi.buildin.controller.NoNatrualSpawnController;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import pl.szelagi.manager.BoardManager;
import pl.szelagi.manager.ControllerManager;

public class NoNaturalSpawnListener implements Listener {
	@EventHandler(ignoreCancelled = true)
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		if (event.getSpawnReason() != CreatureSpawnEvent.SpawnReason.NATURAL)
			return;
		var dungeon = BoardManager.getSession(event.getEntity().getLocation());
		if (dungeon == null)
			return;
		NoNaturalSpawnController controller = ControllerManager.getFirstController(dungeon, NoNaturalSpawnController.class);
		if (controller == null)
			return;
		event.setCancelled(true);
	}
}
