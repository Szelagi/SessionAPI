/*
 * SessionAPI - A framework for game containerization on Minecraft servers
 * Copyright (C) 2025 Szelagi (https://github.com/Szelagi/SessionAPI)
 * Licensed under the GNU General Public License v3.0.
 * For more details, visit <https://www.gnu.org/licenses/>.
 */

package pl.szelagi.buildin.controller.EntryController;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import pl.szelagi.component.ISessionComponent;
import pl.szelagi.component.controller.Controller;
import pl.szelagi.event.component.ComponentConstructorEvent;
import pl.szelagi.util.event.MultiParamEvent;
import pl.szelagi.util.timespigot.Time;

public class EntryController extends Controller {
	private final Location location;
	private final double radius;
	private final boolean isAutoDisable;
	private final long refreshTicks;
	@NotNull
	private final MultiParamEvent<PlayerEntryEvent> playerEntryEventEvent = new MultiParamEvent<>();

	public EntryController(ISessionComponent component, Location location, double radius) {
		this(component, location, Time.Ticks(1), radius, true);
	}

	public EntryController(ISessionComponent component, Location location, Time refreshSpan, double radius, boolean isAutoDisable) {
		super(component);
		this.location = location;
		this.radius = radius;
		this.isAutoDisable = isAutoDisable;
		this.refreshTicks = refreshSpan.toTicks();
	}

	@Override
	public void componentConstructor(ComponentConstructorEvent event) {
		super.componentConstructor(event);
		getProcess().runControlledTaskTimer(() -> {
			var players = location.getNearbyPlayers(radius);
			if (!players.isEmpty()) {
				playerEntryEventEvent.call(c -> c.run(this, players
						.iterator().next()));
				if (isAutoDisable)
					stop();
			}
		}, Time.Ticks(0), Time.Ticks((int) refreshTicks));
	}

	public @NotNull MultiParamEvent<PlayerEntryEvent> getPlayerEntryEventEvent() {
		return playerEntryEventEvent;
	}
}
