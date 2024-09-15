package fr.communaywen.core.customitems.items;

import dev.lone.itemsadder.api.CustomStack;
import dev.lone.itemsadder.api.CustomBlock;
import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import fr.communaywen.core.customitems.objects.CustomItems;
import fr.communaywen.core.customitems.objects.CustomItemsEvents;
import fr.communaywen.core.customitems.utils.CustomItemsUtils;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

@Feature("Diamond Hammer")
@Getter
@Credit("Fnafgameur")
/* Credit to
 * Dartsgame for the 3D model
 */

public class DiamondHammer extends CustomItems implements CustomItemsEvents {

    public DiamondHammer() {
        super(
                new ArrayList<>() {{
                    add("BBB");
                    add("BSB");
                    add("XSX");
                }},
                new HashMap<>() {{
                    put('B', new ItemStack(Material.DIAMOND_BLOCK));
                    put('S', new ItemStack(Material.STICK));
                }},
                "customitems:diamond_hammer"
        );
    }

    @Override
    public void onBlockBreak(BlockBreakEvent event) {

        Block brokenBlock = event.getBlock();
        CustomBlock customBlock = CustomBlock.byAlreadyPlaced(brokenBlock);

        if (customBlock != null) {
            event.setCancelled(true);
            return;
        }

        Player player = event.getPlayer();
        BlockFace playerFacing = CustomItemsUtils.getDestroyedBlockFace(player);

        if (playerFacing == null) {
            return;
        }

        playerFacing = playerFacing.getOppositeFace();
        ItemStack itemToDamage = event.getPlayer().getInventory().getItemInMainHand();

        CustomItemsUtils.destroyArea(playerFacing, brokenBlock, 1, 2, itemToDamage, player);
    }

    @Override
    public void onAnvil(PrepareAnvilEvent event) {

        ItemStack item0 = event.getInventory().getItem(0);

        if (item0 == null) {
            return;
        }

        ItemStack result = event.getResult();

        if (result == null) {
            return;
        }

        CustomStack customStack = CustomStack.byItemStack(result);

        if (customStack == null) {
            return;
        }

        if (!customStack.getNamespacedID().equals(getNamespacedID())) {
            return;
        }

        event.setResult(null);
    }
}
