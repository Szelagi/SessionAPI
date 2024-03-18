package pl.szelagi.buildin.system.recovery;

import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.szelagi.component.controller.Controller;
import pl.szelagi.component.session.Session;
import pl.szelagi.event.component.ComponentConstructorEvent;
import pl.szelagi.event.component.ComponentDestructorEvent;
import pl.szelagi.event.player.initialize.PlayerConstructorEvent;
import pl.szelagi.event.player.initialize.PlayerDestructorEvent;
import pl.szelagi.recovery.RecoveryFileManager;
import pl.szelagi.util.timespigot.Time;

public class RecoveryPlayerController extends Controller {
	private final RecoveryFileManager recoveryFileManager = new RecoveryFileManager();
	private boolean isDestructed = false;

	public RecoveryPlayerController(Session sessionComponent) {
		super(sessionComponent);
	}

	@Nullable
	@Override
	public Listener getListener() {
		return super.getListener();
	}

	@Override
	public void componentConstructor(ComponentConstructorEvent event) {
		super.componentConstructor(event);
		getProcess().runControlledTaskTimer(this::save, Time.Seconds(1), Time.Seconds(1));
	}

	@Override
	public void componentDestructor(ComponentDestructorEvent event) {
		super.componentDestructor(event);
		isDestructed = true;
	}

	@Override
	public void playerConstructor(PlayerConstructorEvent event) {
		super.playerConstructor(event);
		recoveryFileManager.savePlayerRecovery(event.getPlayer());
	}

	@Override
	public void playerDestructor(PlayerDestructorEvent event) {
		super.playerDestructor(event);
		if (recoveryFileManager.existsPlayerRecovery(event.getPlayer())) {
			recoveryFileManager.deletePlayerRecovery(event.getPlayer());
		}
	}

	public void save() {
		if (isDestructed)
			return;
		for (var player : getSession().getPlayers()) {
			recoveryFileManager.savePlayerRecovery(player);
		}
	}

	@Override
	public @NotNull String getName() {
		return "SystemRecoveryPlayerController";
	}
}
