package pl.szelagi.buildin.system.boardwatchdog;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import pl.szelagi.SessionAPI;
import pl.szelagi.manager.ControllerManager;
import pl.szelagi.manager.SessionManager;
import pl.szelagi.util.timespigot.Time;
import pl.szelagi.world.SessionWorldManager;

import java.util.List;

public class BoardWatchDogListener implements Listener {
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		var previousLocation = event.getPlayer()
		                            .getLocation()
		                            .clone();
		var playerSession = SessionManager.getSession(event.getPlayer());
		if (playerSession != null) {
			List<BoardWatchDogController> controllers = ControllerManager.getControllers(playerSession, BoardWatchDogController.class);
			for (var controller : controllers) {
				controller.getProcess()
				          .runControlledTaskLater(() -> {
					          controller.stopWhenPlayerExitSpace(event.getPlayer());
				          }, Time.Ticks(1));
			}
		} else {
			Bukkit.getServer().getScheduler()
			      .runTaskLater(SessionAPI.getInstance(), () -> {
				      if (event.getPlayer()
				               .getWorld()
				               .getName()
				               .equals(SessionWorldManager
						                       .getSessionWorld()
						                       .getName())) {
					      for (var session : SessionManager.getSessions()) {
						      List<BoardWatchDogController> controllers = ControllerManager.getControllers(session, BoardWatchDogController.class);
						      for (var controller : controllers) {
							      controller.warnWhenAlienInSpace(event.getPlayer(), previousLocation);
							      return;
						      }
					      }
				      }
			      }, 1);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onPlayerRespawn(PlayerRespawnEvent event) {

	}
}
