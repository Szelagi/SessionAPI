package pl.szelagi.buildin.controller.interaction;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.Nullable;
import pl.szelagi.component.ISessionComponent;
import pl.szelagi.component.controller.Controller;
import pl.szelagi.manager.ControllerManager;
import pl.szelagi.manager.SessionManager;
import pl.szelagi.util.CooldownVolatile;
import pl.szelagi.util.timespigot.Time;

public class NoPvP extends Controller {
	public NoPvP(ISessionComponent sessionComponent) {
		super(sessionComponent);
	}

	@Override
	public @Nullable Listener getListener() {
		return new MyListener();
	}

	private static class MyListener implements Listener {
		@EventHandler(ignoreCancelled = true)
		public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
			if (!(event.getEntity() instanceof Player victim))
				return;
			if (!(event.getDamager() instanceof Player attacker))
				return;
			var session = SessionManager.getSession(victim);
			if (session == null)
				return;
			var controller = ControllerManager.getFirstController(session, NoPvP.class);
			if (controller == null)
				return;
			if (CooldownVolatile.canUseAndStart(attacker, "nopvp-controller", Time.Seconds(2)))
				attacker.sendMessage("§cYou cannot attack a player because pvp is disabled!");
			event.setCancelled(true);
		}
	}
}
