package pl.szelagi.tag;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class TagQuery extends ArrayList<Tag> implements Serializable {
	@NotNull private final String tag;

	public TagQuery(@NotNull String tag, @NotNull List<Tag> elements) {
		super(elements);
		this.tag = tag;
	}

	public @NotNull String getQueryName() {
		return tag;
	}

	//	public @NotNull Tag getFirst() {
	//		return get(0);
	//	}

	public @NotNull Location getFirstLocation() {
		return get(0).getLocation();
	}

	public @NotNull List<Location> toLocations() {
		return stream().map(Tag::getLocation)
		               .toList();
	}

	public @NotNull <R> List<R> map(Function<Tag, R> mapper) {
		return stream().map(mapper).toList();
	}

	public @NotNull List<Tag> filter(Predicate<Tag> predicate) {
		return stream().filter(predicate)
		               .toList();
	}

	public @Nullable Tag find(Predicate<Tag> predicate) {
		return stream().filter(predicate)
		               .findFirst().orElse(null);
	}
}
