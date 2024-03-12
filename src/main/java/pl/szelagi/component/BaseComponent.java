package pl.szelagi.component;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.MustBeInvokedByOverriders;
import pl.szelagi.component.constructor.InitializeType;
import pl.szelagi.component.constructor.PlayerDestructorLambdas;
import pl.szelagi.component.constructor.UninitializedType;
import pl.szelagi.util.IncrementalGenerator;

import java.util.UUID;

public abstract class BaseComponent implements ISessionComponent, IComponentConstructors {
    private static final IncrementalGenerator incrementalGenerator = new IncrementalGenerator();
    private final UUID uuid = UUID.randomUUID();
    private final long id = incrementalGenerator.next();
    private boolean isEnable = false;

    protected void setEnable(boolean enable) {
        isEnable = enable;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public UUID getUuid() {
        return uuid;
    }

    public long getId() {
        return id;
    }

    @MustBeInvokedByOverriders
    public abstract void systemPlayerConstructor(Player player, InitializeType type);
    @MustBeInvokedByOverriders
    public abstract void systemPlayerDestructor(Player player, UninitializedType type);

    @Override
    public void constructor() {}

    @Override
    public void destructor() {}

    @Override
    public void playerConstructor(Player player, InitializeType type) {}

    @Override
    public void playerDestructor(Player player, UninitializedType type) {}

    @Override
    public PlayerDestructorLambdas getPlayerDestructorRecovery(Player forPlayer) {
        return new PlayerDestructorLambdas();
    }
    protected void startBaseControllers() {}
}
