package fr.communaywen.core;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
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
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Item;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.LootGenerateEvent;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.components.FoodComponent;
import org.bukkit.loot.LootContext;
import org.bukkit.loot.LootTables;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Random;

import dev.lone.itemsadder.api.CustomStack;
import fr.communaywen.core.commands.randomEvents.EventsDifficulties;
import fr.communaywen.core.commands.randomEvents.RandomEventsData;
import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.*;

// Events développés par Armibule: Nuit terrifiante, Pêche miraculeuse

@Credit("Armibule")
@Feature("RandomEvents")
public class EventsManager implements Listener {
    private AywenCraftPlugin plugin;

    // Event Types
    public static final int TERRIFYING_NIGHT = 0;
    public static final int MIRACULOUS_FISHING = 1;

    private static final Map<String, Integer> events = Map.of(
        "terriftingNight", TERRIFYING_NIGHT,
        "miraculousFishing", MIRACULOUS_FISHING
    );
    public List<Integer> enabledEvents;

    private BukkitRunnable eventRunnable;

    private boolean isRunning = false;

    private long lastTerrifyingNight = 0;
    private final int terrifyingNightDuration = 3 * 60 * 20;    // in ticks (3 minutes)
    private final double terrifyingNightDurationMillis = terrifyingNightDuration / 20.0 * 1000.0;
    private double terrifyingNightProbability;
    private final NamespacedKey terrifyingNightKey;

    private long lastMiraculousFishing = 0;
    private final int miraculousFishingDuration = 3 * 60 * 20;    // in ticks (3 minutes)
    private final double miraculousFishingDurationMillis = miraculousFishingDuration / 20.0 * 1000.0;
    private double miraculousFishingProbability;

    public EventsManager(AywenCraftPlugin plugin, FileConfiguration config) {
        this.plugin = plugin;

        terrifyingNightKey = new NamespacedKey(plugin, "terrifying_night");

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

        terrifyingNightProbability = config.getDouble("terrifyingNightProbability");
        miraculousFishingProbability = config.getDouble("miraculousFishingProbability");

        RandomEventsData.loadData();

        start();
    }

    // returns true if succeeded
    public boolean startTerrifyingNight() {
        if (isInTerrifyingNight()) {
            return false;
        }

        lastTerrifyingNight = System.currentTimeMillis();
                            
        System.out.println("\n\nEvent started\n\n");
        
        Bukkit.broadcastMessage(ChatColor.RED + "\nNouvel évènement en approche: " + ChatColor.BOLD + "Nuit Terrifiante\n" + 
                                ChatColor.RESET + ChatColor.GOLD + "Les monstres sont plus puissants, faites attention !\n");
        
        Integer difficulty;
        for (Player player : Bukkit.getOnlinePlayers()) {
            difficulty = RandomEventsData.getPlayerDifficulty(player);

            if (difficulty == EventsDifficulties.DISABLED) {
                continue;
            }

            player.sendTitle("Évènement", "Nuit Terrifiante", 10, 40, 20);
            player.playSound(player.getEyeLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1, 1);
            player.playSound(player.getEyeLocation(), Sound.AMBIENT_CAVE, 1, 1);
        
            if (difficulty != EventsDifficulties.EASY) {
                player.addPotionEffect(new PotionEffect(
                    PotionEffectType.WEAKNESS,
                    terrifyingNightDuration,
                    0,
                    false,
                    false
                ));
            }

            if (difficulty == EventsDifficulties.EASY) {
                player.getAttribute(Attribute.GENERIC_MAX_HEALTH).addModifier(
                    new AttributeModifier(terrifyingNightKey, -0.1, Operation.MULTIPLY_SCALAR_1)
                );
            } else if (difficulty == EventsDifficulties.NORMAL) {
                player.getAttribute(Attribute.GENERIC_MAX_HEALTH).addModifier(
                    new AttributeModifier(terrifyingNightKey, -0.2, Operation.MULTIPLY_SCALAR_1)
                );
            } else if (difficulty == EventsDifficulties.HARD) {
                player.getAttribute(Attribute.GENERIC_MAX_HEALTH).addModifier(
                    new AttributeModifier(terrifyingNightKey, -0.3, Operation.MULTIPLY_SCALAR_1)
                );

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.playSound(player.getEyeLocation(), Sound.ENTITY_WITHER_SPAWN, 1, 1);
                        player.sendTitle(ChatColor.RED + "Version", ChatColor.LIGHT_PURPLE + "Difficile", 10, 40, 20);
                    }
                }.runTaskLater(plugin, 60);
            }
        
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
                    
                        boostMonster(livingEntity, world, difficulty);
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
                        boostMonster(monster, world, difficulty);
                    
                        break;
                    }
                }
            }
        }
        
        // timer to the end of event
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    clearTerrifyingNight(player);
                    player.playSound(player.getEyeLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1, 1);
                }
                Bukkit.broadcastMessage(ChatColor.GREEN + "\nFin de l'évènement Nuit Terrifiante !\n");
            }
        }.runTaskLater(plugin, terrifyingNightDuration);

        return true;
    }

    // returns true if succeeded
    public boolean startMiraculousFishing() {
        if (isInMiraculousFishing()) {
            return false;
        }

        System.out.println("\n\nEvent started\n\n");
                            
        Bukkit.broadcastMessage(ChatColor.GREEN + "Nouvel évènement en approche: " + ChatColor.BOLD + "Pêche Miraculeuse\n" + 
                                ChatColor.RESET + ChatColor.GOLD + "Vous obtenez plus d'xp et de récompense en pêchat et en découvrant des coffres de structures");
        
        lastMiraculousFishing = System.currentTimeMillis();

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendTitle("Évènement", "Pêche Miraculeuse", 10, 40, 20);
            player.playSound(player.getEyeLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
            player.playSound(player.getEyeLocation(), Sound.ENTITY_FISH_SWIM, 1, 1);
        }

        // timer to the end of event
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.playSound(player.getEyeLocation(), Sound.BLOCK_AMETHYST_BLOCK_HIT, 1, 1);
                }
                Bukkit.broadcastMessage(ChatColor.GREEN + "\nFin de l'évènement Pêche Miraculeuse !\n");
            }
        }.runTaskLater(plugin, miraculousFishingDuration);

        return true;
    }

    // Les monstres pourront être rendus plus puissants avec la progression des joueurs
    private void boostMonster(LivingEntity livingEntity, World world, Integer difficulty) {
        if (difficulty == EventsDifficulties.EASY) {
            livingEntity.addPotionEffect(new PotionEffect(
                PotionEffectType.SPEED,
                terrifyingNightDuration,
                0,
                false,
                false
            ));
            livingEntity.addPotionEffect(new PotionEffect(
                PotionEffectType.RESISTANCE,
                terrifyingNightDuration,
                0,
                false,
                true
            ));
        } else if (difficulty == EventsDifficulties.NORMAL) {
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
                0,
                false,
                true
            ));
        } else if (difficulty == EventsDifficulties.NORMAL) {
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
        }
        livingEntity.addPotionEffect(new PotionEffect(
            PotionEffectType.GLOWING,
            terrifyingNightDuration,
            0,
            false,
            false
        ));

        switch (livingEntity.getType()) {
            case EntityType.ZOMBIE:
                livingEntity.setMetadata("terrifyingNightTime", new FixedMetadataValue(plugin, System.currentTimeMillis()));
            
                ItemStack sword;
                if (difficulty == EventsDifficulties.HARD) {
                    sword = new ItemStack(Material.DIAMOND_SWORD);
                } else {
                    sword = new ItemStack(Material.IRON_SWORD);
                }
                if (difficulty != EventsDifficulties.EASY) {
                    sword.addEnchantment(Enchantment.KNOCKBACK, 1);
                    sword.addEnchantment(Enchantment.SHARPNESS, 1);
                }

                livingEntity.getEquipment().setItemInMainHand(sword);

                ItemStack chestplate;
                if (difficulty == EventsDifficulties.EASY) {
                    chestplate = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
                } else if (difficulty == EventsDifficulties.NORMAL) {
                    chestplate = new ItemStack(Material.IRON_CHESTPLATE);
                } else {
                    chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);
                }
                
                chestplate.addEnchantment(Enchantment.PROTECTION, 4);
                chestplate.addEnchantment(Enchantment.BLAST_PROTECTION, 4);

                livingEntity.getEquipment().setChestplate(chestplate, false);

                ItemStack helmet;
                if (difficulty == EventsDifficulties.EASY) {
                    helmet = new ItemStack(Material.CHAINMAIL_HELMET);
                } else if (difficulty == EventsDifficulties.NORMAL) {
                    helmet = new ItemStack(Material.IRON_HELMET);
                } else {
                    helmet = new ItemStack(Material.DIAMOND_HELMET);
                }

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

                if (difficulty == EventsDifficulties.EASY) {
                    chestplate = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
                } else if (difficulty == EventsDifficulties.NORMAL) {
                    chestplate = new ItemStack(Material.IRON_CHESTPLATE);
                } else {
                    chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);
                }
                chestplate.addEnchantment(Enchantment.PROTECTION, 4);
                chestplate.addEnchantment(Enchantment.BLAST_PROTECTION, 4);

                livingEntity.getEquipment().setChestplate(chestplate, false);

                if (difficulty == EventsDifficulties.EASY) {
                    helmet = new ItemStack(Material.CHAINMAIL_HELMET);
                } else if (difficulty == EventsDifficulties.NORMAL) {
                    helmet = new ItemStack(Material.IRON_HELMET);
                } else {
                    helmet = new ItemStack(Material.DIAMOND_HELMET);
                }
                helmet.addEnchantment(Enchantment.PROTECTION, 4);
                helmet.addEnchantment(Enchantment.BLAST_PROTECTION, 4);

                livingEntity.getEquipment().setHelmet(helmet, false);
                break;

            case EntityType.CREEPER:

                livingEntity.setMetadata("terrifyingNightTime", new FixedMetadataValue(plugin, System.currentTimeMillis()));

                Creeper creeper = (Creeper) livingEntity;

                if (difficulty == EventsDifficulties.EASY) {
                    creeper.setExplosionRadius(3);
                } else if (difficulty == EventsDifficulties.NORMAL) {
                    creeper.setExplosionRadius(4);
                    creeper.setVisualFire(true);
                } else {
                    creeper.setExplosionRadius(5);
                    creeper.setVisualFire(true);
                }

                world.spawnEntity(livingEntity.getLocation(), EntityType.CREEPER);

                break;
        
            default:
                break;
        }
        livingEntity.getEquipment().setHelmetDropChance(0);
        livingEntity.getEquipment().setChestplateDropChance(0);
        livingEntity.getEquipment().setItemInMainHandDropChance(0);
    }

    public boolean isInTerrifyingNight() {
        return System.currentTimeMillis() - lastTerrifyingNight < terrifyingNightDurationMillis;
    }
    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event){
        clearTerrifyingNight(event.getPlayer());
    }
    private void clearTerrifyingNight(Player player) {
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).removeModifier(terrifyingNightKey);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {        
        clearTerrifyingNight(event.getPlayer());
        
        if (isInTerrifyingNight()) {
            event.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).addModifier(new AttributeModifier(terrifyingNightKey, -0.2, Operation.MULTIPLY_SCALAR_1));
        }
    }

    public boolean isInMiraculousFishing() {
        return System.currentTimeMillis() - lastMiraculousFishing < miraculousFishingDurationMillis;
    }

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        if (event.getPlayer().getWorld().getName().equals("dreamworld")) { return;}

        if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
            if (isInMiraculousFishing()) {
                /*
                 commun 65%, rare 25%, épique 7%, légendaire 3%
                 drops: 
                 - commun: 20% saumons (4-6), 20% poissons globes (2-3), 15% poisson tropicaux (2-4), 10% coquille de nautille
                 - rare:  6% coeur de la mer, 7% livre enchanté lvl 1, 6% potion commune, 6% golden apple
                 - épique: 2.3% bon livre enchanté, 2.3% bonne potion, 2.4% kebab fermenté/délicieux
                 - légendaire: 1% armure ancienne, 1% potion légendaire, 1% Relique du pain sacré
                 */
                float randomFloat = new Random().nextFloat();
                ItemStack item;
                int rarity;

                if (randomFloat < 0.2) {    // Common
                    item = new ItemStack(Material.SALMON, new Random().nextInt(3) + 4);
                    rarity = 0;
                } else if (randomFloat < 0.4) {
                    item = new ItemStack(Material.PUFFERFISH, new Random().nextInt(2) + 2);
                    rarity = 0;
                } else if (randomFloat < 0.55) {
                    item = new ItemStack(Material.TROPICAL_FISH, new Random().nextInt(3) + 2);
                    rarity = 0;
                } else if (randomFloat < 0.65) {
                    item = new ItemStack(Material.NAUTILUS_SHELL);
                    rarity = 0;
                } else if (randomFloat < 0.71) {     // Rare
                    item = new ItemStack(Material.HEART_OF_THE_SEA);
                    rarity = 1;
                } else if (randomFloat < 0.78) {
                    rarity = 1;

                    item = new ItemStack(Material.ENCHANTED_BOOK);

                    EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();

                    randomFloat = new Random().nextFloat();
                    if (randomFloat < 0.25) {
                        meta.addStoredEnchant(Enchantment.EFFICIENCY, 1, true);
                    } else if (randomFloat < 0.5) {
                        meta.addStoredEnchant(Enchantment.SHARPNESS, 1, true);
                    } else if (randomFloat < 0.75) {
                        meta.addStoredEnchant(Enchantment.PROTECTION, 1, true);
                    } else {
                        meta.addStoredEnchant(Enchantment.KNOCKBACK, 1, true);
                    }

                    item.setItemMeta(meta);
                } else if (randomFloat < 0.84) {
                    rarity = 1;
                    
                    item = new ItemStack(Material.POTION);

                    PotionMeta meta = (PotionMeta) item.getItemMeta();
                    // 1800 ticks = 1.5 minutes
                    randomFloat = new Random().nextFloat();
                    if (randomFloat < 0.25) {
                        meta.addCustomEffect(new PotionEffect(PotionEffectType.SPEED, 1200, 0), true);
                    } else if (randomFloat < 0.5) {
                        meta.addCustomEffect(new PotionEffect(PotionEffectType.STRENGTH, 1200, 0), true);
                    } else if (randomFloat < 0.75) {
                        meta.addCustomEffect(new PotionEffect(PotionEffectType.INSTANT_HEALTH, 1200, 0), true);
                    } else {
                        meta.addCustomEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, 1200, 0), true);
                    }

                    meta.setItemName("Potion de la mer");
                    meta.setRarity(ItemRarity.UNCOMMON);
                    meta.setColor(Color.fromRGB(38, 189, 212));
                    
                    item.setItemMeta(meta);
                } else if (randomFloat < 0.9) {
                    rarity = 1;

                    item = new ItemStack(Material.GOLDEN_APPLE);
                } else if (randomFloat < 0.923) {    // Epic
                    rarity = 2;

                    item = new ItemStack(Material.ENCHANTED_BOOK);

                    EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();

                    randomFloat = new Random().nextFloat();
                    if (randomFloat < 0.10) {
                        meta.addStoredEnchant(Enchantment.EFFICIENCY, 3, true);
                    } else if (randomFloat < 0.2) {
                        meta.addStoredEnchant(Enchantment.SHARPNESS, 3, true);
                    } else if (randomFloat < 0.3) {
                        meta.addStoredEnchant(Enchantment.PROTECTION, 3, true);
                    } else if (randomFloat < 0.4) {
                        meta.addStoredEnchant(Enchantment.KNOCKBACK, 3, true);
                    } else if (randomFloat < 0.5) {
                        meta.addStoredEnchant(Enchantment.FORTUNE, 2, true);
                    } else if (randomFloat < 0.6) {
                        meta.addStoredEnchant(Enchantment.LOOTING, 2, true);
                    } else if (randomFloat < 0.7) {
                        meta.addStoredEnchant(Enchantment.THORNS, 2, true);
                    } else if (randomFloat < 0.8) {
                        meta.addStoredEnchant(Enchantment.UNBREAKING, 3, true);
                    } else if (randomFloat < 0.9) {
                        meta.addStoredEnchant(Enchantment.FIRE_ASPECT, 1, true);
                    } else {
                        meta.addStoredEnchant(Enchantment.FLAME, 1, true);
                    }

                    item.setItemMeta(meta);
                } else if (randomFloat < 0.946) {
                    rarity = 2;

                    item = new ItemStack(Material.POTION);

                    PotionMeta meta = (PotionMeta) item.getItemMeta();
                    // 3600 ticks = 3 minutes
                    randomFloat = new Random().nextFloat();
                    if (randomFloat < 0.17) {
                        meta.addCustomEffect(new PotionEffect(PotionEffectType.SPEED, 3600, 1), true);
                    } else if (randomFloat < 0.34) {
                        meta.addCustomEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, 3600, 1), true);
                    } else if (randomFloat < 0.51) {
                        meta.addCustomEffect(new PotionEffect(PotionEffectType.RESISTANCE, 3600, 0), true);
                    } else if (randomFloat < 0.68) {
                        meta.addCustomEffect(new PotionEffect(PotionEffectType.STRENGTH, 3600, 1), true);
                    } else if (randomFloat < 0.85) {
                        meta.addCustomEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 3600, 0), true);
                    } else {
                        meta.addCustomEffect(new PotionEffect(PotionEffectType.REGENERATION, 3600, 0), true);
                    }

                    meta.setItemName("Potion de la mer épique");
                    meta.setRarity(ItemRarity.RARE);
                    meta.setColor(Color.fromRGB(24, 234, 237));
                    
                    item.setItemMeta(meta);
                } else if (randomFloat < 0.97) {
                    rarity = 2;

                    CustomStack customStack = CustomStack.getInstance("aywen:kebab");
                    item = customStack.getItemStack();
                    ItemMeta meta = item.getItemMeta();
                    rarity = 2;

                    // Une chance sur deux
                    if (new Random().nextInt(2) == 0) {
                        meta.setItemName("Kebab fermenté");
                        meta.setLore(List.of(
                            ChatColor.YELLOW + "Item pêché: " + ChatColor.BOLD + ChatColor.LIGHT_PURPLE + "épique",
                            "\"Il n'a pas l'air frais ce kebab...\""
                        ));
                        meta.setRarity(ItemRarity.RARE);
                        
                        FoodComponent food = meta.getFood();

                        food.setCanAlwaysEat(true);
                        food.setEatSeconds(3);
                        food.setNutrition(2);
                        food.setSaturation(0);
                        food.addEffect(
                            new PotionEffect(
                                PotionEffectType.NAUSEA,
                                300,
                                0), 
                            0.9f);
                        food.addEffect(
                            new PotionEffect(
                                PotionEffectType.POISON,
                                400,
                                0), 
                            1f);
                        food.addEffect(
                            new PotionEffect(
                                PotionEffectType.SLOWNESS,
                                200,
                                0), 
                            0.9f);
                        
                        meta.setFood(food);
                        meta.setMaxStackSize(1);
                    } else {
                        meta.setItemName("Kebab délicieux");
                        meta.setLore(List.of(
                            ChatColor.YELLOW + "Item pêché: " + ChatColor.BOLD + ChatColor.LIGHT_PURPLE + "épique",
                            "\"Enfin un kebab digne de ce nom !\""
                        ));
                        meta.setRarity(ItemRarity.RARE);
                        
                        FoodComponent food = meta.getFood();

                        food.setCanAlwaysEat(true);
                        food.setNutrition(10);
                        food.setSaturation(10);
                        food.addEffect(
                            new PotionEffect(
                                PotionEffectType.HASTE,
                                600,
                                0), 
                            0.9f);
                        food.addEffect(
                            new PotionEffect(
                                PotionEffectType.REGENERATION,
                                200,
                                0), 
                            1f);
                        food.addEffect(
                            new PotionEffect(
                                PotionEffectType.SPEED,
                                600,
                                0), 
                            0.9f);
                        
                        meta.setFood(food);
                        meta.setMaxStackSize(1);
                    }

                    item.setItemMeta(meta);
                } else if (randomFloat < 0.98) {    // légendaire
                    rarity = 3;

                    randomFloat = new Random().nextFloat();

                    ItemMeta meta;

                    if (randomFloat < 0.25) {
                        item = new ItemStack(Material.GOLDEN_BOOTS);
                        meta = item.getItemMeta();

                        meta.addEnchant(Enchantment.FEATHER_FALLING, 8, true);  // -12% / level : -96% fall damage
                        meta.addEnchant(Enchantment.DEPTH_STRIDER, 3, true);    // swim like walking
                        meta.addEnchant(Enchantment.SOUL_SPEED, 1, true);
                        meta.setItemName("Bottes anciennes");
                    } else if (randomFloat < 0.5) {
                        item = new ItemStack(Material.GOLDEN_LEGGINGS);
                        meta = item.getItemMeta();

                        meta.addEnchant(Enchantment.SWIFT_SNEAK, 4, true);  // sneak is 90% walk speed
                        meta.setItemName("Pantalon ancien");
                    } else if (randomFloat < 0.75) {
                        item = new ItemStack(Material.GOLDEN_CHESTPLATE);
                        meta = item.getItemMeta();

                        meta.addEnchant(Enchantment.THORNS, 6, true);  // deals damage almost every time
                        meta.addEnchant(Enchantment.UNBREAKING, 3, true);
                        meta.setItemName("Plastron ancien");
                    } else {
                        item = new ItemStack(Material.GOLDEN_HELMET);
                        meta = item.getItemMeta();

                        meta.addEnchant(Enchantment.RESPIRATION, 6, true);  // +15s / level : +90s
                        meta.addEnchant(Enchantment.AQUA_AFFINITY, 1, true);  // ignore mining slowdown in water
                        meta.setItemName("Casque ancien");
                    }

                    meta.setRarity(ItemRarity.EPIC);
                    meta.setLore(List.of(
                        ChatColor.LIGHT_PURPLE + "Item pêché: " + ChatColor.BOLD + ChatColor.GOLD + "légendaire",
                        "Un objet d'une civilisation ancienne, repêché du fond des mers. \nIl possède une grande valeur..."
                    ));
                    meta.setCustomModelData(42);

                    item.setItemMeta(meta);
                } else if (randomFloat < 0.99) {
                    rarity = 3;

                    item = new ItemStack(Material.POTION);
                    PotionMeta meta = (PotionMeta) item.getItemMeta();

                    // 6000 ticks = 5 minutes
                    randomFloat = new Random().nextFloat();
                    if (randomFloat < 0.3) {
                        meta.addCustomEffect(new PotionEffect(PotionEffectType.SPEED, 6000, 2), true);
                        meta.addCustomEffect(new PotionEffect(PotionEffectType.HASTE, 6000, 1), true);
                        meta.addCustomEffect(new PotionEffect(PotionEffectType.DOLPHINS_GRACE, 6000, 0), true);
                        meta.setItemName("Elixir de rapidité");
                        meta.setColor(Color.fromRGB(230, 45, 247));
                    } else if (randomFloat < 0.6) {
                        meta.addCustomEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, 6000, 2), true);
                        meta.addCustomEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 6000, 0), true);
                        meta.setItemName("Potion de lune");
                        meta.setColor(Color.fromRGB(230, 45, 247));
                    } else if (randomFloat < 0.9) {
                        meta.addCustomEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 6000, 0), true);
                        meta.addCustomEffect(new PotionEffect(PotionEffectType.ABSORPTION, 6000, 0), true);
                        meta.addCustomEffect(new PotionEffect(PotionEffectType.REGENERATION, 600, 0), true);
                        meta.setItemName("Elixir de vie");
                        meta.setColor(Color.fromRGB(230, 45, 247));
                    } else {    
                        // 0.1% potion (to prevent problems)
                        item.setType(Material.SPLASH_POTION);
                        meta.addCustomEffect(new PotionEffect(PotionEffectType.WITHER, 600, 0), true);
                        meta.addCustomEffect(new PotionEffect(PotionEffectType.DARKNESS, 1000, 0), true);
                        meta.addCustomEffect(new PotionEffect(PotionEffectType.WEAKNESS, 1200, 0), true);
                        meta.setItemName("Mixture malfaisante");
                        meta.setColor(Color.fromRGB(50, 0, 0));
                    }

                    meta.setRarity(ItemRarity.EPIC);
                    meta.setEnchantmentGlintOverride(true);

                    meta.setLore(List.of(
                        ChatColor.LIGHT_PURPLE + "Item pêché: " + ChatColor.BOLD + ChatColor.GOLD + "légendaire",
                        "Une potion issue d'une recette antique, \nà employer en cas de grand besoin..."
                    ));
                    
                    item.setItemMeta(meta);
                } else {
                    rarity = 3;

                    item = new ItemStack(Material.BREAD);
                    ItemMeta meta = item.getItemMeta();

                    meta.addEnchant(Enchantment.LOYALTY, 10, true);
                    meta.addEnchant(Enchantment.SHARPNESS, 10, true);
                    meta.setItemName("§l§6Le :baguette: sacré");
                    meta.setLore(List.of(
                        ChatColor.LIGHT_PURPLE + "Item pêché: " + ChatColor.BOLD + ChatColor.GOLD + "légendaire",
                        "§k0§r §l§6Une sainte relique du :baguette: divin§r §k0§r"
                    ));
                    meta.setMaxStackSize(1);
                    meta.setRarity(ItemRarity.EPIC);

                    FoodComponent food = meta.getFood();
                    food.setNutrition(0);
                    food.setSaturation(0);
                    food.addEffect(new PotionEffect(
                        PotionEffectType.SATURATION,
                        3600,  // 3 minutes
                        0
                    ), 1);
                    food.setCanAlwaysEat(true);
                    
                    meta.setFood(food);
                    meta.setCustomModelData(42);

                    item.setItemMeta(meta);
                }

                Player player = event.getPlayer();

                // normal xp: between 1 and 6
                if (rarity == 0) {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy("Tu as pêché un item " + ChatColor.BOLD + "commun", ChatColor.WHITE));
                    
                    // give between 10 and 20 xp
                    event.setExpToDrop(new Random().nextInt(11) + 10);
                } else if (rarity == 1) {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy("Tu as pêché un item " + ChatColor.BOLD + ChatColor.YELLOW + "rare" + ChatColor.RESET + " !", ChatColor.WHITE));
                    player.playSound(player.getEyeLocation(), Sound.BLOCK_NOTE_BLOCK_BANJO, 1, 1);
                    
                    // give between 30 and 40 xp
                    event.setExpToDrop(new Random().nextInt(11) + 30);
                } else if (rarity == 2) {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy("Tu as pêché un item " + ChatColor.BOLD + ChatColor.LIGHT_PURPLE + "épique " + ChatColor.RESET + "!", ChatColor.YELLOW));
                    player.playSound(player.getEyeLocation(), Sound.BLOCK_NOTE_BLOCK_COW_BELL, 1, 1);
                    
                    // give between 40 and 60 xp
                    event.setExpToDrop(new Random().nextInt(21) + 40);
                } else if (rarity == 3) {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy("Tu as pêché un item " + ChatColor.BOLD + ChatColor.GOLD + "légendaire " + ChatColor.RESET + "!", ChatColor.LIGHT_PURPLE));
                    player.playSound(player.getEyeLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);

                    // give between 80 and 100 xp
                    event.setExpToDrop(new Random().nextInt(21) + 80);
                    
                    Bukkit.broadcastMessage(ChatColor.BLUE + player.getName() + ChatColor.RESET + " a pêché un item " + ChatColor.YELLOW + ChatColor.BOLD + "légendaire" + ChatColor.RESET + " !");
                }

                Item caughtItem = (Item) event.getCaught();
                caughtItem.setItemStack(item);
            }
        }
    }

    @EventHandler
    public void onLootGenerate(LootGenerateEvent event) {
        if (isInMiraculousFishing()) {
            
            List<ItemStack> loot = new ArrayList<>(event.getLoot());
            LootContext lootContext = event.getLootContext();

            float randomFloat = new Random().nextFloat();

            NamespacedKey lootTableKey = event.getLootTable().getKey();

            // nether related chests
            if (lootTableKey.equals(LootTables.NETHER_BRIDGE.getKey()) || 
                lootTableKey.equals(LootTables.BASTION_TREASURE.getKey()) ||
                lootTableKey.equals(LootTables.BASTION_BRIDGE.getKey()) ||
                lootTableKey.equals(LootTables.BASTION_HOGLIN_STABLE.getKey()) ||
                lootTableKey.equals(LootTables.BASTION_OTHER.getKey()) ||
                lootTableKey.equals(LootTables.RUINED_PORTAL.getKey())) 
            {
                
                if (randomFloat < 0.5) {
                    ItemStack item = new ItemStack(Material.SPLASH_POTION);

                    PotionMeta meta = (PotionMeta) item.getItemMeta();
                    // 3600 ticks = 3 minutes
                    meta.addCustomEffect(new PotionEffect(
                        PotionEffectType.FIRE_RESISTANCE, 
                        3600,
                         0), true);
                    meta.setLore(List.of(
                        "Loot bonus - Pêche miraculeuse"
                    ));
                    meta.setItemName("Potion de résistance au feu");
                    meta.setColor(Color.fromRGB(228, 154, 58));
                    
                    item.setItemMeta(meta);
                    loot.add(item);
                } else {
                    ItemStack item = new ItemStack(Material.OBSIDIAN, 16);

                    ItemMeta meta = item.getItemMeta();
                    meta.setLore(List.of(
                        "Loot bonus - Pêche miraculeuse"
                    ));
                    
                    item.setItemMeta(meta);
                    loot.add(item);

                    item = new ItemStack(Material.FLINT_AND_STEEL);

                    meta = item.getItemMeta();
                    meta.setLore(List.of(
                        "Loot bonus - Pêche miraculeuse"
                    ));
                    
                    item.setItemMeta(meta);
                    loot.add(item);
                }

                randomFloat = new Random().nextFloat();
                
                if (randomFloat < 0.4) {
                    // 5 - 15 Ingots
                    ItemStack item = new ItemStack(Material.IRON_INGOT, 5 + new Random().nextInt(11));

                    ItemMeta meta = item.getItemMeta();
                    meta.setLore(List.of(
                        "Loot bonus obtenu - Pêche miraculeuse"
                    ));
                    
                    item.setItemMeta(meta);
                    loot.add(item);
                } else if (randomFloat < 0.75) {
                    // 5 - 10 Ingots
                    ItemStack item = new ItemStack(Material.GOLD_INGOT, 5 + new Random().nextInt(6));

                    ItemMeta meta = item.getItemMeta();
                    meta.setLore(List.of(
                        "Loot bonus - Pêche miraculeuse"
                    ));
                    
                    item.setItemMeta(meta);
                    loot.add(item);
                } else {
                    // 1 - 2 Diamonds
                    ItemStack item = new ItemStack(Material.DIAMOND, 1 + new Random().nextInt(2));

                    ItemMeta meta = item.getItemMeta();
                    meta.setLore(List.of(
                        "Loot bonus - Pêche miraculeuse"
                    ));
                    
                    item.setItemMeta(meta);
                    loot.add(item);
                }
            } else {
                if (randomFloat < 0.4) {
                    // 5 - 15 Ingots
                    ItemStack item = new ItemStack(Material.IRON_INGOT, 5 + new Random().nextInt(11));

                    ItemMeta meta = item.getItemMeta();
                    meta.setLore(List.of(
                        "Loot bonus - Pêche miraculeuse"
                    ));
                    
                    item.setItemMeta(meta);
                    loot.add(item);
                } else if (randomFloat < 0.75) {
                    // 5 - 10 Ingots
                    ItemStack item = new ItemStack(Material.GOLD_INGOT, 5 + new Random().nextInt(6));

                    ItemMeta meta = item.getItemMeta();
                    meta.setLore(List.of(
                        "Loot bonus - Pêche miraculeuse"
                    ));
                    
                    item.setItemMeta(meta);
                    loot.add(item);
                } else {
                    // 1 - 2 Diamonds
                    ItemStack item = new ItemStack(Material.DIAMOND, 1 + new Random().nextInt(2));

                    ItemMeta meta = item.getItemMeta();
                    meta.setLore(List.of(
                        "Loot bonus - Pêche miraculeuse"
                    ));
                    
                    item.setItemMeta(meta);
                    loot.add(item);
                }
            }

            Location location = lootContext.getLocation();
            World world = location.getWorld();
            
            world.playSound(location, Sound.BLOCK_NOTE_BLOCK_FLUTE, 1, 1);
            world.spawnParticle(Particle.SMOKE, location.add(0, 1, 0), 10, 1, 1, 1);
            event.setLoot(loot);
            ExperienceOrb orb = world.spawn(location.add(0, 1, 0), ExperienceOrb.class);
            orb.setExperience(new Random().nextInt(21) + 60);
        }
    }


    @EventHandler
    public void onMobDeath(EntityDeathEvent event) {

        LivingEntity entity = event.getEntity();

        if (entity.hasMetadata("terrifyingNightTime")) {

            if (entity.getKiller() instanceof Player) {

                if (entity.getMetadata("terrifyingNightTime").get(0).asLong() + terrifyingNightDurationMillis > System.currentTimeMillis()) {
                    
                    Random random = new Random();
                    float randomFloat = random.nextFloat();

                    final ItemStack item;

                    switch (entity.getType()) {
                        case EntityType.ZOMBIE:

                            item = new ItemStack(Material.ZOMBIE_HEAD);

                            // 30% lingots fer (1-5), 20% lingots or (1-5), 20% xp (+10-+30), 15% émeraudes (2-5), 10% diamant, 4.5% tête de zombie, 0.5% oeuf de zombie
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
                            } else if (randomFloat <= 0.995) {
                                event.getDrops().add(new ItemStack(Material.ZOMBIE_HEAD));
                            } else {
                                event.getDrops().add(new ItemStack(Material.ZOMBIE_SPAWN_EGG));
                            }

                            break;

                        case EntityType.SKELETON:

                            item = new ItemStack(Material.SKELETON_SKULL);

                            // 30% lingots fer (1-5), 20% os (5-10), 20% xp (+200-+300), 15% flèches (10-15), 10% émeraudes (2-5), 4.5% tête de squelette, 0.5% oeuf de squelette
                            if (randomFloat < 0.3) {
                                event.getDrops().add(new ItemStack(Material.IRON_INGOT, random.nextInt(5) + 1));
                            } else if (randomFloat < 0.5) {
                                event.getDrops().add(new ItemStack(Material.BONE, random.nextInt(6) + 5));
                            } else if (randomFloat < 0.7) {
                                event.setDroppedExp(event.getDroppedExp() + random.nextInt(101) + 200);
                            } else if (randomFloat < 0.85) {
                                event.getDrops().add(new ItemStack(Material.ARROW, random.nextInt(6) + 10));
                            } else if (randomFloat < 0.95) {
                                event.getDrops().add(new ItemStack(Material.EMERALD, random.nextInt(4) + 2));
                            } else if (randomFloat <= 0.995) {
                                event.getDrops().add(new ItemStack(Material.SKELETON_SKULL));
                            } else {
                                event.getDrops().add(new ItemStack(Material.SKELETON_SPAWN_EGG));
                            }

                            break;

                        case EntityType.CREEPER:
                        
                            item = new ItemStack(Material.CREEPER_HEAD);
                        
                            // 30% poudres à canon (6-12), 30% fireworks (5-10), 20% tnt (3-5), 15% fire charges (3-8), 4.5% tête de creeper, 0.5% oeuf de creeper
                            if (randomFloat < 0.3) {
                                event.getDrops().add(new ItemStack(Material.GUNPOWDER, random.nextInt(7) + 6));
                            } else if (randomFloat < 0.6) {
                                event.getDrops().add(new ItemStack(Material.FIREWORK_ROCKET, random.nextInt(6) + 5));
                            } else if (randomFloat < 0.8) {
                                event.getDrops().add(new ItemStack(Material.TNT, random.nextInt(4) + 2));
                            } else if (randomFloat < 0.95) {
                                event.getDrops().add(new ItemStack(Material.FIRE_CHARGE, random.nextInt(6) + 3));
                            } else if (randomFloat <= 0.995) {
                                event.getDrops().add(new ItemStack(Material.CREEPER_HEAD));
                            } else {
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

    public void start() {
        if (!isRunning) {
            eventRunnable = new BukkitRunnable() {
                @Override
                public void run() {
                    if (Bukkit.getOnlinePlayers().isEmpty()) return;
                    
                    World overworld = Bukkit.getWorlds().get(0);
    
                    long worldTime = overworld.getTime();
    
                    // handle enabled events
                    for (int event : enabledEvents) {
                        switch (event) {
                            case TERRIFYING_NIGHT:
                                // every midnight: 1/4 chance but not twice in a row
                                if (18000 <= worldTime && worldTime < 18100 && System.currentTimeMillis() - lastTerrifyingNight > 20*61000 && new Random().nextFloat() <= terrifyingNightProbability) {
                                    startTerrifyingNight();
                                }
                                break;
    
                            case MIRACULOUS_FISHING:
                                // every midday: 1/4 chance
                                if (6000 <= worldTime && worldTime < 6100 && System.currentTimeMillis() - lastMiraculousFishing > miraculousFishingDuration && new Random().nextFloat() <= miraculousFishingProbability) {
                                    startMiraculousFishing();
                                }
                                break;
                        
                            default:
                                break;
                        }
                    }
                }
            };
            // runs every 100 ticks
            eventRunnable.runTaskTimer(plugin, 0, 100);
            
            isRunning = true;
        }
    }
    public void stop() {
        if (isRunning) {
            eventRunnable.cancel();
            isRunning = false;
        }
    }
    public boolean isRunning() {
        return isRunning;
    }

    public boolean isTerrifyingNightEnabled() {
        return enabledEvents.contains(TERRIFYING_NIGHT);
    }
    public boolean isMiraculousFishingEnabled() {
        return enabledEvents.contains(MIRACULOUS_FISHING);
    }
}
