package pl.szelagi.filemanager.exception;

import pl.szelagi.util.ServerRuntimeException;

public class InvalidConstructorException extends ServerRuntimeException {
    public InvalidConstructorException(String name) {
        super(name);
    }
}
