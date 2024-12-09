package pl.szelagi.command.manage;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.szelagi.command.CommandHelper;
import pl.szelagi.component.session.exception.player.initialize.RejectedPlayerException;
import pl.szelagi.manager.SessionManager;

import java.util.List;

import static pl.szelagi.command.CommandHelper.PREFIX;

public class JoinSessionCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) return false;

        if (strings.length != 1) {
            player.sendMessage(PREFIX + "§cInvalid pattern. Use: /session-join <session-name>:<session-id>");
            return false;
        }

        var session = CommandHelper.sessionByNameId(strings[0]);

        if (session == null) {
            player.sendMessage(PREFIX + "§cSession not found with pattern: '" + strings[0] + "'");
            return false;
        }
        try {
            session.addPlayer(player);
        } catch (
                RejectedPlayerException rejectedPlayerException) {
            player.sendMessage(PREFIX + "§cUnable to join session: " + rejectedPlayerException.getMessage());
        }

        player.sendMessage(PREFIX + "§aSuccessfully joined the session: §f" + strings[0]);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return SessionManager.getSessions().stream().map(session -> session.getName() + ":" + session.getId()).toList();
    }
}
