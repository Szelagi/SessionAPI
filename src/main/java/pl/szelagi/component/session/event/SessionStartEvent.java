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
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
