/*
 * SessionAPI - A framework for game containerization on Minecraft servers
 * Copyright (C) 2025 Szelagi (https://github.com/Szelagi/SessionAPI)
 * Licensed under the GNU General Public License v3.0.
 * For more details, visit <https://www.gnu.org/licenses/>.
 */

package pl.szelagi.component;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import pl.szelagi.component.session.Session;
import pl.szelagi.process.RemoteProcess;

public interface ISessionComponent {
	RemoteProcess getProcess();

	@NotNull Session getSession();

	@NotNull JavaPlugin getPlugin();

	@NotNull String getName();

	@NotNull ComponentStatus status();
}