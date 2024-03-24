package pl.szelagi.util.event;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.function.Function;

public class Event<T> {
	private final @NotNull ArrayList<Function<T, Void>> listeners = new ArrayList<>();

	public void bind(@NotNull Function<T, Void> listener) {
		this.listeners.add(listener);
	}

	public void call(@NotNull T event) {
		var cloneArrayListListeners = new ArrayList<>(listeners);
		for (var listener : cloneArrayListListeners) {
			listener.apply(event);
		}
	}
}
