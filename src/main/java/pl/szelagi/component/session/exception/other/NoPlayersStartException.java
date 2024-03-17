package pl.szelagi.component.session.exception.other;

import pl.szelagi.component.session.exception.SessionStartException;

public class NoPlayersStartException extends SessionStartException {
	public NoPlayersStartException() {
		super("zero players in dungeon start");
	}
}
