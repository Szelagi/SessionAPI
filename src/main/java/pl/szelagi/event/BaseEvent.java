package pl.szelagi.event;

import pl.szelagi.component.ComponentStatus;
import pl.szelagi.util.ReflectionRecursive;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

public abstract class BaseEvent {
	private final UUID uuid = UUID.randomUUID();

	public UUID getUuid() {
		return uuid;
	}

	public boolean call(EventListener listener) {
		if (listener.status() != ComponentStatus.RUNNING)
			return false;
		try {
			var methods = ReflectionRecursive.getEventMethods(listener.getClass(), this.getClass());
			if (methods.isEmpty())
				return false;
			for (var method : methods) {
				method.setAccessible(true);
				method.invoke(listener, this);
			}
			return true;
		} catch (IllegalAccessException |
		         InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}
}
