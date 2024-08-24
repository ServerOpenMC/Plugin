package fr.communaywen.core.customitems.items;

import dev.lone.itemsadder.api.CustomStack;
import fr.communaywen.core.customitems.objects.CustomItems;
import fr.communaywen.core.customitems.utils.CustomItemsUtils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

@Getter
public class IronHammer implements CustomItems {

    @Setter
    private String name;

    @Setter
    private ItemStack itemStack;

    private final ArrayList<String> recipe = new ArrayList<>() {{
        add("BBB");
        add("BSB");
        add("XSX");
    }};

    private final HashMap<Character, ItemStack> ingredients = new HashMap<>() {{
        put('B', new ItemStack(Material.IRON_BLOCK));
        put('S', new ItemStack(Material.STICK));
    }};

    @Override
    public String getNamespacedID() {
        return "customitems:iron_hammer";
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

        CustomItemsUtils.destroyArea(playerFacing, brokenBlock, 1, 0, itemToDamage, player);
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
