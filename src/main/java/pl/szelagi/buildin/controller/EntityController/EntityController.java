package pl.szelagi.buildin.controller.EntityController;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.szelagi.buildin.controller.EntityController.event.EntitiesClearEvent;
import pl.szelagi.buildin.controller.EntityController.event.EntityDamageByEntityEvent;
import pl.szelagi.buildin.controller.EntityController.event.EntityDeathEvent;
import pl.szelagi.buildin.controller.EntityController.event.ForceStopEvent;
import pl.szelagi.component.ISessionComponent;
import pl.szelagi.component.controller.Controller;
import pl.szelagi.event.component.ComponentConstructorEvent;
import pl.szelagi.event.component.ComponentDestructorEvent;
import pl.szelagi.util.event.MultiParamEvent;

import java.util.ArrayList;
import java.util.Arrays;

public class EntityController extends Controller {
	private final ArrayList<EntityBuild> entityBuilds;
	private final ArrayList<LivingEntity> entities;
	// events
	@NotNull
	private final MultiParamEvent<EntityDeathEvent> entityDeathEvent = new MultiParamEvent<>();
	@NotNull
	private final MultiParamEvent<EntitiesClearEvent> entitiesClearEvent = new MultiParamEvent<>();
	@NotNull
	private final MultiParamEvent<ForceStopEvent> forceStopEventEvent = new MultiParamEvent<>();
	@NotNull
	private final MultiParamEvent<EntityDamageByEntityEvent> entityDamageByEntityEvent = new MultiParamEvent<>();

	public EntityController(ISessionComponent component, @NotNull EntityBuild... builds) {
		super(component);
		this.entityBuilds = new ArrayList<>(Arrays.asList(builds));
		this.entities = new ArrayList<>();
	}

	public @NotNull MultiParamEvent<EntityDeathEvent> getEntityDeathEvent() {
		return entityDeathEvent;
	}

	public @NotNull MultiParamEvent<EntitiesClearEvent> getEntitiesClearEvent() {
		return entitiesClearEvent;
	}

	public @NotNull MultiParamEvent<ForceStopEvent> getForceStopEventEvent() {
		return forceStopEventEvent;
	}

	public @NotNull MultiParamEvent<EntityDamageByEntityEvent> getEntityDamageByEntityEvent() {
		return entityDamageByEntityEvent;
	}

	@Override
	public void componentConstructor(ComponentConstructorEvent event) {
		super.componentConstructor(event);
		Location location;
		World world;
		int spawnCount;
		LivingEntity livingEntity;
		for (var entityBuild : entityBuilds) {
			for (var spawnEntries : entityBuild.getSpawnEntriesSet()) {
				location = spawnEntries.getKey();
				spawnCount = spawnEntries.getValue();
				world = location.getWorld();
				for (int i = 0; i < spawnCount; i++) {
					livingEntity = (LivingEntity) world.spawnEntity(location, entityBuild.getEntityType());
					var builder = entityBuild.getBuilder();
					if (builder != null)
						builder.build(livingEntity);
					entities.add(livingEntity);
				}
			}
		}
	}

	protected void removeEntity(LivingEntity entity) {
		entities.remove(entity);
		entity.getLocation().getWorld()
		      .getEntities().remove(entity);
		if (entities.isEmpty())
			stop();
	}

	@Nullable
	@Override
	public Listener getListener() {
		return new EntityControllerListener(getPlugin());
	}

	@Override
	public void componentDestructor(ComponentDestructorEvent event) {
		super.componentDestructor(event);
		var type = entities.isEmpty() ? EntityControllerFinalizeType.CLEAR : EntityControllerFinalizeType.FORCE;
		var cloneArrayEntities = new ArrayList<>(entities);
		entities.clear();
		for (var entity : cloneArrayEntities) {
			entity.getLocation().getWorld()
			      .getEntities().remove(entity);
			entity.setHealth(0);
		}
		switch (type) {
			case CLEAR ->
					entitiesClearEvent.call(c -> c.run(this));
			case FORCE ->
					forceStopEventEvent.call(c -> c.run(this));
		}
	}

	public ArrayList<LivingEntity> getEntities() {
		return entities;
	}
}