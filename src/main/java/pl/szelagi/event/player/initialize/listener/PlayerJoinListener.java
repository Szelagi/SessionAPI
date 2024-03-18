package pl.szelagi.event.player.initialize.listener;

import pl.szelagi.event.EventListener;
import pl.szelagi.event.player.initialize.PlayerConstructorEvent;

public interface PlayerJoinListener extends EventListener {
	void run(PlayerConstructorEvent event);
}
