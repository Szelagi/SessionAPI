package pl.szelagi.component.baseexception;

import pl.szelagi.util.ServerRuntimeException;

public class StartException extends ServerRuntimeException {
	public StartException(String name) {
		super(name);
	}
}
