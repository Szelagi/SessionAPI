package pl.szelagi.component.session.exception.other;

import pl.szelagi.component.session.exception.SessionStartException;

public class SessionIsEnableException extends SessionStartException {
    public SessionIsEnableException(String name) {
        super(name);
    }
}
