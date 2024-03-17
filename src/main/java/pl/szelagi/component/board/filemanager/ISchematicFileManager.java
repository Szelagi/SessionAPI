package pl.szelagi.component.board.filemanager;

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

	default void saveSchematic(String name, ISpatial optimized) throws SchematicException {
		var path = getCurrentDirectory().getPath() + "/" + name + SCHEMATIC_EXTENSION;
		tryMakeHeadDirectory();
		ISchematicMethods.copyAndSaveSchematic(path, optimized, this.getCenter());
	}

	default void saveEmptySchematic(String name, ISpatial optimized) throws SchematicException {
		var path = getCurrentDirectory().getPath() + "/" + name + SCHEMATIC_EXTENSION;
		tryMakeHeadDirectory();
		ISchematicMethods.emptySaveSchematic(path, optimized, this.getCenter());
	}

	default boolean existsSchematic(String name) {
		if (!getCurrentDirectory().exists() || !getCurrentDirectory().isDirectory())
			return false;
		String schematicPath = getCurrentDirectory().getPath() + "/" + name + SCHEMATIC_EXTENSION;
		File schematicFile = new File(schematicPath);
		return (schematicFile.exists() && schematicFile.isFile());
	}
}
