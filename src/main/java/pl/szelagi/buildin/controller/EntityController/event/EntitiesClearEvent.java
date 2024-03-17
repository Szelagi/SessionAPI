package pl.szelagi.buildin.controller.EntityController.event;

import pl.szelagi.buildin.controller.EntityController.EntityController;

public interface EntitiesClearEvent {
	void run(EntityController controller);
}
