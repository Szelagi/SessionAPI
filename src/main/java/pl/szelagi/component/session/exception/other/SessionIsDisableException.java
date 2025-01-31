/*
 * SessionAPI - A framework for game containerization on Minecraft servers
 * Copyright (C) 2025 Szelagi (https://github.com/Szelagi/SessionAPI)
 * Licensed under the GNU General Public License v3.0.
 * For more details, visit <https://www.gnu.org/licenses/>.
 */

package pl.szelagi.component.session.exception.other;

import pl.szelagi.component.session.exception.SessionStartException;

public class SessionIsDisableException extends SessionStartException {
	public SessionIsDisableException(String name) {
		super(name);
	}
}
