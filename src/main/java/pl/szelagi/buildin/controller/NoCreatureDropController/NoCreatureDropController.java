/*
 * SessionAPI - A framework for game containerization on Minecraft servers
 * Copyright (C) 2025 Szelagi (https://github.com/Szelagi/SessionAPI)
 * Licensed under the GNU General Public License v3.0.
 * For more details, visit <https://www.gnu.org/licenses/>.
 */

package pl.szelagi.buildin.controller.NoCreatureDropController;

import org.bukkit.event.Listener;
import org.jetbrains.annotations.Nullable;
import pl.szelagi.component.ISessionComponent;
import pl.szelagi.component.controller.Controller;

public class NoCreatureDropController extends Controller {
	public NoCreatureDropController(ISessionComponent component) {
		super(component);
	}

	@Nullable
	@Override
	public Listener getListener() {
		return new NoCreatureDropListener();
	}
}
