package pl.szelagi.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import pl.szelagi.SessionAPI;
import pl.szelagi.buildin.creator.Creator;
import pl.szelagi.buildin.creator.CreatorBoard;
import pl.szelagi.buildin.system.testsession.TestSession;
import pl.szelagi.component.board.Board;
import pl.szelagi.component.session.cause.NeutralCause;
import pl.szelagi.component.session.exception.SessionStartException;
import pl.szelagi.manager.SessionManager;
import pl.szelagi.spatial.ISpatial;
import pl.szelagi.tag.SignTagAnalyzer;

import java.util.ArrayList;

public class CommandExecutor implements org.bukkit.command.CommandExecutor {
	private final JavaPlugin plugin;

	public CommandExecutor(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	public static void initialize(JavaPlugin plugin) {
		var executor = new CommandExecutor(plugin);
		for (var commandName : pl.szelagi.command.Command.NAMES) {
			var command = plugin.getCommand(commandName);
			if (command != null) {
				command.setExecutor(executor);
			}
		}
	}

	@Override
	public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
		if (!(commandSender instanceof Player player)) {
			commandSender.sendMessage("§cCommandSender must be a Player!");
			return false;
		}
		if (!player.isOp()) {
			player.sendMessage("§cYou are not server operator!");
			return false;
		}
		switch (command.getName()) {
			//          Board editor
			case "board-save" -> {
				var session = SessionManager.getSession(player);
				if (session == null) {
					player.sendMessage("§cYou are not in session!");
					return false;
				}
				if (!(session instanceof Creator creator)) {
					player.sendMessage("§cYou are not in editor!");
					return false;
				}
				if (!(creator.getCurrentBoard() instanceof CreatorBoard creatorBoard)) {
					player.sendMessage("§cYou are not in editor board!");
					return false;
				}

				player.sendMessage("§7§oSaving...");
				long millis = System.currentTimeMillis();

				ISpatial optimized = creatorBoard
						.getStorage()
						.toOptimizedSpatial();

				creatorBoard.getStorage()
				            .saveSchematic(Board.SCHEMATIC_CONSTRUCTOR_NAME, optimized);
				creatorBoard.getStorage()
				            .saveEmptySchematic(Board.SCHEMATIC_DESTRUCTOR_NAME, optimized);

				var data = SignTagAnalyzer.process(creatorBoard.getSpace());
				creatorBoard.getStorage()
				            .saveSignTagData(Board.SIGN_TAG_DATA_NAME, data);

				long deltaMillis = System.currentTimeMillis() - millis;
				player.sendMessage("§aSaved §f(" + deltaMillis + "ms)");
			}
			case "board-edit" -> {
				if (strings.length != 1) {
					player.sendMessage("Pattern: §c/board-edit <board name>");
					return false;
				}
				var directoryName = strings[0];
				var creator = new Creator(plugin, directoryName);
				try {
					creator.start();
					creator.addPlayer(player);
					player.sendMessage("§aOK");
				} catch (
						SessionStartException e) {
					player.sendMessage("§cEditor start exception: §f" + e.getMessage());
				}
				return true;
			}
			case "board-list" -> {
				var files = SessionAPI.BOARD_DIRECTORY.listFiles();
				if (files == null || files.length == 0) {
					player.sendMessage("§cNot found board saves!");
					return false;
				}
				player.sendMessage("§aBoard saves:");
				for (var file : files) {
					if (!file.isDirectory())
						continue;
					player.sendMessage("§8- §f" + file.getName());
				}
			}
			case "board-exit" -> {
				var session = SessionManager.getSession(player);
				if (session == null) {
					player.sendMessage("§cYou are not in session!");
					return false;
				}
				if (!(session instanceof Creator creator)) {
					player.sendMessage("§cYou are not in editor!");
					return false;
				}
				creator.stop(new NeutralCause("FORCE_STOP"));
				player.sendMessage("§aOK");
				return true;
			}

			//          Session manage
			case "session-exit" -> {
				var dungeon = SessionManager.getSession(player);
				if (dungeon == null) {
					player.sendMessage("§cYou are not in session!");
					return false;
				}
				dungeon.stop(new NeutralCause("FORCE_STOP"));
				player.sendMessage("§aOK");
				return true;
			}
			case "session-add-player" -> {
				var session = SessionManager.getSession(player);
				if (session == null) {
					player.sendMessage("§cYou are not in session!");
					return false;
				}
				if (strings.length == 0) {
					player.sendMessage("Pattern: §c/session-remove-player <player>");
					return false;
				}
				var addPlayer = Bukkit.getPlayer(strings[0]);
				if (addPlayer == null) {
					player.sendMessage("§cPattern: /session-remove-player <player>");
					return false;
				}
				session.addPlayer(addPlayer);
				player.sendMessage("§aOK");
				return true;
			}
			case "session-remove-player" -> {
				var session = SessionManager.getSession(player);
				if (session == null) {
					player.sendMessage("§cYou are not in session!");
					return false;
				}
				if (strings.length == 0) {
					player.sendMessage("Pattern: §c/session-remove-player <player>");
					return false;
				}
				var removePlayer = Bukkit.getPlayer(strings[0]);
				if (removePlayer == null) {
					player.sendMessage("Pattern: §c/session-remove-player <player>");
					return false;
				}
				session.removePlayer(removePlayer);
				player.sendMessage("§aOK");
				return true;
			}
			case "session-join" -> {
				if (strings.length != 1) {
					player.sendMessage("Pattern: §c/session-join <session-name>:<session id>");
					return false;
				}
				var findName = strings[0];
				var session = SessionManager
						.getSessions().stream()
						.filter(loopSession -> {
							var name = loopSession.getName() + ":" + loopSession.getId();
							return findName.equals(name);
						}).findFirst()
						.orElse(null);
				if (session == null) {
					player.sendMessage("§cNot found session with pattern: '" + strings[0] + "'!");
					return false;
				}
				session.addPlayer(player);
			}
			case "test-session" -> {
				try {
					var players = new ArrayList<Player>(plugin
							                                    .getServer()
							                                    .getOnlinePlayers());
					new TestSession(plugin, players).start();
					player.sendMessage("§aOK");
				} catch (
						SessionStartException e) {
					player.sendMessage("§cSession start exception: §f" + e.getMessage());
				}
			}
		}
		return true;
	}
}
