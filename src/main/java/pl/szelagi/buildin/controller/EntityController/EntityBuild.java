package pl.szelagi.buildin.controller.EntityController;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class EntityBuild {

    @NotNull
    private final EntityType entityType;

    @Nullable
    private final EntityBuilder builder;

    @NotNull
    private final HashMap<Location, Integer> spawnEntries;
    public EntityBuild(@NotNull EntityType entityType, @Nullable EntityBuilder builder, @NotNull HashMap<Location, Integer> spawnEntries) {
        this.entityType = entityType;
        this.builder = builder;
        this.spawnEntries = spawnEntries;
    }

    @Deprecated
    public EntityBuild(@NotNull EntityType entityType, @Nullable EntityBuilder builder, @NotNull Location location, int entityCount) {
        this.entityType = entityType;
        this.builder = builder;
        this.spawnEntries = new HashMap<>();
        this.spawnEntries.put(location, entityCount);
    }
    public EntityBuild(@NotNull EntityType entityType, @Nullable EntityBuilder builder, int entityCount, @NotNull Location ...locations) {
        this.entityType = entityType;
        this.builder = builder;
        this.spawnEntries = new HashMap<>();
        for (var l : locations) spawnEntries.put(l, entityCount);
    }

    @NotNull
    public Set<Map.Entry<Location, Integer>> getSpawnEntriesSet() {
        return spawnEntries.entrySet();
    }
    @Nullable
    public EntityBuilder getBuilder() {
        return builder;
    }
    @NotNull
    public EntityType getEntityType() {
        return entityType;
    }
}
