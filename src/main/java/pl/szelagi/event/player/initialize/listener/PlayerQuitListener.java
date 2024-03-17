package pl.szelagi.event.player.initialize.listener;

import pl.szelagi.event.EventListener;
import pl.szelagi.event.player.canchange.PlayerCanQuitEvent;

public interface PlayerQuitListener extends EventListener {
	void run(PlayerCanQuitEvent event);
}
