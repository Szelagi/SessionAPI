package pl.szelagi.tag;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import pl.szelagi.tag.exception.NoTagException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class SignTagQuery implements Serializable {
	@NotNull private final ArrayList<SignTag> elements;
	@NotNull private final String tag;

	public SignTagQuery(@NotNull String tag, @NotNull ArrayList<SignTag> elements) throws NoTagException {
		if (elements.isEmpty())
			throw new NoTagException(tag);
		this.tag = tag;
		this.elements = elements;
	}

	@NotNull
	public ArrayList<SignTag> getElements() {
		return elements;
	}

	@NotNull
	public String getTag() {
		return tag;
	}

	@NotNull
	public ArrayList<Location> toLocations() {
		return elements.stream().map(SignTag::getLocation).collect(Collectors.toCollection(ArrayList::new));
	}

	@NotNull
	public Location toFirstLocation() {
		// element [0] always set, because constructor require this
		return elements.get(0).getLocation();
	}
}
