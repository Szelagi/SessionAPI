/*
 * SessionAPI - A framework for game containerization on Minecraft servers
 * Copyright (C) 2025 Szelagi (https://github.com/Szelagi/SessionAPI)
 * Licensed under the GNU General Public License v3.0.
 * For more details, visit <https://www.gnu.org/licenses/>.
 */

package pl.szelagi.command.debug;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.szelagi.SessionAPI;
import pl.szelagi.buildin.system.testsession.TestSession;
import pl.szelagi.util.Debug;

import static pl.szelagi.command.CommandHelper.PREFIX;

public class DebugSessionCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) return false;
        if (Debug.isAllowView(player)) {
            Debug.denyView(player);
            player.sendMessage(PREFIX + "§aDebug mode has been §coff§a.");
        } else {
            Debug.allowView(player);
            player.sendMessage(PREFIX + "§aDebug mode has been §aon§a.");
        }
        return true;
    }
}
