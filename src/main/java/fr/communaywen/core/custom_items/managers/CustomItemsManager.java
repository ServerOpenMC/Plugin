package fr.communaywen.core.custom_items.managers;

import fr.communaywen.core.custom_items.items.DiamondHammer;
import fr.communaywen.core.custom_items.items.IronHammer;
import fr.communaywen.core.custom_items.objects.CustomItems;
import fr.communaywen.core.custom_items.utils.CustomItemsUtils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomItemsManager {

    @Getter
    private ItemStack ironHammer;
    @Setter
    private ArrayList<CustomItems> customItems = new ArrayList<>();

    public CustomItemsManager() {
        createIronHammer();
        createDiamondHammer();
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

        this.ironHammer = result;
        customItems.add(ironHammer);
        Bukkit.addRecipe(ironHammerRecipe);
    }

    private void createDiamondHammer() {

        DiamondHammer diamondHammer = new DiamondHammer();
        ItemStack result = diamondHammer.getItemStack();

        ShapedRecipe shapedRecipe = new ShapedRecipe(NamespacedKey.minecraft("diamond_hammer"), result);
        shapedRecipe.shape(
                "BBB",
                "BSB",
                " S "
        );
        HashMap<Character, Material> ingredients = diamondHammer.getIngredients();

        for (Character key : ingredients.keySet()) {
            shapedRecipe.setIngredient(key, ingredients.get(key));
        }

        customItems.add(diamondHammer);
        Bukkit.addRecipe(shapedRecipe);
    }

}
