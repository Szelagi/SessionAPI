package pl.szelagi.tag;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import pl.szelagi.relative.RelativeLocation;

import java.io.Serializable;

public class SignTag implements Serializable {

    @NotNull
    private final String tag;

    @NotNull
    private final RelativeLocation relativeLocation;

    @NotNull
    private final BlockFace blockFace;

    @NotNull
    private final String[] args;

    public SignTag(@NotNull String tag,
                   @NotNull RelativeLocation relativeLocation,
                   @NotNull BlockFace blockFace,
                   @NotNull String[] args) {
        this.tag = tag;
        this.relativeLocation = relativeLocation;
        this.blockFace = blockFace;
        this.args = args;
    }

    @NotNull
    public String getTag() {
        return tag;
    }

    @NotNull
    public Location getLocation() {
        return relativeLocation;
    }

    @NotNull
    public RelativeLocation getRelativeLocation() {
        return relativeLocation;
    }

    @NotNull
    public String getArgument(int index) {
        return (index < args.length) ? args[index] : "";
    }

    public int getArgumentCount() {
        return args.length;
    }

    @NotNull
    public BlockFace getBlockFace() {
        return blockFace;
    }

    public String[] getArgs() {
        return args;
    }
}
