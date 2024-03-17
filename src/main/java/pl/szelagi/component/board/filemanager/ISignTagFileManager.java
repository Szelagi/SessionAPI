package pl.szelagi.component.board.filemanager;

import pl.szelagi.filemanager.IFileManager;
import pl.szelagi.spatial.ISpatial;
import pl.szelagi.tag.SignTagData;
import pl.szelagi.tag.exception.SignTagException;
import pl.szelagi.tag.serialization.SignTagDataStorage;

import java.io.*;

public interface ISignTagFileManager extends IFileManager, ISpatial {
	String SIGN_TAG_DATA_EXTENSION = ".dat";

	default void saveSignTagData(String name, SignTagData signTagData) throws SignTagException {
		var filePath = getCurrentDirectory().getPath() + "/" + name + SIGN_TAG_DATA_EXTENSION;
		var file = new File(filePath);
		try {
			file.createNewFile();
			FileOutputStream fileOutputStream = new FileOutputStream(file, false);
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
			objectOutputStream.writeObject(signTagData.toSignTagDataStorage());
			objectOutputStream.flush();
			objectOutputStream.close();
		} catch (IOException e) {
			throw new SignTagException(e.getMessage());
		}
	}

	default SignTagData loadSignTagData(String name) throws SignTagException {
		var filePath = getCurrentDirectory().getPath() + "/" + name + SIGN_TAG_DATA_EXTENSION;
		var file = new File(filePath);
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
			SignTagDataStorage signTagDataStorage = (SignTagDataStorage) objectInputStream.readObject();
			objectInputStream.close();
			return signTagDataStorage.toSignTagData(getCenter());
		} catch (FileNotFoundException e) {
			throw new SignTagException(e.getMessage());
		} catch (IOException | ClassNotFoundException e) {
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
