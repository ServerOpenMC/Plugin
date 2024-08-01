package fr.communaywen.core.fallblood;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.commands.FallBloodCommand;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapedRecipe;

public class BandageRecipe {
    public BandageRecipe() {
        NamespacedKey key = new NamespacedKey(AywenCraftPlugin.getInstance(), "bandage");
        ShapedRecipe recipe = new ShapedRecipe(key, FallBloodCommand.getBandage());

        recipe.shape("PWP");
        
        recipe.setIngredient('P', Material.PAPER);
        recipe.setIngredient('W', Material.GLASS_BOTTLE);
        
        Bukkit.addRecipe(recipe);
    }
}
