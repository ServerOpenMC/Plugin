package fr.communaywen.core.custom_items.items;

import fr.communaywen.core.custom_items.objects.CustomItems;
import fr.communaywen.core.custom_items.utils.CustomItemsUtils;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

public class DiamondHammer implements CustomItems {

    @Getter
    private final HashMap<Character, Material> ingredients = new HashMap<>() {{
        put('B', Material.DIAMOND_BLOCK);
        put('S', Material.STICK);
    }};

    @Override
    public ItemStack getItemStack() {
        return CustomItemsUtils.createItem(Material.DIAMOND_PICKAXE, "§bMarteau en diamant", new ArrayList<>() {{
            add("§7Casse une zone de 5x5 blocs");
        }});
    }

    @Override
    public void onBlockBreak(BlockBreakEvent event) {


    }


}
