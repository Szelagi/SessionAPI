package pl.szelagi.component.session.exception.player.initialize;

import org.bukkit.entity.Player;
import pl.szelagi.manager.SessionManager;

public class PlayerInSessionException extends PlayerJoinException {
	public PlayerInSessionException() {
		super("player in dungeon");
	}

	public static void check(Player p) throws PlayerInSessionException {
		if (SessionManager.getSession(p) != null)
			throw new PlayerInSessionException();
	}
}
