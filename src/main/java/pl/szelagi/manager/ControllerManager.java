/*
 * SessionAPI - A framework for game containerization on Minecraft servers
 * Copyright (C) 2025 Szelagi (https://github.com/Szelagi/SessionAPI)
 * Licensed under the GNU General Public License v3.0.
 * For more details, visit <https://www.gnu.org/licenses/>.
 */

package pl.szelagi.manager;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.szelagi.component.controller.Controller;
import pl.szelagi.component.controller.event.ControllerStartEvent;
import pl.szelagi.component.controller.event.ControllerStopEvent;
import pl.szelagi.component.session.Session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ControllerManager {
	private static final HashMap<String, Listener> CONTROLLER_LISTENER_MAP = new HashMap<>();
	private static final HashMap<String, ArrayList<Long>> ENABLE_CONTROLLER_ID_MAP = new HashMap<>();
	private static JavaPlugin plugin;

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
		Bukkit.getPluginManager()
		      .registerEvents(new MyControllerManager(), p);
	}

	private static int controllerEnableCount(String name) {
		var ids = ENABLE_CONTROLLER_ID_MAP.get(name);
		if (ids == null)
			return 0;
		return ids.size();
	}

	public static void onControllerStart(Controller controller) {
		var name = controller.getName();
		var id = controller.getId();
		var ids = ENABLE_CONTROLLER_ID_MAP.computeIfAbsent(name, k -> new ArrayList<>());
		if (ids.contains(id))
			return;
		if (ids.isEmpty()) {
			startControllerListener(controller);
		}
		ids.add(id);
	}

	public static void onControllerStop(Controller controller) {
		var name = controller.getName();
		var id = controller.getId();
		var ids = ENABLE_CONTROLLER_ID_MAP.computeIfAbsent(name, k -> new ArrayList<>());
		if (!ids.contains(id))
			return;
		ids.remove(id);
		if (ids.isEmpty()) {
			stopControllerListener(controller);
			ENABLE_CONTROLLER_ID_MAP.remove(name); // added
		}
	}

	private static void startControllerListener(Controller controller) {
		var name = controller.getName();
		var listener = CONTROLLER_LISTENER_MAP.get(name);
		if (listener == null) {
			listener = controller.getListener();
			if (listener == null)
				return;
			CONTROLLER_LISTENER_MAP.put(name, listener);
		}
		Bukkit.getPluginManager()
		      .registerEvents(listener, plugin);
	}

	private static void stopControllerListener(Controller controller) {
		var name = controller.getName();
		var listener = CONTROLLER_LISTENER_MAP.get(name);
		if (listener == null)
			return;
		HandlerList.unregisterAll(listener);
		CONTROLLER_LISTENER_MAP.remove(name); // delete listener when is disable
	}

	//    @NotNull
	//    public static <T extends Controller> ArrayList<T> getAllowListenerControllers(Session session, Class<?> classType) {
	//
	//    }
	//    @Nullable
	//    public static <T extends Controller> T getFirstAllowListenerController(Session session, Class<?> classType) {
	//
	//    }

	// Enable controller
	@NotNull
	public static <T extends Controller> List<T> getControllers(@Nullable Session session, @NotNull Class<T> clazz) {
		if (session == null)
			return new ArrayList<>();
		return session.getMainProcess()
		              .getControllers(clazz);
	}

	@Nullable
	public static <T extends Controller> T getFirstController(@Nullable Session session, @NotNull Class<T> clazz) {
		if (session == null)
			return null;
		return session.getMainProcess()
		              .getFirstController(clazz);
	}
}
