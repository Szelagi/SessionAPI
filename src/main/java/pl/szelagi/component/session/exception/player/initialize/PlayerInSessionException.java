/*
 * SessionAPI - A framework for game containerization on Minecraft servers
 * Copyright (C) 2025 Szelagi (https://github.com/Szelagi/SessionAPI)
 * Licensed under the GNU General Public License v3.0.
 * For more details, visit <https://www.gnu.org/licenses/>.
 */

package pl.szelagi.component.session.exception.player.initialize;

import org.bukkit.entity.Player;
import pl.szelagi.manager.SessionManager;

public class PlayerInSessionException extends PlayerJoinException {
	public PlayerInSessionException() {
		super("player in dungeon");
	}

	public static void check(Player p) throws PlayerInSessionException {
		if (SessionManager.getSession(p) != null)
			throw new PlayerInSessionException();
	}
}
