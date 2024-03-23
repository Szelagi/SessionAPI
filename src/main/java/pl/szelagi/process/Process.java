package pl.szelagi.process;

import org.bukkit.plugin.java.JavaPlugin;
import pl.szelagi.process.exception.MultiDestroyException;
import pl.szelagi.process.exception.MultiRegisterException;
import pl.szelagi.process.exception.NotFoundUnregisterException;

import java.util.ArrayList;
import java.util.List;

public abstract class Process {
	private boolean isDestroyed;
	private final JavaPlugin plugin;
	private final ArrayList<RemoteProcess> remoteProcesses = new ArrayList<>();

	public Process(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	protected JavaPlugin getPlugin() {
		return plugin;
	}

	public List<RemoteProcess> getRemoteProcesses() {
		return remoteProcesses;
	}

	public void registerRemoteProcess(RemoteProcess process) throws MultiRegisterException {
		if (getRemoteProcesses().contains(process))
			throw new MultiRegisterException("");
		getRemoteProcesses().add(process);
	}

	public void unregisterRemoteProcess(RemoteProcess process) throws NotFoundUnregisterException {
		var removed = getRemoteProcesses().remove(process);
		if (!removed)
			throw new NotFoundUnregisterException("");
	}

	/**
	 * destroys current process and all child elements hierarchy
	 */
	public abstract void destroy() throws MultiDestroyException;

	public boolean isDestroyed() {
		return isDestroyed;
	}

	public void setDestroyed() {
		isDestroyed = true;
	}
}
