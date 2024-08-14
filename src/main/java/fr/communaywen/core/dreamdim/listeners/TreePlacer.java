package fr.communaywen.core.dreamdim.listeners;

import dev.lone.itemsadder.api.CustomBlock;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

import java.util.Random;

import static java.lang.Math.abs;

public class TreePlacer implements Listener {
    int MAX_Y = 200;
    int MIN_Y = 60;
    CustomBlock log = CustomBlock.getInstance("aywen:dream_log");
    CustomBlock leave = CustomBlock.getInstance("aywen:cloud");

    public void placeTree(World world, int x, int y, int z) {
        // j'écris mon code un peu comme de la merde donc si vous comprenez pas venez mp: gyro3630
        boolean hasTopInperfection = false;
        boolean hasBottomInperfection = false;
        Random rand = new Random();
        int height = rand.nextInt(6-3)+3;

        // Place les feuilles du base
        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                if (world.getBlockAt(x+i, y+height-2, z+j).getType().equals(Material.AIR)) {
                    leave.place(new Location(world, x+i, y+height-2, z+j));
                }

                if (world.getBlockAt(x+i, y+height-1, z+j).getType().equals(Material.AIR)) {
                    if (((abs(i) == 1 && abs(j) == 1)) && !hasTopInperfection) {
                        if (rand.nextDouble() < 0.5) {
                            hasTopInperfection = true;
                            continue;
                        }
                    }
                    leave.place(new Location(world, x+i, y+height-1, z+j));
                }
            }
        }

        // Place les feuilles du haut
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (world.getBlockAt(x+i, y+height, z+j).getType().equals(Material.AIR)) {
                    leave.place(new Location(world, x+i, y+height, z+j));
                }

                if (world.getBlockAt(x+i, y+height+1, z+j).getType().equals(Material.AIR)) {
                    if (((abs(i) == 1 && abs(j) == 1)) && !hasBottomInperfection) {
                        if (rand.nextDouble() < 0.5) {
                            hasBottomInperfection = true;
                            continue;
                        }
                    }
                    leave.place(new Location(world, x+i, y+height+1, z+j));
                }
            }
        }

        // Place les bûches
        for (int i = 0; i <= height; i++) {
            log.place(new Location(world, x,y+i,z));
        }
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChunkLoad(ChunkLoadEvent event) {
        Chunk chunk = event.getChunk();
        World world = event.getWorld();

        if (!event.isNewChunk()) { return; }
        if (!world.getName().equals("dreamworld")) { return; }

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = MAX_Y; y > MIN_Y; y--) {
                    if (chunk.getBlock(x, y, z).getType().equals(Material.BASALT)) {
                        int worldX = chunk.getX()*16 + x;
                        int worldZ = chunk.getZ()*16 + z;

                        placeTree(world, worldX, y, worldZ);
                    }
                }
            }
        }
    }
}
