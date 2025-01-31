/*
 * SessionAPI - A framework for game containerization on Minecraft servers
 * Copyright (C) 2025 Szelagi (https://github.com/Szelagi/SessionAPI)
 * Licensed under the GNU General Public License v3.0.
 * For more details, visit <https://www.gnu.org/licenses/>.
 */

package pl.szelagi.component.controller.exception;

import pl.szelagi.component.baseexception.StartException;

public class ControllerStartException extends StartException {
	public ControllerStartException(String name) {
		super(name);
	}
}
