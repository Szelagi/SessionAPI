package pl.szelagi.process;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

public class ProcessTask implements BukkitTask {
    private final BukkitTask bukkitTask;
    private final Process process;

    public ProcessTask(Process process, BukkitTask bukkitTask) {
        this.process = process;
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
        process.stopControlledTask(bukkitTask);
    }

}
