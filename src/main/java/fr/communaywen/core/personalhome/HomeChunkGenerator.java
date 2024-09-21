package fr.communaywen.core.personalhome;

import org.bukkit.Material;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class HomeChunkGenerator extends ChunkGenerator {

    private static final int PLATFORM_SIZE_CHUNKS = 2;
    private static final int PLATFORM_SPACING = 11;
    private static final int TOTAL_SPACING = PLATFORM_SPACING + PLATFORM_SIZE_CHUNKS;

    @Override
    public void generateNoise(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ, @NotNull ChunkData chunkData) {
        int gridX = Math.floorDiv(chunkX, TOTAL_SPACING);
        int localX = chunkX - gridX * TOTAL_SPACING;

        if (chunkX < 0) { return; }

        if (chunkZ != 1 && chunkZ != 0) {
            return;
        }

        if (localX >= 0 && localX < PLATFORM_SIZE_CHUNKS) {
            int platformY = 100;

            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    chunkData.setBlock(x, platformY, z, Material.STONE);
                }
            }
        }
    }
}