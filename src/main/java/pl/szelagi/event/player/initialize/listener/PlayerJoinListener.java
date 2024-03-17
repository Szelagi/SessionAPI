package pl.szelagi.event.player.initialize.listener;

import pl.szelagi.event.EventListener;

public interface PlayerJoinListener extends EventListener {
	void run(PlayerJoinListener event);
}
