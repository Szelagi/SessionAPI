package pl.szelagi.buildin.controller.environment;

import org.bukkit.WeatherType;
import pl.szelagi.component.ISessionComponent;
import pl.szelagi.component.controller.Controller;
import pl.szelagi.event.player.initialize.PlayerConstructorEvent;
import pl.szelagi.event.player.initialize.PlayerDestructorEvent;

public class CustomPlayerEnvironment extends Controller {
	public static final Class<TimeType> TIME_TYPE = TimeType.class;
	private final long ticks;
	private final WeatherType weatherType;

	public CustomPlayerEnvironment(ISessionComponent sessionComponent, WeatherType weatherType, long ticks) {
		super(sessionComponent);
		this.weatherType = weatherType;
		this.ticks = ticks;
	}

	public CustomPlayerEnvironment(ISessionComponent sessionComponent, WeatherType weatherType, TimeType timeType) {
		super(sessionComponent);
		this.weatherType = weatherType;
		this.ticks = timeType.getTicks();
	}

	@Override
	public void playerConstructor(PlayerConstructorEvent event) {
		super.playerConstructor(event);
		event.getPlayer()
		     .setPlayerWeather(weatherType);
		event.getPlayer()
		     .setPlayerTime(ticks, false);
	}

	@Override
	public void playerDestructor(PlayerDestructorEvent event) {
		super.playerDestructor(event);
		event.getPlayer().resetPlayerWeather();
		event.getPlayer().resetPlayerTime();
	}
}
