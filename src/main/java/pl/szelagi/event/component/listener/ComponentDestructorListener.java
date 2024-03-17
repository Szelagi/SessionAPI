package pl.szelagi.event.component.listener;

import pl.szelagi.event.EventListener;
import pl.szelagi.event.component.ComponentDestructorEvent;

public interface ComponentDestructorListener extends EventListener {
	void run(ComponentDestructorEvent event);
}
