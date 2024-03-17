package pl.szelagi.event.player.canchange.listener;

import pl.szelagi.event.EventListener;
import pl.szelagi.event.player.canchange.PlayerCanQuitEvent;

public interface PlayerCanQuitListener extends EventListener {
	void run(PlayerCanQuitEvent event);
}
