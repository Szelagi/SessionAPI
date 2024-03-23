package pl.szelagi.component.session;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.MustBeInvokedByOverriders;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.szelagi.buildin.system.recovery.RecoveryPlayerController;
import pl.szelagi.buildin.system.sessionsafecontrolplayers.SessionSafeControlPlayers;
import pl.szelagi.buildin.system.sessionwatchdog.SessionWatchDogController;
import pl.szelagi.component.BaseComponent;
import pl.szelagi.component.baseexception.multi.MultiStartException;
import pl.szelagi.component.baseexception.multi.MultiStopException;
import pl.szelagi.component.board.Board;
import pl.szelagi.component.session.cause.StopCause;
import pl.szelagi.component.session.event.SessionStartEvent;
import pl.szelagi.component.session.event.SessionStopEvent;
import pl.szelagi.component.session.exception.SessionStartException;
import pl.szelagi.component.session.exception.SessionStopException;
import pl.szelagi.component.session.exception.player.initialize.PlayerInSessionException;
import pl.szelagi.component.session.exception.player.initialize.PlayerIsNotAliveException;
import pl.szelagi.component.session.exception.player.initialize.PlayerJoinException;
import pl.szelagi.component.session.exception.player.initialize.RejectedPlayerException;
import pl.szelagi.component.session.exception.player.uninitialize.PlayerNoInThisSession;
import pl.szelagi.component.session.exception.player.uninitialize.PlayerQuitException;
import pl.szelagi.event.component.ComponentConstructorEvent;
import pl.szelagi.event.player.canchange.PlayerCanJoinEvent;
import pl.szelagi.event.player.canchange.PlayerCanQuitEvent;
import pl.szelagi.event.player.canchange.type.JoinType;
import pl.szelagi.event.player.canchange.type.QuitType;
import pl.szelagi.event.player.initialize.InvokeType;
import pl.szelagi.event.player.initialize.PlayerConstructorEvent;
import pl.szelagi.event.player.initialize.PlayerDestructorEvent;
import pl.szelagi.manager.SessionManager;
import pl.szelagi.process.MainProcess;
import pl.szelagi.process.RemoteProcess;
import pl.szelagi.util.Debug;
import pl.szelagi.util.timespigot.Time;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public abstract class Session extends BaseComponent {
	protected final ArrayList<Player> players = new ArrayList<>();
	private final MainProcess mainProcess;
	private RemoteProcess remoteProcess;
	private final JavaPlugin plugin;
	private Board currentBoard;
	private RecoveryPlayerController recoveryPlayerController;

	public Session(JavaPlugin plugin) {
		this.plugin = plugin;
		this.mainProcess = new MainProcess(this);
	}

	@MustBeInvokedByOverriders
	public void start() throws SessionStartException, PlayerJoinException {
		if (isEnable())
			throw new MultiStartException(this);
		setEnable(true);
		Debug.send(this, "start");

		remoteProcess = new RemoteProcess(mainProcess);
		remoteProcess.registerListener(this);

		currentBoard = getDefaultStartBoard();

		invokeSelfComponentConstructor();
		invokeSelfPlayerConstructors();

		var event = new SessionStartEvent(this);
		callBukkitEvent(event);

		currentBoard.start();
	}

	@MustBeInvokedByOverriders
	public void stop(StopCause cause) throws SessionStopException {
		if (!isEnable())
			throw new MultiStopException(this);
		setEnable(false);
		Debug.send(this, "stop");

		var playersArrayCopy = new ArrayList<>(players);
		playersArrayCopy.forEach(this::removePlayer);

		invokeSelfPlayerDestructors();
		invokeSelfComponentDestructor();

		//stop and destroy all tasks
		remoteProcess.destroy();
		mainProcess.destroy();

		var event = new SessionStopEvent(this);
		Bukkit.getPluginManager()
		      .callEvent(event);
	}

	@MustBeInvokedByOverriders
	public final void addPlayer(Player player) throws PlayerJoinException {
		PlayerIsNotAliveException.check(player);
		PlayerInSessionException.check(player);
		Debug.send(this, "try add player");
		var canJoinEvent = new PlayerCanJoinEvent(player, getPlayers(), JoinType.PLUGIN);
		getProcess().invokeAllListeners(canJoinEvent);
		if (canJoinEvent.isCanceled()) {
			assert canJoinEvent.getCancelCause() != null;
			throw new RejectedPlayerException(canJoinEvent.getCancelCause());
		}
		var otherPlayers = new ArrayList<>(getPlayers());

		SessionManager.addRelation(player, this.getSession());
		players.add(player);

		Debug.send(this, "add player");
		var joinEvent = new PlayerConstructorEvent(player, otherPlayers, getPlayers(), InvokeType.CHANGE);
		getProcess().invokeAllListeners(joinEvent);
	}

	@MustBeInvokedByOverriders
	public final void removePlayer(Player player) throws PlayerQuitException {
		PlayerNoInThisSession.check(this, player);
		Debug.send(this, "try remove player");
		var canPlayerQuit = new PlayerCanQuitEvent(player, getPlayers(), QuitType.PLUGIN_FORCE);
		getProcess().invokeAllListeners(canPlayerQuit);
		if (canPlayerQuit.isCanceled()) {
			assert canPlayerQuit.getCancelCause() != null;
			throw new PlayerQuitException(canPlayerQuit
					                              .getCancelCause()
					                              .message());
		}
		var otherPlayers = new ArrayList<>(getPlayers());

		SessionManager.removeRelation(player);
		players.remove(player);

		Debug.send(this, "remove player");
		var quitEvent = new PlayerDestructorEvent(player, otherPlayers, getPlayers(), InvokeType.CHANGE);
		getProcess().invokeAllListeners(quitEvent);
	}

	@MustBeInvokedByOverriders
	protected final void setBoard(Board board) {
		//if (board.isUsed()) throw new BoardIsUsedException(); add to #Board.start()
		currentBoard.stop();
		currentBoard = board;
		currentBoard.start();
	}

	public final @NotNull List<Player> getPlayers() {
		return players;
	}

	public final int getPlayerCount() {
		return players.size();
	}

	public final @NotNull RemoteProcess getProcess() {
		return remoteProcess;
	}

	public final @NotNull MainProcess getMainProcess() {
		return mainProcess;
	}

	public final Board getCurrentBoard() {
		return currentBoard;
	}

	@Override
	public final @NotNull JavaPlugin getPlugin() {
		return plugin;
	}

	@Override
	public @Nullable RemoteProcess getParentProcess() {
		return null;
	}

	@Override
	public final @NotNull Session getSession() {
		return this;
	}

	public final void saveRecovery() {
		recoveryPlayerController.save();
	}

	public final void systemSessionConstructor(ComponentConstructorEvent event) {
		new SessionWatchDogController(this).start();
		new SessionSafeControlPlayers(this).start();
		recoveryPlayerController = new RecoveryPlayerController(this);
		recoveryPlayerController.start();
		// run system tasks
		getProcess().runControlledTaskTimer(mainProcess::optimiseTasks, Time.Seconds(60), Time.Seconds((60)));
	}

	protected abstract @Nonnull Board getDefaultStartBoard();
}