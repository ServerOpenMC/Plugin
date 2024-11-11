package fr.communaywen.core.event;

import fr.communaywen.core.AywenCraftPlugin;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import static fr.communaywen.core.event.CubeListener.isCubeBlock;

public class CubeManager {

    private static AywenCraftPlugin plugin;
    public static Location currentLocation;
    public static int CUBE_SIZE = 5;
    public static Material CUBE_MATERIAL = Material.LAPIS_BLOCK;
    private final int MOVE_DELAY = 18000; // 15 minutes
    private final Material CORRUPTION_MATERIAL = Material.WARPED_NYLIUM;
    private final int CORRUPTION_RADIUS = 15;
    private final Random random = new Random();
    private BossBar bossBar;

    public CubeManager(AywenCraftPlugin plugin) {
        this.plugin = plugin;
        World world = Bukkit.getWorld("world");
        if (world == null) return;

        int startX = plugin.getManagers().getKevinConfig().getInt("posX", -126);
        int startZ = plugin.getManagers().getKevinConfig().getInt("posZ", -87);
        int startY = world.getHighestBlockYAt(startX, startZ);

        currentLocation = new Location(world, startX, startY, startZ);

        bossBar = Bukkit.createBossBar("Cube d'OpenMC", BarColor.BLUE, BarStyle.SOLID, BarFlag.CREATE_FOG, BarFlag.DARKEN_SKY);
        bossBar.setVisible(true);

        new BukkitRunnable() {

            @Override
            public void run() {
                moveCube();
            }
        }.runTaskTimer(plugin, 0, MOVE_DELAY);

        startBossBarUpdater();

        startMobSpawnTask();
    }

    private void startBossBarUpdater() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.getWorld().equals(currentLocation.getWorld())) {
                        double distance = player.getLocation().distance(currentLocation);

                        if (distance <= 100) {
                            if (!bossBar.getPlayers().contains(player)) {
                                bossBar.addPlayer(player);
                            }
                        } else {
                            bossBar.removePlayer(player);
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0, 20);
    }

    private void startMobSpawnTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                spawnMobsAroundCube();
            }
        }.runTaskTimer(plugin, 0, 6000); // 6000 ticks = 5 minutes
    }

    private void spawnMobsAroundCube() {
        World world = currentLocation.getWorld();
        int baseX = currentLocation.getBlockX();
        int baseY = currentLocation.getBlockY();
        int baseZ = currentLocation.getBlockZ();

        int mobsToSpawn = random.nextInt(5) + 15;

        for (int i = 0; i < mobsToSpawn; i++) {
            int x = baseX + random.nextInt(CORRUPTION_RADIUS * 2) - CORRUPTION_RADIUS;
            int z = baseZ + random.nextInt(CORRUPTION_RADIUS * 2) - CORRUPTION_RADIUS;
            int y = world.getHighestBlockYAt(x, z);

            Location spawnLocation = new Location(world, x, y, z);

            if (spawnLocation.distance(currentLocation) <= CORRUPTION_RADIUS) {
                EntityType mobType = random.nextBoolean() ? EntityType.ZOMBIE : EntityType.SKELETON;
                LivingEntity entity = (LivingEntity) world.spawnEntity(spawnLocation, mobType);

                entity.getEquipment().setHelmet(new ItemStack(Material.LAPIS_BLOCK));
                entity.getEquipment().setHelmetDropChance(0);

                entity.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, Integer.MAX_VALUE, 1));
                entity.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, Integer.MAX_VALUE, 1));
                entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
                entity.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0));

                entity.getEquipment().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
                entity.getEquipment().setChestplateDropChance(0);
                entity.getEquipment().setBoots(new ItemStack(Material.IRON_BOOTS));
                entity.getEquipment().setBootsDropChance(0);

                if (mobType == EntityType.SKELETON) {
                    Skeleton skeleton = (Skeleton) entity;
                    skeleton.getEquipment().setItemInMainHand(new ItemStack(Material.BOW));
                    skeleton.getEquipment().setItemInMainHandDropChance(0);
                }
            }
        }
    }

    private void createCube(Location location) {
        for (int x = 0; x < CUBE_SIZE; x++) {
            for (int y = 0; y < CUBE_SIZE; y++) {
                for (int z = 0; z < CUBE_SIZE; z++) {
                    location.clone().add(x, y, z).getBlock().setType(CUBE_MATERIAL);
                }
            }
        }
    }

    private void clearCube(Location location) {
        for (int x = 0; x < CUBE_SIZE; x++) {
            for (int y = 0; y < CUBE_SIZE; y++) {
                for (int z = 0; z < CUBE_SIZE; z++) {
                    location.clone().add(x, y, z).getBlock().setType(Material.AIR);
                }
            }
        }
    }

    private int lastDirection = -1;

    private void moveCube() {
        clearCube(currentLocation);

        int direction;
        do {
            direction = random.nextInt(4);
        } while ((lastDirection == 0 && direction == 1) || (lastDirection == 1 && direction == 0) ||
                (lastDirection == 2 && direction == 3) || (lastDirection == 3 && direction == 2));

        lastDirection = direction;

        int newX = currentLocation.getBlockX();
        int newZ = currentLocation.getBlockZ();

        switch (direction) {
            case 0 -> newX += CUBE_SIZE;
            case 1 -> newX -= CUBE_SIZE;
            case 2 -> newZ += CUBE_SIZE;
            case 3 -> newZ -= CUBE_SIZE;
        }

        World world = currentLocation.getWorld();
        int newY = world.getHighestBlockYAt(newX, newZ);

        currentLocation.setX(newX);
        currentLocation.setY(newY+1);
        currentLocation.setZ(newZ);

        world.playSound(currentLocation, Sound.BLOCK_BEACON_ACTIVATE, 100, 1.0f);

        Bukkit.broadcast(Component.text("§9Le Cube §7s'est déplacé en §f" + currentLocation.x() + " " + currentLocation.y() + " " + currentLocation.z()));
        createCube(currentLocation);
        corruptAreaAroundCube();
    }

    private void corruptAreaAroundCube() {
        World world = currentLocation.getWorld();
        int baseX = currentLocation.getBlockX();
        int baseY = currentLocation.getBlockY();
        int baseZ = currentLocation.getBlockZ();

        int corruptionStartX = baseX - CORRUPTION_RADIUS;
        int corruptionEndX = baseX + CUBE_SIZE + CORRUPTION_RADIUS;
        int corruptionStartZ = baseZ - CORRUPTION_RADIUS;
        int corruptionEndZ = baseZ + CUBE_SIZE + CORRUPTION_RADIUS;

        for (int x = corruptionStartX; x < corruptionEndX; x++) {
            for (int z = corruptionStartZ; z < corruptionEndZ; z++) {
                for (int yOffset = -1; yOffset <= 5; yOffset++) {
                    Location targetLocation = new Location(world, x, baseY + yOffset, z);

                    if (targetLocation.getBlock().getType().isSolid() && !isCubeBlock(targetLocation) && random.nextDouble() < 0.4) {
                        targetLocation.getBlock().setType(CORRUPTION_MATERIAL);

                        if (targetLocation.getBlock().getType() == CORRUPTION_MATERIAL) {
                            applyBoneMealEffect(targetLocation);
                        }
                    }
                }
            }
        }
    }

    private void applyBoneMealEffect(Location location) {
        Block block = location.getBlock();


        if (block.getType() == Material.WARPED_NYLIUM || block.getType() == Material.CRIMSON_NYLIUM) {
            block.applyBoneMeal(BlockFace.UP);
        }
    }

    public static void clearCube() {
        for (int x = 0; x < CUBE_SIZE; x++) {
            for (int y = 0; y < CUBE_SIZE; y++) {
                for (int z = 0; z < CUBE_SIZE; z++) {
                    currentLocation.clone().add(x, y, z).getBlock().setType(Material.AIR);
                }
            }
        }
    }

    public static void saveCubeLocation() {
        plugin.getManagers().getKevinConfig().set("posX", currentLocation.getBlockX());
        plugin.getManagers().getKevinConfig().set("posY", currentLocation.getBlockY());
        plugin.getManagers().getKevinConfig().set("posZ", currentLocation.getBlockZ());
        try {
            plugin.getManagers().getKevinConfig().save(new File(plugin.getDataFolder(), "cube.yml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
