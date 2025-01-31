/*
 * SessionAPI - A framework for game containerization on Minecraft servers
 * Copyright (C) 2025 Szelagi (https://github.com/Szelagi/SessionAPI)
 * Licensed under the GNU General Public License v3.0.
 * For more details, visit <https://www.gnu.org/licenses/>.
 */

package pl.szelagi.buildin.controller.interaction;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.jetbrains.annotations.Nullable;
import pl.szelagi.component.ISessionComponent;
import pl.szelagi.component.controller.Controller;
import pl.szelagi.manager.BoardManager;
import pl.szelagi.manager.ControllerManager;

public class ProtectItemFrame extends Controller implements Listener {
	public ProtectItemFrame(ISessionComponent sessionComponent) {
		super(sessionComponent);
	}

	@Override
	public @Nullable Listener getListener() {
		return new MyListener();
	}

	private static class MyListener implements Listener {
		public boolean check(Entity entity) {
			var entityType = entity.getType();
			if (entityType != EntityType.ITEM_FRAME)
				return false;
			var session = BoardManager.getSession(entity.getLocation());
			if (session == null)
				return false;
			var controller = ControllerManager.getFirstController(session, ProtectItemFrame.class);
			return (controller != null);
		}

		@EventHandler(ignoreCancelled = true)
		public void onEntityDamage(EntityDamageEvent event) {
			if (check(event.getEntity()))
				event.setCancelled(true);
		}

		@EventHandler(ignoreCancelled = true)
		public void onHangingBreak(HangingBreakEvent event) {
			if (check(event.getEntity()))
				event.setCancelled(true);
		}

		@EventHandler(ignoreCancelled = true)
		public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
			if (check(event.getRightClicked()))
				event.setCancelled(true);
		}
	}
}
