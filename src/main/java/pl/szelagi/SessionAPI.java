package pl.szelagi;

import org.bukkit.Bukkit;
import org.bukkit.permissions.ServerOperator;
import org.bukkit.plugin.java.JavaPlugin;
import pl.szelagi.command.Command;
import pl.szelagi.manager.BoardManager;
import pl.szelagi.manager.ControllerManager;
import pl.szelagi.manager.SessionManager;
import pl.szelagi.recovery.Recovery;
import pl.szelagi.world.SessionWorldManager;

import java.io.File;

public final class SessionAPI extends JavaPlugin {
	public static final File SESSION_API_DIRECTORY = new File(Bukkit
			                                                          .getServer()
			                                                          .getPluginsFolder()
			                                                          .getPath() + "/SessionAPI");
	public static final File RECOVERY_DIRECTORY = new File(SESSION_API_DIRECTORY.getPath() + "/recovery");
	public static final File BOARD_DIRECTORY = new File(SESSION_API_DIRECTORY.getPath() + "/board");
	private static SessionAPI instance;

	public static SessionAPI getInstance() {
		return instance;
	}

	@Override
	public void onEnable() {
		instance = this;
		// Plugin startup logic
		if (!SESSION_API_DIRECTORY.exists())
			SESSION_API_DIRECTORY.mkdir();
		if (!RECOVERY_DIRECTORY.exists())
			RECOVERY_DIRECTORY.mkdir();
		if (!BOARD_DIRECTORY.exists())
			BOARD_DIRECTORY.mkdir();

		SessionManager.initialize(this);
		BoardManager.initialize(this);
		ControllerManager.initialize(this);

		Recovery.initialize(this);

		SessionWorldManager.initialize(this);
		Command.registerCommands();
	}

	@Override
	public void onDisable() {
		// Plugin shutdown logic
	}

	public static void debug(String message) {
		Bukkit.getOnlinePlayers().stream()
		      .filter(ServerOperator::isOp)
		      .forEach(player -> player.sendMessage(message));
	}
}
