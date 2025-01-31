/*
 * SessionAPI - A framework for game containerization on Minecraft servers
 * Copyright (C) 2025 Szelagi (https://github.com/Szelagi/SessionAPI)
 * Licensed under the GNU General Public License v3.0.
 * For more details, visit <https://www.gnu.org/licenses/>.
 */

package pl.szelagi.recovery;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Recovery {
	public static void initialize(JavaPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(new RecoveryListener(), plugin);

		var recoveryFileManager = new RecoveryFileManager();
		for (var player : Bukkit.getServer().getOnlinePlayers()) {
			if (recoveryFileManager.existsPlayerRecovery(player)) {
				var recovery = recoveryFileManager.loadPlayerRecovery(player);
				recovery.run(player);
				recoveryFileManager.deletePlayerRecovery(player);
			}
		}
	}
}
