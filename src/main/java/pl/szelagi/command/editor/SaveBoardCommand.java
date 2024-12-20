package pl.szelagi.command.editor;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.szelagi.buildin.creator.Creator;
import pl.szelagi.buildin.creator.CreatorBoard;
import pl.szelagi.component.board.Board;
import pl.szelagi.manager.SessionManager;
import pl.szelagi.tag.TagAnalyzer;

import static pl.szelagi.command.CommandHelper.PREFIX;

public class SaveBoardCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) return false;

        var session = SessionManager.getSession(player);
        if (session == null) {
            player.sendMessage(PREFIX + "§cYou are not in a session.");
            return false;
        }
        if (!(session instanceof Creator creator)) {
            player.sendMessage(PREFIX + "§cYou are not in the editor.");
            return false;
        }
        if (!(creator.getCurrentBoard() instanceof CreatorBoard creatorBoard)) {
            player.sendMessage(PREFIX + "§cYou are not in an editor board.");
            return false;
        }

        player.sendMessage(PREFIX + "§7§oSaving...");
        long millis = System.currentTimeMillis();

        var optimized = creatorBoard
                .getStorage()
                .toOptimized();

        creatorBoard.getStorage()
                .saveSchematic(Board.SCHEMATIC_CONSTRUCTOR_NAME, optimized);
        creatorBoard.getStorage()
                .saveEmptySchematic(Board.SCHEMATIC_DESTRUCTOR_NAME, optimized);

        var data = TagAnalyzer.process(optimized);
        creatorBoard.getStorage()
                .saveSignTagData(Board.SIGN_TAG_DATA_NAME, data);

        long deltaMillis = System.currentTimeMillis() - millis;
        player.sendMessage(PREFIX + "§7Board size: §f" + optimized.size() + "§7, size-x: §f" + optimized.sizeX() + "§7, size-y: §f" + optimized.sizeY() + "§7, size-z: §f" + optimized.sizeZ() + "§7!");
        player.sendMessage(PREFIX + "§aBoard saved successfully! §f(" + deltaMillis + "ms)");

        return true;
    }
}
