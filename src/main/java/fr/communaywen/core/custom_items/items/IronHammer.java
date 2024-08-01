package fr.communaywen.core.custom_items.items;

import fr.communaywen.core.custom_items.objects.CustomItems;
import fr.communaywen.core.custom_items.utils.CustomItemsUtils;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

public class IronHammer implements CustomItems {

    @Getter
    private final HashMap<Character, Material> ingredients = new HashMap<>() {{
        put('B', Material.IRON_BLOCK);
        put('S', Material.STICK);
    }};

    @Override
    public ItemStack getItemStack() {
        return CustomItemsUtils.createItem(Material.IRON_PICKAXE, "§6Marteau en fer", new ArrayList<>() {{
            add("§7Casse une zone de 3x3 blocs");
        }});
    }

    @Override
    public void onBlockBreak(BlockBreakEvent event) {

        Block brokenBlock = event.getBlock();
        Player player = event.getPlayer();
        BlockFace playerFacing = CustomItemsUtils.getDestroyedBlockFace(player);

        if (playerFacing == null) {
            return;
        }

        playerFacing = playerFacing.getOppositeFace();
        ItemStack itemToDamage = event.getPlayer().getInventory().getItemInMainHand();

        CustomItemsUtils.destroyArea(playerFacing, brokenBlock, 1, 0, itemToDamage);
    }
}
