package pl.szelagi.event.component.listener;

import pl.szelagi.event.EventListener;
import pl.szelagi.event.component.ComponentConstructorEvent;

public interface ComponentConstructorListener extends EventListener {
	void run(ComponentConstructorEvent event);
}
