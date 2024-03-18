package pl.szelagi.buildin.system.testsession;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import pl.szelagi.component.board.Board;
import pl.szelagi.component.session.Session;

import java.util.ArrayList;

public class TestSession extends Session {
	public TestSession(JavaPlugin plugin) {
		super(plugin);
	}

	public TestSession(JavaPlugin plugin, ArrayList<Player> players) {
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
