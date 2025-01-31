/*
 * SessionAPI - A framework for game containerization on Minecraft servers
 * Copyright (C) 2025 Szelagi (https://github.com/Szelagi/SessionAPI)
 * Licensed under the GNU General Public License v3.0.
 * For more details, visit <https://www.gnu.org/licenses/>.
 */

package pl.szelagi.buildin.controller.entity;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.function.Consumer;

public interface SpawnEntity<T extends Entity> {
	LivingEntity spawn(Location location);

	static <T extends Entity> SpawnEntity<T> make(Class<T> entityClazz, Consumer<? super T> consumer) {
		return location -> (LivingEntity) location
				.getWorld()
				.spawn(location, entityClazz, consumer);
	}

	static <T extends Entity> SpawnEntity<T> make(EntityType entityType, Consumer<? super Entity> consumer) {
		return location -> (LivingEntity) location
				.getWorld()
				.spawnEntity(location, entityType, CreatureSpawnEvent.SpawnReason.COMMAND, consumer);
	}

	static <T extends Entity> SpawnEntity<T> make(Class<T> entityClazz) {
		return location -> (LivingEntity) location
				.getWorld()
				.spawn(location, entityClazz);
	}

	static <T extends Entity> SpawnEntity<T> make(EntityType entityType) {
		return location -> (LivingEntity) location
				.getWorld()
				.spawnEntity(location, entityType);
	}
}
