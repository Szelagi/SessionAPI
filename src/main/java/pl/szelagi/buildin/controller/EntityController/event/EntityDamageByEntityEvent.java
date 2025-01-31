/*
 * SessionAPI - A framework for game containerization on Minecraft servers
 * Copyright (C) 2025 Szelagi (https://github.com/Szelagi/SessionAPI)
 * Licensed under the GNU General Public License v3.0.
 * For more details, visit <https://www.gnu.org/licenses/>.
 */

package pl.szelagi.buildin.controller.EntityController.event;

import pl.szelagi.buildin.controller.EntityController.ControlledEntity;
import pl.szelagi.buildin.controller.EntityController.EntityController;

public interface EntityDamageByEntityEvent {
	void run(EntityController controller, org.bukkit.event.entity.EntityDamageByEntityEvent event, ControlledEntity type);
}