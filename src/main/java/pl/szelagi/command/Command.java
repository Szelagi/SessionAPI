package pl.szelagi.command;

import java.util.ArrayList;
import java.util.List;

public class Command {
    public static final ArrayList<String> NAMES = new ArrayList<>(List.of(
            "board-save", "board-edit", "board-exit", "board-list",
            "session-exit", "session-add-player", "session-remove-player",
            "test-session"
    ));
}
