package fr.communaywen.core.customitems.utils;

import dev.lone.itemsadder.api.CustomBlock;
import dev.lone.itemsadder.api.CustomStack;
import dev.lone.itemsadder.api.ItemsAdder;
import dev.xernas.menulib.Menu;
import dev.xernas.menulib.utils.ItemBuilder;
import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.claim.RegionManager;
import lombok.Getter;
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

    @Getter
    private static final String namespaceStart = "customitems:";

    /**
     * Destroy an area around a block
     * @param face The face of the block
     * @param brokenBlock The block to break
     * @param radius The radius of the area
     * @param depth The depth of the area
     * @param itemInHand The item used
     * @param player The player
     */
    public static void destroyArea(BlockFace face, Block brokenBlock, int radius, int depth, ItemStack itemInHand, Player player) {

        int x;
        int y;
        int z;
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

                            CustomBlock customBlock = CustomBlock.byAlreadyPlaced(blockToBrake);

                            if (customBlock != null) {
                                continue;
                            }

                            if (!canDestroy(blockToBrake.getLocation(), player)) {
                                continue;
                            }

                            if (blockToBrake.getType().getHardness() > 41) {
                                continue;
                            }

                            if (!blockToBrake.getType().equals(brokenBlock.getType())) {
                                continue;
                            }

                            blockToBrake.breakNaturally(itemInHand);
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

                            CustomBlock customBlock = CustomBlock.byAlreadyPlaced(blockToBrake);

                            if (customBlock != null) {
                                continue;
                            }

                            if (!canDestroy(blockToBrake.getLocation(), player)) {
                                continue;
                            }

                            if (blockToBrake.getType().getHardness() > 41) {
                                continue;
                            }

                            if (!blockToBrake.getType().equals(brokenBlock.getType())) {
                                continue;
                            }

                            blockToBrake.breakNaturally(itemInHand);
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

                            CustomBlock customBlock = CustomBlock.byAlreadyPlaced(blockToBrake);

                            if (customBlock != null) {
                                continue;
                            }

                            if (!canDestroy(blockToBrake.getLocation(), player)) {
                                continue;
                            }

                            if (blockToBrake.getType().getHardness() > 41) {
                                continue;
                            }

                            if (!blockToBrake.getType().equals(brokenBlock.getType())) {
                                continue;
                            }

                            blockToBrake.breakNaturally(itemInHand);
                        }
                    }
                }
                break;
        }
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

    /**
     * Get the navigation buttons
     * @return Return a list with the navigation buttons (index 0 = back, index 1 = cancel, index 2 = next)
     */
    public static ArrayList<ItemBuilder> getNavigationButtons(Menu menu) {

        ArrayList<ItemBuilder> navigationButtons = new ArrayList<>();

        String previousName = "§cPrécédent";
        String cancelName = "§cAnnuler";
        String nextName = "§aSuivant";

        for (CustomStack customStack : ItemsAdder.getAllItems("_iainternal")) {
            if (customStack.getNamespacedID().equals("_iainternal:icon_back_orange")) {
                navigationButtons.addFirst(itemBuilderSetName(new ItemBuilder(menu, customStack.getItemStack()), previousName));
            } else if (customStack.getNamespacedID().equals("_iainternal:icon_cancel")) {
                navigationButtons.add(1, itemBuilderSetName(new ItemBuilder(menu, customStack.getItemStack()), cancelName));
            } else if (customStack.getNamespacedID().equals("_iainternal:icon_next_orange")) {
                navigationButtons.addLast(itemBuilderSetName(new ItemBuilder(menu, customStack.getItemStack()), nextName));
            }
        }

        if (navigationButtons.size() != 3) {
            navigationButtons.add(itemBuilderSetName(new ItemBuilder(menu, Material.RED_WOOL), previousName));
            navigationButtons.add(itemBuilderSetName(new ItemBuilder(menu, Material.BARRIER), cancelName));
            navigationButtons.add(itemBuilderSetName(new ItemBuilder(menu, Material.GREEN_WOOL), nextName));
        }

        return navigationButtons;
    }

    public static void damageItem(ItemStack itemToDamage, int damageNum) {
        ItemMeta itemMeta = itemToDamage.getItemMeta();
        Damageable damageable = (Damageable) itemMeta;
        damageable.setDamage(damageable.getDamage() + damageNum);
        itemToDamage.setItemMeta(itemMeta);
    }

    /**
     * Set the name of an ItemBuilder
     * @param itemBuilder The ItemBuilder
     * @param name The name
     * @return The ItemBuilder with the name set
     */
    private static ItemBuilder itemBuilderSetName(ItemBuilder itemBuilder, String name) {
        ItemMeta itemMeta = itemBuilder.getItemMeta();
        itemMeta.setDisplayName(name);
        itemBuilder.setItemMeta(itemMeta);

        return itemBuilder;
    }

    /**
     * Check if a player can destroy a block in a region
     * @param location The location of the block
     * @param player The player
     * @return True if the player can destroy the block, false otherwise
     */
    private static boolean canDestroy(Location location, Player player) {

        for (RegionManager region : AywenCraftPlugin.getInstance().regions) {
            if (!region.isInArea(location)) {
                continue;
            }

            return region.isTeamMember(player.getUniqueId());
        }

        return true;
    }
}
