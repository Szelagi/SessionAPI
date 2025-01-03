package pl.szelagi.spatial;

import org.joml.Vector3i;

public record SpatialResolve(boolean isValid, Vector3i min, Vector3i max) {
}
