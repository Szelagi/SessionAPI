package pl.szelagi.process;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import pl.szelagi.component.controller.Controller;

import java.util.ArrayList;

public abstract class Process implements IControlProcess {
    private final JavaPlugin plugin;
    private final ArrayList<BukkitTask> tasks = new ArrayList<>();
    private final ArrayList<Controller> controllers = new ArrayList<>();

    public Process(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    protected JavaPlugin getPlugin() {
        return plugin;
    }

    public ArrayList<BukkitTask> getTasks() {
        return tasks;
    }

    public ArrayList<Controller> getControllers() {
        return controllers;
    }

    public abstract void destroy();
    public abstract void optimiseTasks();
    protected abstract void stopAllControllers();
    protected abstract void stopAllTasks();
}
