package pl.szelagi.spatial;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public class Spatial implements ISpatial {
    private final Location first;
    private final Location second;

    public Spatial(Location first, Location second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public @NotNull Location getFirstPoint() {
        return first;
    }

    @Override
    public @NotNull Location getSecondPoint() {
        return second;
    }

    @Override
    public String toString() {
        return "Spatial{" +
                "first=" + first +
                ", second=" + second +
                '}';
    }
}
