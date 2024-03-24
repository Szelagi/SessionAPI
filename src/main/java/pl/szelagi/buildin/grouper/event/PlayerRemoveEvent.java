package pl.szelagi.buildin.grouper.event;

import org.bukkit.entity.Player;
import pl.szelagi.buildin.grouper.Group;
import pl.szelagi.buildin.grouper.Grouper;

public record PlayerRemoveEvent(Player player,
                                Grouper grouper,
                                Group group) {}
