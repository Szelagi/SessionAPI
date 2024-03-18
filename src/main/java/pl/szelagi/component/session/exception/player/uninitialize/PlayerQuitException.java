package pl.szelagi.component.session.exception.player.uninitialize;

import pl.szelagi.util.ServerRuntimeException;

public class PlayerQuitException extends ServerRuntimeException {
	public PlayerQuitException(String name) {
		super(name);
	}
}
