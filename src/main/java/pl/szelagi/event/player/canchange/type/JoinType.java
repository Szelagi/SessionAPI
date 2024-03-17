package pl.szelagi.event.player.canchange.type;

import pl.szelagi.cancelable.CanCancelable;

public enum JoinType implements CanCancelable {
	PLUGIN(true), PLUGIN_FORCE(false);
	private final boolean isCancelable;

	JoinType(boolean isCancelable) {
		this.isCancelable = isCancelable;
	}

	public boolean isCancelable() {
		return isCancelable;
	}
}
