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
