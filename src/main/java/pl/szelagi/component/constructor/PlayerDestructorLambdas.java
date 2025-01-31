/*
 * SessionAPI - A framework for game containerization on Minecraft servers
 * Copyright (C) 2025 Szelagi (https://github.com/Szelagi/SessionAPI)
 * Licensed under the GNU General Public License v3.0.
 * For more details, visit <https://www.gnu.org/licenses/>.
 */

package pl.szelagi.component.constructor;

import org.bukkit.entity.Player;

public class PlayerDestructorLambdas extends PlayerRecoveryLambdas<PlayerDestructorLambda> {
	@Override
	public PlayerDestructorLambdas add(PlayerDestructorLambda lambda) {
		return (PlayerDestructorLambdas) super.add(lambda);
	}

	public void runAll(Player player) {
		for (var l : getLambdas()) {
			l.run(player);
		}
	}
}
