/*
 * SessionAPI - A framework for game containerization on Minecraft servers
 * Copyright (C) 2025 Szelagi (https://github.com/Szelagi/SessionAPI)
 * Licensed under the GNU General Public License v3.0.
 * For more details, visit <https://www.gnu.org/licenses/>.
 */

package pl.szelagi.command.manage;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.szelagi.command.CommandHelper;

import java.util.List;
import java.util.stream.Collectors;

import static pl.szelagi.command.CommandHelper.PREFIX;

public class PlayerSessionCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) return false;

        if (strings.length != 2) {
            if (command.getName().equals("session-add-player")) {
                player.sendMessage(PREFIX + "§cInvalid pattern! Usage: /session-add-player <session-name>:<session-id> <player-name>");
            } else {
                player.sendMessage(PREFIX + "§cInvalid pattern! Usage: /session-remove-player <session-name>:<session-id> <player-name>");
            }
            return false;
        }

        var session = CommandHelper.selectSession(commandSender, strings[0]);
        if (session == null) return false;

        var addPlayer = CommandHelper.selectPlayer(commandSender, strings[1]);
        if (addPlayer == null) return false;

        if (command.getName().equals("session-add-player")) {
            session.addPlayer(addPlayer);
            player.sendMessage(PREFIX + "§aPlayer " + strings[1] + " added to session: §f" + strings[0]);
        } else {
            session.removePlayer(addPlayer);
            player.sendMessage(PREFIX + "§aPlayer " + strings[1] + " removed from session: §f" + strings[0]);
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        final var argCount = strings.length;

        return switch (argCount) {
            case 1 -> CommandHelper.sessionsComplete(commandSender);
            case 2 -> Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
            default -> null;
        };

    }
}
