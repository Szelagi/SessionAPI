/*
 * SessionAPI - A framework for game containerization on Minecraft servers
 * Copyright (C) 2025 Szelagi (https://github.com/Szelagi/SessionAPI)
 * Licensed under the GNU General Public License v3.0.
 * For more details, visit <https://www.gnu.org/licenses/>.
 */

package pl.szelagi.component.session.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import pl.szelagi.component.session.Session;

public class SessionStartEvent extends Event {
	private static final HandlerList HANDLERS = new HandlerList();
	private final Session session;

	public SessionStartEvent(Session session) {
		this.session = session;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}

	public Session getSession() {
		return session;
	}

	@Override
	public String getEventName() {
		return "SessionStartEvent";
	}

	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
	}
}
