package pl.szelagi.filemanager.exception;

import pl.szelagi.util.ServerRuntimeException;

public class InvalidFileTypeException extends ServerRuntimeException {
    public InvalidFileTypeException(String name) {
        super(name);
    }
}
