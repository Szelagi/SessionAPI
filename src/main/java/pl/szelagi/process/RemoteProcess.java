package pl.szelagi.process;

import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import pl.szelagi.component.controller.Controller;
import pl.szelagi.util.timespigot.Time;

import java.util.ArrayList;

public class RemoteProcess extends Process {
	private final MainProcess mainControlProcess;

	public RemoteProcess(MainProcess mainControlProcess) {
		super(mainControlProcess.getPlugin());
		this.mainControlProcess = mainControlProcess;
	}

	@Override
	public void destroy() {
		stopAllTasks();
		stopAllControllers();
	}

	@Override
	public void optimiseTasks() {
		var unableTasks = new ArrayList<BukkitTask>();
		boolean isQueued;
		boolean isRunning;
		boolean isExist;
		boolean isCanceled;
		for (var task : getTasks()) {
			isQueued = getPlugin().getServer().getScheduler().isQueued(task.getTaskId());
			isRunning = getPlugin().getServer().getScheduler().isCurrentlyRunning(task.getTaskId());
			isExist = isQueued || isRunning;
			isCanceled = task.isCancelled();
			if (isCanceled || !isExist) {
				unableTasks.add(task);
			}
		}

		for (var task : unableTasks) {
			getTasks().remove(task);
		}
		mainControlProcess.forceStopTasks(unableTasks);
	}

	@Override
	protected void stopAllControllers() {
		mainControlProcess.forceStopControllers(getControllers());
		getControllers().clear();
	}

	@Override
	protected void stopAllTasks() {
		mainControlProcess.forceStopTasks(getTasks());
		getTasks().clear();
	}

	@Override
	public void registerController(Controller controller) {
		getControllers().add(controller);
		mainControlProcess.registerController(controller);
	}

	@Override
	public void unregisterController(Controller controller) {
		getControllers().remove(controller);
		mainControlProcess.unregisterController(controller);
	}

	@Override
	public @NotNull ProcessTask runControlledTask(@NotNull Runnable runnable) {
		var task = mainControlProcess.runControlledBukkitTask(runnable);
		getTasks().add(task);
		return new ProcessTask(this, task);
	}

	@Override
	public @NotNull ProcessTask runControlledTaskAsynchronously(@NotNull Runnable runnable) {
		var task = mainControlProcess.runControlledBukkitTaskAsynchronously(runnable);
		getTasks().add(task);
		return new ProcessTask(this, task);
	}

	@Override
	public @NotNull ProcessTask runControlledTaskLater(@NotNull Runnable runnable, @NotNull Time laterTime) {
		var task = mainControlProcess.runControlledBukkitTaskLater(runnable, laterTime);
		getTasks().add(task);
		return new ProcessTask(this, task);
	}

	@Override
	public @NotNull ProcessTask runControlledTaskLaterAsynchronously(@NotNull Runnable runnable, @NotNull Time laterTime) {
		var task = mainControlProcess.runControlledBukkitTaskLaterAsynchronously(runnable, laterTime);
		getTasks().add(task);
		return new ProcessTask(this, task);
	}

	@Override
	public @NotNull ProcessTask runControlledTaskTimer(@NotNull Runnable runnable, @NotNull Time laterTime, @NotNull Time repeatTime) {
		var task = mainControlProcess.runControlledBukkitTaskTimer(runnable, laterTime, repeatTime);
		getTasks().add(task);
		return new ProcessTask(this, task);
	}

	@Override
	public @NotNull ProcessTask runControlledTaskTimerAsynchronously(@NotNull Runnable runnable, @NotNull Time laterTime, @NotNull Time repeatTime) {
		var task = mainControlProcess.runControlledBukkitTaskTimerAsynchronously(runnable, laterTime, repeatTime);
		getTasks().add(task);
		return new ProcessTask(this, task);
	}

	@Override
	public void stopControlledTask(@NotNull BukkitTask bukkitTask) {
		mainControlProcess.stopControlledTask(bukkitTask);
		getTasks().remove(bukkitTask);
	}
}
