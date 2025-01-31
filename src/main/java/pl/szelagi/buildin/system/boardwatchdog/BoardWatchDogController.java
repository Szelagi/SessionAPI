/*
 * SessionAPI - A framework for game containerization on Minecraft servers
 * Copyright (C) 2025 Szelagi (https://github.com/Szelagi/SessionAPI)
 * Licensed under the GNU General Public License v3.0.
 * For more details, visit <https://www.gnu.org/licenses/>.
 */

package pl.szelagi.buildin.system.boardwatchdog;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.szelagi.component.ISessionComponent;
import pl.szelagi.component.controller.Controller;
import pl.szelagi.component.session.cause.ExceptionCause;
import pl.szelagi.event.component.ComponentConstructorEvent;
import pl.szelagi.util.Debug;
import pl.szelagi.util.ServerWarning;
import pl.szelagi.util.timespigot.Time;

public class BoardWatchDogController extends Controller {
	public BoardWatchDogController(ISessionComponent sessionComponent) {
		super(sessionComponent);
	}

	@Override
	public void componentConstructor(ComponentConstructorEvent event) {
		super.componentConstructor(event);
		getProcess().runControlledTaskTimer(this::checkAllPlayers, Time.Seconds(0), Time.Seconds(3));
	}

	public void checkAllPlayers() {
		for (var player : getSession().getPlayers())
			if (stopWhenPlayerExitSpace(player))
				return;
	}

	@Nullable
	@Override
	public Listener getListener() {
		return new BoardWatchDogListener();
	}

	public boolean stopWhenPlayerExitSpace(Player player) {
		var space = getSession().getCurrentBoard()
		                        .getSpace();
		if (!space.isLocationInXZ(player.getLocation())) {
			var identifier = getSession().getIdentifier() + ", " + getSession()
					.getCurrentBoard()
					.getIdentifier();
			getSession().stop(new ExceptionCause("Player §7" + player.getName() + "§f performed an illegal exit from the assigned board area! §7(" + identifier + ")"));
			return true;
		}
		return false;
	}

	public void warnWhenAlienInSpace(Player player, Location backLocation) {
		boolean isPlayerCorrect = getSession()
				.getPlayers().contains(player);
		if (isPlayerCorrect)
			return;
		var space = getSession().getCurrentBoard()
		                        .getSpace();
		boolean isIn = space.isLocationInXZ(player.getLocation());
		if (!isIn)
			return;

		if (Debug.isAllowView(player))
			return;

		boolean isBackIn = space.isLocationInXZ(backLocation);
		if (isBackIn) {
			var identifier = getSession().getIdentifier() + ", " + getSession()
					.getCurrentBoard()
					.getIdentifier();
			getSession().stop(new ExceptionCause("Incorrect back location for player §7" + player.getName() + "§f. Illegal entry into restricted board area, §7(" + identifier + ")"));
		}

		player.teleport(backLocation);

		if (player.isOp()) {
			player.sendMessage("§4[ADMIN] §fYou tried to enter a restricted board area without §c§DEBUG-MODE§f enabled!");
		} else {
			var identifier = getSession().getIdentifier() + ", " + getSession()
					.getCurrentBoard()
					.getIdentifier();
			new ServerWarning("Player §7" + player.getName() + "§f attempted illegal entry into restricted board area §7(" + identifier + ")");
		}
	}

	@Override
	public @NotNull String getName() {
		return "SystemBoardWatchDogController";
	}
}