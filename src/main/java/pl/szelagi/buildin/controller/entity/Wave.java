package pl.szelagi.buildin.controller.entity;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.jetbrains.annotations.Nullable;
import pl.szelagi.component.ISessionComponent;
import pl.szelagi.component.controller.Controller;
import pl.szelagi.event.component.ComponentConstructorEvent;
import pl.szelagi.event.component.ComponentDestructorEvent;
import pl.szelagi.manager.BoardManager;
import pl.szelagi.manager.ControllerManager;
import pl.szelagi.util.event.Event;
import pl.szelagi.util.timespigot.Time;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Wave extends Controller {
	private final static Random RANDOM = new Random();
	private final List<Location> spawnPoints;
	private final List<SpawnEntity<?>> spawnEntities;
	public final Event<Void> finish = new Event<>();
	public final Event<EntityDeathEvent> death = new Event<>();
	public final List<LivingEntity> entities = new ArrayList<>();

	public Wave(ISessionComponent sessionComponent, List<Location> spawnPoints, List<SpawnEntity<?>> spawnEntities) {
		super(sessionComponent);
		this.spawnPoints = spawnPoints;
		this.spawnEntities = spawnEntities;
	}

	@Override
	public void componentConstructor(ComponentConstructorEvent event) {
		super.componentConstructor(event);
		for (var spawnEntity : spawnEntities)
			entities.add(spawnEntity.spawn(randomSpawnPoint()));

		getProcess().runControlledTaskTimer(this::entityGarbageCollector, Time.Seconds(5), Time.Seconds(5));
	}

	@Override
	public void componentDestructor(ComponentDestructorEvent event) {
		super.componentDestructor(event);
	}

	@Override
	public @Nullable Listener getListener() {
		return new MyListener();
	}

	private static class MyListener implements Listener {
		@EventHandler(ignoreCancelled = true)
		public void onEntityDeath(EntityDeathEvent event) {
			var session = BoardManager.getSession(event.getEntity());
			if (session == null)
				return;
			var controllers = ControllerManager.getControllers(session, Wave.class);
			for (var controller : controllers) {
				var state = controller.entities.remove(event.getEntity());
				if (!state)
					continue;
				controller.death.call(event);
				controller.checkFinish();
				return;
			}
		}
	}

	private Location randomSpawnPoint() {
		var index = RANDOM.nextInt(spawnPoints.size());
		return spawnPoints.get(index);
	}

	private void entityGarbageCollector() {
		var toRemove = new ArrayList<LivingEntity>();
		for (var e : entities)
			if (!e.isValid() || e.isDead())
				toRemove.add(e);

		for (var livingEntity : toRemove)
			entities.remove(livingEntity);
		checkFinish();
	}

	private void checkFinish() {
		if (!entities.isEmpty())
			return;
		finish.call(null);
		stop();
	}
}
