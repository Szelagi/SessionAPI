package pl.szelagi.spatial;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public interface ISpatial {
    interface AxiGetter<T> {
        T get(Location location);
    }
    interface AxiSetter<T> {
        void set(Location location, T value);
    }
    @NotNull
    Location getFirstPoint();
    @NotNull
    Location getSecondPoint();

    default ArrayList<Block> getBlocksInArea(Location loc1, Location loc2) {
        int lowX = Math.min(loc1.getBlockX(), loc2.getBlockX());
        int lowY = Math.min(loc1.getBlockY(), loc2.getBlockY());
        int lowZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());

        ArrayList<Block> locs = new ArrayList<>();
        Block currentBlock;

        for(int x = 0; x<=Math.abs(loc1.getBlockX()-loc2.getBlockX()); x++){
            for(int y = 0; y<=Math.abs(loc1.getBlockY()-loc2.getBlockY()); y++){
                for(int z = 0; z<=Math.abs(loc1.getBlockZ()-loc2.getBlockZ()); z++){
                    currentBlock = new Location(loc1.getWorld(),lowX+x, lowY+y, lowZ+z).getBlock();
                    locs.add(currentBlock);
                }
            }
        }
        return locs;
    }

    @NotNull
    default ArrayList<Block> getBlocksInArea() {
        return getBlocksInArea(getFirstPoint(), getSecondPoint());
    }
    default boolean isLocationIn(Location location) {
        var l1 = this.getFirstPoint();
        var l2 = this.getSecondPoint();
        if (!isSameWorld(l1, l2)) return false;
        if (!isSameWorld(location, l1)) return false;
        boolean isX = isBetween(location.getBlockX(), l1.getBlockX(), l2.getBlockX());
        boolean isY = isBetween(location.getBlockY(), l1.getBlockY(), l2.getBlockY());
        boolean isZ = isBetween(location.getBlockZ(), l1.getBlockZ(), l2.getBlockZ());
        return isX && isY && isZ;
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
    default ISpatial toOptimizedSpatial() {
        return new SpatialOptimizer(getFirstPoint(), getSecondPoint()).optimize();
    }

    default Location getAbove(Location location) {
        return location.clone().add(0, 1, 0);
    }
    private static double average(double ...numbers) {
        double sum = 0;
        for (var num : numbers) sum += num;
        return sum / numbers.length;
    }
    private static boolean isBetween(double p, double a, double b) {
        double min = Math.min(a, b);
        double max = Math.max(a, b);
        return p >= min && p <= max;
    }
    private static boolean isSameWorld(Location location1, Location location2) {
        return location1.getWorld().getName().equals(location2.getWorld().getName());
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


}