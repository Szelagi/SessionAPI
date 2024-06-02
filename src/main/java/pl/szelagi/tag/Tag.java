package pl.szelagi.tag;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import pl.szelagi.relative.RelativeLocation;

import java.io.Serializable;
import java.util.List;

public class Tag implements Serializable {
	@NotNull private final String name;
	@NotNull
	private final RelativeLocation relativeLocation;
	@NotNull private final BlockFace blockFace;
	@NotNull private final List<String> args;

	public Tag(@NotNull String name, @NotNull RelativeLocation relativeLocation, @NotNull BlockFace blockFace, @NotNull List<String> args) {
		this.name = name;
		this.relativeLocation = relativeLocation;
		this.blockFace = blockFace;
		this.args = args;
	}

	public @NotNull String getName() {
		return name;
	}

	public @NotNull Location getLocation() {
		return relativeLocation;
	}

	public @NotNull RelativeLocation getRelativeLocation() {
		return relativeLocation;
	}

	public @NotNull BlockFace getBlockFace() {
		return blockFace;
	}

	public @NotNull List<String> getArgs() {
		return args;
	}
}
