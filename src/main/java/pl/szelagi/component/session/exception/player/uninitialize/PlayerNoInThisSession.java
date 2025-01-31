/*
 * SessionAPI - A framework for game containerization on Minecraft servers
 * Copyright (C) 2025 Szelagi (https://github.com/Szelagi/SessionAPI)
 * Licensed under the GNU General Public License v3.0.
 * For more details, visit <https://www.gnu.org/licenses/>.
 */

package pl.szelagi.component.session.exception.player.uninitialize;

import org.bukkit.entity.Player;
import pl.szelagi.component.ISessionComponent;

public class PlayerNoInThisSession extends PlayerQuitException {
	public PlayerNoInThisSession(String name) {
		super(name);
	}

	public static void check(ISessionComponent component, Player player) throws PlayerNoInThisSession {
		if (!component.getSession().getPlayers().contains(player))
			throw new PlayerNoInThisSession("");
	}
}
