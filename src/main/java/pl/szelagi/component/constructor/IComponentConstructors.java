package pl.szelagi.component.constructor;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.MustBeInvokedByOverriders;

public interface IComponentConstructors {
    @MustBeInvokedByOverriders
    void constructor();
    @MustBeInvokedByOverriders
    void destructor();
    @MustBeInvokedByOverriders
    void playerConstructor(Player player, InitializeType type);
    @MustBeInvokedByOverriders
    void playerDestructor(Player player, UninitializedType type);
    @MustBeInvokedByOverriders
    PlayerDestructorLambdas getPlayerDestructorRecovery(Player forPlayer);
}
