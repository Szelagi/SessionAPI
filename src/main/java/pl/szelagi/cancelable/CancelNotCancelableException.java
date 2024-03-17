package pl.szelagi.cancelable;

public class CancelNotCancelableException extends RuntimeException {
	public CancelNotCancelableException(String message) {
		super(message);
	}
}
