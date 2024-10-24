package pl.szelagi.buildin.controller.life;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.Nullable;
import pl.szelagi.state.PlayerState;

public class LifePlayerState extends PlayerState {
	private boolean isAlive;
	private boolean stopOneSpectateEvent;
	private BukkitTask respawnPlayerTask;
	private BukkitTask respawnRemainTimePlayerTask;

	public LifePlayerState(Player player) {
		super(player);
		this.isAlive = true;
		this.stopOneSpectateEvent = false;
	}

	public void setAlive(boolean alive) {
		isAlive = alive;
	}

	public boolean isAlive() {
		return isAlive;
	}

	public boolean isStopOneSpectateEvent() {
		return stopOneSpectateEvent;
	}

	public void setStopOneSpectateEvent(boolean stopOneSpectateEvent) {
		this.stopOneSpectateEvent = stopOneSpectateEvent;
	}

	@Nullable
	public BukkitTask getRespawnPlayerTask() {
		return respawnPlayerTask;
	}

	public void setRespawnPlayerTask(BukkitTask respawnPlayerTask) {
		this.respawnPlayerTask = respawnPlayerTask;
	}

	@Nullable
	public BukkitTask getRespawnRemainTimePlayerTask() {
		return respawnRemainTimePlayerTask;
	}

	public void setRespawnRemainTimePlayerTask(BukkitTask respawnRemainTimePlayerTask) {
		this.respawnRemainTimePlayerTask = respawnRemainTimePlayerTask;
	}
}
