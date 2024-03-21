package pl.szelagi.recovery;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.szelagi.SessionAPI;
import pl.szelagi.filemanager.IFileManager;
import pl.szelagi.recovery.exception.RecoveryException;

import java.io.*;

public class RecoveryFileManager implements IFileManager {
	private static final String DATA_EXTENSION = ".dat";
	private final File headDirectory;
	private final File currentDirectory;

	public RecoveryFileManager() {
		this.headDirectory = SessionAPI.RECOVERY_DIRECTORY;
		this.currentDirectory = SessionAPI.RECOVERY_DIRECTORY;
	}

	@Override
	public @NotNull File getCurrentDirectory() {
		return headDirectory;
	}

	@Override
	public @NotNull File getHeadDirectory() {
		return currentDirectory;
	}

	public void savePlayerRecovery(Player player) throws RecoveryException {
		var recovery = new PlayerRecovery(player);

		var filePath = getCurrentDirectory().getPath() + "/" + player.getName() + DATA_EXTENSION;
		var file = new File(filePath);
		try {
			file.createNewFile();
			FileOutputStream fileOutputStream = new FileOutputStream(file, false);
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
			objectOutputStream.writeObject(recovery);
			objectOutputStream.flush();
			objectOutputStream.close();
		} catch (IOException e) {
			throw new RecoveryException(e
					                            .getClass()
					                            .getName() + ": " + e.getMessage());
		}
	}

	public PlayerRecovery loadPlayerRecovery(Player player) {
		var filePath = getCurrentDirectory().getPath() + "/" + player.getName() + DATA_EXTENSION;
		var file = new File(filePath);
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
			PlayerRecovery inventory = (PlayerRecovery) objectInputStream.readObject();
			objectInputStream.close();
			return inventory;
		} catch (IOException |
		         ClassNotFoundException e) {
			throw new RecoveryException(e.getMessage());
		}
	}

	public boolean existsPlayerRecovery(Player player) {
		var filePath = getCurrentDirectory().getPath() + "/" + player.getName() + DATA_EXTENSION;
		var file = new File(filePath);
		return file.exists();
	}

	public void deletePlayerRecovery(Player player) {
		var filePath = getCurrentDirectory().getPath() + "/" + player.getName() + DATA_EXTENSION;
		var file = new File(filePath);
		file.delete();
	}
}
