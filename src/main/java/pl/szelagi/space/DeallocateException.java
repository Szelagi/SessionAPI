/*
 * SessionAPI - A framework for game containerization on Minecraft servers
 * Copyright (C) 2025 Szelagi (https://github.com/Szelagi/SessionAPI)
 * Licensed under the GNU General Public License v3.0.
 * For more details, visit <https://www.gnu.org/licenses/>.
 */

package pl.szelagi.space;

import pl.szelagi.util.ServerRuntimeException;

public class DeallocateException extends ServerRuntimeException {
	public DeallocateException(String name, Space space) {
		super(name + "in " + space.toString());
	}
}