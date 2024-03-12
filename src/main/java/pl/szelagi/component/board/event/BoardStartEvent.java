package pl.szelagi.component.board.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import pl.szelagi.component.board.Board;


public class BoardStartEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final Board board;

    public BoardStartEvent(Board board) {
        this.board = board;
    }

    public Board getBoard() {
        return board;
    }

    @Override
    public String getEventName() {
        return "BoardStartEvent";
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
