package pl.szelagi.filemanager.exception;

import pl.szelagi.util.ServerRuntimeException;

public class BeyondHeadDirectoryException extends ServerRuntimeException {
	public BeyondHeadDirectoryException(String name) {
		super(name);
	}
}
