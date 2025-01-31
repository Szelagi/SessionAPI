/*
 * SessionAPI - A framework for game containerization on Minecraft servers
 * Copyright (C) 2025 Szelagi (https://github.com/Szelagi/SessionAPI)
 * Licensed under the GNU General Public License v3.0.
 * For more details, visit <https://www.gnu.org/licenses/>.
 */

package pl.szelagi.buildin.controller;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.Nullable;
import pl.szelagi.component.ISessionComponent;
import pl.szelagi.component.controller.Controller;
import pl.szelagi.event.player.initialize.PlayerConstructorEvent;
import pl.szelagi.event.player.initialize.PlayerDestructorEvent;
import pl.szelagi.manager.ControllerManager;
import pl.szelagi.manager.SessionManager;

public class HideOtherPlayers extends Controller {
	public HideOtherPlayers(ISessionComponent sessionComponent) {
		super(sessionComponent);
	}

	@Override
	public void playerConstructor(PlayerConstructorEvent event) {
		super.playerConstructor(event);
		for (var player : getPlugin().getServer()
		                             .getOnlinePlayers())
			if (!getSession().getPlayers()
			                 .contains(player))
				event.getPlayer()
				     .hidePlayer(getPlugin(), player);

		for (var player : getSession().getPlayers())
			player.showPlayer(getPlugin(), event.getPlayer());
	}

	@Override
	public void playerDestructor(PlayerDestructorEvent event) {
		super.playerDestructor(event);

		for (var player : getPlugin().getServer()
		                             .getOnlinePlayers())
			event.getPlayer()
			     .showPlayer(getPlugin(), player);

		for (var player : getSession().getPlayers())
			player.hidePlayer(getPlugin(), event.getPlayer());
	}

	@Override
	public @Nullable Listener getListener() {
		return new MyListener();
	}

	public static class MyListener implements Listener {
		@EventHandler(ignoreCancelled = true)
		public void onPlayerJoin(PlayerJoinEvent event) {
			for (var session : SessionManager.getSessions()) {
				var controller = ControllerManager.getFirstController(session, HideOtherPlayers.class);
				if (controller == null)
					continue;

				for (var player : session.getPlayers())
					player.hidePlayer(session.getPlugin(), event.getPlayer());
			}
		}
	}
}
