package pl.szelagi.world;

import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class EmptyChunkGenerator extends ChunkGenerator {
	@Override
	@NotNull
	public ChunkData generateChunkData(@NotNull World world, @NotNull Random random, int chunkX, int chunkZ, @NotNull BiomeGrid biome) {
		var chunkData = createChunkData(world);

		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				for (int y = world.getMinHeight(); y < world.getMaxHeight(); y++) {
					biome.setBiome(x, y, z, Biome.MEADOW);
				}
			}
		}

		return chunkData;
	}
}
