package pl.szelagi.buildin.controller.interaction;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.jetbrains.annotations.Nullable;
import pl.szelagi.component.ISessionComponent;
import pl.szelagi.component.controller.Controller;
import pl.szelagi.manager.BoardManager;
import pl.szelagi.manager.ControllerManager;

import java.util.HashSet;
import java.util.Set;

public class NoPlaceBreakExcept extends Controller {
	Set<Material> allowBreak = new HashSet<>();
	Set<Material> allowPlace = new HashSet<>();

	public NoPlaceBreakExcept(ISessionComponent sessionComponent) {
		super(sessionComponent);
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
	public @Nullable Listener getListener() {
		return new MyListener();
	}

	private static class MyListener implements Listener {
		@EventHandler(ignoreCancelled = true)
		public void onBlockPlace(BlockPlaceEvent event) {
			var session = BoardManager.getSession(event.getBlock());
			if (session == null)
				return;
			var material = event.getBlock()
			                    .getType();
			var controllers = ControllerManager.getControllers(session, NoPlaceBreakExcept.class);
			for (var controller : controllers)
				if (controller.canPlace(material))
					return;
			event.setCancelled(true);
		}

		@EventHandler(ignoreCancelled = true)
		public void onBlockBreak(BlockBreakEvent event) {
			var session = BoardManager.getSession(event.getBlock());
			if (session == null)
				return;
			var material = event.getBlock()
			                    .getType();
			var controllers = ControllerManager.getControllers(session, NoPlaceBreakExcept.class);
			for (var controller : controllers)
				if (controller.canBreak(material))
					return;
			event.setCancelled(true);
		}
	}
}
