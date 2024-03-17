package pl.szelagi.recovery.exception;

import pl.szelagi.util.ServerRuntimeException;

public class RecoveryException extends ServerRuntimeException {
	public RecoveryException(String message) {
		super(message);
	}
}
