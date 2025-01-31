/*
 * SessionAPI - A framework for game containerization on Minecraft servers
 * Copyright (C) 2025 Szelagi (https://github.com/Szelagi/SessionAPI)
 * Licensed under the GNU General Public License v3.0.
 * For more details, visit <https://www.gnu.org/licenses/>.
 */

package pl.szelagi.buildin.system.loading;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import pl.szelagi.SessionAPI;
import pl.szelagi.component.board.Board;
import pl.szelagi.component.session.Session;
import pl.szelagi.event.player.initialize.PlayerConstructorEvent;
import pl.szelagi.event.player.initialize.PlayerDestructorEvent;
import pl.szelagi.spatial.ISpatial;

public class LoadingBoard extends Board {
	public LoadingBoard(Session session) {
		super(session);
	}

	@Override
	protected void generate() {
		var centerBlock = getSpace().getCenter()
		                            .getBlock();
		centerBlock.setType(Material.BEDROCK);
		setSecureZone(ISpatial.from(centerBlock));
	}

	@Override
	protected void degenerate() {
		var centerBlock = getSpace().getCenter()
		                            .getBlock();
		Bukkit.getScheduler()
		      .runTask(SessionAPI.getInstance(), () -> {
			      centerBlock.setType(Material.AIR);
		      });
	}

	@Override
	public void playerConstructor(PlayerConstructorEvent event) {
		super.playerConstructor(event);
	}

	@Override
	public void playerDestructor(PlayerDestructorEvent event) {
		super.playerDestructor(event);
	}
}
