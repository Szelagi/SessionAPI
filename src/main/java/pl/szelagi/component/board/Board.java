package pl.szelagi.component.board;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.WeatherType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.MustBeInvokedByOverriders;
import org.jetbrains.annotations.NotNull;
import pl.szelagi.buildin.system.boardwatchdog.BoardWatchDogController;
import pl.szelagi.component.BaseComponent;
import pl.szelagi.component.board.event.BoardStartEvent;
import pl.szelagi.component.board.event.BoardStopEvent;
import pl.szelagi.component.board.filemanager.BoardFileManager;
import pl.szelagi.component.constructor.InitializeType;
import pl.szelagi.component.constructor.UninitializedType;
import pl.szelagi.component.session.Session;
import pl.szelagi.process.IControlProcess;
import pl.szelagi.process.RemoteProcess;
import pl.szelagi.space.Space;
import pl.szelagi.space.SpaceAllocator;
import pl.szelagi.tag.SignTagData;
import pl.szelagi.world.SessionWorldManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class Board extends BaseComponent {
    public final static String SCHEMATIC_CONSTRUCTOR_NAME = "constructor";
    public final static String SCHEMATIC_DESTRUCTOR_NAME = "destructor";
    public final static String SIGN_TAG_DATA_NAME = "signtagdata";
    private final Session session;

    @Override
    public @NotNull Session getSession() {
        return session;
    }

    private boolean isUsed;
    private Space space;
    private final RemoteProcess remoteProcess;
    private BoardFileManager boardFileManager;
    private SignTagData signTagData;

    // todo onFinal(end)
    public Board(Session session) {
        this.session = session;
        this.isUsed = false;
        remoteProcess = new RemoteProcess(session.getMainProcess());
    }

    public SignTagData getSignTagData() {
        return signTagData;
    }

    public @NotNull IControlProcess getProcess() {
        return remoteProcess;
    }

    public @NotNull JavaPlugin getPlugin() {
        return getSession().getPlugin();
    }
    public @NotNull BoardFileManager getSchematicStorage() {
        return boardFileManager;
    }

    // Start and stop

    @MustBeInvokedByOverriders
    public void start() {
        isUsed = true;
        space = SpaceAllocator.allocate(SessionWorldManager.getSessionWorld());
        this.boardFileManager = new BoardFileManager(getName(), getSpace());

        if (boardFileManager.existsSignTagData(SIGN_TAG_DATA_NAME)) {
            this.signTagData = boardFileManager.loadSignTagData(SIGN_TAG_DATA_NAME);
        } else {
            this.signTagData = new SignTagData();
        }

        // initialize players
        constructor();
        for (var p : getSession().getPlayers()) systemPlayerConstructor(p, InitializeType.COMPONENT_CONSTRUCTOR);


        generate();


        // BoardStartEvent
        var event = new BoardStartEvent(this);
        Bukkit.getPluginManager().callEvent(event);

        new BoardWatchDogController(this).start();
    }

    @MustBeInvokedByOverriders
    public void stop() {
        remoteProcess.destroy();

        // initialize players
        for (var p : getSession().getPlayers()) systemPlayerDestructor(p, UninitializedType.COMPONENT_DESTRUCTOR);
        destructor();

        // degenerate
        degenerate();

        // deallocate space
        SpaceAllocator.deallocate(space);

        // BoardStopEvent
        var event = new BoardStopEvent(this);
        Bukkit.getPluginManager().callEvent(event);
    }
    // Protected
    protected Location getBase() {
        return getSpace().getCenter();
    }
    public Space getSpace() {
        return space;
    }
    // Abstract
    protected void generate() {
        getSpace().getCenter().getBlock().setType(Material.BEDROCK);
        if (boardFileManager.existsSchematic(SCHEMATIC_CONSTRUCTOR_NAME)) {
            boardFileManager.loadSchematic(SCHEMATIC_CONSTRUCTOR_NAME);
        }
        if (signTagData != null) {
            for (var l : signTagData.toLocations()) l.getBlock().setType(Material.AIR);
        }

            // error
//            var spatial = schematicStorage.loadSpatial(SCHEMATIC_CONSTRUCTOR_NAME);
//            for (var p : Bukkit.getServer().getOnlinePlayers()) p.sendMessage(spatial.getCenter().toString());
//            for (var p : Bukkit.getServer().getOnlinePlayers()) p.sendMessage(getSpace().getCenter().toString());
//            processedData = PreProcessor.process(spatial);
//            signTagData = SignTagAnalyzer.process(getSpace());
//
//        } else {
//            signTagData = SignTagAnalyzer.process(getSpace());

    };
    protected void degenerate() {
        if (boardFileManager.existsSchematic(SCHEMATIC_DESTRUCTOR_NAME))
            boardFileManager.loadSchematic(SCHEMATIC_DESTRUCTOR_NAME);
    }
    protected int getDefaultTime() {
        return 0;
    }
    @Nullable
    public Listener getListener() {
        return null;
    }
    @Nonnull
    protected WeatherType getDefaultWeather() {
        return WeatherType.CLEAR;
    }
    @Nonnull
    public abstract String getName();

    protected Location getStartSpawnLocation() {
        return getSpace().getAbove(getSpace().getCenter());
    }
    // Public methods
    public boolean isUsed() {
        return isUsed;
    }
    // Private methods


    @MustBeInvokedByOverriders
    public void systemPlayerConstructor(Player player, InitializeType type) {
        playerConstructor(player, type);
        // recursive for player add
        if (type == InitializeType.PLAYER_ADD) {
            // controllers in
            for (var controller : getProcess().getControllers()) {
                controller.systemPlayerConstructor(player, type);
            }
        }
    }

    @MustBeInvokedByOverriders
    public void systemPlayerDestructor(Player player, UninitializedType type) {
        // recursive for player remove
        if (type == UninitializedType.PLAYER_REMOVE) {
            // controllers in
            for (var controller : getProcess().getControllers()) {
                controller.systemPlayerDestructor(player, type);
            }
        }
        playerDestructor(player, type);
    }

    @Override
    public void playerConstructor(Player player, InitializeType type) {
        super.playerConstructor(player, type);
        player.teleport(getStartSpawnLocation());
        player.setPlayerTime(getDefaultTime(), false);
        player.setPlayerWeather(getDefaultWeather());
    }

    @Override
    public void playerDestructor(Player player, UninitializedType type) {
        super.playerDestructor(player, type);
        player.resetPlayerWeather();
        player.resetPlayerTime();
    }
}
