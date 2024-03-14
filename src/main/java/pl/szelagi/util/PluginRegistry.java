package pl.szelagi.util;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

public class PluginRegistry {
    private final static HashMap<File, JavaPlugin> REGISTRY = new HashMap<>();
    private static void updateRegistry() {
        JavaPlugin[] plugins = (JavaPlugin[]) Bukkit.getServer().getPluginManager().getPlugins();
        for (JavaPlugin javaPlugin : plugins) {
            try {
                Method getFileMethod = JavaPlugin.class.getDeclaredMethod("getFile");
                getFileMethod.setAccessible(true);
                File file = (File) getFileMethod.invoke(javaPlugin);
                REGISTRY.put(file, javaPlugin);
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static @Nullable JavaPlugin getPlugin(File jarFile) {
        var plugin = REGISTRY.get(jarFile);
        if (plugin == null) {
            updateRegistry();
            plugin = REGISTRY.get(jarFile);
        }
        return plugin;
    }
}
