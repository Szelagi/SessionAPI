package pl.szelagi.filemanager.exception;

import pl.szelagi.util.ServerRuntimeException;

public class DirectoryNotFoundException extends ServerRuntimeException {
	public DirectoryNotFoundException(String name) {
		super(name);
	}
}
