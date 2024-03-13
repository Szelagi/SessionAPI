package pl.szelagi;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import pl.szelagi.command.CommandExecutor;
import pl.szelagi.command.ConstructTabCompleter;
import pl.szelagi.recovery.RecoveryListener;
import pl.szelagi.world.SessionWorldManager;

import java.io.File;

public final class SessionAPI extends JavaPlugin {
    private static SessionAPI instance;
    public static SessionAPI getInstance() {
        return instance;
    }
    public static final File SESSION_API_DIRECTORY = new File(
            Bukkit.getServer().getPluginsFolder().getPath() + "/SessionAPI"
    );
    public static final File RECOVERY_DIRECTORY = new File(
            SESSION_API_DIRECTORY.getPath() + "/recovery"
    );
    public static final File BOARD_DIRECTORY = new File(
            SESSION_API_DIRECTORY.getPath() + "/board"
    );
    @Override
    public void onEnable() {
        instance = this;
        // Plugin startup logic
        if(!SESSION_API_DIRECTORY.exists()) SESSION_API_DIRECTORY.mkdir();
        if (!RECOVERY_DIRECTORY.exists()) RECOVERY_DIRECTORY.mkdir();
        if (!BOARD_DIRECTORY.exists()) BOARD_DIRECTORY.mkdir();

        getServer().getPluginManager().registerEvents(new RecoveryListener(), this);

        SessionWorldManager.initialize(this);
        CommandExecutor.initialize(this);
        ConstructTabCompleter.initialize(this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
