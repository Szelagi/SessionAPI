package pl.szelagi.event;

import java.util.UUID;

public abstract class BaseEvent {
	private final UUID uuid = UUID.randomUUID();

	public UUID getUuid() {
		return uuid;
	}

	public abstract Class<? extends EventListener> getListenerClazz();

	public boolean isListenerInstance(EventListener listener) {
		return getListenerClazz().isInstance(listener);
	}

	public boolean call(EventListener listener) {
		if (isListenerInstance(listener)) {
			getListenerClazz().cast(listener).run(this);
			return true;
		}
		return false;
	}
}
