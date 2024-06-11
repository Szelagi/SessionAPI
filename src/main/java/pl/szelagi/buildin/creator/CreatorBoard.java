package pl.szelagi.buildin.creator;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import pl.szelagi.buildin.controller.NoCreatureDropController.NoCreatureDropController;
import pl.szelagi.buildin.controller.OtherEquipment.OtherEquipment;
import pl.szelagi.buildin.controller.OtherGameMode.OtherGameMode;
import pl.szelagi.component.board.Board;
import pl.szelagi.component.board.filemanager.BoardFileManager;
import pl.szelagi.component.session.Session;
import pl.szelagi.event.player.initialize.PlayerConstructorEvent;
import pl.szelagi.spatial.ISpatial;

public class CreatorBoard extends Board {
	private final String editName;
	private BoardFileManager storage;

	public CreatorBoard(Session session, String editName) {
		super(session);
		this.editName = editName;
	}

	public BoardFileManager getStorage() {
		return storage;
	}

	@Override
	protected void generate() {
		this.storage = new BoardFileManager(editName, getSpace());
		getBase().getBlock()
		         .setType(Material.BEDROCK);
		if (storage.existsSchematic(SCHEMATIC_CONSTRUCTOR_NAME))
			storage.loadSchematic(SCHEMATIC_CONSTRUCTOR_NAME);
		setSecureZone(ISpatial.clone(getSpace()));
	}

	@Override
	protected void degenerate() {
		getSpace().toOptimized()
		          .eachBlocks(block -> block.setType(Material.AIR));
		for (var entity : getSpace().getMobsIn())
			entity.remove();
	}

	@NotNull
	@Override
	public String getName() {
		return "creatorBoard";
	}

	@Override
	public void start() {
		super.start();
		new NoCreatureDropController(this).start();
		new OtherEquipment(this, true).start();
		new OtherGameMode(this, GameMode.CREATIVE).start();
	}

	@Override
	public void playerConstructor(PlayerConstructorEvent event) {
		super.playerConstructor(event);
	}
}
