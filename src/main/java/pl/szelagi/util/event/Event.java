package pl.szelagi.util.event;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Event<T> {
    @NotNull
    private ArrayList<T> listeners = new ArrayList<>();
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
