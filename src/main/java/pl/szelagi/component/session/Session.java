package pl.szelagi.component.session;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.MustBeInvokedByOverriders;
import org.jetbrains.annotations.NotNull;
import pl.szelagi.buildin.system.recovery.RecoveryPlayerController;
import pl.szelagi.buildin.system.sessionsafecontrolplayers.SessionSafeControlPlayers;
import pl.szelagi.buildin.system.sessionwatchdog.SessionWatchDogController;
import pl.szelagi.component.BaseComponent;
import pl.szelagi.component.board.Board;
import pl.szelagi.component.constructor.InitializeType;
import pl.szelagi.component.constructor.UninitializedType;
import pl.szelagi.component.session.cause.StopCause;
import pl.szelagi.component.session.event.SessionStartEvent;
import pl.szelagi.component.session.event.SessionStopEvent;
import pl.szelagi.component.session.exception.SessionStartException;
import pl.szelagi.component.session.exception.SessionStopException;
import pl.szelagi.component.session.exception.other.SessionIsDisableException;
import pl.szelagi.component.session.exception.other.SessionIsEnableException;
import pl.szelagi.component.session.exception.player.initialize.PlayerInSessionException;
import pl.szelagi.component.session.exception.player.initialize.PlayerInitializeException;
import pl.szelagi.component.session.exception.player.initialize.PlayerIsNotAliveException;
import pl.szelagi.component.session.exception.player.uninitialize.PlayerNoInThisSession;
import pl.szelagi.component.session.exception.player.uninitialize.PlayerUninitializeException;
import pl.szelagi.manager.SessionManager;
import pl.szelagi.process.IControlProcess;
import pl.szelagi.process.MainProcess;
import pl.szelagi.process.RemoteProcess;
import pl.szelagi.util.timespigot.Time;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public abstract class Session extends BaseComponent {
    @Override
    public @NotNull Session getSession() {
        return this;
    }

    private final MainProcess mainProcess;
    private final RemoteProcess remoteProcess;

    private final JavaPlugin plugin;

    protected final ArrayList<Player> players;


    private Board currentBoard;

    private RecoveryPlayerController recoveryPlayerController;

    public Session(JavaPlugin plugin, ArrayList<Player> players) {
        this.plugin = plugin;
        this.players = players;
        this.mainProcess = new MainProcess(this);
        this.remoteProcess = new RemoteProcess(mainProcess);
    }

    public Session(JavaPlugin plugin, Player player) {
        this.plugin = plugin;

        this.players = new ArrayList<>();
        this.players.add(player);

        this.mainProcess = new MainProcess(this);
        this.remoteProcess = new RemoteProcess(mainProcess);
    }

    public Session(JavaPlugin plugin) {
        this.plugin = plugin;

        this.players = new ArrayList<>();
        this.mainProcess = new MainProcess(this);
        this.remoteProcess = new RemoteProcess(mainProcess);
    }

    @MustBeInvokedByOverriders
    public void start() throws SessionStartException, PlayerInitializeException {
        if (isEnable()) throw new SessionIsEnableException("session is enable exception " + getName());
        // exceptions system
        for (var player : getPlayers()) {
            PlayerInSessionException.check(player);
            PlayerIsNotAliveException.check(player);
        }

        setEnable(true);

        currentBoard = getDefaultStartBoard();



        // initialize consturcot
        constructor();
        // initialize players
        for (var p : players) systemPlayerConstructor(p, InitializeType.COMPONENT_CONSTRUCTOR);

        // run system tasks
        getProcess().runControlledTaskTimer(
                mainProcess::optimiseTasks,
                Time.Seconds(60),
                Time.Seconds((60)));

        var event = new SessionStartEvent(this);
        Bukkit.getPluginManager().callEvent(event);

        currentBoard.start();

        recoveryPlayerController = new RecoveryPlayerController(this);
        recoveryPlayerController.start();

        new SessionWatchDogController(this).start();
        //new LifeController(this, 5, Time.Seconds(5)).start();
        //new PhysicsController(this).start();
        //new StopQuitController(this).start();
    }


    @Override
    public void constructor() {
        super.constructor();
        new SessionSafeControlPlayers(this).start();
    }

    @MustBeInvokedByOverriders
    public void stop(StopCause cause) throws SessionStopException {
        if (!isEnable()) throw new SessionIsDisableException(".stop() for disable session " + getName());

        // deinitialize players
        for (var p : players) systemPlayerDestructor(p, UninitializedType.COMPONENT_DESTRUCTOR);
        // system destructor
        destructor(cause);

        //stop and destroy all tasks
        mainProcess.destroy();
        currentBoard.stop();

        setEnable(false);

        var event = new SessionStopEvent(this);
        Bukkit.getPluginManager().callEvent(event);
    }

    @MustBeInvokedByOverriders
    protected final void setBoard(Board board) {
        //if (board.isUsed()) throw new BoardIsUsedException(); add to #Board.start()
        currentBoard.stop();
        currentBoard = board;
        currentBoard.start();
    }

    // Public
    public ArrayList<Player> getPlayers() {
        return players;
    }
    public int getPlayerCount() {
        return players.size();
    }

    @MustBeInvokedByOverriders
    public final void systemPlayerConstructor(Player player, InitializeType type) {
        playerConstructor(player, type);
        // recursive for player add
        if (type == InitializeType.PLAYER_ADD) {
            // controllers in
            for (var controller : getProcess().getControllers()) {
                controller.systemPlayerConstructor(player, type);
            }
            // board
            getCurrentBoard().systemPlayerConstructor(player, type);
        }
    }
    @MustBeInvokedByOverriders
    public final void systemPlayerDestructor(Player player, UninitializedType type) {
        // recursive for player remove
        if (type == UninitializedType.PLAYER_REMOVE) {
            // controllers in
            for (var controller : getProcess().getControllers()) {
                controller.systemPlayerDestructor(player, type);
            }
            // board
            getCurrentBoard().systemPlayerDestructor(player, type);
        }
        playerDestructor(player, type);
    }

    // Abstract
    @Nonnull
    protected abstract Board getDefaultStartBoard();
    @Nonnull
    public abstract String getName();
    // Private
    // Systems
    // Tasks
    public @NotNull IControlProcess getProcess() {
        return remoteProcess;
    }

    public @NotNull MainProcess getMainProcess() {
        return mainProcess;
    }


    // todo: playerAdd, playerRemove initializer


    public Board getCurrentBoard() {
        return currentBoard;
    }

    @MustBeInvokedByOverriders
    public void addPlayer(Player player) throws PlayerInitializeException {
        PlayerIsNotAliveException.check(player);
        PlayerInSessionException.check(player);

        players.add(player);
        systemPlayerConstructor(player, InitializeType.PLAYER_ADD);
    }

    @MustBeInvokedByOverriders
    public void removePlayer(Player player) throws PlayerUninitializeException {
        PlayerNoInThisSession.check(this, player);

        players.remove(player);

        systemPlayerDestructor(player, UninitializedType.PLAYER_REMOVE);

    }

    @Override
    public void playerConstructor(Player player, InitializeType type) {
        super.playerConstructor(player, type);
        SessionManager.addRelation(player, this);
    }

    @Override
    public void playerDestructor(Player player, UninitializedType type) {
        super.playerDestructor(player, type);
        SessionManager.removeRelation(player);
    }

    @Override
    public final void destructor() {
        super.destructor();
    }

    public void destructor(StopCause cause) {}

    public void forceSaveRecovery() {
        recoveryPlayerController.save();
    }

    @NotNull
    @Override
    public JavaPlugin getPlugin() {
        return plugin;
    }

    /**
     * @controllers PhysicsController, LifeController, StopQuitController, NoPlaceAndBreakController
     */
    @Override
    protected void startBaseControllers() {
        super.startBaseControllers();
//        new PhysicsController(this).start();
//        new LifeController(this, 5, Time.Seconds(10)).start();
//        new StopQuitController(this).start();
//        new NoPlaceAndBreakController(this).start();
    }
}