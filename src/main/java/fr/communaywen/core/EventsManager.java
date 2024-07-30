package fr.communaywen.core;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.joml.Matrix4f;

import net.md_5.bungee.api.ChatColor;

import java.util.*;
import java.io.File;

// Events initialement développés par Armibule: Terrifying Night

public class EventsManager implements Listener {
    // Event Types
    private final AywenCraftPlugin plugin;
    private static final int TERRIFYING_NIGHT = 0;
    // private static final int DROUGHT = 1;    idée d'event

    private static final Map<String, Integer> EVENTS = new HashMap<String, Integer>() {{
        put("terrifingNight", TERRIFYING_NIGHT);
        // put("drought", DROUGHT);
    }};
    
    private final List<Integer> enabledEvents;
    private BukkitRunnable eventRunnable;

    private long lastTerrifyingNight = 0;
    private static final int TERRIFYING_NIGHT_DURATION = 3 * 60 * 20;    // in ticks (3 minutes)
    private static final double TERRIFYING_NIGHT_DURATION_MILLIS = TERRIFYING_NIGHT_DURATION / 20.0 * 1000.0;
    private static final double TERRIFYING_NIGHT_PROBABILITY = 0.25;

    public EventsManager(AywenCraftPlugin plugin, FileConfiguration config) {
        this.plugin = plugin;
        List<String> disabledEvents = (List<String>) config.getList("disabledEvents");
        if (disabledEvents == null) {
            disabledEvents = Arrays.asList(); // Empty list if null
        }

        enabledEvents = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : EVENTS.entrySet()) {
            if (!disabledEvents.contains(entry.getKey())) {
                enabledEvents.add(entry.getValue());
            }
        }

        if (enabledEvents.isEmpty()) return;

        eventRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                if (Bukkit.getOnlinePlayers().isEmpty()) return;

                World overworld = Bukkit.getWorlds().get(0);

                // every midnight: 1/4 chance but not twice in a row
                if (18000 <= overworld.getTime() && overworld.getTime() < 18100 &&
                    System.currentTimeMillis() - lastTerrifyingNight > 20 * 61000 &&
                    new Random().nextFloat() <= TERRIFYING_NIGHT_PROBABILITY) {

                    lastTerrifyingNight = System.currentTimeMillis();

                    System.out.println("\n\nEvent started\n\n");

                    Bukkit.broadcastMessage(ChatColor.RED + "Nouvel évènement en approche: " + ChatColor.BOLD + "Nuit Terrifiante\n" +
                                            ChatColor.RESET + ChatColor.GOLD + "Les monstres sont plus puissants, faites attention !");

                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.sendTitle("évènement", "Nuit Terrifiante", 10, 40, 20);
                        player.playSound(player.getEyeLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1, 1);
                        player.playSound(player.getEyeLocation(), Sound.AMBIENT_CAVE, 1, 1);

                        player.addPotionEffect(new PotionEffect(
                            PotionEffectType.WEAKNESS,
                            TERRIFYING_NIGHT_DURATION,
                            0,
                            false,
                            false
                        ));

                        AttributeInstance attribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
                        if (attribute != null) {
                            attribute.addModifier(new AttributeModifier("terrifyingNight", -0.2, Operation.MULTIPLY_SCALAR_1));
                        }

                        Location location = player.getLocation();
                        Chunk playerChunk = location.getWorld().getChunkAt(location);

                        int monsterCounter = 0;

                        for (Entity entity : playerChunk.getEntities()) {
                            if (entity instanceof LivingEntity && entity instanceof Monster) {
                                LivingEntity livingEntity = (LivingEntity) entity;
                                livingEntity.setHealth(livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
                                boostMonster(livingEntity, location.getWorld());
                                monsterCounter++;
                            }
                        }

                        Random rand = new Random();
                        for (int i = monsterCounter; i < 9; i++) {
                            EntityType monsterType = switch (rand.nextInt(3)) {
                                case 0 -> EntityType.ZOMBIE;
                                case 1 -> EntityType.SKELETON;
                                default -> EntityType.CREEPER;
                            };

                            Location monsterLocation = location.clone();
                            monsterLocation.add((rand.nextFloat() - 0.5) * 20.0, 0.0, (rand.nextFloat() - 0.5) * 20.0);

                            for (int j = 0; j < 20; j++) {
                                monsterLocation.add(0, 0, 1);
                                if (monsterLocation.getBlock().isPassable()) {
                                    monsterLocation.add(0, 0, -1);
                                    LivingEntity monster = (LivingEntity) location.getWorld().spawnEntity(monsterLocation, monsterType);
                                    boostMonster(monster, location.getWorld());
                                    break;
                                }
                            }
                        }
                    }

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                clearTerrifyingNight(player);
                                player.playSound(player.getEyeLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1, 1);
                            }
                            Bukkit.broadcastMessage(ChatColor.GREEN + "Fin de l'évènement Nuit Terrifiante !");
                        }
                    }.runTaskLater(plugin, TERRIFYING_NIGHT_DURATION);
                }
            }
        };
        // runs every 100 ticks
        eventRunnable.runTaskTimer(plugin, 0, 100);
    }

    private void boostMonster(LivingEntity livingEntity, World world) {
        livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, TERRIFYING_NIGHT_DURATION, 1, false, false));
        livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, TERRIFYING_NIGHT_DURATION, 1, false, true));
        livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, TERRIFYING_NIGHT_DURATION, 1, false, false));

        switch (livingEntity.getType()) {
            case ZOMBIE -> {
                livingEntity.setMetadata("terrifyingNightTime", new FixedMetadataValue(plugin, System.currentTimeMillis()));

                ItemStack sword = new ItemStack(Material.IRON_SWORD);
                sword.addEnchantment(Enchantment.KNOCKBACK, 1);
                sword.addEnchantment(Enchantment.SHARPNESS, 1);
                livingEntity.getEquipment().setItemInMainHand(sword);

                ItemStack chestplate = new ItemStack(Material.IRON_CHESTPLATE);
                chestplate.addEnchantment(Enchantment.PROTECTION, 4);
                chestplate.addEnchantment(Enchantment.BLAST_PROTECTION, 4);
                livingEntity.getEquipment().setChestplate(chestplate);

                ItemStack helmet = new ItemStack(Material.IRON_HELMET);
                helmet.addEnchantment(Enchantment.PROTECTION, 4);
                helmet.addEnchantment(Enchantment.BLAST_PROTECTION, 4);
                livingEntity.getEquipment().setHelmet(helmet);
            }
            case SKELETON -> {
                livingEntity.setMetadata("terrifyingNightTime", new FixedMetadataValue(plugin, System.currentTimeMillis()));

                ItemStack bow = new ItemStack(Material.BOW);
                bow.addEnchantment(Enchantment.FLAME, 1);
                bow.addEnchantment(Enchantment.PUNCH, 1);
                livingEntity.getEquipment().setItemInMainHand(bow);

                ItemStack chestplate = new ItemStack(Material.IRON_CHESTPLATE);
                chestplate.addEnchantment(Enchantment.PROTECTION, 4);
                chestplate.addEnchantment(Enchantment.BLAST_PROTECTION, 4);
                livingEntity.getEquipment().setChestplate(chestplate);

                ItemStack helmet = new ItemStack(Material.IRON_HELMET);
                helmet.addEnchantment(Enchantment.PROTECTION, 4);
                helmet.addEnchantment(Enchantment.BLAST_PROTECTION, 4);
                livingEntity.getEquipment().setHelmet(helmet);
            }
            case CREEPER -> {
                livingEntity.setMetadata("terrifyingNightTime", new FixedMetadataValue(plugin, System.currentTimeMillis()));

                Creeper creeper = (Creeper) livingEntity;
                creeper.setExplosionRadius(6);
                creeper.setVisualFire(true);

                world.spawnEntity(livingEntity.getLocation(), EntityType.CREEPER);
            }
            default -> {}
        }
    }

    public boolean isInTerrifyingNight() {
        return System.currentTimeMillis() - lastTerrifyingNight < TERRIFYING_NIGHT_DURATION_MILLIS;
    }

    private void clearTerrifyingNight(Player player) {
        AttributeInstance attribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (attribute != null) {
            for (AttributeModifier modifier : attribute.getModifiers()) {
                if (modifier.getName().equals("terrifyingNight")) {
                    attribute.removeModifier(modifier);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        clearTerrifyingNight(event.getPlayer());

        if (isInTerrifyingNight()) {
            AttributeInstance attribute = event.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH);
            if (attribute != null) {
                attribute.addModifier(new AttributeModifier("terrifyingNight", -0.2, Operation.MULTIPLY_SCALAR_1));
            }
            Bukkit.broadcastMessage("isInTerrifyingNight");
        }
    }

    @EventHandler
    public void onMobDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();

        if (entity.hasMetadata("terrifyingNightTime") && entity.getKiller() instanceof Player killer) {
            if (entity.getMetadata("terrifyingNightTime").get(0).asLong() + TERRIFYING_NIGHT_DURATION_MILLIS > System.currentTimeMillis()) {
                Random random = new Random();
                float randomFloat = random.nextFloat();
                ItemStack item = switch (entity.getType()) {
                    case ZOMBIE -> new ItemStack(Material.ZOMBIE_HEAD);
                    case SKELETON -> new ItemStack(Material.SKELETON_SKULL);
                    case CREEPER -> new ItemStack(Material.CREEPER_HEAD);
                    default -> null;
                };

                if (item != null) {
                    switch (entity.getType()) {
                        case ZOMBIE -> {
                            if (randomFloat < 0.3) {
                                event.getDrops().add(new ItemStack(Material.IRON_INGOT, random.nextInt(4) + 1));
                            } else if (randomFloat < 0.5) {
                                event.getDrops().add(new ItemStack(Material.GOLD_INGOT, random.nextInt(4) + 1));
                            } else if (randomFloat < 0.7) {
                                event.setDroppedExp(event.getDroppedExp() + random.nextInt(21) + 10);
                            } else if (randomFloat < 0.85) {
                                event.getDrops().add(new ItemStack(Material.EMERALD, random.nextInt(4) + 2));
                            } else if (randomFloat < 0.95) {
                                event.getDrops().add(new ItemStack(Material.DIAMOND));
                            } else {
                                event.getDrops().add(new ItemStack(Material.ZOMBIE_SPAWN_EGG));
                            }
                        }
                        case SKELETON -> {
                            if (randomFloat < 0.3) {
                                event.getDrops().add(new ItemStack(Material.IRON_INGOT, random.nextInt(5) + 1));
                            } else if (randomFloat < 0.5) {
                                event.getDrops().add(new ItemStack(Material.BONE, random.nextInt(6) + 5));
                            } else if (randomFloat < 0.7) {
                                event.setDroppedExp(event.getDroppedExp() + random.nextInt(21) + 10);
                            } else if (randomFloat < 0.85) {
                                event.getDrops().add(new ItemStack(Material.ARROW, random.nextInt(6) + 10));
                            } else if (randomFloat < 0.95) {
                                event.getDrops().add(new ItemStack(Material.EMERALD, random.nextInt(4) + 2));
                            } else {
                                event.getDrops().add(new ItemStack(Material.SKELETON_SPAWN_EGG));
                            }
                        }
                        case CREEPER -> {
                            if (randomFloat < 0.3) {
                                event.getDrops().add(new ItemStack(Material.GUNPOWDER, random.nextInt(7) + 6));
                            } else if (randomFloat < 0.6) {
                                event.getDrops().add(new ItemStack(Material.FIREWORK_ROCKET, random.nextInt(6) + 5));
                            } else if (randomFloat < 0.8) {
                                event.getDrops().add(new ItemStack(Material.TNT, random.nextInt(4) + 2));
                            } else if (randomFloat < 0.95) {
                                event.getDrops().add(new ItemStack(Material.FIRE_CHARGE, random.nextInt(6) + 3));
                            } else {
                                event.getDrops().add(new ItemStack(Material.CREEPER_SPAWN_EGG));
                            }
                        }
                        default -> {}
                    }

                    killer.playSound(entity.getLocation(), Sound.AMBIENT_CAVE, 1f, 1f);

                    ItemDisplay display = entity.getWorld().spawn(entity.getLocation().setDirection(new Vector(1, 0, 0)), ItemDisplay.class, spawned -> {
                        spawned.setItemStack(item);
                        spawned.setPersistent(false);
                    });

                    int animationDuration = 3 * 20;
                    Matrix4f matrix = new Matrix4f().scale(0.5f);

                    Bukkit.getScheduler().runTaskTimer(plugin, task -> {
                        if (!display.isValid()) {
                            task.cancel();
                            return;
                        }

                        display.setTransformationMatrix(matrix.translate(0f, 3f, 0f).rotateY((float) Math.toRadians(270)).scale(0.0f));
                        display.setInterpolationDelay(0);
                        display.setInterpolationDuration(animationDuration);
                    }, 1, animationDuration);

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            display.remove();
                        }
                    }.runTaskLater(plugin, animationDuration);
                }
            }
        }
    }

    public void close() {
        eventRunnable.cancel();
    }
}
