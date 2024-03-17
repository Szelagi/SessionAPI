package pl.szelagi.buildin.system.testsession;

import org.jetbrains.annotations.NotNull;
import pl.szelagi.buildin.controller.OtherEquipment.OtherEquipment;
import pl.szelagi.component.board.Board;
import pl.szelagi.component.session.Session;

public class TestBoard extends Board {
	public TestBoard(Session session) {
		super(session);
	}

	@Override
	public @NotNull String getName() {
		return "SystemTestBoard";
	}

	@Override
	public void constructor() {
		super.constructor();
		new OtherEquipment(this, true).start();
	}
}
