/*
 * SessionAPI - A framework for game containerization on Minecraft servers
 * Copyright (C) 2025 Szelagi (https://github.com/Szelagi/SessionAPI)
 * Licensed under the GNU General Public License v3.0.
 * For more details, visit <https://www.gnu.org/licenses/>.
 */

package pl.szelagi.buildin.controller.environment;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.jetbrains.annotations.Nullable;
import pl.szelagi.component.ISessionComponent;
import pl.szelagi.component.controller.Controller;
import pl.szelagi.manager.BoardManager;
import pl.szelagi.manager.ControllerManager;

public class WorldEnvironment extends Controller implements Listener {
	private boolean explosionDestroy = true;
	private boolean blockIgnite = true;
	private boolean fireSpread = true;
	private boolean blockBurn = true;

	public WorldEnvironment(ISessionComponent sessionComponent) {
		super(sessionComponent);
	}

	public WorldEnvironment setExplosionDestroy(boolean state) {
		explosionDestroy = state;
		return this;
	}

	public WorldEnvironment setBlockIgnite(boolean state) {
		blockIgnite = state;
		return this;
	}

	public WorldEnvironment setFireSpread(boolean state) {
		fireSpread = state;
		return this;
	}

	public WorldEnvironment setBlockBurn(boolean state) {
		blockBurn = state;
		return this;
	}

	@Override
	public @Nullable Listener getListener() {
		return new MyListener();
	}

	private static class MyListener implements Listener {
		@EventHandler(ignoreCancelled = true)
		public void onEntityExplode(EntityExplodeEvent event) {
			var session = BoardManager.getSession(event.getLocation());
			if (session == null)
				return;
			var controllers = ControllerManager.getControllers(session, WorldEnvironment.class);
			for (var controller : controllers) {
				if (controller.explosionDestroy)
					continue;
				event.blockList().clear();
				return;
			}
		}

		@EventHandler(ignoreCancelled = true)
		public void onBlockIgnite(BlockIgniteEvent event) {
			var session = BoardManager.getSession(event.getBlock());
			if (session == null)
				return;
			var controllers = ControllerManager.getControllers(session, WorldEnvironment.class);
			for (var controller : controllers) {
				if (controller.blockIgnite) {
					event.setCancelled(true);
					return;
				}
				var cause = event.getCause();
				if (cause == BlockIgniteEvent.IgniteCause.SPREAD && !controller.fireSpread) {
					event.setCancelled(true);
					return;
				}
			}
		}

		@EventHandler(ignoreCancelled = true)
		public void onBlockBurn(BlockBurnEvent event) {
			var session = BoardManager.getSession(event.getBlock());
			if (session == null)
				return;
			var controllers = ControllerManager.getControllers(session, WorldEnvironment.class);
			for (var controller : controllers) {
				if (controller.blockBurn)
					continue;
				event.setCancelled(true);
				return;
			}
		}
	}
}
