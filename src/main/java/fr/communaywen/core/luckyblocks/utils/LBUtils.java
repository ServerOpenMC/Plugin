package fr.communaywen.core.luckyblocks.utils;

import dev.lone.itemsadder.api.CustomStack;
import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.claim.RegionManager;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class LBUtils {

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
}
