/*
 * SessionAPI - A framework for game containerization on Minecraft servers
 * Copyright (C) 2025 Szelagi (https://github.com/Szelagi/SessionAPI)
 * Licensed under the GNU General Public License v3.0.
 * For more details, visit <https://www.gnu.org/licenses/>.
 */

package pl.szelagi.component;

import pl.szelagi.component.board.Board;
import pl.szelagi.component.controller.Controller;
import pl.szelagi.component.session.Session;

public enum ComponentType {
	SESSION, BOARD, CONTROLLER;

	public static ComponentType toType(ISessionComponent component) {
		if (component instanceof Controller)
			return ComponentType.CONTROLLER;
		if (component instanceof Board)
			return ComponentType.BOARD;
		if (component instanceof Session)
			return ComponentType.SESSION;
		throw new RuntimeException("unknown type of component");
	}
}
