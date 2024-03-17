package pl.szelagi.tag.exception;

import pl.szelagi.util.ServerRuntimeException;

public class SignTagException extends ServerRuntimeException {
	public SignTagException(String name) {
		super(name);
	}
}
