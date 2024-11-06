package pl.szelagi.buildin.system.archerySession;

import org.bukkit.GameMode;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import pl.szelagi.buildin.controller.OtherEquipment.OtherEquipment;
import pl.szelagi.buildin.controller.OtherGameMode.OtherGameMode;
import pl.szelagi.component.board.Board;
import pl.szelagi.component.session.Session;
import pl.szelagi.event.component.ComponentConstructorEvent;

public class ArcherySession extends Session {
	public ArcherySession(JavaPlugin plugin) {
		super(plugin);
	}

	@Override
	protected @NotNull Board getDefaultStartBoard() {
		return new ArcheryBoard(this);
	}

	@Override
	public void componentConstructor(ComponentConstructorEvent event) {
		super.componentConstructor(event);
		new OtherEquipment(this, false).start();
		new OtherGameMode(this, GameMode.SURVIVAL).start();
	}
}
