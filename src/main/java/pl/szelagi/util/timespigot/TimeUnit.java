package pl.szelagi.util.timespigot;

public enum TimeUnit {
	// Base unit = MILLIS
	SECONDS(1000), MILLIS(1), TICKS(50);
	private final int multiple;

	TimeUnit(int multiple) {
		this.multiple = multiple;
	}

	public int getMultiple() {
		return multiple;
	}
}
