package fr.communaywen.core.dreamdim;

import org.bukkit.Material;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.bukkit.util.noise.SimplexOctaveGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class DreamChunkGenerator extends ChunkGenerator {

    @Override
    public void generateNoise(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ, @NotNull ChunkGenerator.ChunkData chunkData) {
        SimplexOctaveGenerator generator = new SimplexOctaveGenerator(new Random(worldInfo.getSeed()), 6);
        generator.setScale(0.008);

        Material material = Material.SCULK;

        int worldX = chunkX * 16;
        int worldZ = chunkZ * 16;

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                double noise = generator.noise(worldX + x, worldZ + z, 1, 1, true);
                int height = (int) (noise * 40);
                height += 84;
                if (height > chunkData.getMaxHeight()) {
                    height = chunkData.getMaxHeight();
                }
                for (int y = chunkData.getMinHeight(); y < height; y++) {
                    chunkData.setBlock(x, y, z, material);
                }
            }
        }
    }

    @Override
    public void generateBedrock(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ, @NotNull ChunkGenerator.ChunkData chunkData) {
        if (chunkData.getMinHeight() == worldInfo.getMinHeight()) {
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    chunkData.setBlock(x, chunkData.getMinHeight(), z, Material.BEDROCK);
                }
            }
        }
    }

    @Override
    public boolean shouldGenerateMobs() {
        return true;
    }
}
