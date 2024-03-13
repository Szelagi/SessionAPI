package pl.szelagi.component;

import org.bukkit.entity.Player;
import pl.szelagi.component.constructor.InitializeType;
import pl.szelagi.component.constructor.PlayerDestructorLambdas;
import pl.szelagi.component.constructor.UninitializedType;
import pl.szelagi.util.IncrementalGenerator;
import pl.szelagi.util.ReflectionRecursive;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

// Component must implement methods:
//    private void systemPlayerConstructor(Player player, InitializeType type)
//    private void systemPlayerDestructor(Player player, UninitializedType type)

public abstract class BaseComponent implements ISessionComponent, IComponentConstructors {
    protected static <T extends BaseComponent> void reflectionSystemPlayerConstructor(T object, Player player, InitializeType type) {
        try {
            var method = ReflectionRecursive.getDeclaredMethod(object, "systemPlayerConstructor", Player.class, InitializeType.class);
            method.setAccessible(true);
            method.invoke(object, player, type);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException exception) {
            throw new RuntimeException(exception);
        }
    }
    protected static <T extends BaseComponent> void reflectionSystemPlayerDestructor(T object, Player player, UninitializedType type) {
        try {
            var method = ReflectionRecursive.getDeclaredMethod(object, "systemPlayerDestructor", Player.class, UninitializedType.class);
            method.setAccessible(true);
            method.invoke(object, player, type);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException exception) {
            throw new RuntimeException(exception);
        }
    }
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
