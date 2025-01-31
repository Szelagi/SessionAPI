/*
 * SessionAPI - A framework for game containerization on Minecraft servers
 * Copyright (C) 2025 Szelagi (https://github.com/Szelagi/SessionAPI)
 * Licensed under the GNU General Public License v3.0.
 * For more details, visit <https://www.gnu.org/licenses/>.
 */

package pl.szelagi.event.player.initialize.listener;

import pl.szelagi.event.EventListener;
import pl.szelagi.event.player.initialize.PlayerConstructorEvent;

public interface PlayerJoinListener extends EventListener {
	void run(PlayerConstructorEvent event);
}
