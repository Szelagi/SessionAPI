/*
 * SessionAPI - A framework for game containerization on Minecraft servers
 * Copyright (C) 2025 Szelagi (https://github.com/Szelagi/SessionAPI)
 * Licensed under the GNU General Public License v3.0.
 * For more details, visit <https://www.gnu.org/licenses/>.
 */

package pl.szelagi.buildin.controller.interaction;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import pl.szelagi.component.baseComponent.BaseComponent;
import pl.szelagi.component.controller.Controller;
import pl.szelagi.manager.BoardManager;
import pl.szelagi.manager.listener.ListenerManager;
import pl.szelagi.manager.listener.Listeners;

import java.util.HashSet;
import java.util.Set;

public class NoPlaceBreakExcept extends Controller {
	Set<Material> allowBreak = new HashSet<>();
	Set<Material> allowPlace = new HashSet<>();

	public NoPlaceBreakExcept(BaseComponent baseComponent) {
		super(baseComponent);
	}

	public NoPlaceBreakExcept setPlaceFlag(Material material, boolean allow) {
		if (allow)
			allowPlace.add(material);
		else
			allowPlace.remove(material);
		return this;
	}

	public NoPlaceBreakExcept setBreakFlag(Material material, boolean allow) {
		if (allow)
			allowBreak.add(material);
		else
			allowBreak.remove(material);
		return this;
	}

	public void clearPlaceFlags() {
		allowPlace.clear();
	}

	public void clearBreakFlags() {
		allowBreak.clear();
	}

	public void clearAllFlags() {
		clearPlaceFlags();
		clearBreakFlags();
	}

	public boolean canPlace(Material material) {
		return allowPlace.contains(material);
	}

	public boolean canBreak(Material material) {
		return allowBreak.contains(material);
	}

	@Override
	public Listeners defineListeners() {
		return super.defineListeners().add(MyListener.class);
	}

	private static class MyListener implements Listener {
		@EventHandler(ignoreCancelled = true)
		public void onBlockPlace(BlockPlaceEvent event) {
			var session = BoardManager.session(event.getBlock());
			if (session == null)
				return;
			var material = event.getBlock()
			                    .getType();
			ListenerManager.each(session, getClass(), NoPlaceBreakExcept.class, noPlaceBreakExcept -> {
				if (noPlaceBreakExcept.canPlace(material))
					return;
				event.setCancelled(true);
			});
		}

		@EventHandler(ignoreCancelled = true)
		public void onBlockBreak(BlockBreakEvent event) {
			var session = BoardManager.session(event.getBlock());
			if (session == null)
				return;
			var material = event.getBlock()
			                    .getType();
			ListenerManager.each(session, getClass(), NoPlaceBreakExcept.class, noPlaceBreakExcept -> {
				if (noPlaceBreakExcept.canBreak(material))
					return;
				event.setCancelled(true);
			});
		}
	}
}
