/*
 * SessionAPI - A framework for game containerization on Minecraft servers
 * Copyright (C) 2025 Szelagi (https://github.com/Szelagi/SessionAPI)
 * Licensed under the GNU General Public License v3.0.
 * For more details, visit <https://www.gnu.org/licenses/>.
 */

package pl.szelagi.util;

import org.bukkit.entity.Player;
import pl.szelagi.component.ISessionComponent;
import pl.szelagi.component.board.Board;
import pl.szelagi.component.controller.Controller;
import pl.szelagi.component.session.Session;

import java.util.HashSet;

public class Debug {
	private static final HashSet<Player> allowPlayers = new HashSet<>();

	public static void allowView(Player player) {
		allowPlayers.add(player);
	}

	public static void denyView(Player player) {
		allowPlayers.remove(player);
	}

	public static void send(String message) {
		for (var p : allowPlayers) {
			p.sendMessage("§8D: §f" + message);
		}
	}

	public static boolean isAllowView(Player player) {
		return allowPlayers.contains(player);
	}

	public static void send(ISessionComponent component, String message) {
		String prefix = "";
		if (component instanceof Controller)
			prefix = "§3[C]";
		if (component instanceof Board)
			prefix = "§5[B]";
		if (component instanceof Session)
			prefix = "§6[S]";
		send(prefix + " §n" + component.getName() + "§f " + message);
	}
}
