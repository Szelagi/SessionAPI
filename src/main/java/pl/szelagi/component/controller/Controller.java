package pl.szelagi.component.controller;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.MustBeInvokedByOverriders;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.szelagi.component.BaseComponent;
import pl.szelagi.component.ComponentStatus;
import pl.szelagi.component.ISessionComponent;
import pl.szelagi.component.baseexception.StartException;
import pl.szelagi.component.baseexception.StopException;
import pl.szelagi.component.controller.event.ControllerStartEvent;
import pl.szelagi.component.controller.event.ControllerStopEvent;
import pl.szelagi.component.session.Session;
import pl.szelagi.process.RemoteProcess;
import pl.szelagi.util.Debug;

public abstract class Controller extends BaseComponent {
	private final JavaPlugin plugin;
	private final Session session;
	private final RemoteProcess parentProcess;
	private RemoteProcess remoteProcess;

	public Controller(ISessionComponent sessionComponent) {
		this(sessionComponent.getSession(), sessionComponent.getProcess());
	}

	public Controller(ISessionComponent sessionComponent, RemoteProcess parentProcess) {
		this(sessionComponent.getSession(), parentProcess);
	}

	private Controller(Session session, RemoteProcess parentProcess) {
		this.plugin = session.getPlugin();
		this.session = session;
		this.parentProcess = parentProcess;
	}

	@MustBeInvokedByOverriders
	public void start() throws StartException {
		validateStartable();
		setStatus(ComponentStatus.RUNNING);
		Debug.send(this, "start");

		remoteProcess = new RemoteProcess(this);
		remoteProcess.registerListener(this);

		invokeSelf();

		// ControllerStartEvent
		var event = new ControllerStartEvent(this);
		callBukkitEvent(event);
	}

	@MustBeInvokedByOverriders
	public void stop() throws StopException {
		validateDisableable();

		Debug.send(this, "stop");

		invokeSelfPlayerDestructors();
		invokeSelfComponentDestructor();

		setSynchronized(false);

		// destroy hierarchy, tasks, listeners
		remoteProcess.destroy();

		setStatus(ComponentStatus.SHUTDOWN);
		// ControllerStopEvent
		var event = new ControllerStopEvent(this);
		Bukkit.getPluginManager()
		      .callEvent(event);
	}

	public final RemoteProcess getProcess() {
		return remoteProcess;
	}

	public final @NotNull JavaPlugin getPlugin() {
		return plugin;
	}

	public final @NotNull Session getSession() {
		return session;
	}

	public final RemoteProcess getParentProcess() {
		return parentProcess;
	}

	public @Nullable Listener getListener() {
		return null;
	}
}