/*
 * SessionAPI - A framework for game containerization on Minecraft servers
 * Copyright (C) 2025 Szelagi (https://github.com/Szelagi/SessionAPI)
 * Licensed under the GNU General Public License v3.0.
 * For more details, visit <https://www.gnu.org/licenses/>.
 */

package pl.szelagi.event.component;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.szelagi.component.BaseComponent;

import java.util.List;

public class ComponentConstructorEvent extends ComponentChangeEvent {
	public ComponentConstructorEvent(@NotNull BaseComponent component, @NotNull List<Player> currentPlayers) {
		super(component, currentPlayers);
	}
}
