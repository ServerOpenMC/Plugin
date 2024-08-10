package fr.communaywen.core.customitems.items;

import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.customitems.objects.CustomItems;
import fr.communaywen.core.customitems.utils.CustomItemsUtils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

@Getter
@Setter
@Credit("Fnafgameur")
public class NetheriteHammer implements CustomItems {

    private String name;

    private ItemStack itemStack;

    private final ArrayList<String> recipe = new ArrayList<>() {{
        add("BBB");
        add("BSB");
        add("XSX");
    }};

    private final HashMap<Character, ItemStack> ingredients = new HashMap<>() {{
        put('B', new ItemStack(Material.NETHERITE_BLOCK));
        put('S', new ItemStack(Material.STICK));
    }};

    @Override
    public String getNamespacedID() {
        return "customitems:netherite_hammer";
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

        CustomItemsUtils.destroyArea(playerFacing, brokenBlock, 1, 2, itemToDamage);
    }

    @Override
    public void onAnvil(PrepareAnvilEvent event) {

        ItemStack item0 = event.getInventory().getItem(0);

        if (item0 == null) {
            return;
        }

        Player player = (Player) event.getView().getPlayer();

        player.sendMessage("§cVous ne pouvez pas modifier cet objet");
        player.getInventory().addItem(item0);
        player.closeInventory();
    }
}
