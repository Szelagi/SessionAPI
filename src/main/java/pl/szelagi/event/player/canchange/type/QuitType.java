package pl.szelagi.event.player.canchange.type;

import pl.szelagi.cancelable.CanCancelable;

public enum QuitType implements CanCancelable {
	PLUGIN(true), PLUGIN_FORCE(false), DISCONNECT(false), SESSION_STOP(false);
	private final boolean isCancelable;

	QuitType(boolean isCancelable) {
		this.isCancelable = isCancelable;
	}

	public boolean isCancelable() {
		return isCancelable;
	}
}
