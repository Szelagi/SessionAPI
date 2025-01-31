/*
 * SessionAPI - A framework for game containerization on Minecraft servers
 * Copyright (C) 2025 Szelagi (https://github.com/Szelagi/SessionAPI)
 * Licensed under the GNU General Public License v3.0.
 * For more details, visit <https://www.gnu.org/licenses/>.
 */

package pl.szelagi.util.event;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.function.Function;

public class Event<T> {
	private final @NotNull ArrayList<Function<T, ?>> listeners = new ArrayList<>();
	private final @NotNull ArrayList<Runnable> runnableListeners = new ArrayList<>();

	public void bind(@NotNull Function<T, ?> listener) {
		listeners.add(listener);
	}

	public void bind(@NotNull Runnable listener) {
		runnableListeners.add(listener);
	}

	public void call(T event) {
		var cloneArrayListListeners = new ArrayList<>(listeners);
		for (var listener : cloneArrayListListeners) {
			listener.apply(event);
		}
		var cloneRunnable = new ArrayList<>(runnableListeners);
		for (var listener : cloneRunnable) {
			listener.run();
		}
	}
}
