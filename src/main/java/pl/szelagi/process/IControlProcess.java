/*
 * SessionAPI - A framework for game containerization on Minecraft servers
 * Copyright (C) 2025 Szelagi (https://github.com/Szelagi/SessionAPI)
 * Licensed under the GNU General Public License v3.0.
 * For more details, visit <https://www.gnu.org/licenses/>.
 */

package pl.szelagi.process;

import org.jetbrains.annotations.NotNull;
import pl.szelagi.event.BaseEvent;
import pl.szelagi.event.EventListener;
import pl.szelagi.process.exception.MultiRegisterException;
import pl.szelagi.process.exception.NotFoundUnregisterException;
import pl.szelagi.util.timespigot.Time;

import java.util.List;

public interface IControlProcess {
	void registerListener(EventListener listener) throws MultiRegisterException;

	void unregisterListener(EventListener listener) throws NotFoundUnregisterException;

	/**
	 * invoke listeners only in process
	 */
	void invokeSelfListeners(BaseEvent event);

	void invokeAllListeners(BaseEvent event);

	@NotNull ProcessTask runControlledTask(@NotNull Runnable runnable);

	@NotNull ProcessTask runControlledTaskAsynchronously(@NotNull Runnable runnable);

	@NotNull ProcessTask runControlledTaskLater(@NotNull Runnable runnable, @NotNull Time laterTime);

	@NotNull ProcessTask runControlledTaskLaterAsynchronously(@NotNull Runnable runnable, @NotNull Time laterTime);

	@NotNull ProcessTask runControlledTaskTimer(@NotNull Runnable runnable, @NotNull Time laterTime, @NotNull Time repeatTime);

	@NotNull ProcessTask runControlledTaskTimerAsynchronously(@NotNull Runnable runnable, @NotNull Time laterTime, @NotNull Time repeatTime);

	void stopControlledTask(@NotNull ProcessTask processTask);

	List<ProcessTask> getTasks();

	List<EventListener> getListeners();
}