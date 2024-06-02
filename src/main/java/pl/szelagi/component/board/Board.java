package pl.szelagi.component.board;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.WeatherType;
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
import pl.szelagi.tag.TagResolve;
import pl.szelagi.util.Debug;
import pl.szelagi.world.SessionWorldManager;

public abstract class Board extends BaseComponent {
	public final static String SCHEMATIC_CONSTRUCTOR_NAME = "constructor";
	public final static String SCHEMATIC_DESTRUCTOR_NAME = "destructor";
	public final static String SIGN_TAG_DATA_NAME = "signtagdata";
	private final Session session;
	private RemoteProcess remoteProcess;
	private boolean isUsed;
	private Space space;
	private BoardFileManager boardFileManager;
	private TagResolve tagResolve;

	public Board(Session session) {
		this.session = session;
		this.isUsed = false;
	}

	@MustBeInvokedByOverriders
	public void start() throws StartException {
		if (isEnable())
			throw new MultiStartException(this);
		if (isUsed)
			throw new StartException("board start used");
		setEnable(true);
		isUsed = true;
		Debug.send(this, "start");

		remoteProcess = new RemoteProcess(this);
		remoteProcess.registerListener(this);

		space = SpaceAllocator.allocate(SessionWorldManager.getSessionWorld());
		this.boardFileManager = new BoardFileManager(getName(), getSpace());

		if (boardFileManager.existsSignTagData(SIGN_TAG_DATA_NAME)) {
			this.tagResolve = boardFileManager.loadSignTagData(SIGN_TAG_DATA_NAME);
		} else {
			this.tagResolve = new TagResolve();
		}

		Debug.send(this, "generate");
		//syncBukkitTask(this::generate);
		generate();

		invokeSelfComponentConstructor();
		invokeSelfPlayerConstructors();

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

		invokeSelfPlayerDestructors();
		invokeSelfComponentDestructor();

		remoteProcess.destroy();

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

	protected void generate() {
		getSpace().getCenter().getBlock()
		          .setType(Material.BEDROCK);
		if (boardFileManager.existsSchematic(SCHEMATIC_DESTRUCTOR_NAME)) {
			var secureZone = boardFileManager.toSpatial(SCHEMATIC_DESTRUCTOR_NAME, getBase());
		}
		if (boardFileManager.existsSchematic(SCHEMATIC_CONSTRUCTOR_NAME)) {
			boardFileManager.loadSchematic(SCHEMATIC_CONSTRUCTOR_NAME);
		}
		if (tagResolve != null) {
			for (var l : tagResolve.toLocations())
				l.getBlock()
				 .setType(Material.AIR);
		}

		//SessionAPI.debug("" + spatial.size());
	}

	protected void degenerate() {
		if (boardFileManager.existsSchematic(SCHEMATIC_DESTRUCTOR_NAME))
			boardFileManager.loadSchematic(SCHEMATIC_DESTRUCTOR_NAME);
		for (var entity : getSpace().getMobsIn())
			entity.remove();
	}

	public final Location getBase() {
		return getSpace().getCenter();
	}

	public final Space getSpace() {
		return space;
	}

	@Override
	public final @NotNull Session getSession() {
		return session;
	}

	public final @NotNull TagResolve getSignTagData() {
		return tagResolve;
	}

	public final @NotNull RemoteProcess getProcess() {
		return remoteProcess;
	}

	public final @NotNull JavaPlugin getPlugin() {
		return getSession().getPlugin();
	}

	public final @NotNull BoardFileManager getSchematicStorage() {
		return boardFileManager;
	}

	public final boolean isUsed() {
		return isUsed;
	}

	@Override
	public final @NotNull RemoteProcess getParentProcess() {
		return session.getProcess();
	}

	protected int getDefaultTime() {
		return 0;
	}

	protected @NotNull WeatherType getDefaultWeather() {
		return WeatherType.CLEAR;
	}

	protected @NotNull Location getStartSpawnLocation() {
		return getSpace().getAbove(getSpace().getCenter());
	}

	@Override
	public void playerConstructor(PlayerConstructorEvent event) {
		super.playerConstructor(event);
		var player = event.getPlayer();
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
}
