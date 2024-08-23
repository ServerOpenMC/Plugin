package fr.communaywen.core.dreamdim.populators;

import org.bukkit.Material;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.LimitedRegion;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class TreePopulator extends BlockPopulator {
    private static final double TREE_PROBABILITY = 0.50;

    @Override
    public void populate(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ, @NotNull LimitedRegion limitedRegion) {
        int x = random.nextInt(16) + chunkX * 16;
        int z = random.nextInt(16) + chunkZ * 16;
        int y = 319;
        while (limitedRegion.getType(x, y, z).isAir() && y > -64) y--;

        if (limitedRegion.getType(x, y, z).isSolid()) {
            if (random.nextDouble() < TREE_PROBABILITY) {
                limitedRegion.setType(x, y, z, Material.BASALT);
            }
        }
    }
}
