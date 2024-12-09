package pl.szelagi.command.editor;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.szelagi.SessionAPI;

import static pl.szelagi.command.CommandHelper.PREFIX;

public class ListBoardsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) return false;

        var files = SessionAPI.BOARD_DIRECTORY.listFiles();
        if (files == null || files.length == 0) {
            player.sendMessage(PREFIX + "§cNo board saves found.");
            return false;
        }
        player.sendMessage(PREFIX + "§aAvailable board saves:");
        for (var file : files) {
            if (!file.isDirectory())
                continue;
            player.sendMessage("§8- §f" + file.getName());
        }

        return true;
    }
}
