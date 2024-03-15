package pl.szelagi.process;

import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import pl.szelagi.component.controller.Controller;
import pl.szelagi.component.session.Session;
import pl.szelagi.util.timespigot.Time;

import java.util.ArrayList;

public class MainProcess extends Process {
    private final Session session;
    public MainProcess(Session session) {
        super(session.getPlugin());
        this.session = session;
    }

    protected Session getDungeon() {
        return session;
    }

    protected void forceStopTasks(ArrayList<BukkitTask> tasks) {
        ArrayList<BukkitTask> cloneArrayListTasks = new ArrayList<>();
        cloneArrayListTasks.addAll(tasks);

        for (var task : cloneArrayListTasks ) {
            getTasks().remove(task);
            task.cancel();
        }
    }

    protected void forceStopControllers(ArrayList<Controller> controllers) {
        ArrayList<Controller> cloneArrayListControllers = new ArrayList<>();
        cloneArrayListControllers.addAll(controllers);

        for (var controller : cloneArrayListControllers ) {
            getControllers().remove(controller);
            controller.stop();
        }
    }

    @Override
    public void destroy() {
        stopAllTasks();
        stopAllControllers();
    }

    @Override
    public void optimiseTasks() {
        boolean isQueued;
        boolean isRunning;
        boolean isExist;
        boolean isCanceled;

        ArrayList<BukkitTask> cloneArrayListTasks = new ArrayList<>();
        cloneArrayListTasks.addAll(getTasks());

        for (var task : cloneArrayListTasks) {
            isQueued = getPlugin().getServer().getScheduler().isQueued(task.getTaskId());
            isRunning = getPlugin().getServer().getScheduler().isCurrentlyRunning(task.getTaskId());
            isExist = isQueued || isRunning;
            isCanceled = task.isCancelled();
            if (isCanceled || !isExist) {
                if (!isCanceled) task.cancel();
                getTasks().remove(task);
            }
        }
    }

    @Override
    protected void stopAllControllers() {
        ArrayList<Controller> cloneArrayListControllers = new ArrayList<>();
        cloneArrayListControllers.addAll(getControllers());

        for (var controller : cloneArrayListControllers ) {
            controller.stop();
        }
    }

    @Override
    protected void stopAllTasks() {
        ArrayList<BukkitTask> cloneArrayListTasks = new ArrayList<>();
        cloneArrayListTasks.addAll(getTasks());

        for (var task : cloneArrayListTasks) {
            task.cancel();
        }
    }

    @Override
    public void registerController(Controller controller) {
        getControllers().add(controller);
    }

    @Override
    public void unregisterController(Controller controller) {
        getControllers().remove(controller);
    }
    @Override
    public @NotNull ProcessTask runControlledTask(@NotNull Runnable runnable) {
        var task = runControlledBukkitTask(runnable);
        return new ProcessTask(this, task);
    }
    protected @NotNull BukkitTask runControlledBukkitTask(@NotNull Runnable runnable) {
        var bukkitTask = getPlugin().getServer().getScheduler().runTask(getPlugin(), runnable);
        getTasks().add(bukkitTask);
        return bukkitTask;
    }

    @Override
    public @NotNull ProcessTask runControlledTaskAsynchronously(@NotNull Runnable runnable) {
        var task = runControlledBukkitTaskAsynchronously(runnable);
        return new ProcessTask(this, task);
    }

    protected @NotNull BukkitTask runControlledBukkitTaskAsynchronously(@NotNull Runnable runnable) {
        var bukkitTask = getPlugin().getServer().getScheduler().runTaskAsynchronously(getPlugin(), runnable);
        getTasks().add(bukkitTask);
        return bukkitTask;
    }

    @Override
    public @NotNull ProcessTask runControlledTaskLater(@NotNull Runnable runnable, @NotNull Time laterTime) {
        var task = runControlledBukkitTaskLater(runnable, laterTime);
        return new ProcessTask(this, task);
    }

    protected @NotNull BukkitTask runControlledBukkitTaskLater(@NotNull Runnable runnable, @NotNull Time laterTime) {
        var bukkitTask = getPlugin().getServer().getScheduler().runTaskLater(getPlugin(), runnable, laterTime.toTicks());
        getTasks().add(bukkitTask);
        return bukkitTask;
    }

    @Override
    public @NotNull ProcessTask runControlledTaskLaterAsynchronously(@NotNull Runnable runnable, @NotNull Time laterTime) {
        var task = runControlledBukkitTaskLaterAsynchronously(runnable, laterTime);
        return new ProcessTask(this, task);
    }

    protected @NotNull BukkitTask runControlledBukkitTaskLaterAsynchronously(@NotNull Runnable runnable, @NotNull Time laterTime) {
        var bukkitTask = getPlugin().getServer().getScheduler().runTaskLaterAsynchronously(getPlugin(), runnable, laterTime.toTicks());
        getTasks().add(bukkitTask);
        return bukkitTask;
    }

    @Override
    public @NotNull ProcessTask runControlledTaskTimer(@NotNull Runnable runnable, @NotNull Time laterTime, @NotNull Time repeatTime) {
        var task = runControlledBukkitTaskTimer(runnable, laterTime, repeatTime);
        return new ProcessTask(this, task);
    }

    protected @NotNull BukkitTask runControlledBukkitTaskTimer(@NotNull Runnable runnable, @NotNull Time laterTime, @NotNull Time repeatTime) {
        var bukkitTask = getPlugin().getServer().getScheduler().runTaskTimer(getPlugin(), runnable, laterTime.toTicks(), repeatTime.toTicks());
        getTasks().add(bukkitTask);
        return bukkitTask;
    }

    @Override
    public @NotNull ProcessTask runControlledTaskTimerAsynchronously(@NotNull Runnable runnable, @NotNull Time laterTime, @NotNull Time repeatTime) {
        var task = runControlledBukkitTaskTimerAsynchronously(runnable, laterTime, repeatTime);
        return new ProcessTask(this, task);
    }

    protected @NotNull BukkitTask runControlledBukkitTaskTimerAsynchronously(@NotNull Runnable runnable, @NotNull Time laterTime, @NotNull Time repeatTime) {
        var bukkitTask = getPlugin().getServer().getScheduler().runTaskTimerAsynchronously(getPlugin(), runnable, laterTime.toTicks(), repeatTime.toTicks());
        getTasks().add(bukkitTask);
        return bukkitTask;
    }

    @Override
    public void stopControlledTask(@NotNull BukkitTask bukkitTask) {
        bukkitTask.cancel();
        getTasks().remove(bukkitTask);
    }
}
