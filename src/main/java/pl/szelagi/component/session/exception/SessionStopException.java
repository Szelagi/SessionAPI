/*
 * SessionAPI - A framework for game containerization on Minecraft servers
 * Copyright (C) 2025 Szelagi (https://github.com/Szelagi/SessionAPI)
 * Licensed under the GNU General Public License v3.0.
 * For more details, visit <https://www.gnu.org/licenses/>.
 */

package pl.szelagi.component.session.exception;

import pl.szelagi.component.baseexception.StopException;

public class SessionStopException extends StopException {
	public SessionStopException(String name) {
		super(name);
	}
}
