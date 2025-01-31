/*
 * SessionAPI - A framework for game containerization on Minecraft servers
 * Copyright (C) 2025 Szelagi (https://github.com/Szelagi/SessionAPI)
 * Licensed under the GNU General Public License v3.0.
 * For more details, visit <https://www.gnu.org/licenses/>.
 */

package pl.szelagi.buildin.system.testsession;

import org.jetbrains.annotations.NotNull;
import pl.szelagi.buildin.controller.OtherEquipment.OtherEquipment;
import pl.szelagi.component.board.Board;
import pl.szelagi.component.session.Session;
import pl.szelagi.event.component.ComponentConstructorEvent;

public class TestBoard extends Board {
	public TestBoard(Session session) {
		super(session);
	}

	@Override
	public @NotNull String getName() {
		return "SystemTestBoard";
	}

	@Override
	public void componentConstructor(ComponentConstructorEvent event) {
		super.componentConstructor(event);
		new OtherEquipment(this, false).start();
	}
}
