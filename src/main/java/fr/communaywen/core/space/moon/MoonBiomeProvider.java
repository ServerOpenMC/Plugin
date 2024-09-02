package fr.communaywen.core.space.moon;

import org.bukkit.block.Biome;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.WorldInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MoonBiomeProvider extends BiomeProvider {
    private final PerlinNoiseGenerator noiseGenerator;
    private final List<Biome> biomes;
    private final int octaves = 4;
    private final double scale = 0.0025; // Reduced scale for smoother transitions

    public MoonBiomeProvider(long seed) {
        this.noiseGenerator = new PerlinNoiseGenerator(new Random(seed));
        this.biomes = new ArrayList<>();
        this.biomes.add(Biome.PLAINS);
    }


    @Override
    public Biome getBiome(WorldInfo worldInfo, int x, int y, int z) {
        double noise = 0;
        double amplitude = 1;
        double frequency = 1;
        double maxValue = 0;

        for (int i = 0; i < octaves; i++) {
            noise += noiseGenerator.noise(x * scale * frequency, z * scale * frequency) * amplitude;
            maxValue += amplitude;
            amplitude *= 0.5;
            frequency *= 2;
        }

        // Normalize the noise value
        noise = noise / maxValue;

        int biomeIndex = (int) ((noise + 1) * biomes.size() / 2) % biomes.size();
        return biomes.get(biomeIndex);
    }

    @Override
    public List<Biome> getBiomes(WorldInfo worldInfo) {
        return biomes;
    }

    private static class PerlinNoiseGenerator {
        private final Random random;
        private final int[] permutation;

        public PerlinNoiseGenerator(Random random) {
            this.random = random;
            this.permutation = new int[512];
            for (int i = 0; i < 256; i++) {
                permutation[i] = i;
            }
            for (int i = 0; i < 256; i++) {
                int j = random.nextInt(256 - i) + i;
                int temp = permutation[i];
                permutation[i] = permutation[j];
                permutation[j] = temp;
                permutation[i + 256] = permutation[i];
            }
        }

        public double noise(double x, double z) {
            int X = (int) Math.floor(x) & 255;
            int Z = (int) Math.floor(z) & 255;
            x -= Math.floor(x);
            z -= Math.floor(z);
            double u = fade(x);
            double w = fade(z);
            int A = permutation[X] + Z;
            int B = permutation[X + 1] + Z;
            return lerp(w, lerp(u, grad(permutation[A], x, z),
                            grad(permutation[B], x - 1, z)),
                    lerp(u, grad(permutation[A + 1], x, z - 1),
                            grad(permutation[B + 1], x - 1, z - 1)));
        }

        private double fade(double t) {
            return t * t * t * (t * (t * 6 - 15) + 10);
        }

        private double lerp(double t, double a, double b) {
            return a + t * (b - a);
        }

        private double grad(int hash, double x, double z) {
            int h = hash & 15;
            double u = h < 8 ? x : z;
            double v = h < 4 ? z : (h == 12 || h == 14 ? x : 0);
            return ((h & 1) == 0 ? u : -u) + ((h & 2) == 0 ? v : -v);
        }
    }
}