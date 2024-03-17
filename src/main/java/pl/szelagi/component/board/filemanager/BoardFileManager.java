package pl.szelagi.component.board.filemanager;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import pl.szelagi.SessionAPI;
import pl.szelagi.filemanager.IFileManager;
import pl.szelagi.filemanager.exception.BeyondHeadDirectoryException;
import pl.szelagi.filemanager.exception.DirectoryNotFoundException;
import pl.szelagi.filemanager.exception.InvalidConstructorException;
import pl.szelagi.spatial.ISpatial;

import java.io.File;

public class BoardFileManager implements ISchematicFileManager, ISignTagFileManager {
	private static final String BASE_DIRECTORY = SessionAPI.BOARD_DIRECTORY.getPath();
	private final File headDirectory;
	private final File currentDirectory;
	private final ISpatial spatial;
	private final String name;

	public BoardFileManager(String name, ISpatial spatial) {
		if (name == null || spatial == null)
			throw new InvalidConstructorException("name or spatial is null, initialize on bad function, only in #Board.generate()");
		this.name = name;
		this.spatial = spatial;
		this.headDirectory = new File(getHeadDirectory(name));
		this.currentDirectory = new File(headDirectory.getPath());
	}

	private BoardFileManager(String name, ISpatial spatial, File currentDirectory) {
		this.name = name;
		this.spatial = spatial;
		this.headDirectory = new File(getHeadDirectory(name));
		this.currentDirectory = currentDirectory;
	}

	private BoardFileManager(String name, ISpatial spatial, IFileManager fileManager) {
		this.name = name;
		this.spatial = spatial;
		this.headDirectory = fileManager.getHeadDirectory();
		this.currentDirectory = fileManager.getCurrentDirectory();
	}

	private static String getHeadDirectory(String name) {
		return BASE_DIRECTORY + "/" + name;
	}

	@Override
	public BoardFileManager getDirectory(String name) throws DirectoryNotFoundException {
		return new BoardFileManager(name, spatial, ISchematicFileManager.super.getDirectory(name));
	}

	@Override
	public BoardFileManager getPreviousDirectory() throws BeyondHeadDirectoryException {
		return new BoardFileManager(name, spatial, ISchematicFileManager.super.getPreviousDirectory());
	}

	@Override
	public @NotNull File getCurrentDirectory() {
		return currentDirectory;
	}

	@Override
	public @NotNull File getHeadDirectory() {
		return headDirectory;
	}

	@Override
	public @NotNull Location getFirstPoint() {
		return spatial.getFirstPoint();
	}

	@Override
	public @NotNull Location getSecondPoint() {
		return spatial.getSecondPoint();
	}
}
