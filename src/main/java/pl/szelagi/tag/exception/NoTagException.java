package pl.szelagi.tag.exception;

import pl.szelagi.util.ServerRuntimeException;

public class NoTagException extends SignTagException {
    public NoTagException(String elementName) {
        super("Not found sign with tag: " + elementName);
    }
}
