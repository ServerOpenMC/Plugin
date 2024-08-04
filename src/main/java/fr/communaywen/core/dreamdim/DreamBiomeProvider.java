package fr.communaywen.core.dreamdim;

import org.bukkit.block.Biome;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DreamBiomeProvider extends BiomeProvider {
    private final List<Biome> biomes;

    public DreamBiomeProvider() {
        this.biomes = List.of(Biome.PLAINS);
    }

    @Override
    public @NotNull Biome getBiome(@NotNull WorldInfo worldInfo, int x, int y, int z) {
        // Simple biome generation based on coordinates
        int biomeIndex = Math.abs((x / 64 + z / 64) % biomes.size());
        return biomes.get(biomeIndex);
    }

    @Override
    public @NotNull List<Biome> getBiomes(@NotNull WorldInfo worldInfo) {
        return biomes;
    }
}