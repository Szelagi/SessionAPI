package pl.szelagi.component.session.exception.player.uninitialize;

import pl.szelagi.util.ServerRuntimeException;

public class PlayerUninitializeException extends ServerRuntimeException {
	public PlayerUninitializeException(String name) {
		super(name);
	}
}
