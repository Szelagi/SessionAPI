package pl.szelagi.buildin.creator;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import pl.szelagi.component.board.Board;
import pl.szelagi.component.session.Session;

import java.util.ArrayList;

public class Creator extends Session {
	private final String mainDirectory;

	public Creator(JavaPlugin plugin, ArrayList<Player> players, String mainDirectory) {
		super(plugin, players);
		this.mainDirectory = mainDirectory;
	}

	@NotNull
	@Override
	protected Board getDefaultStartBoard() {
		return new CreatorBoard(this, mainDirectory);
	}

	@NotNull
	@Override
	public String getName() {
		return "creatorSession";
	}
}
