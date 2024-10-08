package fr.communaywen.core.dreamdim.listeners;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;
import dev.lone.itemsadder.api.CustomStack;
import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.dreamdim.BossFight;
import fr.communaywen.core.dreamdim.DreamUtils;
import fr.communaywen.core.guideline.GuidelineManager;
import fr.communaywen.core.utils.Skull;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class BossManager implements Listener {
    HashMap<Entity, BossFight> fights = new HashMap<>();
    HashMap<Player, Entity> bosses = new HashMap<>();

    public void paste(@NotNull Location location, File file) {
        ClipboardFormat clipboardFormat = ClipboardFormats.findByFile(file);
        Clipboard clipboard;

        BlockVector3 blockVector3 = BlockVector3.at(location.getBlockX(), location.getBlockY(), location.getBlockZ());

        if (clipboardFormat != null) {
            try (ClipboardReader clipboardReader = clipboardFormat.getReader(new FileInputStream(file))) {

                if (location.getWorld() == null)
                    throw new NullPointerException("Failed to paste schematic due to world being null");

                World world = BukkitAdapter.adapt(location.getWorld());
                EditSession editSession = WorldEdit.getInstance().newEditSessionBuilder().world(world).build();
                clipboard = clipboardReader.read();

                Operation operation = new ClipboardHolder(clipboard)
                        .createPaste(editSession)
                        .to(blockVector3)
                        .ignoreAirBlocks(false)
                        .build();

                try {
                    Operations.complete(operation);
                    editSession.close();
                } catch (WorldEditException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @EventHandler
    public void onBlockBreak(@NotNull BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (!player.getWorld().getName().equals("dreamworld")){ return; }
        if (!block.getType().equals(Material.ANCIENT_DEBRIS)) { return; }

        event.setDropItems(false);

        ItemStack essence = CustomStack.getInstance("aywen:dream_essence").getItemStack();
        DreamUtils.setFromDream(essence);

        GuidelineManager.getAPI().getAdvancement("dream:essence").grant(player);

        player.getWorld().dropItemNaturally(block.getLocation(), essence);

        if (bosses.containsKey(player)) { return; } // Le joueur est déjà en bossfight *comment ??* donc on passe

        if (new Random().nextDouble() <= 0.01) {
            player.getServer().broadcast(Component.text(player.getName()+" a commencé un combat contre le ").append(Component.text("Dévorêve").color(TextColor.color(16733695))));
            player.sendTitle("§5Le Dévorêve", "Tu as fais apparaître un boss",0, 3*20, 20);
            player.playSound(player.getEyeLocation(), Sound.ENTITY_LIGHTNING_BOLT_IMPACT, SoundCategory.AMBIENT, 1, 1);

            paste(player.getLocation(), new File(AywenCraftPlugin.getInstance().getDataFolder(), "dream_boss_area.schem"));
            Location location = player.getLocation();
            location.setYaw(-90);
            location.setPitch((float) -5.5);
            player.teleport(location);

            Location spawnloc = player.getLocation().add(11, 1, 0);

            // Pour qu'il spawn au centre du block
            spawnloc.setX(spawnloc.getBlockX()+0.5);
            spawnloc.setY(spawnloc.getBlockY()+0.5);
            spawnloc.setZ(spawnloc.getBlockZ()+0.5);

            spawnloc.setYaw(90);
            Skeleton boss = (Skeleton) block.getWorld().spawnEntity(spawnloc, EntityType.SKELETON, CreatureSpawnEvent.SpawnReason.BUILD_WITHER);
            boss.setAI(false);

            boss.getPersistentDataContainer().set(NamespacedKey.minecraft("boss_type"), PersistentDataType.STRING, "devoreve");
            EntityEquipment equipment = boss.getEquipment();
            boss.customName(Component.text("§l§5Dévorêve"));
            boss.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(400);
            boss.setHealth(400);
            boss.setAggressive(true);

            equipment.setChestplate(getChestplate());
            equipment.setItem(EquipmentSlot.HAND, getWeapon());
            equipment.setHelmet(getHelmet());

            boss.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, PotionEffect.INFINITE_DURATION, 1, false, false));
            boss.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, PotionEffect.INFINITE_DURATION, 0, false, false));

            boss.setAI(true);
            event.setCancelled(true);

            BlockVector3 pos1 = BlockVector3.at(location.getBlockX()-3, location.getBlockY()-2, location.getBlockZ()-5);
            BlockVector3 pos2 = BlockVector3.at(location.getBlockX()+14, location.getBlockY()+6, location.getBlockZ()+5);
            CuboidRegion region = new CuboidRegion(pos1, pos2); //18x9x11 | Profondeur, Hauteur, Largeur

            BossFight bossFight = new BossFight(player, boss, region, block.getWorld());
            fights.put(boss, bossFight);
            bosses.put(player, boss);
        }
    }

    @EventHandler
    public void onStrangerDamage(@NotNull EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) return;
        if (!(fights.containsKey(event.getEntity()))) { return; }

        if (bosses.get(player) != event.getEntity()) {
            player.kick(Component.text("Tu t'invite à un combat qui n'est pas le tiens."));
        }
    }

    private void onLose(Player player) {
        if (!bosses.containsKey(player)) return;

        BossFight fight = fights.get(bosses.get(player));

        player.getServer().broadcast(Component.text(player.getName()+" a été vaincu par le ").append(Component.text("Dévorêve").color(TextColor.color(16733695))));
        fight.getBoss().remove();
        fight.clean();
        fights.remove(bosses.get(player));
        bosses.remove(player);
    }

    @EventHandler
    public void dimensionSwap(@NotNull PlayerChangedWorldEvent event) {
        if (event.getFrom().getName().equals("dreamworld")) {
            onLose(event.getPlayer());
        }
    }

    @EventHandler
    public void disableEnchant(@NotNull PrepareAnvilEvent event) {
        ItemStack item = event.getResult();
        if (item == null) return;
        if (item.getItemMeta() == null) return;
        if (event.getInventory().getFirstItem() == null) return;

        if (!item.getItemMeta().getPersistentDataContainer().has(NamespacedKey.fromString("replenish", AywenCraftPlugin.getInstance()), PersistentDataType.BOOLEAN)) return;
        if (event.getInventory().getFirstItem().getEnchantments() != item.getEnchantments()) {
            event.setResult(null);
        }
    }

    @EventHandler
    public void onBossDeath(@NotNull EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        if (!entity.getWorld().getName().equals("dreamworld")) return;
        if (!(Objects.equals(entity.getPersistentDataContainer().get(NamespacedKey.minecraft("boss_type"), PersistentDataType.STRING), "devoreve"))) return;
        if (!(event.getDamageSource().getCausingEntity() instanceof Player player)) return;

        BossFight fight = fights.get(entity);
        fight.clean();
        fights.remove(entity);
        bosses.remove(player);

        if (player != fight.getPlayer()) {
            player.kick(Component.text("Tu as volé l'honneur d'un autre."));
            return;
        }

        player.getServer().broadcast(Component.text(player.getName()+" a gagné son combat contre le ").append(Component.text("Dévorêve").color(TextColor.color(16733695))));

        event.setDroppedExp(2500);

        List<ItemStack> drops = event.getDrops();
        drops.clear();
        Random random = new Random();

        ItemStack essence = CustomStack.getInstance("aywen:dream_essence").getItemStack();
        essence.setAmount(random.nextInt(4)+1);
        DreamUtils.setFromDream(essence);
        drops.add(essence);

        GuidelineManager.getAPI().getAdvancement("dream:dreameater").grant(player);
        if (random.nextDouble() <= 0.3) {
            GuidelineManager.getAPI().getAdvancement("dream:devoreve/chestplate").grant(player);
            ItemStack chestplate = getChestplate();

            drops.add(chestplate);
        }

        if (random.nextDouble() <= 0.3) {
            GuidelineManager.getAPI().getAdvancement("dream:devoreve/skull").grant(player);
            ItemStack head = getHelmet();

            drops.add(head);
        }

        if (random.nextDouble() <= 0.1) {
            GuidelineManager.getAPI().getAdvancement("dream:devoreve/weapon").grant(player);
            ItemStack hoe = getWeapon();
            hoe.removeEnchantments();
            hoe.addEnchant(Enchantment.UNBREAKING, 5, true);

            ItemMeta meta = hoe.getItemMeta();
            meta.getPersistentDataContainer().set(NamespacedKey.fromString("replenish", AywenCraftPlugin.getInstance()), PersistentDataType.BOOLEAN, true);
            hoe.setItemMeta(meta);

            drops.add(hoe);
        }
    }

    public static @NotNull ItemStack getWeapon() {
        ItemStack weapon = new ItemStack(Material.NETHERITE_HOE);
        weapon.addEnchant(Enchantment.SHARPNESS, 3, true);

        ItemMeta meta = weapon.getItemMeta();
        meta.displayName(Component.text("Houe du Dévorêve").color(TextColor.color(16733695)).decoration(TextDecoration.ITALIC, false));
        meta.lore(List.of(
                Component.text("§7Replantation"),
                Component.text(""),
                Component.text("§4D'après les pires légendes,"),
                Component.text("§4il s'en servirait pour"),
                Component.text("§4dépouiller la peau des"),
                Component.text("§4réveurs égarés.")
                ));
        meta.setEnchantmentGlintOverride(false);
        meta.setFireResistant(true);
        weapon.setItemMeta(meta);

        return weapon;
    }

    public static @NotNull ItemStack getHelmet() {
        try {
            ItemStack helmet = Skull.getCustomSkull("http://textures.minecraft.net/texture/3553c0fba71df9f4d613edee5529ca5a2199a52a017e5ff1dcba76af203f36ab");
            ;

            ItemMeta meta = helmet.getItemMeta();
            meta.displayName(Component.text("§r§dTête du Dévorêve"));
            meta.setEnchantmentGlintOverride(false);
            meta.setFireResistant(true);
            helmet.setItemMeta(meta);

            helmet.addEnchant(Enchantment.RESPIRATION, 1, true);

            helmet.lore(List.of(
                    Component.text("§8Wazzup?")
            ));

            return helmet;
        } catch (Exception e) {
            e.printStackTrace();
            return new ItemStack(Material.NETHERITE_HELMET);
        }
    }

    public static @NotNull ItemStack getChestplate() {
        ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
        LeatherArmorMeta meta = (LeatherArmorMeta) chestplate.getItemMeta();
        meta.setColor(Color.fromRGB(32,32,32));
        chestplate.setItemMeta(meta);

        ArmorMeta armorMeta = (ArmorMeta) chestplate.getItemMeta();
        armorMeta.setTrim(new ArmorTrim(TrimMaterial.NETHERITE, TrimPattern.SPIRE));
        armorMeta.displayName(Component.text("§r§dPlastron du Dévorêve"));
        armorMeta.setFireResistant(true);
        chestplate.setItemMeta(armorMeta);

        return chestplate;
    }
}
