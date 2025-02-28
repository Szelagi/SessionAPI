/*
 * SessionAPI - A framework for game containerization on Minecraft servers
 * Copyright (C) 2025 Szelagi (https://github.com/Szelagi/SessionAPI)
 * Licensed under the GNU General Public License v3.0.
 * For more details, visit <https://www.gnu.org/licenses/>.
 */

package pl.szelagi.buildin.lobby;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.jetbrains.annotations.Nullable;
import pl.szelagi.component.ISessionComponent;
import pl.szelagi.component.controller.Controller;
import pl.szelagi.event.component.ComponentConstructorEvent;
import pl.szelagi.event.component.ComponentDestructorEvent;
import pl.szelagi.process.ProcessTask;
import pl.szelagi.util.event.Event;
import pl.szelagi.util.timespigot.Time;

import java.util.ArrayList;
import java.util.HashSet;

public class MessageTimer extends Controller {
	private static final Sound MESSAGE_SOUND = Sound.sound(Key.key("block.note_block.snare"), Sound.Source.AMBIENT, 1f, 1.8f);
	private boolean isCounting = false;
	private final Time waitTime;
	private final ArrayList<ProcessTask> countDownTasks = new ArrayList<>();
	private final ArrayList<Runnable> messages = new ArrayList<>();
	private @Nullable String startCountMessage = null;
	private @Nullable String breakCountMessage = null;
	private ProcessTask mainTask = null;
	private final Event<Void> finalizeEvent = new Event<>();
	private final HashSet<Integer> busySeconds = new HashSet<>();

	public MessageTimer(ISessionComponent sessionComponent, Time waitTime) {
		super(sessionComponent);
		this.waitTime = waitTime;
	}

	@Override
	public void componentConstructor(ComponentConstructorEvent event) {
		super.componentConstructor(event);
		if (isCounting)
			return;

		isCounting = true;
		if (startCountMessage != null)
			broadcast(startCountMessage);
		for (var message : messages)
			getProcess().runControlledTask(message);

		mainTask = getProcess().runControlledTaskLater(this::countdown, waitTime);
	}

	@Override
	public void componentDestructor(ComponentDestructorEvent event) {
		super.componentDestructor(event);
		if (!isCounting)
			return;
		isCounting = false;
		if (breakCountMessage != null)
			broadcast(breakCountMessage);
		clearData();
	}

	private void clearData() {
		for (var task : countDownTasks)
			task.cancel();
		countDownTasks.clear();
		if (mainTask != null) {
			mainTask.cancel();
			mainTask = null;
		}
	}

	public boolean isCounting() {
		return isCounting;
	}

	public void registerMessage(Time timeBefore, String message) {
		int waitSeconds = waitTime.toSeconds();
		if (timeBefore.toSeconds() > waitSeconds)
			return;
		busySeconds.add(waitSeconds);
		messages.add(() -> {
			countDownTasks.add(getProcess().runControlledTaskLater(() -> {
				broadcast(message);
				broadcastSound(MESSAGE_SOUND);
			}, Time.Seconds(waitSeconds - timeBefore.toSeconds())));
		});
	}

	public void registerMessageIfSecNotBusy(Time timeBefore, String message) {
		if (busySeconds.contains(timeBefore.toSeconds()))
			return;
		registerMessage(timeBefore, message);
	}

	public Event<Void> getFinalizeEvent() {
		return finalizeEvent;
	}

	public void setStartCountMessage(@Nullable String message) {
		startCountMessage = message;
	}

	public void setBreakCountMessage(@Nullable String message) {
		breakCountMessage = message;
	}

	private void broadcast(String message) {
		for (var player : getSession().getPlayers())
			player.sendMessage(message);
	}

	private void broadcastSound(Sound sound) {
		for (var player : getSession().getPlayers())
			Audience.audience(player)
			        .playSound(sound);
	}

	private void countdown() {
		isCounting = false;
		clearData();
		finalizeEvent.call(null);
		stop();
	}
}
