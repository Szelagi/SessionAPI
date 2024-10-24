package pl.szelagi.buildin.creator;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.world.block.BlockTypes;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import pl.szelagi.Scheduler;
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
		Scheduler.runAndWait(() -> {
			getBase().getBlock()
			         .setType(Material.BEDROCK);
		});
		if (storage.existsSchematic(SCHEMATIC_CONSTRUCTOR_NAME))
			storage.loadSchematic(SCHEMATIC_CONSTRUCTOR_NAME);
		setSecureZone(ISpatial.clone(getSpace()));
	}

	@Override
	protected void degenerate() {
		var space = getSpace().toOptimized();
		var pointA = space.getFirstPoint();
		var pointB = space.getSecondPoint();
		BlockVector3 vecA = BlockVector3.at(pointA.getBlockX(), pointA.getBlockY(), pointA.getBlockZ());
		BlockVector3 vecB = BlockVector3.at(pointB.getBlockX(), pointB.getBlockY(), pointB.getBlockZ());

		CuboidRegion region = new CuboidRegion(vecA, vecB);

		try (EditSession editSession = WorldEdit
				.getInstance()
				.newEditSession(BukkitAdapter.adapt(pointA.getWorld()))) {
			assert BlockTypes.AIR != null;
			editSession.setBlocks(region, BlockTypes.AIR.getDefaultState());
			Operations.complete(editSession.commit());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		Scheduler.runAndWait(() -> {
			for (var entity : getSpace().getMobsIn())
				entity.remove();
		});
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
