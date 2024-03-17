package pl.szelagi.spatial;

import org.bukkit.Location;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

public class SpatialOptimizer implements ISpatial {
	private final static ArrayList<Material> AIR_MATERIALS = new ArrayList<>(Arrays.asList(Material.AIR, Material.CAVE_AIR, Material.VOID_AIR, Material.LAVA, Material.WATER));
	private Location first;
	private Location second;

	public SpatialOptimizer(Location first, Location second) {
		this.first = first;
		this.second = second;
	}

	public SpatialOptimizer optimize() {
		AxiSetter<Integer> xSetter = (location, value) -> location.setX(value.doubleValue());
		AxiSetter<Integer> ySetter = (location, value) -> location.setY(value.doubleValue());
		AxiSetter<Integer> zSetter = (location, value) -> location.setZ(value.doubleValue());
		AxiGetter<Integer> xGetter = Location::getBlockX;
		AxiGetter<Integer> yGetter = Location::getBlockY;
		AxiGetter<Integer> zGetter = Location::getBlockZ;

		// optimize for first point
		optimizeT(xSetter, xGetter, first);
		optimizeT(ySetter, yGetter, first);
		optimizeT(zSetter, zGetter, first);

		// optimize for second point
		optimizeT(xSetter, xGetter, second);
		optimizeT(ySetter, yGetter, second);
		optimizeT(zSetter, zGetter, second);

		return this;
	}

	@Override
	public @NotNull Location getFirstPoint() {
		return first;
	}

	@Override
	public @NotNull Location getSecondPoint() {
		return second;
	}

	private boolean isAnyBlock(Location loc1, Location loc2) {
		for (var b : getBlocksInArea(loc1, loc2)) {
			if (!AIR_MATERIALS.contains(b.getType()))
				return true;
		}
		return false;
	}

	private Location getAnotherPoint(Location firstOrSecondPoint) {
		if (getFirstPoint().equals(firstOrSecondPoint))
			return getSecondPoint();
		return getFirstPoint();
	}

	private boolean isSecondPoint(Location point) {
		return getSecondPoint().equals(point);
	}

	private boolean isFirstPoint(Location point) {
		return getFirstPoint().equals(point);
	}

	private void optimizeT(AxiSetter<Integer> setter, AxiGetter<Integer> getter, final Location operative) {
		final Location opposite = getAnotherPoint(operative);
		final int direction = axiDirection(operative, opposite, getter);

		Location operativeDynamic = operative.clone();
		Location oppositeDynamic = opposite.clone();

		for (int i = 0; i < axiDistance(operative, opposite, getter); i++) {
			setter.set(oppositeDynamic, getter.get(operative)); // set stable to same line
			if (isAnyBlock(operativeDynamic, oppositeDynamic))
				break;
			setter.set(operativeDynamic, getter.get(operativeDynamic) + direction); // move operative
		}

		if (isFirstPoint(operative)) {
			this.first = operativeDynamic;
		} else {
			this.second = operativeDynamic;
		}
	}

	private int axiDirection(Location operative, Location stable, AxiGetter<Integer> axiGetter) {
		int compared = axiGetter.get(stable) - axiGetter.get(operative);
		if (compared == 0)
			return 0;
		return compared / Math.abs(compared);
	}

	private int axiDistance(Location loc1, Location loc2, AxiGetter<Integer> getter) {
		return Math.abs(getter.get(loc2) - getter.get(loc1));
	}
}
