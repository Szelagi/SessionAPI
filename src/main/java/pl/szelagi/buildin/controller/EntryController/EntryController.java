package pl.szelagi.buildin.controller.EntryController;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import pl.szelagi.component.ISessionComponent;
import pl.szelagi.component.controller.Controller;
import pl.szelagi.util.event.Event;
import pl.szelagi.util.timespigot.Time;

public class EntryController extends Controller {
	private final Location location;
	private final double radius;
	private final boolean isAutoDisable;
	private final long refreshTicks;
	@NotNull private final Event<PlayerEntryEvent> playerEntryEventEvent = new Event<>();

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
	public void constructor() {
		super.constructor();
		getProcess().runControlledTaskTimer(() -> {
			var players = location.getNearbyPlayers(radius);
			if (!players.isEmpty()) {
				playerEntryEventEvent.call(c -> c.run(this, players.iterator().next()));
				if (isAutoDisable)
					stop();
			}
		}, Time.Ticks(0), Time.Ticks((int) refreshTicks));
	}

	public @NotNull Event<PlayerEntryEvent> getPlayerEntryEventEvent() {
		return playerEntryEventEvent;
	}
}
