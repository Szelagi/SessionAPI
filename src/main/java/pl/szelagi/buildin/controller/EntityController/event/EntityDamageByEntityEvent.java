package pl.szelagi.buildin.controller.EntityController.event;


import pl.szelagi.buildin.controller.EntityController.ControlledEntity;
import pl.szelagi.buildin.controller.EntityController.EntityController;

public interface EntityDamageByEntityEvent {
    void run(EntityController controller, org.bukkit.event.entity.EntityDamageByEntityEvent event, ControlledEntity type);
}