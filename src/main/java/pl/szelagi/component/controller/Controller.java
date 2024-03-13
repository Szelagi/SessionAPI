package pl.szelagi.component.controller;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.MustBeInvokedByOverriders;
import org.jetbrains.annotations.NotNull;
import pl.szelagi.component.BaseComponent;
import pl.szelagi.component.ISessionComponent;
import pl.szelagi.component.constructor.InitializeType;
import pl.szelagi.component.constructor.UninitializedType;
import pl.szelagi.component.controller.event.ControllerStartEvent;
import pl.szelagi.component.controller.event.ControllerStopEvent;
import pl.szelagi.component.session.Session;
import pl.szelagi.process.IControlProcess;
import pl.szelagi.process.RemoteProcess;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class Controller extends BaseComponent {
    private final JavaPlugin plugin;
    private final Session session;
    private final IControlProcess parentProcess;
    private boolean isLocalEnable;
    private final RemoteProcess remoteProcess;

    public Controller(ISessionComponent sessionComponent) {
        this(sessionComponent.getSession(), sessionComponent.getProcess());
    }
    public Controller(ISessionComponent sessionComponent, IControlProcess parentProcess) {
        this(sessionComponent.getSession(), parentProcess);
    }

    private Controller(Session session, IControlProcess parentProcess) {
        this.plugin = session.getPlugin();
        this.session = session;
        this.parentProcess = parentProcess;
        isLocalEnable = false;
        this.remoteProcess = new RemoteProcess(session.getMainProcess());
    }

    public @NotNull IControlProcess getProcess() {
        return remoteProcess;
    }

    public @NotNull JavaPlugin getPlugin() {
        return plugin;
    }
    public @NotNull Session getSession() {
        return session;
    }

    @Nullable
    public Listener getListener() {
        return null;
    }

    @MustBeInvokedByOverriders
    public void start() {
        for (var p : getPlugin().getServer().getOnlinePlayers()) p.sendMessage("Start: " + this.getName() + " contoller");

        if (isLocalEnable) return;
        isLocalEnable = true;

        // auto register
        parentProcess.registerController(this);

        constructor();
        for (var player : getSession().getPlayers())
            systemPlayerConstructor(player, InitializeType.COMPONENT_CONSTRUCTOR);

        // ControllerStartEvent
        var event = new ControllerStartEvent(this);
        Bukkit.getPluginManager().callEvent(event);
    }

    @MustBeInvokedByOverriders
    public void stop() {
        if (!isLocalEnable) return;
        isLocalEnable = false;

        for (var p : getPlugin().getServer().getOnlinePlayers()) p.sendMessage("Stop: " + this.getName() + " contoller");

        parentProcess.unregisterController(this); // unregister in parent
        remoteProcess.destroy(); // destroy children

        for (var player : getSession().getPlayers())
            systemPlayerDestructor(player, UninitializedType.COMPONENT_DESTRUCTOR);
        destructor();

        // ControllerStopEvent
        var event = new ControllerStopEvent(this);
        Bukkit.getPluginManager().callEvent(event);
    }
    public boolean isLocalEnable() {
        return isLocalEnable;
    }
    @Nonnull
    public abstract String getName();


    @MustBeInvokedByOverriders
    private void systemPlayerConstructor(Player player, InitializeType type) {
        playerConstructor(player, type);
        // recursive for player add
        if (type == InitializeType.PLAYER_ADD) {
            // controllers in
            for (var controller : getProcess().getControllers()) {
                reflectionSystemPlayerConstructor(controller, player, type);
            }
        }
    }

    @MustBeInvokedByOverriders
    private void systemPlayerDestructor(Player player, UninitializedType type) {
        // recursive for player remove
        if (type == UninitializedType.PLAYER_REMOVE) {
            // controllers in
            for (var controller : getProcess().getControllers()) {
                reflectionSystemPlayerDestructor(controller, player, type);
            }
        }
        playerDestructor(player, type);
    }
}