/*
 * SessionAPI - A framework for game containerization on Minecraft servers
 * Copyright (C) 2025 Szelagi (https://github.com/Szelagi/SessionAPI)
 * Licensed under the GNU General Public License v3.0.
 * For more details, visit <https://www.gnu.org/licenses/>.
 */

package pl.szelagi.manager;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;
import pl.szelagi.component.board.Board;
import pl.szelagi.component.board.event.BoardStartEvent;
import pl.szelagi.component.board.event.BoardStopEvent;
import pl.szelagi.component.session.Session;
import pl.szelagi.space.Space;

import java.util.ArrayList;

public class BoardManager {
	private static final ArrayList<Board> runningBoards = new ArrayList<>();
	private static JavaPlugin plugin;

	public static void initialize(JavaPlugin p) {
		plugin = p;

		class MyListener implements Listener {
			@EventHandler(ignoreCancelled = true)
			public void onBoardStart(BoardStartEvent event) {
				if (!runningBoards.contains(event.getBoard())) {
					runningBoards.add(event.getBoard());
				}
			}

			@EventHandler(ignoreCancelled = true)
			public void onBoardStop(BoardStopEvent event) {
				runningBoards.remove(event.getBoard());
			}
		}

		plugin.getServer().getPluginManager()
		      .registerEvents(new MyListener(), plugin);
	}

	@Nullable
	public static Session getSession(Location location) {
		Space space;
		for (var board : runningBoards) {
			space = board.getSpace();
			if (space.isLocationIn(location))
				return board.getSession();
		}
		return null;
	}

	@Nullable
	public static Session getSession(LivingEntity entity) {
		return getSession(entity.getLocation());
	}

	@Nullable
	public static Session getSession(@Nullable Block block) {
		if (block == null)
			return null;
		return getSession(block.getLocation());
	}
}
