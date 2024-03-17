package pl.szelagi.component.session.exception.other;

import pl.szelagi.component.session.exception.SessionStartException;

public class SessionIsDisableException extends SessionStartException {
	public SessionIsDisableException(String name) {
		super(name);
	}
}
