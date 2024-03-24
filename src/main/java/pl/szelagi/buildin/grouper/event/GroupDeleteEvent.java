package pl.szelagi.buildin.grouper.event;

import pl.szelagi.buildin.grouper.Group;
import pl.szelagi.buildin.grouper.Grouper;

public record GroupDeleteEvent(Grouper grouper,
                               Group group) {}
