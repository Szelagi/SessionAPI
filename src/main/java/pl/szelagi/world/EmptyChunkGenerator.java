/*
 * SessionAPI - A framework for game containerization on Minecraft servers
 * Copyright (C) 2025 Szelagi (https://github.com/Szelagi/SessionAPI)
 * Licensed under the GNU General Public License v3.0.
 * For more details, visit <https://www.gnu.org/licenses/>.
 */

package pl.szelagi.world;

import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class EmptyChunkGenerator extends ChunkGenerator {
	@Override
	@NotNull
	public ChunkData generateChunkData(@NotNull World world, @NotNull Random random, int chunkX, int chunkZ, @NotNull BiomeGrid biome) {
		var chunkData = createChunkData(world);

		// Temporarily disable biome overwriting code to evaluate performance
		//		for (int x = 0; x < 16; x++) {
		//			for (int z = 0; z < 16; z++) {
		//				//						for (int y = world.getMinHeight(); y < world.getMaxHeight(); y++) {
		//				biome.setBiome(x, 0, z, Biome.MEADOW);
		//				//						}
		//			}
		//		}

		return chunkData;
	}
}
