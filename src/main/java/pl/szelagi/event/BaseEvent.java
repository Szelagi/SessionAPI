package pl.szelagi.event;

import pl.szelagi.util.ReflectionRecursive;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

public abstract class BaseEvent {
	private final UUID uuid = UUID.randomUUID();

	public UUID getUuid() {
		return uuid;
	}

	public boolean call(EventListener listener) {
		try {
			var methods = ReflectionRecursive.getEventMethods(listener.getClass(), this.getClass());
			if (methods.isEmpty())
				return false;
			for (var method : methods)
				method.invoke(listener, this);
			return true;
		} catch (IllegalAccessException |
		         InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}
}
