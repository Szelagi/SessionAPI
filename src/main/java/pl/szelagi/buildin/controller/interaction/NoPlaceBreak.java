package pl.szelagi.buildin.controller.interaction;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.jetbrains.annotations.Nullable;
import pl.szelagi.component.ISessionComponent;
import pl.szelagi.component.controller.Controller;
import pl.szelagi.manager.BoardManager;
import pl.szelagi.manager.ControllerManager;

public class NoPlaceBreak extends Controller {
	public NoPlaceBreak(ISessionComponent sessionComponent) {
		super(sessionComponent);
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
			var controller = ControllerManager.getFirstController(session, NoPlaceBreak.class);
			if (controller == null)
				return;
			event.setCancelled(true);
		}

		@EventHandler(ignoreCancelled = true)
		public void onBlockBreak(BlockBreakEvent event) {
			var session = BoardManager.getSession(event.getBlock());
			if (session == null)
				return;
			var controller = ControllerManager.getFirstController(session, NoPlaceBreak.class);
			if (controller == null)
				return;
			event.setCancelled(true);
		}
	}
}
