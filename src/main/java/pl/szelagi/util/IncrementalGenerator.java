package pl.szelagi.util;

public class IncrementalGenerator {
	private long currentId = 0;

	public long next() {
		return currentId++;
	}
}
