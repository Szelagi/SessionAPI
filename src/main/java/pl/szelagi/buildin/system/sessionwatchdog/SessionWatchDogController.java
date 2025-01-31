/*
 * SessionAPI - A framework for game containerization on Minecraft servers
 * Copyright (C) 2025 Szelagi (https://github.com/Szelagi/SessionAPI)
 * Licensed under the GNU General Public License v3.0.
 * For more details, visit <https://www.gnu.org/licenses/>.
 */

package pl.szelagi.buildin.system.sessionwatchdog;

import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.szelagi.component.ISessionComponent;
import pl.szelagi.component.controller.Controller;

public class SessionWatchDogController extends Controller {
	public SessionWatchDogController(ISessionComponent sessionComponent) {
		super(sessionComponent);
	}

	@Nullable
	@Override
	public Listener getListener() {
		return new SessionWatchDogListener();
	}

	@Override
	public @NotNull String getName() {
		return "SystemSessionWatchDogController";
	}
}
