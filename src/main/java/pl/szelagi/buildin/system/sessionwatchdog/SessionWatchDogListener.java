package pl.szelagi.buildin.system.sessionwatchdog;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import pl.szelagi.component.session.cause.ExceptionCause;
import pl.szelagi.manager.ControllerManager;
import pl.szelagi.manager.SessionManager;
import pl.szelagi.util.timespigot.Time;
import pl.szelagi.world.SessionWorldManager;

public class SessionWatchDogListener implements Listener {
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
		var session = SessionManager.getSession(event.getPlayer());
		if (session == null)
			return;
		SessionWatchDogController controller = ControllerManager.getFirstController(session, SessionWatchDogController.class);
		if (controller == null)
			return;

		controller.getProcess()
		          .runControlledTaskLater(() -> checkCorrectWorld(event.getPlayer()), Time.Ticks(1));
	}

	private static void checkCorrectWorld(Player player) {
		var session = SessionManager.getSession(player);
		SessionWatchDogController controller = ControllerManager.getFirstController(session, SessionWatchDogController.class);
		if (controller == null)
			return;
		var playerWorldName = player.getWorld()
		                            .getName();
		var sessionWorldName = SessionWorldManager
				.getSessionWorld().getName();
		boolean isSameWorld = playerWorldName.equals(sessionWorldName);
		var identifier = session.getIdentifier() + ", " + session
				.getCurrentBoard()
				.getIdentifier();
		if (isSameWorld)
			return;
		session.stop(new ExceptionCause("Player " + player.getName() + " performed an illegal teleportation out of the assigned SessionWorld! §7(" + identifier + "§7)"));
	}
}
