package pl.szelagi.buildin.system.boardwatchdog;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.szelagi.component.ISessionComponent;
import pl.szelagi.component.controller.Controller;
import pl.szelagi.component.session.cause.ExceptionCause;
import pl.szelagi.event.component.ComponentDestructorEvent;
import pl.szelagi.util.ServerWarning;
import pl.szelagi.util.timespigot.Time;

public class BoardWatchDogController extends Controller {
	public BoardWatchDogController(ISessionComponent sessionComponent) {
		super(sessionComponent);
	}

	@Override
	public void componentDestructor(ComponentDestructorEvent event) {
		super.componentDestructor(event);
		getProcess().runControlledTaskTimer(() -> {
			for (var player : getSession().getPlayers()) {
				if (stopWhenPlayerExitSpace(player))
					return;
			}
		}, Time.Seconds(4), Time.Seconds(4));
	}

	@Nullable
	@Override
	public Listener getListener() {
		return new BoardWatchDogListener();
	}

	public boolean stopWhenPlayerExitSpace(Player player) {
		var space = getSession().getCurrentBoard()
		                        .getSpace();
		if (!space.isLocationIn(player.getLocation())) {
			getSession().stop(new ExceptionCause("Player §7" + player.getName() + "§f illegal exit board space §7(" + getSession()
					.getCurrentBoard()
					.getName() + "§7)"));
			return true;
		}
		return false;
	}

	public void warnWhenAlienAdminInSpace(Player player, Location backLocation) {
		if (player.getGameMode() != GameMode.SPECTATOR) {
			player.sendMessage("§4[ADMIN] §fYou tried to get into a session without §c§nSPECTATOR§f mode!");
			player.teleport(backLocation);
		} else {
			player.sendMessage("§4[ADMIN] §fYou got into the session as an unregistered spectator!");
			player.sendMessage("§7(Session name: §f§n" + getSession().getName() + "§7, Board name: §f§n" + getSession()
					.getCurrentBoard()
					.getName() + "§7)");
		}
	}

	public void warnWhenAlienInSpace(Player player, Location backLocation) {
		if (getSession().getPlayers()
		                .contains(player))
			return;
		var space = getSession().getCurrentBoard()
		                        .getSpace();
		if (space.isLocationIn(player.getLocation())) {
			if (player.isOp()) {
				warnWhenAlienAdminInSpace(player, backLocation);
			} else {
				player.teleport(backLocation);
				new ServerWarning("Player §7" + player.getName() + "§f illegal entry board space §7(" + getSession()
						.getCurrentBoard()
						.getName() + "§7)");
			}
		}
	}

	@Override
	public @NotNull String getName() {
		return "SystemBoardWatchDogController";
	}
}