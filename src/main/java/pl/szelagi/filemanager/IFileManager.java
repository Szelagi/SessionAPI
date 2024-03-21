package pl.szelagi.filemanager;

import org.jetbrains.annotations.NotNull;
import pl.szelagi.filemanager.exception.BackwardFilePathException;
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

		var parent = getCurrentDirectory().getParentFile();
		if (!parent.exists() || !parent.isDirectory())
			throw new DirectoryNotFoundException(parent.getPath());

		var head = getHeadDirectory();
		return new IFileManager() {
			@Override
			public @NotNull File getCurrentDirectory() {
				return parent;
			}

			@Override
			public @NotNull File getHeadDirectory() {
				return head;
			}
		};
	}

	default IFileManager getDirectory(String name) throws DirectoryNotFoundException, BackwardFilePathException {
		var currentPath = getCurrentDirectory().getPath() + "/" + name;
		var current = new File(currentPath);
		var head = getHeadDirectory();

		if (isBackward(name))
			throw new BackwardFilePathException(name);

		if (!current.exists() || !current.isDirectory())
			throw new DirectoryNotFoundException(current.getPath());
		return new IFileManager() {
			@Override
			public @NotNull File getCurrentDirectory() {
				return current;
			}

			@Override
			public @NotNull File getHeadDirectory() {
				return head;
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

	private boolean tryMakeDirectory(File file) throws InvalidFileTypeException {
		if (file.exists() && file.isFile())
			throw new InvalidFileTypeException(getHeadDirectory().getPath() + "type is file, instead of directory!");
		if (!file.exists()) {
			file.mkdir();
			return true;
		}
		return false;
	}

	default boolean tryMakeHeadDirectory() throws InvalidFileTypeException {
		return tryMakeDirectory(getHeadDirectory());
	}

	default boolean tryMakeCurrentDirectory() throws InvalidFileTypeException {
		return tryMakeDirectory(getCurrentDirectory());
	}

	default IFileManager asHeadDirectory() {
		var head = new File(getCurrentDirectory().getPath());
		var current = new File(getCurrentDirectory().getPath());
		return new IFileManager() {
			@Override
			public @NotNull File getCurrentDirectory() {
				return current;
			}

			@Override
			public @NotNull File getHeadDirectory() {
				return head;
			}
		};
	}
	
	default IFileManager goDirectory(String name, Exists exists, Path path) throws BackwardFilePathException, DirectoryNotFoundException {
		var newCurrentDirectoryPath = getCurrentDirectory().getPath() + "/" + name;
		var newCurrentDirectory = new File(newCurrentDirectoryPath);
		var newHeadDirectory = getHeadDirectory();
		switch (exists) {
			case REQUIRE -> {
				if (!newCurrentDirectory.exists())
					throw new DirectoryNotFoundException(newCurrentDirectoryPath);
			}
			case AUTO_CREATE -> {
				newCurrentDirectory.mkdir();
			}
		}
		switch (path) {
			case ONLY_FORWARD -> {
				if (isBackward(newCurrentDirectoryPath))
					throw new BackwardFilePathException(newCurrentDirectoryPath);
			}
		}
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

	static boolean isBackward(String path) {
		return path.contains("../");
	}
}
