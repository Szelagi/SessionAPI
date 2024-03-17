package pl.szelagi.spatial;

public class ImmutableRadius3D<T extends Number> {
	private final T x, y, z;

	public ImmutableRadius3D(T x, T y, T z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public T getX() {
		return x;
	}

	public T getY() {
		return y;
	}

	public T getZ() {
		return z;
	}
}
