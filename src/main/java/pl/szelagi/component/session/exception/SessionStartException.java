package pl.szelagi.component.session.exception;

import pl.szelagi.component.baseexception.StartException;

public class SessionStartException extends StartException {
    public SessionStartException(String name) {
        super(name);
    }
}
