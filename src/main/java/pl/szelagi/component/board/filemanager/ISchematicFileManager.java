/*
 * SessionAPI - A framework for game containerization on Minecraft servers
 * Copyright (C) 2025 Szelagi (https://github.com/Szelagi/SessionAPI)
 * Licensed under the GNU General Public License v3.0.
 * For more details, visit <https://www.gnu.org/licenses/>.
 */

package pl.szelagi.component.board.filemanager;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import pl.szelagi.filemanager.IFileManager;
import pl.szelagi.spatial.ISpatial;
import pl.szelagi.util.schematic.ISchematicMethods;
import pl.szelagi.util.schematic.SchematicException;

import java.io.File;

public interface ISchematicFileManager extends IFileManager, ISchematicMethods, ISpatial {
	String SCHEMATIC_EXTENSION = ".schem";

	default void loadSchematic(String name) throws SchematicException {
		String schematicPath = getCurrentDirectory().getPath() + "/" + name + SCHEMATIC_EXTENSION;
		ISchematicMethods.loadAndPlaceSchematic(schematicPath, this);
	}

	default void asyncLoadSchematic(String name) throws SchematicException {
		String schematicPath = getCurrentDirectory().getPath() + "/" + name + SCHEMATIC_EXTENSION;
		ISchematicMethods.asyncLoadAndPlaceSchematic(schematicPath, this);
	}

	default void saveSchematic(String name, ISpatial optimized) throws SchematicException {
		var path = getCurrentDirectory().getPath() + "/" + name + SCHEMATIC_EXTENSION;
		tryMakeHeadDirectory();
		ISchematicMethods.copyAndSaveSchematic(path, optimized, optimized.getCenter());
	}

	default void saveEmptySchematic(String name, ISpatial optimized) throws SchematicException {
		var path = getCurrentDirectory().getPath() + "/" + name + SCHEMATIC_EXTENSION;
		tryMakeHeadDirectory();
		ISchematicMethods.emptySaveSchematic(path, optimized, optimized.getCenter());
	}

	default @NotNull ISpatial toSpatial(@NotNull String name, @NotNull Location base) throws SchematicException {
		var path = getCurrentDirectory().getPath() + "/" + name + SCHEMATIC_EXTENSION;
		return ISchematicMethods.toSchematicSpatial(path, base);
	}

	default boolean existsSchematic(String name) {
		if (!getCurrentDirectory().exists() || !getCurrentDirectory().isDirectory())
			return false;
		String schematicPath = getCurrentDirectory().getPath() + "/" + name + SCHEMATIC_EXTENSION;
		File schematicFile = new File(schematicPath);
		return (schematicFile.exists() && schematicFile.isFile());
	}
}
