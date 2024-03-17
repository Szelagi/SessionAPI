package pl.szelagi.util;

public class FloatConverter {
	public static String floatingFormat(float n) {
		return String.format("%.02f", n);
	}

	public static String floatingFloorFormat(float n) {
		return String.format("%.0f", Math.floor(n));
	}

	public static String floatingCeilFormat(float n) {
		return String.format("%.0f", Math.ceil(n));
	}
}
