package fr.communaywen.core.dreamdim;

import fr.communaywen.core.utils.FastNoiseLite;
import org.bukkit.Material;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class DreamChunkGenerator extends ChunkGenerator {
    /* https://www.spigotmc.org/threads/545616/ */
    private final FastNoiseLite terrainNoise = new FastNoiseLite();
    private final FastNoiseLite detailNoise = new FastNoiseLite();

    private final HashMap<Integer, List<Material>> layers = new HashMap<Integer, List<Material>>() {{
        put(0, Arrays.asList(Material.SCULK));
        put(1, Arrays.asList(Material.MUD));
        put(2, Arrays.asList(Material.DEEPSLATE_COAL_ORE, Material.DEEPSLATE_COPPER_ORE, Material.CRYING_OBSIDIAN));
        put(3, Arrays.asList(Material.BEDROCK));
    }};

    @Override
    public boolean shouldGenerateMobs(){
        return true;
    }

    public DreamChunkGenerator() {
        // Set frequencies
        terrainNoise.SetFrequency(0.001f);
        detailNoise.SetFrequency(0.05f);

        // Add fractals
        terrainNoise.SetFractalType(FastNoiseLite.FractalType.FBm);
        terrainNoise.SetFractalOctaves(5);
    }

    @Override
    public void generateNoise(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ, ChunkData chunkData) {
        for(int y = chunkData.getMinHeight(); y < 130 && y < chunkData.getMaxHeight(); y++) {
            for(int x = 0; x < 16; x++) {
                for(int z = 0; z < 16; z++) {
                    float noise2 = (terrainNoise.GetNoise(x + (chunkX * 16), z + (chunkZ * 16)) * 2) + (detailNoise.GetNoise(x + (chunkX * 16), z + (chunkZ * 16)) / 10);
                    float noise3 = detailNoise.GetNoise(x + (chunkX * 16), y, z + (chunkZ * 16));
                    float currentY = (65 + (noise2 * 30));

                    if(y < -61) {
                        chunkData.setBlock(x, y, z, layers.get(3).get(random.nextInt(layers.get(3).size())));
                    }
                    else if (y < -60) {
                        if (random.nextBoolean()){
                            chunkData.setBlock(x, y, z, layers.get(3).get(random.nextInt(layers.get(3).size())));
                        } else {
                            chunkData.setBlock(x, y, z, Material.DEEPSLATE);
                        }
                    }
                    else if(y < currentY) {
                        float distanceToSurface = Math.abs(y - currentY); // The absolute y distance to the world surface.
                        double function = .1 * Math.pow(distanceToSurface, 2) - 1; // A second grade polynomial offset to the noise max and min (1, -1).

                        if(noise3 > Math.min(function, -.3)) {
                            // Set sculk if the block closest to the surface.
                            if(distanceToSurface < 3 && y > 63) {
                                chunkData.setBlock(x, y, z, layers.get(0).getFirst());
                            }

                            // It is not the closest block to the surface but still very close.
                            else if(distanceToSurface < 5) {
                                chunkData.setBlock(x, y, z, layers.get(1).get(random.nextInt(layers.get(1).size())));
                            }

                            // Not close to the surface at all.
                            else {
                                Material neighbour = Material.DEEPSLATE;
                                List<Material> neighbourBlocks = new ArrayList<Material>(Arrays.asList(chunkData.getType(Math.max(x - 1, 0), y, z), chunkData.getType(x, Math.max(y - 1, 0), z), chunkData.getType(x, y, Math.max(z - 1, 0)))); // A list of all neighbour blocks.

                                // Randomly place vein anchors.
                                if(random.nextFloat() < 0.01) {
                                    if (random.nextFloat() < 0.025) {
                                        neighbour = Material.ANCIENT_DEBRIS;
                                    } else {
                                        neighbour = layers.get(2).get(Math.min(random.nextInt(layers.get(2).size()), random.nextInt(layers.get(2).size()))); // A basic way to shift probability to lower values.
                                    }
                                }

                                // If the current block has an ore block as neighbour, try the current block.
                                if((!Collections.disjoint(neighbourBlocks, layers.get(2)))) {
                                    for (Material neighbourBlock : neighbourBlocks) {
                                        if (layers.get(2).contains(neighbourBlock) && random.nextFloat() < -0.01 * layers.get(2).indexOf(neighbourBlock) + 0.4) {
                                            neighbour = neighbourBlock;
                                        }
                                    }
                                }

                                chunkData.setBlock(x, y, z, neighbour);
                            }
                        }
                    }
                    else if(y < 65) {
                        chunkData.setBlock(x, y, z, Material.WATER);
                    }
                }
            }
        }
    }
}