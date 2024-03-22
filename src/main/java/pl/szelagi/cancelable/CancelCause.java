package pl.szelagi.cancelable;

import org.jetbrains.annotations.NotNull;
import pl.szelagi.component.BaseComponent;

public record CancelCause(
		@NotNull BaseComponent invokeComponent,
		@NotNull String message) {
	@Override
	public String toString() {
		return "CancelCause{" + "invokeComponent=" + invokeComponent + ", message='" + message + '\'' + '}';
	}
}
