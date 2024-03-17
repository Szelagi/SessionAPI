package pl.szelagi.component.constructor;

import org.bukkit.entity.Player;

public class PlayerDestructorLambdas extends PlayerRecoveryLambdas<PlayerDestructorLambda> {
	@Override
	public PlayerDestructorLambdas add(PlayerDestructorLambda lambda) {
		return (PlayerDestructorLambdas) super.add(lambda);
	}

	public void runAll(Player player, UninitializedType type) {
		for (var l : getLambdas()) {
			l.run(player, type);
		}
	}
}
