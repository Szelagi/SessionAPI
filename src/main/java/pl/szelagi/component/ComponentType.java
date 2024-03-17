package pl.szelagi.component;

import pl.szelagi.component.board.Board;
import pl.szelagi.component.controller.Controller;
import pl.szelagi.component.session.Session;

public enum ComponentType {
	SESSION, BOARD, CONTROLLER, OTHER;

	public static ComponentType toType(ISessionComponent component) {
		if (component instanceof Controller)
			return ComponentType.CONTROLLER;
		if (component instanceof Board)
			return ComponentType.BOARD;
		if (component instanceof Session)
			return ComponentType.SESSION;
		return ComponentType.OTHER;
	}
}
