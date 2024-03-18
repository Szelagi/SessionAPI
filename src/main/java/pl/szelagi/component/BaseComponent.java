package pl.szelagi.component;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.MustBeInvokedByOverriders;
import org.jetbrains.annotations.NotNull;
import pl.szelagi.component.board.Board;
import pl.szelagi.component.controller.Controller;
import pl.szelagi.component.session.Session;
import pl.szelagi.event.EventListener;
import pl.szelagi.event.component.ComponentConstructorEvent;
import pl.szelagi.event.component.ComponentDestructorEvent;
import pl.szelagi.event.player.canchange.PlayerCanJoinEvent;
import pl.szelagi.event.player.initialize.InvokeType;
import pl.szelagi.event.player.initialize.PlayerConstructorEvent;
import pl.szelagi.event.player.initialize.PlayerDestructorEvent;
import pl.szelagi.event.player.recovery.PlayerRecoveryEvent;
import pl.szelagi.process.RemoteProcess;
import pl.szelagi.util.IncrementalGenerator;
import pl.szelagi.util.PluginRegistry;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

// Component must implement methods:
//    private void systemPlayerConstructor(Player player, InitializeType type)
//    private void systemPlayerDestructor(Player player, UninitializedType type)

public abstract class BaseComponent implements ISessionComponent, EventListener {
	private static final IncrementalGenerator incrementalGenerator = new IncrementalGenerator();
	private final UUID uuid = UUID.randomUUID();
	private final long id = incrementalGenerator.next();
	private final String name = generateName();
	private boolean isEnable = false;

	public boolean isEnable() {
		return isEnable;
	}

	protected void setEnable(boolean enable) {
		isEnable = enable;
	}

	public UUID getUuid() {
		return uuid;
	}

	public long getId() {
		return id;
	}

	@Override
	public @NotNull String getName() {
		return name;
	}

	private char getComponentTypeChar() {
		if (this instanceof Controller)
			return 'C';
		if (this instanceof Board)
			return 'B';
		if (this instanceof Session)
			return 'S';
		return '-';
	}

	private String generateName() {
		var currentJarFile = new File(this
				                              .getClass()
				                              .getProtectionDomain()
				                              .getCodeSource()
				                              .getLocation()
				                              .getFile());
		var plugin = PluginRegistry.getPlugin(currentJarFile.getName());
		var pluginName = plugin != null ? plugin.getName() : currentJarFile.getName();
		return this.getClass()
		           .getSimpleName() + getComponentTypeChar() + '#' + pluginName;
	}

	public abstract RemoteProcess getParentProcess();

	protected void callBukkitEvent(Event event) {
		getPlugin().getServer().getScheduler()
		           .runTask(getPlugin(), () -> getPlugin()
				           .getServer()
				           .getPluginManager()
				           .callEvent(event));
	}

	protected void syncBukkitTask(Runnable runnable) {
		getPlugin().getServer().getScheduler()
		           .runTask(getPlugin(), runnable);
	}

	private Collection<Player> getOtherPlayers(Player player, Collection<Player> allPlayers) {
		return allPlayers.stream()
		                 .filter(p -> !p.equals(player))
		                 .collect(Collectors.toCollection(ArrayList::new));
	}

	protected void invokeSelfPlayerConstructors() {
		var players = getSession().getPlayers();
		var clone = new ArrayList<>(players);
		clone.forEach((p) -> invokeSelfPlayerConstructor(p, players));
	}

	protected void invokeSelfPlayerDestructors() {
		var players = getSession().getPlayers();
		var clone = new ArrayList<>(players);
		clone.forEach((p) -> invokeSelfPlayerDestructor(p, players));
	}

	protected void invokeSelfComponentConstructor() {
		getProcess().invokeSelfListeners(new ComponentConstructorEvent(this, getSession().getPlayers()));
	}

	protected void invokeSelfComponentDestructor() {
		getProcess().invokeSelfListeners(new ComponentDestructorEvent(this, getSession().getPlayers()));
	}

	private void invokeSelfPlayerConstructor(Player player, Collection<Player> allPlayers) {
		var otherPlayer = getOtherPlayers(player, allPlayers);
		var event = new PlayerConstructorEvent(player, otherPlayer, allPlayers, InvokeType.SELF);
		getProcess().invokeSelfListeners(event);
	}

	private void invokeSelfPlayerDestructor(Player player, Collection<Player> allPlayers) {
		var otherPlayer = getOtherPlayers(player, allPlayers);
		var event = new PlayerDestructorEvent(player, otherPlayer, allPlayers, InvokeType.SELF);
		getProcess().invokeReverseSelfListeners(event);
	}

	@MustBeInvokedByOverriders
	public void componentConstructor(ComponentConstructorEvent event) {}

	@MustBeInvokedByOverriders
	public void componentDestructor(ComponentDestructorEvent event) {}

	@MustBeInvokedByOverriders
	public void playerConstructor(PlayerConstructorEvent event) {}

	@MustBeInvokedByOverriders
	public void playerDestructor(PlayerDestructorEvent event) {}

	@MustBeInvokedByOverriders
	public void playerCanJoin(PlayerCanJoinEvent event) {}

	@MustBeInvokedByOverriders
	public void playerCanQuit(PlayerQuitEvent event) {}

	@MustBeInvokedByOverriders
	public void playerDestructorRecovery(PlayerRecoveryEvent event) {}
}
