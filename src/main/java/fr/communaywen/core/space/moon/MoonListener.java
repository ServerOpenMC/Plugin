package fr.communaywen.core.space.moon;

import com.destroystokyo.paper.event.entity.EntityJumpEvent;
import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import dev.lone.itemsadder.api.CustomBlock;
import dev.lone.itemsadder.api.CustomEntity;
import dev.lone.itemsadder.api.CustomStack;
import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.customitems.objects.CustomItems;
import fr.communaywen.core.teams.menu.TeamMenu;
import fr.communaywen.core.utils.FallingBlocksExplosion;
import fr.communaywen.core.utils.ItemBuilder;
import fr.communaywen.core.utils.constant.MessageManager;
import fr.communaywen.core.utils.constant.MessageType;
import fr.communaywen.core.utils.constant.Prefix;
import io.papermc.paper.event.entity.EntityPortalReadyEvent;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

import static fr.communaywen.core.space.moon.Utils.isPlayerLookingAtMoon;
import static fr.communaywen.core.utils.ProgressBar.createProgressBar;
import static fr.communaywen.core.utils.Skull.*;

public class MoonListener implements Listener {


    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (!player.getWorld().getName().equals("moon")){ return; }

        if (block.getType().equals(Material.IRON_BLOCK)) {
            event.setDropItems(false);
            player.getWorld().dropItemNaturally(block.getLocation(),
                    CustomStack.getInstance("space:steel_ingot").getItemStack());
            if(Math.random() < 0.2) {
                player.getWorld().dropItemNaturally(block.getLocation(),
                        CustomStack.getInstance("space:steel_ingot").getItemStack());
            }
        }
        if(block.getType().equals(Material.END_STONE)) {
            event.setDropItems(false);
            player.getWorld().dropItemNaturally(block.getLocation(),
                    CustomStack.getInstance("space:moon_shard").getItemStack());
            if(Math.random() < 0.3) {
                player.getWorld().dropItemNaturally(block.getLocation(),
                        CustomStack.getInstance("space:moon_shard").getItemStack());
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (!player.getWorld().getName().equals("moon")){ return; }

        if (block.getType().equals(Material.END_STONE)) {
            event.setCancelled(true);
        }

        if(block.getType().equals(Material.IRON_BLOCK)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPhysics(BlockPhysicsEvent event){
        Block block = event.getBlock();
        if(!block.getLocation().getWorld().getName().equals("moon")) return;
        if(block.getType()==Material.WATER){
            block.setType(Material.AIR);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if(!e.getWhoClicked().hasPermission("ayw.space.head.glass")) return;
        if(e.getSlot() == 39 && e.getClickedInventory().getType().equals(InventoryType.PLAYER) && Objects.equals(e.getCurrentItem(), new ItemStack(Material.AIR))) {
            if(e.getCursor().getType().equals(Material.GLASS)) {
                ItemStack glassBlock = e.getCursor();
                e.setCancelled(true);
                e.getWhoClicked().setItemOnCursor(null);
                e.getWhoClicked().getInventory().setHelmet(glassBlock);
            }
        }
    }


    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent e) {
        if(!e.getLocation().getWorld().getName().equals("moon")) return;
        if(List.of(EntityType.ARMOR_STAND, EntityType.BLOCK_DISPLAY, EntityType.ITEM_DISPLAY, EntityType.TEXT_DISPLAY, EntityType.ITEM, EntityType.WOLF, EntityType.PARROT, EntityType.OCELOT, EntityType.CAT, EntityType.TROPICAL_FISH, EntityType.ARROW, EntityType.ENDER_PEARL, EntityType.EXPERIENCE_ORB, EntityType.EXPERIENCE_BOTTLE, EntityType.ITEM_FRAME, EntityType.GLOW_ITEM_FRAME, EntityType.TRIDENT, EntityType.POTION, EntityType.AREA_EFFECT_CLOUD, EntityType.SNOWBALL, EntityType.IRON_GOLEM, EntityType.SNOW_GOLEM, EntityType.BREEZE_WIND_CHARGE, EntityType.TNT).contains(e.getEntity().getType())) return;
        if(!List.of(EntityType.ZOMBIE, EntityType.SKELETON, EntityType.COW, EntityType.SPIDER, EntityType.VILLAGER).contains(e.getEntity().getType())) {
            e.setCancelled(true);
        }
        if(e.getEntity().getType().equals(EntityType.SPIDER)) {
            e.setCancelled(true);

            double random = Math.random();
            if(random < 0.2) {
                e.getLocation().getWorld().spawnEntity(e.getLocation(), EntityType.COW);
            }
            if(random >= 0.2 && random < 0.25) {
                e.getLocation().getWorld().spawnEntity(e.getLocation(), EntityType.VILLAGER);
            }

        }

        if(!(e.getEntity() instanceof LivingEntity)) return;


        EntityEquipment ee = ((LivingEntity) e.getEntity()).getEquipment();
        assert ee != null;
        ee.setHelmet(new ItemStack(Material.GLASS));
        ((LivingEntity) e.getEntity()).getAttribute(Attribute.GENERIC_GRAVITY).setBaseValue(0.013211);
        ((LivingEntity) e.getEntity()).getAttribute(Attribute.GENERIC_SAFE_FALL_DISTANCE).setBaseValue(10);
        if(e.getEntity().getType().equals(EntityType.COW)) {
            ((LivingEntity) e.getEntity()).customName(Component.text("§eVache lunaire"));
            e.getEntity().setCustomNameVisible(true);
        }
        if(e.getEntity().getType().equals(EntityType.VILLAGER)) {
            Villager villager = (Villager) e.getEntity();
            villager.customName(Component.text("§eVillageois lunaire"));
            villager.setCustomNameVisible(true);
            villager.setProfession(Villager.Profession.ARMORER);
            villager.setVillagerType(Villager.Type.SNOW);
            villager.setVillagerLevel(5);
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        if(!e.getEntity().getWorld().getName().equals("moon")) return;
        if(!e.getEntity().getType().equals(EntityType.ZOMBIE)) return;

        //1 chance sur 10 de drop la tete de cosmonaute
        if(Math.random() < 0.1) {
            ItemStack head = getCustomSkull("https://textures.minecraft.net/texture/c5545c6457c447464f308f0063876e334bc48747dc0dfc442efd8fed89b3c292");
            head.setDisplayName("§6Tête de cosmonaute");
            head.setLore(Arrays.asList("§r", "§r§fTête d'un cosmonaute mort sur la lune", "§r§f§oPermet d'activer un rituel...", "§r"));
            e.getDrops().add(head);
        }
    }

    @EventHandler
    public void onVillagerTradeMenuOpen(InventoryOpenEvent e) {
        if(!e.getPlayer().getWorld().getName().equals("moon")) return;
        if(e.getInventory() instanceof MerchantInventory) {
            MerchantInventory inventory = (MerchantInventory) e.getInventory();

            List<MerchantRecipe> newRecipes = new ArrayList<>();

            MerchantRecipe glassRecipe = new MerchantRecipe(new ItemStack(Material.GLASS), 3);
            glassRecipe.addIngredient(CustomStack.getInstance("space:moon_shard").getItemStack().add(3));
            newRecipes.add(glassRecipe);

            MerchantRecipe moonMilkRecipe = new MerchantRecipe(CustomStack.getInstance("space:moon_milk_bucket").getItemStack(), 3);
            moonMilkRecipe.addIngredient(new ItemStack(Material.BUCKET));
            moonMilkRecipe.addIngredient(CustomStack.getInstance("space:moon_shard").getItemStack().add(5));
            newRecipes.add(moonMilkRecipe);

            MerchantRecipe bucketRecipe = new MerchantRecipe(new ItemStack(Material.BUCKET), 3);
            bucketRecipe.addIngredient(new ItemStack(Material.COAL, 30));
            newRecipes.add(bucketRecipe);

            MerchantRecipe coalRecipe = new MerchantRecipe(new ItemStack(Material.COAL, 30), 10);
            coalRecipe.addIngredient(CustomStack.getInstance("space:moon_shard").getItemStack().add(63));
            coalRecipe.addIngredient(new ItemStack(Material.GLASS));
            newRecipes.add(coalRecipe);

            MerchantRecipe spyGlassRecipe = new MerchantRecipe(new ItemStack(Material.SPYGLASS, 1), 2);
            spyGlassRecipe.addIngredient(CustomStack.getInstance("space:moon_shard").getItemStack().add(10));
            spyGlassRecipe.addIngredient(new ItemStack(Material.GLASS, 3));
            newRecipes.add(spyGlassRecipe);

            MerchantRecipe cheeseRecipe = new MerchantRecipe(CustomStack.getInstance("space:cheese").getItemStack().add(9), 3);
            cheeseRecipe.addIngredient(CustomStack.getInstance("space:moon_milk_bucket").getItemStack());
            newRecipes.add(cheeseRecipe);

            inventory.getMerchant().setRecipes(newRecipes);


        }
    }



    @EventHandler
    public void onMilk(PlayerInteractEntityEvent e) {
        if(!e.getPlayer().getWorld().getName().equals("moon")) return;
        if(e.getRightClicked().getType().equals(EntityType.COW) && e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.BUCKET)) {
            e.setCancelled(true);
            if(!((Ageable) e.getRightClicked()).isAdult()) return;
            e.getPlayer().getInventory().getItemInMainHand().setAmount(e.getPlayer().getInventory().getItemInMainHand().getAmount() - 1);
            e.getPlayer().getInventory().addItem(CustomStack.getInstance("space:moon_milk_bucket").getItemStack());
            ((Ageable) e.getRightClicked()).setBaby();
        }
    }

    @EventHandler
    public void onPlayerDrink(PlayerItemConsumeEvent e) {
        ItemStack item = e.getItem();
        CustomStack customStack = CustomStack.byItemStack(item);
        if(customStack != null && customStack.getNamespacedID().equals("space:moon_milk_bucket")) {
            // Le lait enlève les effets donc il faut delay des effets pour pas qu'ils soient cancel par le lait (j'ai pris 1h à m'en rendre compte)
            new BukkitRunnable() {
                @Override
                public void run() {
                    e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, 20*60, 3));
                    e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 20*60, 5));
                    e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 40, 59));
                }
            }.runTaskLater(AywenCraftPlugin.getInstance(), 5);
        };
    }

    @EventHandler
    public void onBoomEntity(EntityExplodeEvent e) {
        if(!e.getEntity().getWorld().getName().equals("moon")) return;
        e.setCancelled(true);
    }


    @EventHandler
    public void onDimChange(PlayerChangedWorldEvent e) {
        if(e.getPlayer().getWorld() == Bukkit.getWorld("moon")) {
            // Alors le produit en croix : La gravité sur la terre d'mc c'est 0.08, la gravité sur terre irl c'est 9.81. La gravité sur la lune irl c'est 1.62. (1.62*0.08)/9.81 = environ 0.013211
            e.getPlayer().getAttribute(Attribute.GENERIC_GRAVITY).setBaseValue(0.013211);
            e.getPlayer().getAttribute(Attribute.GENERIC_SAFE_FALL_DISTANCE).setBaseValue(10);

            if(e.getPlayer().getInventory().getHelmet() != null && e.getPlayer().getInventory().getHelmet().getType().equals(Material.GLASS)) {
                //playerBreath(e.getPlayer()); -- Le Joueur respire déjà avec l'event de l'item equip
            } else {
                playerSuffocate(e.getPlayer());
            }

        }
        if(e.getFrom() == Bukkit.getWorld("moon")) {
            e.getPlayer().getAttribute(Attribute.GENERIC_GRAVITY).setBaseValue(0.08);
            e.getPlayer().getAttribute(Attribute.GENERIC_SAFE_FALL_DISTANCE).setBaseValue(3);
        }
    }

    @EventHandler
    public void onItemEquip(PlayerArmorChangeEvent e) {
        if(e.getNewItem().getType().equals(Material.GLASS)) {
            playerBreath(e.getPlayer());
        }
    }

    @EventHandler
    public void onItemUnequip(PlayerArmorChangeEvent e) {
        if(e.getOldItem().getType().equals(Material.GLASS)) {
            playerSuffocate(e.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if(e.getPlayer().getWorld().getName().equals("moon")) {
            e.getPlayer().getAttribute(Attribute.GENERIC_GRAVITY).setBaseValue(0.013211);
            e.getPlayer().getAttribute(Attribute.GENERIC_SAFE_FALL_DISTANCE).setBaseValue(10);
            if(e.getPlayer().getInventory().getHelmet() != null && e.getPlayer().getInventory().getHelmet().getType().equals(Material.GLASS)) {
                playerBreath(e.getPlayer());
            } else {
                playerSuffocate(e.getPlayer());
            }
        }
    }


    //Les portails du nether sont désactivés sur la lune pour éviter le transfert de mobs, items, etc.
    @EventHandler
    public void onPortalCreate(PortalCreateEvent e) {
        if(e.getWorld().getName().equals("moon")) {
            e.setCancelled(true);
            if(e.getEntity() instanceof Player) {
                Player player = (Player) e.getEntity();
                MessageManager.sendMessageType(player, "§cCette lune n'a aucun nether où vous emmener", Prefix.SPACE, MessageType.ERROR, true);
            }
        }

    }

    //On sait jamais pour les portails existants
    @EventHandler
    public void onEntityPortalReady(EntityPortalReadyEvent e) {
        if(e.getEntity().getWorld().getName().equals("moon")) {
            e.setCancelled(true);
            if(e.getEntity() instanceof Player) {
                Player player = (Player) e.getEntity();
                MessageManager.sendMessageType(player, "§cCette lune n'a aucun nether où vous emmener", Prefix.SPACE, MessageType.ERROR, true);
            }
        }
    }


    //On est jamais trop prudent (même si c'est pas censé être possible vu que le drop d'un bloc cassé à la mano est deja check et que les explosions sont désactivées)
    @EventHandler
    public void onBlockDropItem(BlockDropItemEvent e) {
        if(e.getBlock().getWorld().getName().equals("moon")) {
            e.getItems().forEach(item -> {
                if(item.getItemStack().getType().equals(Material.END_STONE)) {
                    e.setCancelled(true);
                    item.getWorld().dropItemNaturally(item.getLocation(), CustomStack.getInstance("space:moon_shard").getItemStack());
                }
                if(item.getItemStack().getType().equals(Material.IRON_BLOCK)) {
                    e.setCancelled(true);
                    item.getWorld().dropItemNaturally(item.getLocation(), CustomStack.getInstance("space:steel_ingot").getItemStack());
                }
            });
        }
    }

    @EventHandler
    public void onSpyglass(PlayerInteractEvent e) {
        if(!e.getAction().equals(Action.RIGHT_CLICK_AIR)) return;
        if(e.getItem() == null) return;
        if(!e.getPlayer().getWorld().equals(Bukkit.getWorld("world"))) return;
        if(e.getItem().getType().equals(Material.SPYGLASS) && isPlayerLookingAtMoon(e.getPlayer())) {
            if(ShardLoading.getByPlayer(e.getPlayer()) != null) {
                ShardLoading.getByPlayer(e.getPlayer()).set(0);
                ShardLoading.getByPlayer(e.getPlayer()).remove();
            }
            ShardLoading shardLoading = new ShardLoading(e.getPlayer(), 0);
            new BukkitRunnable() {
                @Override
                public void run() {
                    if(!e.getItem().getType().equals(Material.SPYGLASS) || !isPlayerLookingAtMoon(e.getPlayer()) || !e.getPlayer().getWorld().equals(Bukkit.getWorld("world"))){
                        shardLoading.remove();
                        this.cancel();
                        return;
                    }
                    if(shardLoading.get() == 100) {
                        shardLoading.set(0);
                        shardLoading.remove();
                        MessageManager.sendMessageType(e.getPlayer(), "§aVous avez trouvé un fragment de lune !", Prefix.SPACE, MessageType.SUCCESS, true);
                        e.getPlayer().getInventory().addItem(CustomStack.getInstance("space:moon_shard").getItemStack());
                        this.cancel();
                        return;
                    }
                    shardLoading.set(shardLoading.get() + 1);
                    e.getPlayer().sendTitle("", createProgressBar(shardLoading.get(), 10, "§6"), 0, 11, 0);
                }
            }.runTaskTimer(AywenCraftPlugin.getInstance(), 0, 10);
        }
    }

    private void playerBreath(Player p) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if(p.getInventory().getHelmet() == null || !p.getInventory().getHelmet().getType().equals(Material.GLASS)) {
                    this.cancel();
                    return;
                }
                p.playSound(p.getLocation(), "sons:effects.breath", 1, 1);
            }
        }.runTaskTimer(AywenCraftPlugin.getInstance(), 0, 100);
    }

    private void playerSuffocate(Player p) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if(!p.getWorld().getName().equals("moon")) {
                    this.cancel();
                    return;
                }
                if(p.getInventory().getHelmet() != null && p.getInventory().getHelmet().getType().equals(Material.GLASS)) {
                    this.cancel();
                    return;
                }
                if(p.getGameMode().equals(GameMode.CREATIVE) || p.getGameMode().equals(GameMode.SPECTATOR)) {
                    return;
                }
                if(p.getHealth() <= 0) {
                    this.cancel();
                    return;
                }
                p.damage(2);
                p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 80, 3));
                p.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA, 160, 3));

                p.sendTitle("§cVous avez besoin d'un scaphandre", "§cpour réspirer sur la lune", 0, 40, 0);
                MessageManager.sendMessageType(p, "§cVous avec besoin d'un scaphandre pour réspirer sur la lune", Prefix.SPACE, MessageType.ERROR, false);

                if(p.getHealth() <= 4 && p.getHealth() >= 3) {
                    p.playSound(p.getLocation(), "sons:effects.hyperventilate", 1, 1);
                }
            }
        }.runTaskTimer(AywenCraftPlugin.getInstance(), 0, 40);
    }


                                                                ////////////////////
                                                                // COSMONAUT BOSS //
                                                                ////////////////////


    private final Map<UUID, BossBar> bossBars = new HashMap<>();


    // Ritual activation

    @EventHandler
    public void onRitualActivation(PlayerInteractEvent e) {
        if(e.getPlayer().getWorld() != Bukkit.getWorld("moon")) return;
        if(e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if(e.getItem() == null) return;
        if(!e.getItem().getType().equals(Material.PLAYER_HEAD)) return;
        if(!e.getItem().getItemMeta().getDisplayName().equals("§6Tête de cosmonaute")) return;
        if(CustomBlock.byAlreadyPlaced(e.getClickedBlock()) == null) return;
        if(!CustomBlock.byAlreadyPlaced(e.getClickedBlock()).getNamespacedID().equals("space:cheese_block")) return;

        //Check if the blocks below (like the beacon's pattern) are steel blocks
        if(CustomBlock.byAlreadyPlaced(e.getClickedBlock().getLocation().add(0, -1, 0).getBlock()) == null || !CustomBlock.byAlreadyPlaced(e.getClickedBlock().getLocation().add(0, -1, 0).getBlock()).getNamespacedID().equals("space:steel_block")) return;
        if(CustomBlock.byAlreadyPlaced(e.getClickedBlock().getLocation().add(1, -1, 0).getBlock()) == null || !CustomBlock.byAlreadyPlaced(e.getClickedBlock().getLocation().add(1, -1, 0).getBlock()).getNamespacedID().equals("space:steel_block")) return;
        if(CustomBlock.byAlreadyPlaced(e.getClickedBlock().getLocation().add(-1, -1, 0).getBlock()) == null || !CustomBlock.byAlreadyPlaced(e.getClickedBlock().getLocation().add(-1, -1, 0).getBlock()).getNamespacedID().equals("space:steel_block")) return;
        if(CustomBlock.byAlreadyPlaced(e.getClickedBlock().getLocation().add(0, -1, 1).getBlock()) == null || !CustomBlock.byAlreadyPlaced(e.getClickedBlock().getLocation().add(0, -1, 1).getBlock()).getNamespacedID().equals("space:steel_block")) return;
        if(CustomBlock.byAlreadyPlaced(e.getClickedBlock().getLocation().add(0, -1, -1).getBlock()) == null || !CustomBlock.byAlreadyPlaced(e.getClickedBlock().getLocation().add(0, -1, -1).getBlock()).getNamespacedID().equals("space:steel_block")) return;
        if(CustomBlock.byAlreadyPlaced(e.getClickedBlock().getLocation().add(1, -1, 1).getBlock()) == null || !CustomBlock.byAlreadyPlaced(e.getClickedBlock().getLocation().add(1, -1, 1).getBlock()).getNamespacedID().equals("space:steel_block")) return;
        if(CustomBlock.byAlreadyPlaced(e.getClickedBlock().getLocation().add(-1, -1, -1).getBlock()) == null || !CustomBlock.byAlreadyPlaced(e.getClickedBlock().getLocation().add(-1, -1, -1).getBlock()).getNamespacedID().equals("space:steel_block")) return;
        if(CustomBlock.byAlreadyPlaced(e.getClickedBlock().getLocation().add(1, -1, -1).getBlock()) == null || !CustomBlock.byAlreadyPlaced(e.getClickedBlock().getLocation().add(1, -1, -1).getBlock()).getNamespacedID().equals("space:steel_block")) return;
        if(CustomBlock.byAlreadyPlaced(e.getClickedBlock().getLocation().add(-1, -1, 1).getBlock()) == null || !CustomBlock.byAlreadyPlaced(e.getClickedBlock().getLocation().add(-1, -1, 1).getBlock()).getNamespacedID().equals("space:steel_block")) return;

        //Check if the blocks above are air
        if(!e.getClickedBlock().getLocation().add(0, 1, 0).getBlock().getType().equals(Material.AIR)) return;
        if(!e.getClickedBlock().getLocation().add(1, 1, 0).getBlock().getType().equals(Material.AIR)) return;
        if(!e.getClickedBlock().getLocation().add(-1, 1, 0).getBlock().getType().equals(Material.AIR)) return;
        if(!e.getClickedBlock().getLocation().add(0, 1, 1).getBlock().getType().equals(Material.AIR)) return;
        if(!e.getClickedBlock().getLocation().add(0, 1, -1).getBlock().getType().equals(Material.AIR)) return;
        if(!e.getClickedBlock().getLocation().add(1, 1, 1).getBlock().getType().equals(Material.AIR)) return;
        if(!e.getClickedBlock().getLocation().add(-1, 1, -1).getBlock().getType().equals(Material.AIR)) return;
        if(!e.getClickedBlock().getLocation().add(1, 1, -1).getBlock().getType().equals(Material.AIR)) return;
        if(!e.getClickedBlock().getLocation().add(-1, 1, 1).getBlock().getType().equals(Material.AIR)) return;


        e.setCancelled(true);


        e.getPlayer().getInventory().removeItem(e.getItem().asOne());



        Location loc = e.getClickedBlock().getLocation();
        World world = loc.getWorld();
        assert world != null;
        world.strikeLightning(loc);
        world.playSound(loc, Sound.ITEM_TOTEM_USE,  SoundCategory.HOSTILE, 1, 0);

        //remove the blocks
        CustomBlock.byAlreadyPlaced(e.getClickedBlock()).remove();
        CustomBlock.byAlreadyPlaced(e.getClickedBlock().getLocation().add(0, -1, 0).getBlock()).remove();
        CustomBlock.byAlreadyPlaced(e.getClickedBlock().getLocation().add(1, -1, 0).getBlock()).remove();
        CustomBlock.byAlreadyPlaced(e.getClickedBlock().getLocation().add(-1, -1, 0).getBlock()).remove();
        CustomBlock.byAlreadyPlaced(e.getClickedBlock().getLocation().add(0, -1, 1).getBlock()).remove();
        CustomBlock.byAlreadyPlaced(e.getClickedBlock().getLocation().add(0, -1, -1).getBlock()).remove();
        CustomBlock.byAlreadyPlaced(e.getClickedBlock().getLocation().add(1, -1, 1).getBlock()).remove();
        CustomBlock.byAlreadyPlaced(e.getClickedBlock().getLocation().add(-1, -1, -1).getBlock()).remove();
        CustomBlock.byAlreadyPlaced(e.getClickedBlock().getLocation().add(1, -1, -1).getBlock()).remove();
        CustomBlock.byAlreadyPlaced(e.getClickedBlock().getLocation().add(-1, -1, 1).getBlock()).remove();


        Bukkit.broadcast(Component.text("§6§lUn rituel a été activé sur la lune !"));

        // Create and show the boss bar
        BossBar bossBar = Bukkit.createBossBar("§6§lCosmonaute", BarColor.YELLOW, BarStyle.SOLID);
        bossBar.setProgress(0.0);
        bossBar.setVisible(true);
        for (Player player : Bukkit.getOnlinePlayers()) {
            if(player.getWorld() != Bukkit.getWorld("moon")) return;
            bossBar.addPlayer(player);
        }

        AywenCraftPlugin.getInstance().getLogger().info("Spawned Cosmonaut boss : " + loc.toString() + " by " + e.getPlayer().getName());

        //spawn the boss
        Entity boss = world.spawnEntity(loc.add(0.5, 0, 0.5), EntityType.ZOMBIE, CreatureSpawnEvent.SpawnReason.DEFAULT, entity -> {
            entity.customName(Component.text("§6§lCosmonaute"));
            entity.setCustomNameVisible(true);
            entity.setInvulnerable(true);
            entity.setSilent(true);
            entity.setGravity(false);
            entity.setMetadata("isCosmonaut", new FixedMetadataValue(AywenCraftPlugin.getInstance(), true));
        });

        if(!boss.isValid()) {
            AywenCraftPlugin.getInstance().getLogger().warning("Failed to spawn Cosmonaut boss (serveur en eau)");
            //remboursement des items
            MessageManager.sendMessageType(e.getPlayer(), "§cUne erreur est survenue lors du spawn du boss, vous avez été remboursé(e) !\nDate et heure de l'incident : " + new Date().toString(), Prefix.SPACE, MessageType.ERROR, true);
            e.getPlayer().getInventory().addItem(e.getItem().asOne());
            e.getPlayer().getInventory().addItem(CustomStack.getInstance("space:steel_block").getItemStack().add(8));
            e.getPlayer().getInventory().addItem(CustomStack.getInstance("space:cheese_block").getItemStack());

            return;
        }

        bossBars.put(boss.getUniqueId(), bossBar);

        //animation boss : grow (attribute)
        ((Zombie) boss).getAttribute(Attribute.GENERIC_SCALE).setBaseValue(0.5);
        ((Zombie) boss).getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(200);
        ((Zombie) boss).setHealth(10);
        ((Zombie) boss).setAI(false);


        new BukkitRunnable() {
            @Override
            public void run() {
                if(!boss.isValid() || boss.isDead()) {
                    this.cancel();
                    removeBossBar(boss.getUniqueId());
                    return;
                }
                ((Zombie) boss).setHealth(((Zombie) boss).getHealth() + 5);
                ((Zombie) boss).getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(((Zombie) boss).getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getValue()+0.0001);
                ((Zombie) boss).getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(((Zombie) boss).getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getValue()+0.03);
                ((Zombie) boss).getAttribute(Attribute.GENERIC_SCALE).setBaseValue(((Zombie) boss).getAttribute(Attribute.GENERIC_SCALE).getValue()+0.05);

                bossBar.setProgress(((Zombie) boss).getHealth()/200);

                if(((Zombie) boss).getHealth() >= 200) {
                    this.cancel();
                    new BukkitRunnable() {

                        int i = 0;

                        ItemStack helmet = new ItemStack(Material.GLASS);
                        ItemStack chestplate = new ItemStack(Material.IRON_CHESTPLATE);
                        ItemStack leggings = new ItemStack(Material.IRON_LEGGINGS);
                        ItemStack boots = new ItemStack(Material.IRON_BOOTS);
                        ItemStack sword = new ItemStack(Material.IRON_SWORD);
                        @Override
                        public void run() {
                            if(!boss.isValid()) {
                                this.cancel();
                                return;
                            }

                            i++;

                            world.playSound(loc, Sound.BLOCK_ANVIL_BREAK,  SoundCategory.MASTER, 1, 0);

                            switch (i) {
                                case 1 -> ((Zombie) boss).getEquipment().setHelmet(helmet);
                                case 2 -> ((Zombie) boss).getEquipment().setChestplate(chestplate);
                                case 3 -> ((Zombie) boss).getEquipment().setLeggings(leggings);
                                case 4 -> ((Zombie) boss).getEquipment().setBoots(boots);
                                case 5 -> ((Zombie) boss).getEquipment().setItemInMainHand(sword);
                            }

                            if(i == 6) {
                                this.cancel();
                                ((Zombie) boss).setAI(true);
                                ((Zombie) boss).setInvulnerable(false);
                                ((Zombie) boss).setSilent(false);
                                ((Zombie) boss).setGravity(true);
                            }
                        }
                    }.runTaskTimer(AywenCraftPlugin.getInstance(), 20, 10);


                }
            }
        }.runTaskTimer(AywenCraftPlugin.getInstance(), 0, 1);



    }


    //Pas très beau ce code :

    @EventHandler
    public void onBossDamage(EntityDamageByEntityEvent e) {
        if(e.getEntity().getWorld() != Bukkit.getWorld("moon")) return;
        if(e.getEntity().hasMetadata("isCosmonaut")) {
            bossBars.get(e.getEntity().getUniqueId()).setProgress(((Zombie) e.getEntity()).getHealth()/200);
            if(e.getDamager() instanceof Player) {
                e.getEntity().getWorld().playSound(e.getEntity().getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_WOODEN_DOOR,  SoundCategory.HOSTILE, 1, 1);
                e.getEntity().getWorld().spawnParticle(Particle.CRIT, e.getEntity().getLocation().add(0, 1, 0), 10, 0.5, 0.5, 0.5, 0.1);
                if(Math.random() < 0.7) {
                    e.getEntity().getWorld().strikeLightning(e.getEntity().getLocation().add(Math.random()*10, 0, Math.random()*10));
                }
                if(Math.random() < 0.4) {
                    e.getEntity().getWorld().strikeLightning(e.getEntity().getLocation().add(Math.random()*10, 0, Math.random()*10));
                }

                if(Math.random() < 0.2) {
                    ((Zombie) e.getEntity()).setHealth(((Zombie) e.getEntity()).getHealth() + 3);
                }
                if(Math.random() < 0.1) {
                    //drop the player's helmet
                    if(((Player) e.getDamager()).getInventory().getHelmet() != null) {
                        e.getEntity().getWorld().dropItemNaturally(e.getDamager().getLocation().add(0, 2, 0), ((Player) e.getDamager()).getInventory().getHelmet());
                        e.getEntity().getWorld().playSound(e.getDamager().getLocation(), Sound.ENTITY_ITEM_BREAK,  SoundCategory.HOSTILE, 1, 1);
                        ((Player) e.getDamager()).getInventory().setHelmet(null);
                        e.getEntity().getWorld().spawnParticle(Particle.FALLING_DRIPSTONE_LAVA, e.getDamager().getLocation().add(0, 2, 0), 10, 0.5, 0.5, 0.5, 0.1);
                    }
                } else {
                    ((Player) e.getDamager()).damage(3, e.getEntity());

                    if(Math.random() < 0.1) {
                        e.getEntity().getWorld().dropItemNaturally(e.getDamager().getLocation().add(0, 2, 0), ((Player) e.getDamager()).getInventory().getItemInMainHand());
                        e.getEntity().getWorld().playSound(e.getDamager().getLocation(), Sound.ENTITY_ITEM_BREAK,  SoundCategory.HOSTILE, 1, 1);
                        ((Player) e.getDamager()).getInventory().setItemInMainHand(null);
                        e.getEntity().getWorld().spawnParticle(Particle.FALLING_DRIPSTONE_WATER, e.getDamager().getLocation().add(0, 2, 0), 10, 0.5, 0.5, 0.5, 0.1);
                    }
                }


                if(e.getEntity().hasMetadata("cosmonautHits")) {
                    int hits = e.getEntity().getMetadata("cosmonautHits").get(0).asInt();
                    e.getEntity().setMetadata("cosmonautHits", new FixedMetadataValue(AywenCraftPlugin.getInstance(), hits+1));
                } else {
                    e.getEntity().setMetadata("cosmonautHits", new FixedMetadataValue(AywenCraftPlugin.getInstance(), 1));
                }



                if(e.getEntity().getMetadata("cosmonautHits").get(0).asInt() % 10 == 0) {
                    e.getEntity().getWorld().playSound(e.getEntity().getLocation(), Sound.ENTITY_ZOMBIE_AMBIENT,  SoundCategory.HOSTILE, 1, 1);
                    for (int i = 0; i < Math.random()*5; i++) {
                        e.getEntity().getWorld().spawnEntity(e.getEntity().getLocation().add(Math.random()*3, 2, Math.random()*3), EntityType.ZOMBIE);
                    }
                }

                if(e.getEntity().getMetadata("cosmonautHits").get(0).asInt() % 15 == 0) {
                    e.getEntity().getWorld().playSound(e.getEntity().getLocation(), Sound.ENTITY_ZOMBIE_AMBIENT,  SoundCategory.HOSTILE, 1, 1);

                    e.getEntity().getWorld().createExplosion(e.getDamager().getLocation().add(0, -1, 0), 10, false, false);
                    e.getEntity().getWorld().spawnParticle(Particle.EXPLOSION, e.getDamager().getLocation().add(0, -1, 0), 10, 0.5, 0.5, 0.5, 0.1);
                }


                if(e.getEntity().getMetadata("cosmonautHits").get(0).asInt() % 20 == 0) {
                    e.getEntity().getWorld().playSound(e.getEntity().getLocation(), Sound.ENTITY_ZOMBIE_AMBIENT,  SoundCategory.HOSTILE, 1, 1);
                    e.getDamager().setVelocity(new Vector(0, 1.5, 0));
                }

            }
        }
    }

    @EventHandler
    public void onBossDeath(EntityDeathEvent e) {
        if(e.getEntity().getWorld() != Bukkit.getWorld("moon")) return;
        if(e.getEntity().hasMetadata("isCosmonaut")) {
            Bukkit.broadcast(Component.text("§6§lLe cosmonaute a été vaincu !"));
            removeBossBar(e.getEntity().getUniqueId());
            e.getEntity().getWorld().playSound(e.getEntity().getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, SoundCategory.MASTER, 1, 1);
            e.getEntity().getWorld().spawnParticle(Particle.EXPLOSION, e.getEntity().getLocation(), 10, 0.5, 0.5, 0.5, 0.1);
            e.getEntity().getWorld().spawnParticle(Particle.EXPLOSION_EMITTER, e.getEntity().getLocation(), 100, 0.5, 0.5, 0.5, 0.1);
            e.getEntity().getWorld().dropItemNaturally(e.getEntity().getLocation(), CustomStack.getInstance("space:moon_sword").getItemStack());
        }
    }

    private void removeBossBar(UUID bossUUID) {
        BossBar bar = bossBars.remove(bossUUID);
        if (bar != null) {
            bar.removeAll();
        }
    }



    // Cosmonaut -- END

    @EventHandler
    public void onMoonSwordHit(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player && e.getEntity() instanceof LivingEntity) {

            ItemStack item = ((Player) e.getDamager()).getItemInHand();
            CustomStack customStack = CustomStack.byItemStack(item);

            if (customStack != null && customStack.getNamespacedID().equals("space:moon_sword")) {
                // 1.5x damage on the moon
                if(e.getEntity().getWorld().getName().equals("moon")) {
                    e.setDamage(e.getDamage()*1.5);
                }

                if(Math.random() < 0.99) {
                    e.getEntity().setVelocity(new Vector(0, Math.random()*0.5+1, 0));
                }


                e.getEntity().getWorld().playSound(e.getEntity().getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_WOODEN_DOOR,  SoundCategory.HOSTILE, 1, 1);
                e.getEntity().getWorld().spawnParticle(Particle.CRIT, e.getEntity().getLocation().add(0, 1, 0), 10, 0.5, 0.5, 0.5, 0.1);
            }

        }

    }

}
