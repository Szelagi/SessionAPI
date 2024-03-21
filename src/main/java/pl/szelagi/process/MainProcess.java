package pl.szelagi.process;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.szelagi.component.controller.Controller;
import pl.szelagi.component.session.Session;
import pl.szelagi.event.BaseEvent;
import pl.szelagi.process.exception.MultiDestroyException;
import pl.szelagi.util.ReverseStream;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class MainProcess extends Process {
	private final Session session;

	public MainProcess(Session session) {
		super(session.getPlugin());
		this.session = session;
	}

	@Override
	public void destroy() throws MultiDestroyException {
		if (isDestroyed())
			throw new MultiDestroyException("");
		setDestroyed();
		var remoteProcesses = new ArrayList<>(getRemoteProcesses());
		remoteProcesses.forEach(RemoteProcess::destroy);
	}

	public void invokeAllListeners(BaseEvent event) {
		var remoteProcess = new ArrayList<>(getRemoteProcesses());
		remoteProcess.stream()
		             .map(RemoteProcess::getListeners)
		             .forEach(eventListeners -> {
			             var clone = new ArrayList<>(eventListeners);
			             clone.forEach(event::call);
		             });
	}

	public void invokeReverseAllListeners(BaseEvent event) {
		var remoteProcess = new ArrayList<>(getRemoteProcesses());
		ReverseStream
				.reverse(remoteProcess.stream())
				.map(RemoteProcess::getListeners)
				.forEach(eventListeners -> {
					var clone = new ArrayList<>(eventListeners);
					ReverseStream
							.reverse(clone.stream())
							.forEach(event::call);
				});
	}

	public void optimiseTasks() {
		getRemoteProcesses().forEach(RemoteProcess::optimiseTasks);
	}

	@NotNull
	public <T extends Controller> ArrayList<T> getControllers(@NotNull Class<T> clazz) {
		return getRemoteProcesses().stream()
		                           .filter(RemoteProcess::isController)
		                           .map(RemoteProcess::getComponent)
		                           .filter(clazz::isInstance)
		                           .map(clazz::cast)
		                           .collect(Collectors.toCollection(ArrayList::new));
	}

	@Nullable
	public <T extends Controller> T getFirstController(@NotNull Class<T> clazz) {
		return getRemoteProcesses().stream()
		                           .filter(RemoteProcess::isController)
		                           .map(RemoteProcess::getComponent)
		                           .filter(clazz::isInstance)
		                           .map(clazz::cast)
		                           .findFirst()
		                           .orElse(null);
	}
}
