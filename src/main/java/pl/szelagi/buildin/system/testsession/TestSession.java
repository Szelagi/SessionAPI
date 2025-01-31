/*
 * SessionAPI - A framework for game containerization on Minecraft servers
 * Copyright (C) 2025 Szelagi (https://github.com/Szelagi/SessionAPI)
 * Licensed under the GNU General Public License v3.0.
 * For more details, visit <https://www.gnu.org/licenses/>.
 */

package pl.szelagi.buildin.system.testsession;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import pl.szelagi.component.board.Board;
import pl.szelagi.component.session.Session;

public class TestSession extends Session {
	public TestSession(JavaPlugin plugin) {
		super(plugin);
	}

	@NotNull
	@Override
	protected Board getDefaultStartBoard() {
		return new TestBoard(this);
	}

	@Override
	public @NotNull String getName() {
		return "SystemTestSession";
	}
}
