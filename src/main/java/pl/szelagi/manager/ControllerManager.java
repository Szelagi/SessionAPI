package pl.szelagi.manager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import pl.szelagi.component.controller.Controller;
import pl.szelagi.component.controller.event.ControllerStartEvent;
import pl.szelagi.component.controller.event.ControllerStopEvent;
import pl.szelagi.component.session.Session;
import pl.szelagi.manager.compare.CompareController;
import pl.szelagi.util.IncrementalGenerator;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;

public class ControllerManager {
    private static final HashMap<String, Listener> controllerListenerMap = new HashMap<>();
    private static final HashMap<String, ArrayList<Long>> controllerIdMap = new HashMap<>();
    private final static IncrementalGenerator idGenerator = new IncrementalGenerator();
    private static JavaPlugin plugin;

    public static IncrementalGenerator getIdGenerator() {
        return idGenerator;
    }

    public static void initialize(JavaPlugin p) {
        plugin = p;
        class MyControllerManager implements Listener {

            @EventHandler(ignoreCancelled = true)
            public void onControllerStart(ControllerStartEvent event) {
                ControllerManager.onControllerStart(event.getController());
            }

            @EventHandler(ignoreCancelled = true)
            public void onControllerStop(ControllerStopEvent event) {
                ControllerManager.onControllerStop(event.getController());
            }

        }
        Bukkit.getPluginManager().registerEvents(new MyControllerManager(), p);
    }

    private static int countControllerCurrentEnable(String name) {
        var ids = controllerIdMap.get(name);
        if (ids == null) return 0;
        return ids.size();
    }
    public static void onControllerStart(Controller controller) {
        var name = controller.getName();
        var id = controller.getId();
        var ids = controllerIdMap.get(name);
        if (ids == null) {
            ids = new ArrayList<>();
            controllerIdMap.put(name, ids);
        }
        if (ids.contains(id)) return;
        if (ids.isEmpty()) {
            startControllerListener(controller);
        }
        ids.add(id);
    }
    public static void onControllerStop(Controller controller) {
        var name = controller.getName();
        var id = controller.getId();
        var ids = controllerIdMap.get(name);
        if (ids == null) {
            ids = new ArrayList<>();
            controllerIdMap.put(name, ids);
        }
        if (!ids.contains(id)) return;
        ids.remove(id);
        if (ids.isEmpty()) {
            stopControllerListener(controller);
            controllerIdMap.remove(name); // added
        }
    }
    private static void startControllerListener(Controller controller) {
        var name = controller.getName();
        var listener = controllerListenerMap.get(name);
        if (listener == null) {
            listener = controller.getListener();
            if (listener == null) return;
            controllerListenerMap.put(name, listener);
        }
        Bukkit.getPluginManager().registerEvents(listener, plugin);
    }
    private static void stopControllerListener(Controller controller) {
        var name = controller.getName();
        var listener = controllerListenerMap.get(name);
        if (listener == null) return;
        HandlerList.unregisterAll(listener);
        controllerListenerMap.remove(name); // delete listener when is disable
    }
    // methods // problem getMainProcess
    @Nonnull
    public static <T extends Controller> ArrayList<T> getControllers(Session d, CompareController cp) {
        var out = new ArrayList<T>();
        if (d == null) return out;
        for (var c : d.getMainProcess().getControllers()) {
            if (cp.compare(c)) out.add((T) c);
        }
        return out;
    }
    @Nonnull
    public static <T extends Controller> ArrayList<T> getControllers(Player p, CompareController cp) {
        var d = SessionManager.getSession(p);
        if (d == null) return new ArrayList<>();
        return getControllers(d, cp);
    }
    @Nullable
    public static <T extends Controller> T getFirstController(Session d, CompareController cp) {
        if (d == null) return null;
        for (var c : d.getMainProcess().getControllers()) {
            if (cp.compare(c)) return (T) c;
        }
        return null;
    }
    @Nullable
    public static <T extends Controller> T getFirstController(Player p, CompareController cp) {
        var d = SessionManager.getSession(p);
        if (d == null) return null;
        return getFirstController(d, cp);
    }
}
