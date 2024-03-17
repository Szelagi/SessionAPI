package pl.szelagi.event.player.canchange.listener;

import pl.szelagi.event.EventListener;
import pl.szelagi.event.player.canchange.PlayerCanJoinEvent;

public interface PlayerCanJoinListener extends EventListener {
	void run(PlayerCanJoinEvent event);
}
