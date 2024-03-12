package pl.szelagi.buildin.controller.EntryController;

import org.bukkit.entity.Player;

public interface PlayerEntryEvent {
    void run(EntryController controller, Player castFirstPlayer);
}
