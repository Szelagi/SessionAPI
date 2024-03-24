package pl.szelagi.state;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.szelagi.state.manual.ManualContainerException;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.function.Function;
import java.util.function.Predicate;

public class ManualContainer<I, S> implements Serializable, Iterable<S> {
	private final HashMap<I, S> inputStorageMap = new HashMap<>();

	public @NotNull S create(@NotNull I input, @NotNull Function<I, S> creator) throws ManualContainerException {
		if (inputStorageMap.containsKey(input))
			throw new ManualContainerException("manual container of " + input + " multi initialization");
		inputStorageMap.put(input, creator.apply(input));
		return get(input);
	}

	public @NotNull S get(@NotNull I input) throws ManualContainerException {
		var record = inputStorageMap.get(input);
		if (record == null)
			throw new ManualContainerException("manual container of " + input + " is not initialized");
		return record;
	}

	public @NotNull S remove(@NotNull I input) throws ManualContainerException {
		var record = inputStorageMap.remove(input);
		if (record == null)
			throw new ManualContainerException("remove " + input + " not exists in manual container");
		return record;
	}

	public @Nullable S find(Predicate<S> predicate) {
		return inputStorageMap.values().stream()
		                      .filter(predicate)
		                      .findFirst()
		                      .orElse(null);
	}

	public boolean isExists(@NotNull I input) {
		return inputStorageMap.containsKey(input);
	}

	@NotNull
	@Override
	public Iterator<S> iterator() {
		return inputStorageMap.values()
		                      .iterator();
	}
}
