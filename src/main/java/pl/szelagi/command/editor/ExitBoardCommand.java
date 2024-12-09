package pl.szelagi.command.editor;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.szelagi.buildin.creator.Creator;
import pl.szelagi.component.session.cause.NeutralCause;
import pl.szelagi.manager.SessionManager;

import static pl.szelagi.command.CommandHelper.PREFIX;

public class ExitBoardCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) return false;

        var session = SessionManager.getSession(player);
        if (session == null) {
            player.sendMessage(PREFIX + "§cYou are not currently in a session.");
            return false;
        }
        if (!(session instanceof Creator creator)) {
            player.sendMessage(PREFIX + "§cYou are not in the editor.");
            return false;
        }
        creator.stop(new NeutralCause("FORCE_STOP"));
        player.sendMessage(PREFIX + "§aYou have successfully exited the board editor.");
        return true;
    }
}
