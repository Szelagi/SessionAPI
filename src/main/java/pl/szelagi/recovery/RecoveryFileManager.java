package pl.szelagi.recovery;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.szelagi.SessionAPI;
import pl.szelagi.filemanager.IFileManager;
import pl.szelagi.recovery.exception.RecoveryException;

import java.io.*;

public class RecoveryFileManager implements IFileManager {
    private final File headDirectory;
    private final File currentDirectory;
    private static final String BASE_DIRECTORY_NAME = SessionAPI.RECOVERY_DIRECTORY.getPath();
    private static final String DATA_EXTENSION = ".dat";

    public RecoveryFileManager() {
        this.headDirectory = new File(BASE_DIRECTORY_NAME);
        this.currentDirectory = new File(BASE_DIRECTORY_NAME);
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
            throw new RecoveryException(e.getClass().getName() + ": " + e.getMessage());
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
        } catch (IOException | ClassNotFoundException e) {
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