/*
 * SessionAPI - A framework for game containerization on Minecraft servers
 * Copyright (C) 2025 Szelagi (https://github.com/Szelagi/SessionAPI)
 * Licensed under the GNU General Public License v3.0.
 * For more details, visit <https://www.gnu.org/licenses/>.
 */

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
