package pl.szelagi.buildin.creator;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.szelagi.buildin.controller.NoCreatureDropController.NoCreatureDropController;
import pl.szelagi.buildin.controller.NoNatrualSpawnController.NoNaturalSpawnController;
import pl.szelagi.component.board.Board;
import pl.szelagi.component.board.filemanager.BoardFileManager;
import pl.szelagi.component.constructor.InitializeType;
import pl.szelagi.component.session.Session;


public class CreatorBoard extends Board {
    private BoardFileManager storage;
    private final String editName;
    public CreatorBoard(Session session, String editName) {
        super(session);
        this.editName = editName;
    }

    public BoardFileManager getStorage() {
        return storage;
    }

    @Override
    protected void generate() {
        for (var b : getSpace().getBlocksInArea()) b.setType(Material.AIR);
        this.storage = new BoardFileManager(editName, getSpace());
        getBase().getBlock().setType(Material.BEDROCK);
        if (storage.existsSchematic(SCHEMATIC_CONSTRUCTOR_NAME)) storage.loadSchematic(SCHEMATIC_CONSTRUCTOR_NAME);
    }
    @Override
    protected void degenerate() {
        for (var b : getSpace().getBlocksInArea()) b.setType(Material.AIR);
    }


    @NotNull
    @Override
    public String getName() {
        return "creatorBoard";
    }

    @Override
    public void start() {
        super.start();
        new NoNaturalSpawnController(this).start();
        new NoCreatureDropController(this).start();
    }

    @Override
    public void playerConstructor(Player player, InitializeType type) {
        super.playerConstructor(player, type);
        player.setGameMode(GameMode.CREATIVE);
    }
}
