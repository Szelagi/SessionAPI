package pl.szelagi.spatial;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.szelagi.SessionAPI;
import pl.szelagi.component.session.Session;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public interface ISpatial extends Cloneable {
	ArrayList<Material> AIR_MATERIALS = new ArrayList<>(Arrays.asList(Material.AIR, Material.CAVE_AIR, Material.VOID_AIR, Material.LAVA, Material.WATER));

	static boolean isAirMaterial(Material material) {
		return AIR_MATERIALS.contains(material);
	}

	static ISpatial clone(ISpatial spatial) {
		return new ISpatial() {
			@Override
			public @NotNull Location getFirstPoint() {
				return spatial.getFirstPoint()
				              .clone();
			}

			@Override
			public @NotNull Location getSecondPoint() {
				return spatial.getSecondPoint()
				              .clone();
			}
		};
	}

	static ISpatial from(Block block) {
		return new ISpatial() {
			@Override
			public @NotNull Location getFirstPoint() {
				return block.getLocation()
				            .clone();
			}

			@Override
			public @NotNull Location getSecondPoint() {
				return block.getLocation()
				            .clone();
			}
		};
	}

	private static double average(double... numbers) {
		double sum = 0;
		for (var num : numbers)
			sum += num;
		return sum / numbers.length;
	}

	private static boolean isBetween(double p, double a, double b) {
		double min = Math.min(a, b);
		double max = Math.max(a, b);
		return p >= min && p <= max;
	}

	private static boolean isSameWorld(Location location1, Location location2) {
		return location1.getWorld().getName()
		                .equals(location2
				                        .getWorld()
				                        .getName());
	}

	@NotNull Location getFirstPoint();

	@NotNull Location getSecondPoint();

	@NotNull
	default List<Block> getBlocksIn() {
		return getBlocksIn(getFirstPoint(), getSecondPoint());
	}

	default void eachBlocks(Consumer<Block> predicate) {
		eachBlocks(getFirstPoint(), getSecondPoint(), predicate);
	}

	static List<Block> getBlocksIn(Location loc1, Location loc2) {
		var list = new ArrayList<Block>();
		eachBlocks(loc1, loc2, list::add);
		return list;
	}

	static void eachBlocks(Location loc1, Location loc2, Consumer<Block> predicate) {
		int minX = Math.min(loc1.getBlockX(), loc2.getBlockX());
		int minY = Math.min(loc1.getBlockY(), loc2.getBlockY());
		int minZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
		int maxX = Math.max(loc1.getBlockX(), loc2.getBlockX());
		int maxY = Math.max(loc1.getBlockY(), loc2.getBlockY());
		int maxZ = Math.max(loc1.getBlockZ(), loc2.getBlockZ());
		Block block;

		for (int x = minX; x <= maxX; x++)
			for (int y = minY; y <= maxY; y++)
				for (int z = minZ; z <= maxZ; z++) {
					block = loc1.getWorld()
					            .getBlockAt(x, y, z);
					predicate.accept(block);
				}
	}

	default boolean isLocationIn(Location location) {
		var l1 = this.getFirstPoint();
		var l2 = this.getSecondPoint();
		boolean isXZ = isLocationInXZ(location);
		boolean isZ = isBetween(location.getBlockZ(), l1.getBlockZ(), l2.getBlockZ());
		return isXZ && isZ;
	}

	default boolean isLocationInXZ(Location location) {
		var l1 = this.getFirstPoint();
		var l2 = this.getSecondPoint();
		if (!isSameWorld(l1, l2))
			return false;
		if (!isSameWorld(location, l1))
			return false;
		boolean isX = isBetween(location.getBlockX(), l1.getBlockX(), l2.getBlockX());
		boolean isZ = isBetween(location.getBlockZ(), l1.getBlockZ(), l2.getBlockZ());
		return isX && isZ;
	}

	default Location getCenter() {
		var x = getAxiAverage(Location::getBlockX);
		var y = getAxiAverage(Location::getBlockY);
		var z = getAxiAverage(Location::getBlockZ);
		var location = new Location(getFirstPoint().getWorld(), x, y, z);
		return location.add(0.5, 0, 0.5);
	}

	default Location getCenterBlockLocation() {
		return getCenter().toBlockLocation();
	}

	default int getAxiAverage(AxiGetter<Integer> getter) {
		var a = getter.get(getFirstPoint());
		var b = getter.get(getSecondPoint());
		return (a + b) / 2;
	}

	@Deprecated
	default ISpatial toOptimized() {
		return new SpatialOptimizer(getFirstPoint(), getSecondPoint()).optimize();
	}

	default void minimalizeAsync(Consumer<ISpatial> callback) {
		SpatialMinimalize.async(this, callback);
	}

	default ISpatial minimalizeSync() {
		return SpatialMinimalize.sync(this);
	}

	default Location getAbove(Location location) {
		return location.clone().add(0, 1, 0);
	}

	private int distanceBlock(int a, int b) {
		return Math.abs(a - b) + 1;
	}

	default int sizeX() {
		return distanceBlock(getFirstPoint().getBlockX(), getSecondPoint().getBlockX());
	}

	default int sizeY() {
		return distanceBlock(getFirstPoint().getBlockY(), getSecondPoint().getBlockY());
	}

	default int sizeZ() {
		return distanceBlock(getFirstPoint().getBlockZ(), getSecondPoint().getBlockZ());
	}

	default int size() {
		return sizeX() * sizeY() * sizeZ();
	}

	private double getRadius(AxiGetter<Integer> axiGetter) {
		return Math.abs(axiGetter.get(getFirstPoint()) - axiGetter.get(getSecondPoint())) / 2d + 1;
	}

	default ImmutableRadius3D<Double> getRadiusInscribed() {
		var radiusX = getRadius(Location::getBlockX);
		var radiusY = getRadius(Location::getBlockY);
		var radiusZ = getRadius(Location::getBlockZ);
		return new ImmutableRadius3D<>(radiusZ, radiusY, radiusZ);
	}

	default ImmutableRadius3D<Double> getRadiusCircumscribed() {
		final var sqrt2 = Math.sqrt(2);
		var inscribed = getRadiusInscribed();
		return new ImmutableRadius3D<>(inscribed.getX() * sqrt2, inscribed.getY() * sqrt2, inscribed.getZ() * sqrt2);
	}

	default @NotNull Collection<Entity> getEntitiesIn() {
		var radius = getRadiusCircumscribed();
		return getCenterBlockLocation()
				.getNearbyEntities(radius.getX(), radius.getY(), radius.getZ())
				.stream()
				.filter(entity -> isLocationIn(entity.getLocation()))
				.collect(Collectors.toCollection(ArrayList::new));
	}

	default @NotNull Collection<Entity> getMobsIn() {
		var radius = getRadiusCircumscribed();
		return getCenterBlockLocation().getNearbyEntitiesByType(Entity.class, radius.getX(), radius.getY(), radius.getZ(), entity -> !(entity instanceof Player));
	}

	default @NotNull Collection<Player> getPlayersIn() {
		var radius = getRadiusCircumscribed();
		return getCenterBlockLocation().getNearbyPlayers(radius.getX(), radius.getY(), radius.getZ());
	}

	interface AxiGetter<T> {
		T get(Location location);
	}

	interface AxiSetter<T> {
		void set(Location location, T value);
	}
}
