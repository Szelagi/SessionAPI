package pl.szelagi.component;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.MustBeInvokedByOverriders;
import pl.szelagi.component.constructor.InitializeType;
import pl.szelagi.component.constructor.PlayerDestructorLambdas;
import pl.szelagi.component.constructor.UninitializedType;

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
