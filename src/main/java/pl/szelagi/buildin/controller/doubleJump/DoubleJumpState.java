package pl.szelagi.buildin.controller.doubleJump;

import org.bukkit.entity.Player;
import pl.szelagi.state.PlayerState;
import pl.szelagi.util.timespigot.Time;

class DoubleJumpState extends PlayerState {
	private final boolean oldFlyState;
	private long last;

	public DoubleJumpState(Player player, boolean oldFlyState) {
		super(player);
		this.oldFlyState = oldFlyState;
	}

	public boolean oldFlyState() {
		return oldFlyState;
	}

	public void jump() {
		last = System.currentTimeMillis();
	}

	public boolean canJump(Time cooldown) {
		return System.currentTimeMillis() - last > cooldown.toMillis();
	}
}
