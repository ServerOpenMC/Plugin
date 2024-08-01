package fr.communaywen.core.custom_items.managers;

import fr.communaywen.core.custom_items.items.DiamondHammer;
import fr.communaywen.core.custom_items.items.IronHammer;
import fr.communaywen.core.custom_items.objects.CustomItems;
import fr.communaywen.core.custom_items.utils.CustomItemsUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomItemsManager {

    private final ArrayList<CustomItems> customItems = new ArrayList<>();

    public CustomItemsManager() {
        createIronHammer();
    }

    public CustomItems getCustomItem(ItemStack itemStack) {
        for (CustomItems customItem : customItems) {
            if (CustomItemsUtils.isSimilarIgnoringDamage(itemStack, customItem.getItemStack())) {
                return customItem;
            }
        }
        return null;
    }

    private void createIronHammer() {

        IronHammer ironHammer = new IronHammer();
        ItemStack result = ironHammer.getItemStack();

        ShapedRecipe ironHammerRecipe = new ShapedRecipe(NamespacedKey.minecraft("iron_hammer"), result);
        ironHammerRecipe.shape(
                "BBB",
                "BSB",
                " S "
        );
        HashMap<Character, Material> ingredients = ironHammer.getIngredients();

        for (Character key : ingredients.keySet()) {
            ironHammerRecipe.setIngredient(key, ingredients.get(key));
        }

        customItems.add(ironHammer);
        Bukkit.addRecipe(ironHammerRecipe);
    }

}
