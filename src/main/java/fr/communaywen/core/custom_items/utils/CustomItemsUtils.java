package fr.communaywen.core.custom_items.utils;

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

import javax.annotation.Nullable;
import java.util.ArrayList;

public class CustomItemsUtils {

    public static ItemStack createItem(Material material, String name, ArrayList<String> lore) {

        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemStackMeta = itemStack.getItemMeta();
        itemStackMeta.setDisplayName(name);
        itemStackMeta.setLore(lore);
        itemStack.setItemMeta(itemStackMeta);

        return itemStack;
    }

    public static void destroyArea(BlockFace face, Block brokenBlock, int radius, int depth, @Nullable ItemStack itemToDamage) {

        int x;
        int y;
        int z;
        ItemMeta itemMeta = null;
        Damageable damageable = null;
        Block blockToBrake;

        if (itemToDamage != null) {
            itemMeta = itemToDamage.getItemMeta();
            damageable = (Damageable) itemMeta;
        }

        switch (face) {
            case NORTH:
            case SOUTH:
                for (x = -radius; x <= radius; x++) {
                    for (y = -radius; y <= radius; y++) {
                        for (z = -depth; z <= depth; z++) {

                            blockToBrake = brokenBlock.getRelative(x, y, z);

                            if (!blockToBrake.getType().equals(brokenBlock.getType())) {
                                continue;
                            }

                            blockToBrake.breakNaturally(itemToDamage);

                            if (itemToDamage == null) {
                                continue;
                            }

                            damageable.setDamage(damageable.getDamage() + 1);
                        }
                    }
                }
                break;
            case EAST:
            case WEST:
                for (x = -depth; x <= depth; x++) {
                    for (y = -radius; y <= radius; y++) {
                        for (z = -radius; z <= radius; z++) {

                            blockToBrake = brokenBlock.getRelative(x, y, z);

                            if (!blockToBrake.getType().equals(brokenBlock.getType())) {
                                continue;
                            }

                            blockToBrake.breakNaturally(itemToDamage);

                            if (itemToDamage == null) {
                                continue;
                            }

                            damageable.setDamage(damageable.getDamage() + 1);
                        }
                    }
                }
                break;
            case UP:
            case DOWN:
                for (x = -radius; x <= radius; x++) {
                    for (y = -depth; y <= depth; y++) {
                        for (z = -radius; z <= radius; z++) {

                            blockToBrake = brokenBlock.getRelative(x, y, z);

                            if (!blockToBrake.getType().equals(brokenBlock.getType())) {
                                continue;
                            }

                            blockToBrake.breakNaturally(itemToDamage);

                            if (itemToDamage == null) {
                                continue;
                            }

                            damageable.setDamage(damageable.getDamage() + 1);
                        }
                    }
                }
                break;
        }

        if (itemToDamage == null) {
            return;
        }

        itemToDamage.setItemMeta(itemMeta);
    }

    public static BlockFace getDestroyedBlockFace(Player player) {
        Location eyeLoc = player.getEyeLocation();
        RayTraceResult result = player.getLocation().getWorld().rayTraceBlocks(eyeLoc,eyeLoc.getDirection(),10, FluidCollisionMode.NEVER);

        if(result.getHitBlockFace() == null) {
            return null;
        }

        return result.getHitBlockFace();
    }

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
