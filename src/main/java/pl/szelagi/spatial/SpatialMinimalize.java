package pl.szelagi.spatial;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.TestOnly;
import org.joml.Vector3i;
import pl.szelagi.SessionAPI;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class SpatialMinimalize {
	private static final int PART_SIZE = 70;

	public static void async(ISpatial spatial, Consumer<ISpatial> callback) {
		var parts = SpatialMinimalize.toParts(spatial);
		var iterator = parts.iterator();
		var resolves = new HashSet<SpatialResolve>();
		var instance = SessionAPI.getInstance();

		var nextElement = new Runnable() {
			@Override
			public void run() {
				var resolved = resolve(iterator.next());
				resolves.add(resolved);
				if (iterator.hasNext()) {
					Bukkit.getScheduler().runTaskLater(instance, this, 1L);
				} else {
					var minimalized = minimalize(spatial.getFirstPoint().getWorld(), resolves);
					callback.accept(minimalized);
				}
			}
		};

		Bukkit.getScheduler().runTask(instance, nextElement);
	}

	public static ISpatial sync(ISpatial spatial) {
		var parts = SpatialMinimalize.toParts(spatial);
		var resolves = new HashSet<SpatialResolve>();
		for (var part : parts) {
			var resolved = resolve(part);
			resolves.add(resolved);
		}
		return minimalize(spatial.getFirstPoint().getWorld(), resolves);
	}

	private static Set<ISpatial> toParts(ISpatial spatial) {
		var first = spatial.getFirstPoint();
		var second = spatial.getSecondPoint();
		var parts = new HashSet<ISpatial>();
		var xParts = (int) Math.ceil(spatial.sizeX() / (double) PART_SIZE);
		var zParts = (int) Math.ceil(spatial.sizeZ() / (double) PART_SIZE);

		var minX = Math.min(first.getX(), second.getX());
		var minZ = Math.min(first.getZ(), second.getZ());
		var minY = Math.min(first.getY(), second.getY());
		var maxY = Math.max(first.getY(), second.getY());
		var maxX = Math.max(first.getX(), second.getX());
		var maxZ = Math.max(first.getZ(), second.getZ());
		var world = first.getWorld();

		var currentX = minX;
		for (int x = 0; x < xParts; x++) {
			var currentZ = minZ;
			final var deltaX = Math.min(PART_SIZE, maxX - currentX + 1);

			for (int z = 0; z < zParts; z++) {
				final var deltaZ = Math.min(PART_SIZE, maxZ - currentZ + 1);

				var firstPointPart = new Location(world, currentX, minY, currentZ);
				var secondPointPart = new Location(world, currentX + deltaX - 1, maxY, currentZ + deltaZ - 1);
				var spatialPart = new Spatial(firstPointPart, secondPointPart);
				parts.add(spatialPart);

				currentZ += deltaZ;
			}
			currentX += deltaX;
		}
		return parts;
	}

	private static ISpatial minimalize(World world, Collection<SpatialResolve> resolves) {
		boolean isInitialized = false;
		var globalMin = new Vector3i(0, 0, 0);
		var globalMax = new Vector3i(0, 0, 0);

		for (var res : resolves) {
			if (!res.isValid()) continue;
			if (!isInitialized) {
				isInitialized = true;
				globalMin = res.min();
				globalMax = res.max();
				continue;
			}
			globalMin.x = Math.min(globalMin.x, res.min().x);
			globalMax.x = Math.max(globalMax.x, res.max().x);

			globalMin.y = Math.min(globalMin.y, res.min().y);
			globalMax.y = Math.max(globalMax.y, res.max().y);

			globalMin.z = Math.min(globalMin.z, res.min().z);
			globalMax.z = Math.max(globalMax.z, res.max().z);
		}

		var newFirst = new Location(world, globalMin.x, globalMin.y, globalMin.z);
		var newSecond = new Location(world, globalMax.x, globalMax.y, globalMax.z);
		return new Spatial(newFirst, newSecond);
	}


	private static SpatialResolve resolve(ISpatial spatial) {
		var min = new Vector3i(0, 0, 0);
		var max = new Vector3i(0, 0, 0);
		var hasBlock = new AtomicBoolean(false);

		spatial.eachBlocks(block -> {
			var material = block.getType();
			var x = block.getX();
			var y = block.getY();
			var z = block.getZ();
			if (ISpatial.isAirMaterial(material))
				return;

			if (!hasBlock.get()) {
				hasBlock.set(true);
				min.set(x, y, z);
				max.set(x, y, z);
			}

			min.x = Math.min(min.x, x);
			max.x = Math.max(max.x, x);

			min.y = Math.min(min.y, y);
			max.y = Math.max(max.y, y);

			min.z = Math.min(min.z, z);
			max.z = Math.max(max.z, z);
		});

		return new SpatialResolve(hasBlock.get(), min, max);
	}

	@TestOnly
	private static boolean testPartAlgorithm(ISpatial spatial, Set<ISpatial> parts) {
		var first = spatial.getFirstPoint();
		var second = spatial.getSecondPoint();
		var minX = (int) Math.min(first.getX(), second.getX());
		var minZ = (int) Math.min(first.getZ(), second.getZ());
		var maxX = (int) Math.max(first.getX(), second.getX());
		var maxZ = (int) Math.max(first.getZ(), second.getZ());

		boolean[][] covered = new boolean[maxX - minX + 1][maxZ - minZ + 1];
		for (var part : parts) {
			int startX = (int) part.getFirstPoint().getX();
			int endX = (int) part.getSecondPoint().getX();
			int startZ = (int) part.getFirstPoint().getZ();
			int endZ = (int) part.getSecondPoint().getZ();

			for (int x = startX; x <= endX; x++) {
				for (int z = startZ; z <= endZ; z++) {
					if (covered[x - minX][z - minZ]) {
						System.out.println("Overlap found at X: " + x + ", Z: " + z);
						return false;
					}
					covered[x - minX][z - minZ] = true;
				}
			}
		}
		for (int x = 0; x < covered.length; x++) {
			for (int z = 0; z < covered[x].length; z++) {
				if (!covered[x][z]) {
					System.out.println("Gap found at X: " + (x + minX) + ", Z: " + (z + minZ));
					return false;
				}
			}
		}
		System.out.println("Verification complete.");
		return true;
	}
}
