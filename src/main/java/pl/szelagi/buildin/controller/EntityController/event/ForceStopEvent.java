package pl.szelagi.buildin.controller.EntityController.event;

import pl.szelagi.buildin.controller.EntityController.EntityController;

public interface ForceStopEvent {
	void run(EntityController controller);
}