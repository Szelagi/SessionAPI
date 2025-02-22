/*
 * SessionAPI - A framework for game containerization on Minecraft servers
 * Copyright (C) 2025 Szelagi (https://github.com/Szelagi/SessionAPI)
 * Licensed under the GNU General Public License v3.0.
 * For more details, visit <https://www.gnu.org/licenses/>.
 */

package pl.szelagi.manager;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.szelagi.component.controller.Controller;
import pl.szelagi.component.session.Session;
import pl.szelagi.util.TreeAnalyzer;

import java.util.List;

public class ControllerManager {
    private static JavaPlugin plugin;

    public static void initialize(JavaPlugin p) {
        plugin = p;
    }

    @Deprecated
    @NotNull
    public static <T extends Controller> List<T> getControllers(@Nullable Session session, @NotNull Class<T> clazz) {
        var analyze = new TreeAnalyzer(session);
        return analyze.layers().values().stream()
                .flatMap(List::stream)
                .filter(clazz::isInstance)
                .map(clazz::cast)
                .toList();
    }

    @Deprecated
    @Nullable
    public static <T extends Controller> T getFirstController(@Nullable Session session, @NotNull Class<T> clazz) {
        var analyze = new TreeAnalyzer(session);
        return analyze.layers().values().stream()
                .flatMap(List::stream)
                .filter(clazz::isInstance)
                .map(clazz::cast)
                .findFirst().orElse(null);
    }
}
