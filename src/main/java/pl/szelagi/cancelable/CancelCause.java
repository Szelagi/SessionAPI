package pl.szelagi.cancelable;

import org.jetbrains.annotations.NotNull;
import pl.szelagi.component.BaseComponent;

public record CancelCause(@NotNull String message, @NotNull BaseComponent invokeObject) {
}
