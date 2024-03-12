package pl.szelagi.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import pl.szelagi.buildin.system.testsession.TestSession;

import java.util.ArrayList;
import java.util.List;

public class CommandExecutor implements org.bukkit.command.CommandExecutor {
    private final JavaPlugin plugin;
    public static final ArrayList<String> COMMAND_NAMES = new ArrayList<>(List.of(
            "board-save", "board-edit", "board-exit",
            "session-exit", "session-add-player", "session-remove-player",
            "test-session"
    ));
    public static void initialize(JavaPlugin plugin) {
        var executor = new CommandExecutor(plugin);
        for (var commandName : COMMAND_NAMES) {
            var command = plugin.getCommand(commandName);
            if (command != null) command.setExecutor(executor);
        }
    }

    public CommandExecutor(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage("§cCommandSender must be a Player!");
            return false;
        }
        if (!player.isOp()) {
            player.sendMessage("§cYou are not server operator!");
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
            case "test-session" -> {
                new TestSession(plugin, player).start();
            }
        }
        return true;
    }
}
