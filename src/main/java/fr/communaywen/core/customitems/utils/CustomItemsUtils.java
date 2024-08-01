package fr.communaywen.core.customitems.utils;

import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.RayTraceResult;

import java.util.ArrayList;

public class CustomItemsUtils {

    /**
     * Create an item with a type, a name and a lore
     * @param material The type of the item
     * @param name The name of the item
     * @param lore The lore of the item
     * @return The item created
     */
    public static ItemStack createItem(Material material, String name, ArrayList<String> lore) {

        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemStackMeta = itemStack.getItemMeta();
        itemStackMeta.setDisplayName(name);
        itemStackMeta.setLore(lore);
        itemStack.setItemMeta(itemStackMeta);

        return itemStack;
    }

    /**
     * Destroy an area around a block
     * @param face The face of the block
     * @param brokenBlock The block to break
     * @param radius The radius of the area
     * @param depth The depth of the area
     * @param itemToDamage The item to damage
     */
    public static void destroyArea(BlockFace face, Block brokenBlock, int radius, int depth, ItemStack itemToDamage) {

        int x;
        int y;
        int z;
        int damageToDeal = 0;
        ItemMeta itemMeta = itemToDamage.getItemMeta();
        Damageable damageable = (Damageable) itemMeta;
        Block blockToBrake;
        Material brokenBlockType = brokenBlock.getType();

        switch (face) {
            case NORTH:
            case SOUTH:
                for (x = -radius; x <= radius; x++) {
                    for (y = -radius; y <= radius; y++) {
                        for (z = -depth; z <= depth; z++) {

                            if (brokenBlock.getType().isAir()) {
                                brokenBlock.setType(brokenBlockType);
                            }

                            blockToBrake = brokenBlock.getRelative(x, y, z);

                            if (blockToBrake.equals(brokenBlock)) {
                                continue;
                            }

                            if (blockToBrake.getType().getHardness() > 41) {
                                continue;
                            }

                            if (!blockToBrake.getType().equals(brokenBlock.getType())) {
                                continue;
                            }

                            blockToBrake.breakNaturally(itemToDamage);
                            damageToDeal++;
                        }
                    }
                }
                break;
            case EAST:
            case WEST:
                for (x = -depth; x <= depth; x++) {
                    for (y = -radius; y <= radius; y++) {
                        for (z = -radius; z <= radius; z++) {

                            if (brokenBlock.getType().isAir()) {
                                brokenBlock.setType(brokenBlockType);
                            }

                            blockToBrake = brokenBlock.getRelative(x, y, z);

                            if (blockToBrake.equals(brokenBlock)) {
                                continue;
                            }

                            if (blockToBrake.getType().getHardness() > 41) {
                                continue;
                            }

                            if (!blockToBrake.getType().equals(brokenBlock.getType())) {
                                continue;
                            }

                            blockToBrake.breakNaturally(itemToDamage);
                            damageToDeal++;
                        }
                    }
                }
                break;
            case UP:
            case DOWN:
                for (x = -radius; x <= radius; x++) {
                    for (y = -depth; y <= depth; y++) {
                        for (z = -radius; z <= radius; z++) {

                            if (brokenBlock.getType().isAir()) {
                                brokenBlock.setType(brokenBlockType);
                            }

                            blockToBrake = brokenBlock.getRelative(x, y, z);

                            if (blockToBrake.equals(brokenBlock)) {
                                continue;
                            }

                            if (blockToBrake.getType().getHardness() > 41) {
                                continue;
                            }

                            if (!blockToBrake.getType().equals(brokenBlock.getType())) {
                                continue;
                            }

                            blockToBrake.breakNaturally(itemToDamage);
                            damageToDeal++;
                        }
                    }
                }
                break;
        }
        damageable.setDamage(damageable.getDamage() + (damageToDeal/2));
        itemToDamage.setItemMeta(itemMeta);
    }

    /**
     * Get the face of the block destroyed by the player
     * @param player The player
     * @return The face of the block destroyed
     */
    public static BlockFace getDestroyedBlockFace(Player player) {
        Location eyeLoc = player.getEyeLocation();
        RayTraceResult result = player.getLocation().getWorld().rayTraceBlocks(eyeLoc,eyeLoc.getDirection(),10, FluidCollisionMode.NEVER);

        if(result.getHitBlockFace() == null) {
            return null;
        }

        return result.getHitBlockFace();
    }

    /**
     * Check if two ItemStack are similar ignoring the damage (durability)
     * @param itemStack1 The first ItemStack
     * @param itemStack2 The second ItemStack
     * @return True if the ItemStack are similar ignoring the damage, false otherwise
     */
    public static boolean isSimilarIgnoringDamage(ItemStack itemStack1, ItemStack itemStack2) {
        ItemStack cloneItem1 = itemStack1.clone();
        ItemStack cloneItem2 = itemStack2.clone();

        if (!cloneItem1.hasItemMeta() || !cloneItem2.hasItemMeta()) {
            return false;
        }

        ItemMeta itemMeta1 = cloneItem1.getItemMeta();
        ItemMeta itemMeta2 = cloneItem2.getItemMeta();
        Damageable damageable1 = (Damageable) itemMeta1;
        Damageable damageable2 = (Damageable) itemMeta2;
        damageable1.setDamage(0);
        damageable2.setDamage(0);
        cloneItem1.setItemMeta(itemMeta1);
        cloneItem2.setItemMeta(itemMeta2);

        return cloneItem1.isSimilar(cloneItem2);
    }
}
