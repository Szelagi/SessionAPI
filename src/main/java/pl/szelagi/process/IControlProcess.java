package pl.szelagi.process;

import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import pl.szelagi.component.controller.Controller;
import pl.szelagi.util.timespigot.Time;

import java.util.ArrayList;

public interface IControlProcess {
    void registerController(Controller controller);
    void unregisterController(Controller controller);
    @NotNull ProcessTask runControlledTask(@NotNull Runnable runnable);
    @NotNull ProcessTask runControlledTaskAsynchronously(@NotNull Runnable runnable);
    @NotNull ProcessTask runControlledTaskLater(@NotNull Runnable runnable, @NotNull Time laterTime);
    @NotNull ProcessTask runControlledTaskLaterAsynchronously(@NotNull Runnable runnable, @NotNull Time laterTime);
    @NotNull ProcessTask runControlledTaskTimer(@NotNull Runnable runnable, @NotNull Time laterTime, @NotNull Time repeatTime);
    @NotNull ProcessTask runControlledTaskTimerAsynchronously(@NotNull Runnable runnable, @NotNull Time laterTime, @NotNull Time repeatTime);
    void stopControlledTask(@NotNull BukkitTask bukkitTask);
    default void stopControlledTask(@NotNull ProcessTask processTask) {
        processTask.cancel();
    }
    ArrayList<BukkitTask> getTasks();
    ArrayList<Controller> getControllers();
}