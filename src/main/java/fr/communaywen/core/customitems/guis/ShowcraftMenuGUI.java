package fr.communaywen.core.customitems.guis;

import dev.xernas.menulib.Menu;
import dev.xernas.menulib.utils.InventorySize;
import dev.xernas.menulib.utils.ItemBuilder;
import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.customitems.objects.CustomItems;
import fr.communaywen.core.customitems.utils.CustomItemsUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Credit("Fnafgameur")
public class ShowcraftMenuGUI extends Menu {

    private final CustomItems customItems;

    public ShowcraftMenuGUI(Player owner, CustomItems customItems) {
        super(owner);
        this.customItems = customItems;
    }

    @Override
    public @NotNull String getName() {
        return "Showcraft | " + customItems.getName();
    }

    @Override
    public @NotNull InventorySize getInventorySize() {
        return InventorySize.LARGER;
    }

    @Override
    public void onInventoryClick(InventoryClickEvent inventoryClickEvent) {

    }

    @Override
    public @NotNull Map<Integer, ItemStack> getContent() {

        Map<Integer, ItemStack> content = fill(Material.BLACK_STAINED_GLASS_PANE);
        List<String> recipe = customItems.getRecipe();
        Map<Character, ItemStack> ingredients = customItems.getIngredients();

        for (int i = 0; i < recipe.size(); i++) {
            for (int j = 0; j < recipe.get(i).length(); j++) {
                content.put(10 + j + (i * 9), ingredients.get(recipe.get(i).charAt(j)));
            }
        }

        content.put(25, customItems.getItemStack());

        ArrayList<ItemBuilder> navigationBtns = CustomItemsUtils.getNavigationButtons(this);
        content.put(8, navigationBtns.get(1).setBackButton());

        return content;
    }
}
