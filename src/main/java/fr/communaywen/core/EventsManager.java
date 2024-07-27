package fr.communaywen.core;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
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
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Random;

import net.md_5.bungee.api.ChatColor;

import java.util.*;

// Events initialement développés par Armibule: Terrifying Night

public class EventsManager implements Listener {
    private AywenCraftPlugin plugin;

    // Event Types
    private static final int TERRIFYING_NIGHT = 0;
    // private static final int DROUGHT = 1;    idée d'event

    private static final Map<String, Integer> events = Map.of(
        "terriftingNight", TERRIFYING_NIGHT//,
        //"drought", DROUGHT
    );
    private List<Integer> enabledEvents;

    private BukkitRunnable eventRunnable;

    //private final int interval;
    private long lastTerrifyingNight = 0;
    private final int terrifyingNightDuration = 3 * 60 * 20;    // in ticks (3 minutes)
    private final double terrifyingNightDurationMillis = terrifyingNightDuration / 20.0 * 1000.0;
    private final double terrifyingNightProbability = 0.25;

    public EventsManager(AywenCraftPlugin plugin, FileConfiguration config) {
        this.plugin = plugin;

        List<String> disabledEvents = (List<String>) config.getList("disabledEvents");

        if (disabledEvents == null) {
            disabledEvents = List.of();
        }

        enabledEvents = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : events.entrySet() ) {
        
            if ( !disabledEvents.contains(entry.getKey()) ) {

                enabledEvents.add(entry.getValue());
                
            }
        };

        if (enabledEvents.size() == 0) {
            return;
        }

        eventRunnable = new BukkitRunnable() {
            
            @Override
            public void run() {
                if (Bukkit.getOnlinePlayers().isEmpty()) return;
                
                World overworld = Bukkit.getWorlds().get(0);

                // every midnight: 1/4 chance but not twice in a row
                if (18000 <= overworld.getTime() && overworld.getTime() < 18100 && System.currentTimeMillis() - lastTerrifyingNight > 20*61000 && new Random().nextFloat() <= terrifyingNightProbability) {

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
                            terrifyingNightDuration,
                            0,
                            false,
                            false
                        ));

                        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).addModifier(
                            new AttributeModifier("terrifyingNight", -0.2, Operation.MULTIPLY_SCALAR_1)
                        );

                        Location location = player.getLocation();
                        World world = location.getWorld();

                        Chunk playerChunk = world.getChunkAt(location);


                        Entity[] entities = playerChunk.getEntities();

                        int monsterCounter = 0;

                        for (Entity entity : entities) {
                            if (entity instanceof LivingEntity) {

                                LivingEntity livingEntity = (LivingEntity) entity;

                                if (livingEntity instanceof Monster) {

                                    // full heal
                                    livingEntity.setHealth(livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());

                                    boostMonster(livingEntity, world);
                                    monsterCounter += 1;
                                }
                            }
                        }

                        int monsteInt;
                        EntityType monsterType;
                        Location monsterLocation;
                        LivingEntity monster;

                        // ensure there is at least 8 boosted monsters in chunk (can fail)
                        for (int i = monsterCounter ; i < 9 ; i++) {
                            Random rand = new Random();
                            monsteInt = rand.nextInt(3);
                            if (monsteInt == 0) {
                                monsterType = EntityType.ZOMBIE;
                            } else if (monsteInt == 1) {
                                monsterType = EntityType.SKELETON;
                            } else {
                                monsterType = EntityType.CREEPER;
                            }


                            monsterLocation = location.clone();
                            monsterLocation.add((rand.nextFloat()-0.5)*20.0, 0.0, (rand.nextFloat()-0.5)*20.0);

                            for (int j = 0 ; j < 20 ; j++) {
                                monsterLocation.add(0, 0, 1);
                                if (monsterLocation.getBlock().isPassable()) {
                                    monsterLocation.add(0, 0, -1);

                                    monster = (LivingEntity) world.spawnEntity(monsterLocation, monsterType);
                                    boostMonster(monster, world);

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
                    }.runTaskLater(plugin, terrifyingNightDuration);
                }
            }
        };

        // runs every 100 ticks
        eventRunnable.runTaskTimer(plugin, 0, 100);
    }

    private void boostMonster(LivingEntity livingEntity, World world) {
        livingEntity.addPotionEffect(new PotionEffect(
                PotionEffectType.SPEED,
                terrifyingNightDuration,
                1,
                false,
                false
            ));
        livingEntity.addPotionEffect(new PotionEffect(
            PotionEffectType.RESISTANCE,
            terrifyingNightDuration,
            1,
            false,
            true
        ));
        livingEntity.addPotionEffect(new PotionEffect(
            PotionEffectType.GLOWING,
            terrifyingNightDuration,
            1,
            false,
            false
        ));

        switch (livingEntity.getType()) {
            case EntityType.ZOMBIE:
                livingEntity.setMetadata("terrifyingNightTime", new FixedMetadataValue(plugin, System.currentTimeMillis()));
            
                ItemStack sword = new ItemStack(Material.IRON_SWORD);
                sword.addEnchantment(Enchantment.KNOCKBACK, 1);
                sword.addEnchantment(Enchantment.SHARPNESS, 1);

                livingEntity.getEquipment().setItemInMainHand(sword);

                ItemStack chestplate = new ItemStack(Material.IRON_CHESTPLATE);
                chestplate.addEnchantment(Enchantment.PROTECTION, 4);
                chestplate.addEnchantment(Enchantment.BLAST_PROTECTION, 4);

                livingEntity.getEquipment().setChestplate(chestplate, false);

                ItemStack helmet = new ItemStack(Material.IRON_HELMET);
                helmet.addEnchantment(Enchantment.PROTECTION, 4);
                helmet.addEnchantment(Enchantment.BLAST_PROTECTION, 4);

                livingEntity.getEquipment().setHelmet(helmet, false);
                break;

            case EntityType.SKELETON:

                livingEntity.setMetadata("terrifyingNightTime", new FixedMetadataValue(plugin, System.currentTimeMillis()));

                ItemStack bow = new ItemStack(Material.BOW);
                bow.addEnchantment(Enchantment.FLAME, 1);
                bow.addEnchantment(Enchantment.PUNCH, 1);

                livingEntity.getEquipment().setItemInMainHand(bow);

                chestplate = new ItemStack(Material.IRON_CHESTPLATE);
                chestplate.addEnchantment(Enchantment.PROTECTION, 4);
                chestplate.addEnchantment(Enchantment.BLAST_PROTECTION, 4);

                livingEntity.getEquipment().setChestplate(chestplate, false);

                helmet = new ItemStack(Material.IRON_HELMET);
                helmet.addEnchantment(Enchantment.PROTECTION, 4);
                helmet.addEnchantment(Enchantment.BLAST_PROTECTION, 4);

                livingEntity.getEquipment().setHelmet(helmet, false);
                break;

            case EntityType.CREEPER:

                livingEntity.setMetadata("terrifyingNightTime", new FixedMetadataValue(plugin, System.currentTimeMillis()));

                Creeper creeper = (Creeper) livingEntity;

                creeper.setExplosionRadius(6);
                creeper.setVisualFire(true);

                world.spawnEntity(livingEntity.getLocation(), EntityType.CREEPER);

                break;
        
            default:
                break;
        }
    }

    public boolean isInTerrifyingNight() {
        return System.currentTimeMillis() - lastTerrifyingNight < terrifyingNightDurationMillis;
    }

    private void clearTerrifyingNight(Player player) {        
        AttributeInstance attribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);

        for (AttributeModifier modifier : attribute.getModifiers()) {
            if (modifier.getName().equals("terrifyingNight")) {
                attribute.removeModifier(modifier);
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Bukkit.broadcastMessage("PLAYER JOIN");
        
        clearTerrifyingNight(event.getPlayer());
        
        if (isInTerrifyingNight()) {
            event.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).addModifier(new AttributeModifier("terrifyingNight", -0.2, Operation.MULTIPLY_SCALAR_1));
            Bukkit.broadcastMessage("isInTerrifyingNight");
        }
    }


    @EventHandler
    public void onMobDeath(EntityDeathEvent event) {

        LivingEntity entity = event.getEntity();

        if (entity.hasMetadata("terrifyingNightTime")) {

            if (entity.getKiller() instanceof Player){

                if (entity.getMetadata("terrifyingNightTime").get(0).asLong() + terrifyingNightDurationMillis > System.currentTimeMillis()) {
                    
                    Random random = new Random();
                    float randomFloat = random.nextFloat();

                    final ItemStack item;

                    switch (entity.getType()) {
                        case EntityType.ZOMBIE:

                            item = new ItemStack(Material.ZOMBIE_HEAD);

                            // 30% lingots fer (1-3), 20% lingots or (1-3), 20% xp (+10-+30), 10% diamant, 10% émeraudes (2-5), 10% oeuf de zombie
                            if (randomFloat < 0.3) {
                                event.getDrops().add(new ItemStack(Material.IRON_INGOT, random.nextInt(2) + 1));
                            } else if (randomFloat < 0.5) {
                                event.getDrops().add(new ItemStack(Material.GOLD_INGOT, random.nextInt(2) + 1));
                            } else if (randomFloat < 0.7) {
                                event.setDroppedExp(event.getDroppedExp() + random.nextInt(21) + 10);
                            } else if (randomFloat < 0.8) {
                                event.getDrops().add(new ItemStack(Material.DIAMOND));
                            } else if (randomFloat < 0.9) {
                                event.getDrops().add(new ItemStack(Material.EMERALD, random.nextInt(4) + 2));
                            }  else if (randomFloat <= 1) {
                                event.getDrops().add(new ItemStack(Material.ZOMBIE_SPAWN_EGG));
                            }

                            break;

                        case EntityType.SKELETON:

                            item = new ItemStack(Material.SKELETON_SKULL);

                            // 30% lingots fer (1-3), 20% os (5-10), 20% xp (+10-+30), 10% flèches (10-15), 10% émeraudes (2-5), 10% oeuf de squelette
                            if (randomFloat < 0.3) {
                                event.getDrops().add(new ItemStack(Material.IRON_INGOT, random.nextInt(2) + 1));
                            } else if (randomFloat < 0.5) {
                                event.getDrops().add(new ItemStack(Material.BONE, random.nextInt(6) + 5));
                            } else if (randomFloat < 0.7) {
                                event.setDroppedExp(event.getDroppedExp() + random.nextInt(21) + 10);
                            } else if (randomFloat < 0.8) {
                                event.getDrops().add(new ItemStack(Material.ARROW, random.nextInt(6) + 10));
                            } else if (randomFloat < 0.9) {
                                event.getDrops().add(new ItemStack(Material.EMERALD, random.nextInt(4) + 2));
                            }  else if (randomFloat <= 1) {
                                event.getDrops().add(new ItemStack(Material.SKELETON_SPAWN_EGG));
                            }

                            break;

                        case EntityType.CREEPER:
                        
                            item = new ItemStack(Material.CREEPER_HEAD);
                        
                            // 30% poudres à canon (5-10), 30% fireworks (5-10), 20% tnt (3-5), 10% fire charge (3-8), 10% oeuf de creeper
                            if (randomFloat < 0.3) {
                                event.getDrops().add(new ItemStack(Material.GUNPOWDER, random.nextInt(6) + 5));
                            } else if (randomFloat < 0.6) {
                                event.getDrops().add(new ItemStack(Material.FIREWORK_ROCKET, random.nextInt(6) + 5));
                            } else if (randomFloat < 0.8) {
                                event.getDrops().add(new ItemStack(Material.TNT, random.nextInt(4) + 2));
                            } else if (randomFloat < 0.9) {
                                event.getDrops().add(new ItemStack(Material.FIRE_CHARGE, random.nextInt(6) + 3));
                            }  else if (randomFloat <= 1) {
                                event.getDrops().add(new ItemStack(Material.CREEPER_SPAWN_EGG));
                            }

                            break;
                    
                        default:
                            // Achievement unlocked: How did we get there ?
                            return;
                    }

                    if (item != null) {

                        entity.getKiller().playSound(entity.getLocation(), Sound.AMBIENT_CAVE, 1f, 1f);

                        ItemDisplay display = entity.getWorld().spawn(entity.getLocation().setDirection(new Vector(1, 0,  0)), ItemDisplay.class, spawned -> {
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
                        
                            display.setTransformationMatrix(matrix.translate(0f, 3f, 0f).rotateY(((float) Math.toRadians(270))).scale(0.0f));
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
    }

    public void close() {
        eventRunnable.cancel();
    }
}
