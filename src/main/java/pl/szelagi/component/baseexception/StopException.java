package pl.szelagi.component.baseexception;

import pl.szelagi.util.ServerRuntimeException;

public class StopException extends ServerRuntimeException {
	public StopException(String name) {
		super(name);
	}
}
