/*
 * SessionAPI - A framework for game containerization on Minecraft servers
 * Copyright (C) 2025 Szelagi (https://github.com/Szelagi/SessionAPI)
 * Licensed under the GNU General Public License v3.0.
 * For more details, visit <https://www.gnu.org/licenses/>.
 */

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
			RecoveryPlayerController controller = ControllerManager.getFirstController(session, RecoveryPlayerController.class);
			if (controller == null)
				continue;
			controller.save();
		}
	}
}
