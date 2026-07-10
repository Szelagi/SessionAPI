/*
 * SessionAPI - A framework for game containerization on Minecraft servers
 * Copyright (C) 2025 Szelagi (https://github.com/Szelagi/SessionAPI)
 * Licensed under the GNU General Public License v3.0.
 * For more details, visit <https://www.gnu.org/licenses/>.
 */

package pl.szelagi.event;

import org.jetbrains.annotations.NotNull;
import pl.szelagi.component.base.Component;
import pl.szelagi.component.base.ComponentStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Dispatches events to registered listeners.
 *
 * @param <T> the type of event data passed to listeners
 */
public class EventDispatcher<T> {
    private final @NotNull List<RegisteredConsumer<T, ?>> componentListeners = new ArrayList<>();
    private final @NotNull List<RegisteredRunnable<?>> componentRunnableListeners = new ArrayList<>();
    private final @NotNull List<Consumer<T>> uncheckedListeners = new ArrayList<>();
    private final @NotNull List<Runnable> uncheckedRunnableListeners = new ArrayList<>();

    /**
     * Registers a listener that consumes the event.
     *
     * @param component the component that owns the listener
     * @param listener the listener to register
     * @param <C> the type of the component
     */
    public <C extends Component> void register(@NotNull C component, @NotNull BiConsumer<C, T> listener) {
        componentListeners.add(new RegisteredConsumer<>(component, listener));
    }

    /**
     * Registers a listener that runs without event context.
     *
     * @param component the component that owns the listener
     * @param listener the listener to register
     * @param <C> the type of the component
     */
    public <C extends Component> void register(@NotNull C component, @NotNull Consumer<C> listener) {
        componentRunnableListeners.add(new RegisteredRunnable<>(component, listener));
    }

    /**
     * Registers an unchecked listener that consumes the event.
     *
     * @param listener the listener to register
     */
    public void registerUnchecked(@NotNull Consumer<T> listener) {
        uncheckedListeners.add(listener);
    }

    /**
     * Registers an unchecked listener that runs without event context.
     *
     * @param listener the listener to register
     */
    public void registerUnchecked(@NotNull Runnable listener) {
        uncheckedRunnableListeners.add(listener);
    }

    /**
     * Dispatches the event to all registered listeners.
     *
     * @param event the event object to pass to listeners
     */
    public void dispatch(T event) {
        new ArrayList<>(componentListeners).forEach(registered -> registered.dispatch(event));
        new ArrayList<>(componentRunnableListeners).forEach(RegisteredRunnable::dispatch);
        new ArrayList<>(uncheckedListeners).forEach(listener -> listener.accept(event));
        new ArrayList<>(uncheckedRunnableListeners).forEach(Runnable::run);
    }

    private record RegisteredConsumer<T, C extends Component>(C component, BiConsumer<C, T> listener) {
        public void dispatch(T event) {
            if (component.status() == ComponentStatus.RUNNING) {
                listener.accept(component, event);
            }
        }
    }

    private record RegisteredRunnable<C extends Component>(C component, Consumer<C> listener) {
        public void dispatch() {
            if (component.status() == ComponentStatus.RUNNING) {
                listener.accept(component);
            }
        }
    }
}