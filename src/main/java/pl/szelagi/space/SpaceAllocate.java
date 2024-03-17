package pl.szelagi.space;

import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

public class SpaceAllocate extends Space {
	private long lockEndMillis = 0;
	private boolean isPutOnTimeLock = false;
	private boolean isAllocate = true;

	public SpaceAllocate(int slot, @NotNull World world) {
		super(slot, world);
	}

	public long getLockEndMillis() {
		return lockEndMillis;
	}

	public void setLockEndMillis(long lockEndMillis) {
		this.lockEndMillis = lockEndMillis;
	}

	public boolean isPutOnTimeLock() {
		return isPutOnTimeLock;
	}

	public void setPutOnTimeLock(boolean putOnTimeLock) {
		isPutOnTimeLock = putOnTimeLock;
	}

	public boolean isAllocate() {
		return isAllocate;
	}

	public void setAllocate(boolean allocate) {
		isAllocate = allocate;
	}
}
