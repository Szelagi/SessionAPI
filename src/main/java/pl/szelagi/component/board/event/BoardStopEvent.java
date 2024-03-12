package pl.szelagi.component.board.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import pl.szelagi.component.board.Board;


public class BoardStopEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final Board board;

    public BoardStopEvent(Board board) {
        this.board = board;
    }

    public Board getBoard() {
        return board;
    }

    @Override
    public String getEventName() {
        return "BoardStopEvent";
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
