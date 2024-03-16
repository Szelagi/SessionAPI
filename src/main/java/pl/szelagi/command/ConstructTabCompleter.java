package pl.szelagi.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.szelagi.SessionAPI;
import pl.szelagi.manager.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class ConstructTabCompleter implements TabCompleter {
    private final JavaPlugin plugin;
    public static final ArrayList<String> TAB_COMMAND_NAMES = new ArrayList<>(List.of(
            "board-edit", "session-join"
    ));
    public static void initialize(JavaPlugin plugin) {
        var tabCompleter = new ConstructTabCompleter(plugin);
        for (var commandName : TAB_COMMAND_NAMES) {
            var command = plugin.getCommand(commandName);
            if (command != null) {
                command.setTabCompleter(tabCompleter);
            }
        }
    }

    public ConstructTabCompleter(JavaPlugin plugin) {
        this.plugin = plugin;
    }
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        switch (command.getName()) {
            case "board-edit" -> {
                var files = SessionAPI.BOARD_DIRECTORY.listFiles();
                if (files == null || files.length == 0) return null;
                var list = new ArrayList<String>();
                for (var file : files) {
                    if (!file.isDirectory()) continue;
                    list.add(file.getName());
                }
                return list;
            }
            case "session-join" -> {
                return SessionManager.getSessions().stream()
                        .map(session -> session.getName() + ":" + session.getId()).toList();
            }
        }
        return null;
    }
}
