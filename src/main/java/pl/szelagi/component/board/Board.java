package pl.szelagi.component.board;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.WeatherType;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.MustBeInvokedByOverriders;
import org.jetbrains.annotations.NotNull;
import pl.szelagi.buildin.controller.NoNatrualSpawnController.NoNaturalSpawnController;
import pl.szelagi.buildin.system.boardwatchdog.BoardWatchDogController;
import pl.szelagi.component.BaseComponent;
import pl.szelagi.component.baseexception.StartException;
import pl.szelagi.component.baseexception.StopException;
import pl.szelagi.component.baseexception.multi.MultiStartException;
import pl.szelagi.component.baseexception.multi.MultiStopException;
import pl.szelagi.component.board.event.BoardStartEvent;
import pl.szelagi.component.board.event.BoardStopEvent;
import pl.szelagi.component.board.filemanager.BoardFileManager;
import pl.szelagi.component.session.Session;
import pl.szelagi.event.player.initialize.PlayerConstructorEvent;
import pl.szelagi.event.player.initialize.PlayerDestructorEvent;
import pl.szelagi.process.RemoteProcess;
import pl.szelagi.space.Space;
import pl.szelagi.space.SpaceAllocator;
import pl.szelagi.tag.SignTagData;
import pl.szelagi.util.Debug;
import pl.szelagi.world.SessionWorldManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class Board extends BaseComponent {
	public final static String SCHEMATIC_CONSTRUCTOR_NAME = "constructor";
	public final static String SCHEMATIC_DESTRUCTOR_NAME = "destructor";
	public final static String SIGN_TAG_DATA_NAME = "signtagdata";
	private final Session session;
	private RemoteProcess remoteProcess;
	private boolean isUsed;
	private Space space;
	private BoardFileManager boardFileManager;
	private SignTagData signTagData;

	public Board(Session session) {
		this.session = session;
		this.isUsed = false;
	}

	@Override
	public @NotNull Session getSession() {
		return session;
	}

	public SignTagData getSignTagData() {
		return signTagData;
	}

	public @NotNull RemoteProcess getProcess() {
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
	public void start() throws StartException {
		if (isEnable())
			throw new MultiStartException(this);
		if (isUsed)
			throw new StartException("board start used");
		setEnable(true);
		isUsed = true;

		remoteProcess = new RemoteProcess(this);
		remoteProcess.registerListener(this);

		// start exception

		Debug.send(this, "start");

		space = SpaceAllocator.allocate(SessionWorldManager.getSessionWorld());
		this.boardFileManager = new BoardFileManager(getName(), getSpace());

		if (boardFileManager.existsSignTagData(SIGN_TAG_DATA_NAME)) {
			this.signTagData = boardFileManager.loadSignTagData(SIGN_TAG_DATA_NAME);
		} else {
			this.signTagData = new SignTagData();
		}

		// initialize players
		Debug.send(this, "constructor");

		invokeSelfComponentConstructor();
		invokeSelfPlayerConstructors();

		Debug.send(this, "generate");
		syncBukkitTask(this::generate);

		// BoardStartEvent
		var event = new BoardStartEvent(this);
		callBukkitEvent(event);

		new BoardWatchDogController(this).start();
		new NoNaturalSpawnController(this).start();
	}

	@MustBeInvokedByOverriders
	public void stop() throws StopException {
		if (!isEnable())
			throw new MultiStopException(this);
		setEnable(false);

		Debug.send(this, "stop");

		remoteProcess.destroy();

		Debug.send(this, "destructor");
		invokeSelfPlayerDestructors();
		invokeSelfComponentDestructor();

		// degenerate
		Debug.send(this, "degenerate");
		degenerate();

		// deallocate space
		SpaceAllocator.deallocate(space);

		// BoardStopEvent
		var event = new BoardStopEvent(this);
		Bukkit.getPluginManager()
		      .callEvent(event);
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
		getSpace().getCenter().getBlock()
		          .setType(Material.BEDROCK);
		if (boardFileManager.existsSchematic(SCHEMATIC_CONSTRUCTOR_NAME)) {
			boardFileManager.loadSchematic(SCHEMATIC_CONSTRUCTOR_NAME);
		}
		if (signTagData != null) {
			for (var l : signTagData.toLocations())
				l.getBlock()
				 .setType(Material.AIR);
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

	}

	protected void degenerate() {
		if (boardFileManager.existsSchematic(SCHEMATIC_DESTRUCTOR_NAME))
			boardFileManager.loadSchematic(SCHEMATIC_DESTRUCTOR_NAME);
		for (var entity : getSpace().getMobsIn())
			entity.remove();
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

	protected Location getStartSpawnLocation() {
		return getSpace().getAbove(getSpace().getCenter());
	}

	// Public methods
	public boolean isUsed() {
		return isUsed;
	}
	// Private methods

	@Override
	public void playerConstructor(PlayerConstructorEvent event) {
		super.playerConstructor(event);
		var player = event.getPlayer();
		player.sendMessage("b");
		player.teleport(getStartSpawnLocation());
		player.setPlayerTime(getDefaultTime(), false);
		player.setPlayerWeather(getDefaultWeather());
	}

	@Override
	public void playerDestructor(PlayerDestructorEvent event) {
		super.playerDestructor(event);
		var player = event.getPlayer();
		player.resetPlayerWeather();
		player.resetPlayerTime();
	}

	@Override
	public RemoteProcess getParentProcess() {
		return session.getProcess();
	}
}
