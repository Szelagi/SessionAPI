/*
 * SessionAPI - A framework for game containerization on Minecraft servers
 * Copyright (C) 2025 Szelagi (https://github.com/Szelagi/SessionAPI)
 * Licensed under the GNU General Public License v3.0.
 * For more details, visit <https://www.gnu.org/licenses/>.
 */

package pl.szelagi.component.board.filemanager;

import pl.szelagi.filemanager.IFileManager;
import pl.szelagi.spatial.ISpatial;
import pl.szelagi.tag.TagResolve;
import pl.szelagi.tag.exception.SignTagException;
import pl.szelagi.tag.serialization.TagResolveStorage;

import java.io.*;

public interface ISignTagFileManager extends IFileManager, ISpatial {
	String SIGN_TAG_DATA_EXTENSION = ".bin";

	default void saveSignTagData(String name, TagResolve tagResolve) throws SignTagException {
		var filePath = getCurrentDirectory().getPath() + "/" + name + SIGN_TAG_DATA_EXTENSION;
		var file = new File(filePath);
		try {
			file.createNewFile();
			FileOutputStream fileOutputStream = new FileOutputStream(file, false);
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
			objectOutputStream.writeObject(tagResolve.toSignTagDataStorage());
			objectOutputStream.flush();
			objectOutputStream.close();
		} catch (IOException e) {
			throw new SignTagException(e.getMessage());
		}
	}

	default TagResolve loadSignTagData(String name) throws SignTagException {
		var filePath = getCurrentDirectory().getPath() + "/" + name + SIGN_TAG_DATA_EXTENSION;
		var file = new File(filePath);
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
			TagResolveStorage tagResolveStorage = (TagResolveStorage) objectInputStream.readObject();
			objectInputStream.close();
			return tagResolveStorage.toTagResolve(getCenter());
		} catch (FileNotFoundException e) {
			throw new SignTagException(e.getMessage());
		} catch (IOException |
		         ClassNotFoundException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	default boolean existsSignTagData(String name) {
		if (!getCurrentDirectory().exists() || !getCurrentDirectory().isDirectory())
			return false;
		String path = getCurrentDirectory().getPath() + "/" + name + SIGN_TAG_DATA_EXTENSION;
		File file = new File(path);
		return (file.exists() && file.isFile());
	}
}
