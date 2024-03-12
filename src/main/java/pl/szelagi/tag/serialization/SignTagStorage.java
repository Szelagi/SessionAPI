package pl.szelagi.tag.serialization;

import org.jetbrains.annotations.NotNull;
import pl.szelagi.relative.RelativePoint;

import java.io.Serializable;

public class SignTagStorage implements Serializable {
    @NotNull
    private final String tag;

    @NotNull
    private final RelativePoint relativePoint;

    @NotNull
    private final String[] args;

    public SignTagStorage(@NotNull String tag, @NotNull RelativePoint relativePoint, @NotNull String[] args) {
        this.tag = tag;
        this.relativePoint = relativePoint;
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
    public String[] getArgs() {
        return args;
    }
}
