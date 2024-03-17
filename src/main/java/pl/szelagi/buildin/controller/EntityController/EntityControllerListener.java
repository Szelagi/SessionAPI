package pl.szelagi.buildin.controller.EntityController;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;
import pl.szelagi.manager.BoardManager;
import pl.szelagi.manager.ControllerManager;

import java.util.ArrayList;

class EntityControllerListener implements Listener {
	private final JavaPlugin plugin;

	public EntityControllerListener(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	private void callEntityDeathEvent(EntityController controller, EntityDeathEvent event) {
		controller.getEntityDeathEvent().call(c -> c.run(controller, event));
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		event.getPlayer().sendMessage("BR");
	}

	private void check(EntityDeathEvent event) {
		LivingEntity entity = event.getEntity();
		var dungeon = BoardManager.getSession(entity);
		if (dungeon == null)
			return;
		ArrayList<EntityController> controllers = ControllerManager.getControllers(dungeon, EntityController.class);
		for (var controller : controllers) {
			var storedEntity = controller.getEntities().stream().filter(e -> e.equals(entity)).findFirst().orElse(null);
			if (storedEntity == null)
				continue;
			controller.removeEntity(entity);
			callEntityDeathEvent(controller, event);
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onEntityDeath(EntityDeathEvent event) {
		check(event);
	}

	private boolean checkEntityDamageByEntity(Entity entity, EntityDamageByEntityEvent event, ControlledEntity type) {
		boolean isCall = false;
		var dungeon = BoardManager.getSession(entity.getLocation());
		if (dungeon == null)
			return false;
		ArrayList<EntityController> controllers = ControllerManager.getControllers(dungeon, EntityController.class);
		for (var controller : controllers) {
			var storedEntity = controller.getEntities().stream().filter(e -> e.equals(entity)).findFirst().orElse(null);
			if (storedEntity == null)
				continue;
			controller.getEntityDamageByEntityEvent().call(entityDamageByEntityEvent -> entityDamageByEntityEvent.run(controller, event, type));
			isCall = true;
		}
		return isCall;
	}

	@EventHandler(ignoreCancelled = true)
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		// as attacker
		if (checkEntityDamageByEntity(event.getDamager(), event, ControlledEntity.ATTACKER))
			return;
		// as victim
		checkEntityDamageByEntity(event.getEntity(), event, ControlledEntity.VICTIM);
	}
}