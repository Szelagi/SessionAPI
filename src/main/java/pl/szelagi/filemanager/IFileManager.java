package pl.szelagi.filemanager;

import org.jetbrains.annotations.NotNull;
import pl.szelagi.filemanager.exception.BeyondHeadDirectoryException;
import pl.szelagi.filemanager.exception.DirectoryNotFoundException;
import pl.szelagi.filemanager.exception.InvalidFileTypeException;

import java.io.File;

public interface IFileManager {
	@NotNull File getCurrentDirectory();

	@NotNull File getHeadDirectory();

	default IFileManager getPreviousDirectory() throws BeyondHeadDirectoryException {
		if (getCurrentDirectory().equals(getHeadDirectory()))
			throw new BeyondHeadDirectoryException(getCurrentDirectory().getPath());
		var parentDirectory = new File(getCurrentDirectory().getParent());
		if (!parentDirectory.exists() || !parentDirectory.isDirectory())
			throw new DirectoryNotFoundException(parentDirectory.getPath());
		var newHeadDirectory = getHeadDirectory();
		return new IFileManager() {
			@Override
			public @NotNull File getCurrentDirectory() {
				return parentDirectory;
			}

			@Override
			public @NotNull File getHeadDirectory() {
				return newHeadDirectory;
			}
		};
	}

	default IFileManager getDirectory(String name) throws DirectoryNotFoundException {
		var newCurrentDirectoryPath = getCurrentDirectory().getPath() + "/" + name;
		var newCurrentDirectory = new File(newCurrentDirectoryPath);
		var newHeadDirectory = getHeadDirectory();
		if (!newCurrentDirectory.exists() || !newCurrentDirectory.isDirectory())
			throw new DirectoryNotFoundException(newCurrentDirectory.getPath());
		return new IFileManager() {
			@Override
			public @NotNull File getCurrentDirectory() {
				return newCurrentDirectory;
			}

			@Override
			public @NotNull File getHeadDirectory() {
				return newHeadDirectory;
			}
		};
	}

	default boolean existsDirectory(String name) {
		if (!getCurrentDirectory().exists() || !getCurrentDirectory().isDirectory())
			return false;
		var path = getCurrentDirectory().getPath() + "/" + name;
		File directory = new File(path);
		return (directory.exists() && directory.isDirectory());
	}

	default void tryMakeHeadDirectory() throws InvalidFileTypeException {
		if (getHeadDirectory().exists() && getHeadDirectory().isFile())
			throw new InvalidFileTypeException(getHeadDirectory().getPath() + "type is file, instead of directory!");
		if (!getHeadDirectory().exists()) {
			getHeadDirectory().mkdir();
		}
	}
}
