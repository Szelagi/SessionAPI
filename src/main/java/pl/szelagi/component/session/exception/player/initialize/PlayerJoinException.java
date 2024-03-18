package pl.szelagi.component.session.exception.player.initialize;

import pl.szelagi.util.ServerRuntimeException;

public class PlayerJoinException extends ServerRuntimeException {
	public PlayerJoinException(String name) {
		super(name);
	}
}
