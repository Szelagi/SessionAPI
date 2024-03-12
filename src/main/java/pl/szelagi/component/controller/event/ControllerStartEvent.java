package pl.szelagi.component.controller.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import pl.szelagi.component.controller.Controller;

public class ControllerStartEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final Controller controller;

    public ControllerStartEvent(Controller controller) {
        this.controller = controller;
    }

    public Controller getController() {
        return controller;
    }

    @Override
    public String getEventName() {
        return "ControllerStartEvent";
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
