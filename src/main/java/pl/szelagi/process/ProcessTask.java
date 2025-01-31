/*
 * SessionAPI - A framework for game containerization on Minecraft servers
 * Copyright (C) 2025 Szelagi (https://github.com/Szelagi/SessionAPI)
 * Licensed under the GNU General Public License v3.0.
 * For more details, visit <https://www.gnu.org/licenses/>.
 */

package pl.szelagi.process;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

public class ProcessTask implements BukkitTask {
	private final BukkitTask bukkitTask;
	private final RemoteProcess remoteProcess;

	public ProcessTask(RemoteProcess remoteProcess, BukkitTask bukkitTask) {
		this.remoteProcess = remoteProcess;
		this.bukkitTask = bukkitTask;
	}

	@Override
	public int getTaskId() {
		return bukkitTask.getTaskId();
	}

	@Override
	public @NotNull Plugin getOwner() {
		return bukkitTask.getOwner();
	}

	@Override
	public boolean isSync() {
		return bukkitTask.isSync();
	}

	@Override
	public boolean isCancelled() {
		return bukkitTask.isCancelled();
	}

	@Override
	public void cancel() {
		remoteProcess.stopControlledTask(this);
	}

	public BukkitTask getBukkitTask() {
		return bukkitTask;
	}
}
