package pl.szelagi.world;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.java.JavaPlugin;
import org.codehaus.plexus.util.FileUtils;

import java.io.IOException;

public class SessionWorldManager {
	private static final String SESSION_WORLD_NAME = "session_world";
	private static World SESSION_WORLD;

	public static void initialize(JavaPlugin plugin) {
		var baseWorld = Bukkit.getWorld("world");
		assert baseWorld != null;

		var sessionWorld = Bukkit.getWorld(SESSION_WORLD_NAME);
		if (sessionWorld != null) {
			for (var p : plugin.getServer().getOnlinePlayers()) {
				if (p.getWorld().getName().equals(sessionWorld.getName())) {
					p.teleport(baseWorld.getSpawnLocation());
				}
			}
			Bukkit.unloadWorld(sessionWorld, false);
			var directory = sessionWorld.getWorldFolder();
			sessionWorld.getWorldFolder().deleteOnExit();
			try {
				FileUtils.deleteDirectory(directory);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		var worldCreator = new WorldCreator(SESSION_WORLD_NAME);
		worldCreator.generator(new EmptyChunkGenerator());
		SESSION_WORLD = worldCreator.createWorld();
	}

	public static World getSessionWorld() {
		return SESSION_WORLD;
	}
}