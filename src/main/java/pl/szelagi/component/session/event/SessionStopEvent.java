package pl.szelagi.component.session.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import pl.szelagi.component.session.Session;


public class SessionStopEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final Session session;

    public SessionStopEvent(Session session) {
        this.session = session;
    }

    public Session getDungeon() {
        return session;
    }

    @Override
    public String getEventName() {
        return "SessionStopEvent";
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
