/*
 * SessionAPI - A framework for game containerization on Minecraft servers
 * Copyright (C) 2025 Szelagi (https://github.com/Szelagi/SessionAPI)
 * Licensed under the GNU General Public License v3.0.
 * For more details, visit <https://www.gnu.org/licenses/>.
 */

package pl.szelagi.buildin.system.sessionsafecontrolplayers;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import pl.szelagi.bukkitadapted.LocationAdapted;
import pl.szelagi.state.PlayerState;

import java.io.Serializable;

public class SessionSafeControlPlayerState extends PlayerState implements Serializable {
	private GameMode gameMode;
	private LocationAdapted locationAdapted;

	public SessionSafeControlPlayerState(Player player) {
		super(player);
	}

	public void save() {
		this.gameMode = getPlayer().getGameMode();
		this.locationAdapted = new LocationAdapted(getPlayer().getLocation());
	}

	public void load(Player player) {
		player.setGameMode(gameMode);
		player.teleport(locationAdapted.getLocation());
	}
}
