package pl.szelagi.filemanager.exception;

import pl.szelagi.util.ServerRuntimeException;

public class BackwardFilePathException extends ServerRuntimeException {
	public BackwardFilePathException(String name) {
		super(name);
	}
}
