/*
 * SessionAPI - A framework for game containerization on Minecraft servers
 * Copyright (C) 2025 Szelagi (https://github.com/Szelagi/SessionAPI)
 * Licensed under the GNU General Public License v3.0.
 * For more details, visit <https://www.gnu.org/licenses/>.
 */

package pl.szelagi.component.baseexception.multi;

import pl.szelagi.component.ComponentType;
import pl.szelagi.component.ISessionComponent;
import pl.szelagi.component.baseexception.StartException;

public class MultiStartException extends StartException {
	public MultiStartException(ISessionComponent component) {
		super(component.getName() + ", " + ComponentType.toType(component).name());
	}
}
