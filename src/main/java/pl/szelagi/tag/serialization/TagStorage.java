package pl.szelagi.tag.serialization;

import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import pl.szelagi.relative.RelativePoint;

import java.io.Serializable;
import java.util.List;

public record TagStorage(@NotNull String tag,
                         @NotNull RelativePoint relativePoint,
                         @NotNull BlockFace blockFace,
                         @NotNull List<String> args) implements Serializable {}
