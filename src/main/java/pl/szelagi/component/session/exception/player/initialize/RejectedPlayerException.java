package pl.szelagi.component.session.exception.player.initialize;

import pl.szelagi.cancelable.CancelCause;
import pl.szelagi.component.BaseComponent;

public class RejectedPlayerException extends RuntimeException {
	private final CancelCause cancelCause;

	public RejectedPlayerException(CancelCause cancelCause) {
		super(cancelCause.message());
		this.cancelCause = cancelCause;
	}

	public CancelCause getCancelCause() {
		return cancelCause;
	}

	public BaseComponent getInvokeController() {
		return cancelCause.invokeComponent();
	}
}
