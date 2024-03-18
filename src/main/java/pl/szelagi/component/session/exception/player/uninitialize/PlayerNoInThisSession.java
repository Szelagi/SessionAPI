package pl.szelagi.component.session.exception.player.uninitialize;

import org.bukkit.entity.Player;
import pl.szelagi.component.ISessionComponent;

public class PlayerNoInThisSession extends PlayerQuitException {
	public PlayerNoInThisSession(String name) {
		super(name);
	}

	public static void check(ISessionComponent component, Player player) throws PlayerNoInThisSession {
		if (!component.getSession().getPlayers().contains(player))
			throw new PlayerNoInThisSession("");
	}
}
