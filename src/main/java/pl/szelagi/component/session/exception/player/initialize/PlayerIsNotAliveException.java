/*
 * SessionAPI - A framework for game containerization on Minecraft servers
 * Copyright (C) 2025 Szelagi (https://github.com/Szelagi/SessionAPI)
 * Licensed under the GNU General Public License v3.0.
 * For more details, visit <https://www.gnu.org/licenses/>.
 */

package pl.szelagi.component.session.exception.player.initialize;

import org.bukkit.entity.Player;

public class PlayerIsNotAliveException extends PlayerJoinException {
	public PlayerIsNotAliveException() {
		super("player in not alive");
	}

	public static void check(Player p) throws PlayerIsNotAliveException {
		if (p.getHealth() <= 0)
			throw new PlayerIsNotAliveException();
	}
}