package pl.szelagi.buildin.controller.OtherGameMode;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import pl.szelagi.state.PlayerState;

public class GameModeState extends PlayerState {
	private final GameMode gameMode;

	public GameModeState(Player player) {
		super(player);
		gameMode = player.getGameMode();
	}

	public GameMode getGameMode() {
		return gameMode;
	}
}
