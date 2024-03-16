package pl.szelagi.tag.serialization;

import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import pl.szelagi.relative.RelativePoint;

import java.io.Serializable;

public class SignTagStorage implements Serializable {
    @NotNull
    private final String tag;

    @NotNull
    private final RelativePoint relativePoint;

    @NotNull
    private final BlockFace blockFace;

    @NotNull
    private final String[] args;

    public SignTagStorage(@NotNull String tag, @NotNull RelativePoint relativePoint, @NotNull BlockFace blockFace, @NotNull String[] args) {
        this.tag = tag;
        this.relativePoint = relativePoint;
        this.blockFace = blockFace;
        this.args = args;
    }

    @NotNull
    public String getTag() {
        return tag;
    }

    @NotNull
    public RelativePoint getRelativePoint() {
        return relativePoint;
    }

    @NotNull
    public BlockFace getBlockFace() {
        return blockFace;
    }

    @NotNull
    public String[] getArgs() {
        return args;
    }
}
