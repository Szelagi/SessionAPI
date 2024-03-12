package pl.szelagi.buildin.system.testsession;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import pl.szelagi.component.board.Board;
import pl.szelagi.component.session.Session;

public class TestSession extends Session {
    public TestSession(JavaPlugin plugin, Player player) {
        super(plugin, player);
    }

    @NotNull
    @Override
    protected Board getDefaultStartBoard() {
        return new TestBoard(this);
    }

    @NotNull
    @Override
    public String getName() {
        return "testSession";
    }
}
