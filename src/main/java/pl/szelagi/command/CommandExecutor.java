package pl.szelagi.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CommandExecutor implements org.bukkit.command.CommandExecutor {
    public static final ArrayList<String> COMMAND_NAMES = new ArrayList<>(List.of(
            "board-save", "board-edit", "board-exit",
            "session-exit", "session-add-player", "session-remove-player"
    ));
    public static void initialize(JavaPlugin plugin) {
        var executor = new CommandExecutor();
        for (var commandName : COMMAND_NAMES) {
            var command = plugin.getCommand(commandName);
            if (command != null) command.setExecutor(executor);
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage("§cCommandSender must be a Player!");
            return false;
        }
        switch (command.getName()) {
//          Board editor
            case "board-save" -> {

            }
            case "board-edit" -> {

            }
            case "board-exit" -> {

            }

//          Session manage
            case "session-exit" -> {

            }
            case "session-add-player" -> {

            }
            case "session-remove-player" -> {

            }
        }
        return false;
    }
}
