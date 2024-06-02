package pl.szelagi.buildin.controller.environment;

public enum TimeType {
	BEGINNING(0), DAY(1000), NOON(6000), SUNSET(12000), NIGHT(13000), MIDNIGHT(18000), SUNRISE(23000);
	private final long time;

	TimeType(long time) {
		this.time = time;
	}

	public long getTicks() {
		return time;
	}
}
