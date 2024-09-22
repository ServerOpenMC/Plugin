package fr.communaywen.core.luckyblocks.utils;

import dev.lone.itemsadder.api.CustomStack;
import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.claim.RegionManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;

public class LBUtils {

    public static final String BOB_NAME = "§4Bob";
    public static String getBlockNamespaceID() {
        return "luckyblock:luckyblock";
    }

    /**
     * Get the lucky block item
     * @return The lucky block item
     */
    public static ItemStack getLuckyBlockItem() {
        CustomStack lb = CustomStack.getInstance(getBlockNamespaceID());

        return lb.getItemStack();
    }

    /**
     * Check if a player can destroy a block in a claim
     * @param player The player involved
     * @param block The block to destroy
     * @return True if the player can destroy the block, false otherwise
     */
    public static boolean canDestroyBlockInClaim(Player player, Block block) {
        for (RegionManager region : AywenCraftPlugin.getInstance().regions) {
            if (region.isInArea(block.getLocation()) && !region.isTeamMember(player.getUniqueId())) {
                return false;
            }
        }

        return true;
    }

    /**
     * Spawn a zombie with a full netherite armor and a netherite sword
     * @param location The location where the zombie will spawn
     */
    public static void spawnBob(Location location) {

        World world = location.getWorld();
        ItemStack helmet = new ItemStack(Material.NETHERITE_HELMET);
        ItemStack chestplate = new ItemStack(Material.NETHERITE_CHESTPLATE);
        ItemStack boots = new ItemStack(Material.NETHERITE_BOOTS);
        ItemStack leggings = new ItemStack(Material.NETHERITE_LEGGINGS);
        ItemStack sword = new ItemStack(Material.NETHERITE_SWORD);

        // Setup of the armor
        helmet.addEnchantment(Enchantment.PROTECTION, 4);
        chestplate.addEnchantment(Enchantment.THORNS, 3);
        chestplate.addEnchantment(Enchantment.PROTECTION, 4);
        leggings.addEnchantment(Enchantment.PROTECTION, 4);
        boots.addEnchantment(Enchantment.PROTECTION, 4);

        // Setup of the sword
        sword.addEnchantment(Enchantment.SHARPNESS, 5);
        sword.addEnchantment(Enchantment.FIRE_ASPECT, 2);

        // Spawn of the zombie
        Zombie zombie = world.spawn(location, Zombie.class);
        zombie.setCustomName(BOB_NAME);
        zombie.setCustomNameVisible(true);
        zombie.getEquipment().setHelmet(helmet);
        zombie.getEquipment().setChestplate(chestplate);
        zombie.getEquipment().setLeggings(leggings);
        zombie.getEquipment().setBoots(boots);
        zombie.getEquipment().setItemInMainHand(sword);

        double zombieHealth = zombie.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        zombie.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.5);
        zombie.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(zombieHealth * 1.5);
        zombie.setMaxHealth(zombieHealth * 1.5);
    }
}
