package pl.szelagi.component.session.cause;

public abstract class StopCause {
	private final String reason;

	public StopCause(String reason) {
		this.reason = reason;
	}

	public String getReason() {
		return reason;
	}
}
