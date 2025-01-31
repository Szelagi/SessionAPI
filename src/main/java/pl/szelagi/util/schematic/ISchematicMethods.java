/*
 * SessionAPI - A framework for game containerization on Minecraft servers
 * Copyright (C) 2025 Szelagi (https://github.com/Szelagi/SessionAPI)
 * Licensed under the GNU General Public License v3.0.
 * For more details, visit <https://www.gnu.org/licenses/>.
 */

package pl.szelagi.util.schematic;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.*;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.block.BlockTypes;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.szelagi.SessionAPI;
import pl.szelagi.spatial.ISpatial;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public interface ISchematicMethods {
	private static void showSchematicError(String error) {
		String errorMessage = "§4[SCHEMATIC ERROR] §f" + error;
		for (Player player : Bukkit.getServer()
		                           .getOnlinePlayers())
			if (player.isOp())
				player.sendMessage(errorMessage);
		Bukkit.getServer().getConsoleSender()
		      .sendMessage(errorMessage);
	}

	static ISpatial toSchematicSpatial(@NotNull String filePath, @NotNull Location base) {
		return toSchematicSpatialCore(filePath, base);
	}

	static void loadAndPlaceSchematic(@NotNull String filePath, @NotNull ISpatial ISpatial) throws SchematicException {
		loadAndPlaceSchematic(filePath, ISpatial.getCenterBlockLocation());
	}

	static void asyncLoadAndPlaceSchematic(@NotNull String filePath, @NotNull ISpatial ISpatial) throws SchematicException {
		asyncLoadAndPlaceSchematic(filePath, ISpatial.getCenterBlockLocation());
	}

	static void copyAndSaveSchematic(@NotNull String filePath, @NotNull ISpatial ISpatial) throws SchematicException {
		copyAndSaveSchematicCore(filePath, ISpatial.getFirstPoint(), ISpatial.getSecondPoint(), ISpatial.getCenter());
	}

	static void emptySaveSchematic(@NotNull String filePath, @NotNull ISpatial ISpatial, Location toLocation) throws SchematicException {
		emptySaveSchematicCore(filePath, ISpatial.getFirstPoint(), ISpatial.getSecondPoint(), toLocation);
	}

	static void copyAndSaveSchematic(@NotNull String filePath, @NotNull ISpatial ISpatial, @NotNull Location toLocation) throws SchematicException {
		copyAndSaveSchematicCore(filePath, ISpatial.getFirstPoint(), ISpatial.getSecondPoint(), toLocation);
	}

	static void loadAndPlaceSchematic(@NotNull String filePath, @NotNull Location location) throws SchematicException {
		try {
			loadAndPlaceSchematicCore(filePath, location);
		} catch (
				SchematicException schematicException) {
			showSchematicError("load: filePath: " + filePath + " Exception: " + schematicException.getMessage());
			throw schematicException;
		}
	}

	static void copyAndSaveSchematic(@NotNull String filePath, @NotNull Location location1, @NotNull Location location2, @NotNull Location locationCenter) {
		try {
			copyAndSaveSchematicCore(filePath, location1, location2, locationCenter);
		} catch (
				SchematicException schematicException) {
			showSchematicError("save: filePath: " + filePath + " Exception: " + schematicException.getMessage());
			throw schematicException;
		}
	}

	private static void loadAndPlaceSchematicCore(@NotNull String filePath, @NotNull Location toLocation) throws SchematicException {
		File file = new File(filePath);
		ClipboardFormat format = ClipboardFormats.findByFile(file);
		World adaptedWorld = BukkitAdapter.adapt(toLocation.getWorld());
		Clipboard clipboard;
		FileInputStream fis;
		ClipboardReader reader;
		BlockVector3 to = BukkitAdapter.asBlockVector(toLocation);
		try {
			fis = new FileInputStream(file);
			assert format != null;
			reader = format.getReader(fis);
			clipboard = reader.read();
			try (EditSession editSession = WorldEdit
					.getInstance()
					.newEditSession(adaptedWorld)) {
				Operation operation = new ClipboardHolder(clipboard)
						.createPaste(editSession)
						.copyEntities(false)
						.copyBiomes(true).to(to)
						// configure here
						.build();
				Operations.complete(operation);
			} catch (WorldEditException e) {
				throw new SchematicException(e.getMessage());
			}
		} catch (Exception e) {
			throw new SchematicException(e.getMessage());
		}
	}

	private static @NotNull ISpatial toSchematicSpatialCore(@NotNull String filePath, @NotNull Location base) throws SchematicException {
		File file = new File(filePath);
		ClipboardFormat format = ClipboardFormats.findByFile(file);
		Clipboard clipboard;
		FileInputStream fis;
		ClipboardReader reader;
		try {
			fis = new FileInputStream(file);
			assert format != null;
			reader = format.getReader(fis);
			clipboard = reader.read();

			var min = clipboard.getMinimumPoint();
			var max = clipboard.getMaximumPoint();
			var origin = clipboard.getOrigin();

			var deltaMin = min.subtract(origin);
			var deltaMax = max.subtract(origin);
			var baseVector3 = BukkitAdapter.asBlockVector(base);

			var world = base.getWorld();
			var firstPoint = BukkitAdapter.adapt(world, deltaMin.add(baseVector3));
			var secondPoint = BukkitAdapter.adapt(world, deltaMax.add(baseVector3));

			return new ISpatial() {
				@Override
				public @NotNull Location getFirstPoint() {
					return firstPoint;
				}

				@Override
				public @NotNull Location getSecondPoint() {
					return secondPoint;
				}
			};
		} catch (Exception e) {
			throw new SchematicException(e.getMessage());
		}
	}

	public static void asyncLoadAndPlaceSchematic(@NotNull String filePath, @NotNull Location toLocation) throws SchematicException {
		Bukkit.getScheduler()
		      .runTaskAsynchronously(SessionAPI.getInstance(), () -> loadAndPlaceSchematicCore(filePath, toLocation));
	}

	private static void copyAndSaveSchematicCore(@NotNull String filePath, @NotNull Location location1, @NotNull Location location2, @NotNull Location toLocation) throws SchematicException {
		var file = new File(filePath);
		var world = BukkitAdapter.adapt(location1.getWorld());
		var loc1 = BukkitAdapter.asBlockVector(location1);
		var loc2 = BukkitAdapter.asBlockVector(location2);
		var to = BukkitAdapter.asBlockVector(toLocation);

		BlockVector3 min;
		BlockVector3 max;
		if (location1.getBlockY() < location2.getBlockY()) {
			min = loc1;
			max = loc2;
		} else {
			min = loc2;
			max = loc1;
		}

		CuboidRegion region = new CuboidRegion(world, min, max);
		BlockArrayClipboard clipboard = new BlockArrayClipboard(region);
		clipboard.setOrigin(to);

		var forwardExtentCopy = new ForwardExtentCopy(world, region, clipboard, region.getMinimumPoint());

		forwardExtentCopy.setCopyingBiomes(true);
		forwardExtentCopy.setCopyingEntities(false);
		forwardExtentCopy.setRemovingEntities(true);

		try {
			Operations.complete(forwardExtentCopy);
			try (ClipboardWriter writer = BuiltInClipboardFormat.SPONGE_SCHEMATIC.getWriter(new FileOutputStream(file))) {
				writer.write(clipboard);
			} catch (Exception e) {
				throw new SchematicException(e.getMessage());
			}
		} catch (Exception e) {
			throw new SchematicException(e.getMessage());
		}
	}

	private static void emptySaveSchematicCore(@NotNull String filePath, @NotNull Location location1, @NotNull Location location2, @NotNull Location toLocation) throws SchematicException {
		var file = new File(filePath);
		var world = BukkitAdapter.adapt(location1.getWorld());
		var loc1 = BukkitAdapter.asBlockVector(location1);
		var loc2 = BukkitAdapter.asBlockVector(location2);
		var to = BukkitAdapter.asBlockVector(toLocation);

		BlockVector3 min;
		BlockVector3 max;
		if (location1.getBlockY() < location2.getBlockY()) {
			min = loc1;
			max = loc2;
		} else {
			min = loc2;
			max = loc1;
		}

		CuboidRegion region = new CuboidRegion(world, min, max);
		BlockArrayClipboard clipboard = new BlockArrayClipboard(region);
		clipboard.setOrigin(to);

		try (EditSession editSession = WorldEdit
				.getInstance()
				.newEditSession(world)) {
			for (BlockVector3 vec : region) {
				assert BlockTypes.AIR != null;
				clipboard.setBlock(vec, BlockTypes.AIR.getDefaultState());
			}
		} catch (Exception e) {
			throw new SchematicException("Error while setting blocks to AIR: " + e.getMessage());
		}

		try {
			try (ClipboardWriter writer = BuiltInClipboardFormat.SPONGE_SCHEMATIC.getWriter(new FileOutputStream(file))) {
				writer.write(clipboard);
			} catch (Exception e) {
				throw new SchematicException(e.getMessage());
			}
		} catch (Exception e) {
			throw new SchematicException(e.getMessage());
		}
	}
}
