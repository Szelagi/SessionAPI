package pl.szelagi.buildin.controller.EntityController.event;

import pl.szelagi.buildin.controller.EntityController.EntityController;

public interface EntityDeathEvent {
	void run(EntityController controller, org.bukkit.event.entity.EntityDeathEvent event);
}