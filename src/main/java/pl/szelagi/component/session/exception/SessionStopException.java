package pl.szelagi.component.session.exception;

import pl.szelagi.component.baseexception.StopException;

public class SessionStopException extends StopException {
    public SessionStopException(String name) {
        super(name);
    }
}
