package pl.szelagi.event;

import java.util.UUID;

public abstract class BaseEvent<T extends EventListener> {
	private final Class<T> listenerClass;
	private final UUID uuid = UUID.randomUUID();

	public BaseEvent(Class<T> listenerClass) {
		this.listenerClass = listenerClass;
	}

	public UUID getUuid() {
		return uuid;
	}

	public boolean isListenerInstance(EventListener listener) {
		return listenerClass.isInstance(listener);
	}
}
