/*
 * SessionAPI - A framework for game containerization on Minecraft servers
 * Copyright (C) 2025 Szelagi (https://github.com/Szelagi/SessionAPI)
 * Licensed under the GNU General Public License v3.0.
 * For more details, visit <https://www.gnu.org/licenses/>.
 */

package pl.szelagi.buildin.controller.otherGameMode;

import org.bukkit.GameMode;
import pl.szelagi.component.baseComponent.BaseComponent;
import pl.szelagi.component.baseComponent.internalEvent.component.ComponentConstructor;
import pl.szelagi.component.baseComponent.internalEvent.player.PlayerConstructor;
import pl.szelagi.component.baseComponent.internalEvent.player.PlayerDestructor;
import pl.szelagi.component.controller.Controller;
import pl.szelagi.recovery.internalEvent.PlayerRecovery;
import pl.szelagi.state.PlayerContainer;

public class OtherGameMode extends Controller {
	private PlayerContainer<GameModeState> states;
	private final GameMode gameMode;

	public OtherGameMode(BaseComponent baseComponent, GameMode gameMode) {
		super(baseComponent);
		this.gameMode = gameMode;
	}

	@Override
	public void onComponentInit(ComponentConstructor event) {
		super.onComponentInit(event);
		states = new PlayerContainer<>(GameModeState::new);
	}

	@Override
	public void onPlayerInit(PlayerConstructor event) {
		super.onPlayerInit(event);
		states.getOrCreate(event.getPlayer());
		event.getPlayer().setGameMode(gameMode);
	}

	@Override
	public void onPlayerDestroy(PlayerDestructor event) {
		super.onPlayerDestroy(event);
		var player = event.getPlayer();
		var state = states.getOrCreate(player);
		player.setGameMode(state.getGameMode());
		states.removeIfExists(player);
	}

	@Override
	public void onPlayerRecovery(PlayerRecovery event) {
		super.onPlayerRecovery(event);
		var state = states.getOrThrow(event.owner());
		final var gameMode = state.getGameMode();
		event.register(this, player -> {
			player.setGameMode(gameMode);
		});
	}

}
