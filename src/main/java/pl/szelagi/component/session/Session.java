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
import pl.szelagi.component.baseexception.multi.MultiStartException;
import pl.szelagi.component.baseexception.multi.MultiStopException;
import pl.szelagi.component.board.Board;
import pl.szelagi.component.constructor.InitializeType;
import pl.szelagi.component.constructor.UninitializedType;
import pl.szelagi.component.session.cause.StopCause;
import pl.szelagi.component.session.event.SessionStartEvent;
import pl.szelagi.component.session.event.SessionStopEvent;
import pl.szelagi.component.session.exception.SessionStartException;
import pl.szelagi.component.session.exception.SessionStopException;
import pl.szelagi.component.session.exception.player.initialize.PlayerInSessionException;
import pl.szelagi.component.session.exception.player.initialize.PlayerInitializeException;
import pl.szelagi.component.session.exception.player.initialize.PlayerIsNotAliveException;
import pl.szelagi.component.session.exception.player.uninitialize.PlayerNoInThisSession;
import pl.szelagi.component.session.exception.player.uninitialize.PlayerUninitializeException;
import pl.szelagi.manager.SessionManager;
import pl.szelagi.process.IControlProcess;
import pl.szelagi.process.MainProcess;
import pl.szelagi.process.RemoteProcess;
import pl.szelagi.util.Debug;
import pl.szelagi.util.timespigot.Time;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public abstract class Session extends BaseComponent {
    @Override
    public @NotNull final Session getSession() {
        return this;
    }
    private final MainProcess mainProcess;
    private final RemoteProcess remoteProcess;
    private final JavaPlugin plugin;
    protected final ArrayList<Player> players = new ArrayList<>();
    private final ArrayList<Player> initialPlayers;
    private Board currentBoard;

    private RecoveryPlayerController recoveryPlayerController;

    public Session(JavaPlugin plugin, ArrayList<Player> players) {
        this.plugin = plugin;
        this.initialPlayers = players;
        this.mainProcess = new MainProcess(this);
        this.remoteProcess = new RemoteProcess(mainProcess);
    }

    public Session(JavaPlugin plugin, Player player) {
        this.plugin = plugin;

        this.initialPlayers = new ArrayList<>();
        initialPlayers.add(player);

        this.mainProcess = new MainProcess(this);
        this.remoteProcess = new RemoteProcess(mainProcess);
    }

    public Session(JavaPlugin plugin) {
        this.plugin = plugin;

        this.initialPlayers = new ArrayList<>();
        this.mainProcess = new MainProcess(this);
        this.remoteProcess = new RemoteProcess(mainProcess);
    }

    @MustBeInvokedByOverriders
    public void start() throws SessionStartException, PlayerInitializeException {
        if (isEnable()) throw new MultiStartException(this);
        // exceptions system
        for (var player : initialPlayers) {
            PlayerInSessionException.check(player);
            PlayerIsNotAliveException.check(player);
        }
        setEnable(true);
        Debug.send(this, "start");
        currentBoard = getDefaultStartBoard();

        // initialize consturcot
        Debug.send(this, "constructor");
        constructor();
        // initialize players
        for (var player : initialPlayers) {
            systemPlayerConstructor(player, InitializeType.COMPONENT_CONSTRUCTOR, true, false);
        }

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
        if (!isEnable()) throw new MultiStopException(this);

        Debug.send(this, "stop");

        //stop and destroy all tasks
        mainProcess.destroy();
        currentBoard.stop();

        // deinitialize players
        var playersArrayCopy = new ArrayList<>(players);
        for (var player : playersArrayCopy) {
            systemPlayerDestructor(player, UninitializedType.COMPONENT_DESTRUCTOR);
        }
        // system destructor
        Debug.send(this, "destructor");
        destructor(cause);


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
    private void systemPlayerConstructor(Player player, InitializeType type, boolean invokeControllers, boolean invokeBoard) {
        SessionManager.addRelation(player, this.getSession());
        players.add(player);
        Debug.send(this, "playerConstructor " + player.getName() + ", " + type.name());
        playerConstructor(player, type);
        // recursive for player add
        // controllers in
        if (invokeControllers) {
            for (var controller : getProcess().getControllers()) {
                reflectionSystemPlayerConstructor(controller, player, type);
            }
        }
        // board
        if (invokeBoard) {
            reflectionSystemPlayerConstructor(getCurrentBoard(), player, type);
        }
    }
    @MustBeInvokedByOverriders
    private void systemPlayerDestructor(Player player, UninitializedType type) {
        SessionManager.removeRelation(player);
        players.remove(player);
        // recursive for player remove
        if (type == UninitializedType.PLAYER_REMOVE) {
            // controllers in
            for (var controller : getProcess().getControllers()) {
                reflectionSystemPlayerDestructor(controller, player, type);
            }
            // board
            reflectionSystemPlayerDestructor(getCurrentBoard(), player, type);
        }
        Debug.send(this, "playerDestructor " + player.getName() + ", " + type.name());
        playerDestructor(player, type);
    }

    // Abstract
    @Nonnull
    protected abstract Board getDefaultStartBoard();
    public final @NotNull IControlProcess getProcess() {
        return remoteProcess;
    }

    public final @NotNull MainProcess getMainProcess() {
        return mainProcess;
    }

    public final Board getCurrentBoard() {
        return currentBoard;
    }

    @MustBeInvokedByOverriders
    public void addPlayer(Player player) throws PlayerInitializeException {
        PlayerIsNotAliveException.check(player);
        PlayerInSessionException.check(player);
        systemPlayerConstructor(player, InitializeType.PLAYER_ADD, true, true);
    }

    @MustBeInvokedByOverriders
    public void removePlayer(Player player) throws PlayerUninitializeException {
        PlayerNoInThisSession.check(this, player);
        systemPlayerDestructor(player, UninitializedType.PLAYER_REMOVE);

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
    public final JavaPlugin getPlugin() {
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

    public ArrayList<Player> getInitialPlayers() {
        return initialPlayers;
    }
}