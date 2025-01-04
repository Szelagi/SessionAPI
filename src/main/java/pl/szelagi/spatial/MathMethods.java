package pl.szelagi.spatial;

public class MathMethods {
    public static boolean isBetween(double p, double a, double b) {
        double min = Math.min(a, b);
        double max = Math.max(a, b);
        return p >= min && p <= max;
    }
}
