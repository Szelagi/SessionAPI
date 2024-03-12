package pl.szelagi;

import org.bukkit.plugin.java.JavaPlugin;
import pl.szelagi.command.CommandExecutor;
import pl.szelagi.world.SessionWorldManager;

public final class SessionAPI extends JavaPlugin {
    @Override
    public void onEnable() {
        // Plugin startup logic
        SessionWorldManager.initialize();
        CommandExecutor.initialize(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
