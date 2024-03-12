package pl.szelagi.util.timespigot;

import pl.szelagi.util.FloatConverter;

public class Time {
    public static Time Zero() {
        return new Time(0, TimeUnit.TICKS);
    }
    public static Time Seconds(int span) {
        return new Time(span, TimeUnit.SECONDS);
    }
    public static Time Ticks(int span) {
        return new Time(span, TimeUnit.TICKS);
    }
    public static Time Millis(long span) {
        return new Time(span, TimeUnit.MILLIS);
    }
    private final TimeUnit unit;
    private final long span;
    public Time(long span, TimeUnit unit) {
        this.span = span;
        this.unit = unit;
    }
    public long toMillis() {
        return span * unit.getMultiple();
    }
    public int toTicks() {
        return (int) (toMillis() / TimeUnit.TICKS.getMultiple());
    }
    public int toSeconds() {
        return (int) (toMillis() / TimeUnit.SECONDS.getMultiple());
    }
    public float toFloatSeconds() {
        return toMillis() / (float) TimeUnit.SECONDS.getMultiple();
    }
    public String toVisualCeilSeconds() {
        return FloatConverter.floatingCeilFormat(toFloatSeconds());
    }
    public String toVisualFloatSeconds() {
        return FloatConverter.floatingFormat(toFloatSeconds());
    }
}