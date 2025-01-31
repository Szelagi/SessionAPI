/*
 * SessionAPI - A framework for game containerization on Minecraft servers
 * Copyright (C) 2025 Szelagi (https://github.com/Szelagi/SessionAPI)
 * Licensed under the GNU General Public License v3.0.
 * For more details, visit <https://www.gnu.org/licenses/>.
 */

package pl.szelagi.recovery;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class RecoveryListener implements Listener {
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onPlayerJoin(PlayerJoinEvent event) {
		var recoveryFileManager = new RecoveryFileManager();
		if (recoveryFileManager.existsPlayerRecovery(event.getPlayer())) {
			var recovery = recoveryFileManager.loadPlayerRecovery(event.getPlayer());
			recovery.run(event.getPlayer());
			recoveryFileManager.deletePlayerRecovery(event.getPlayer());
		}
	}
}
