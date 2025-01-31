/*
 * SessionAPI - A framework for game containerization on Minecraft servers
 * Copyright (C) 2025 Szelagi (https://github.com/Szelagi/SessionAPI)
 * Licensed under the GNU General Public License v3.0.
 * For more details, visit <https://www.gnu.org/licenses/>.
 */

package pl.szelagi.util.event;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

@Deprecated

public class MultiParamEvent<T> {
	@NotNull
	private final ArrayList<T> listeners = new ArrayList<>();

	public void bind(@NotNull T t) {
		this.listeners.add(t);
	}

	public void call(@NotNull CallBuilder<T> callBuilder) {
		ArrayList<T> cloneArrayListListeners = new ArrayList<>();
		cloneArrayListListeners.addAll(listeners);
		for (var l : cloneArrayListListeners) {
			callBuilder.run(l);
		}
	}
}
