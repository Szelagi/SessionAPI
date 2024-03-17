package pl.szelagi.cancelable;

public interface Cancelable extends CanCancelable {
	void setCanceled(CancelCause cause);

	boolean isCanceled();
}
