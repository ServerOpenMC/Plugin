package fr.communaywen.core.customitems.objects;

import fr.communaywen.core.credit.Credit;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

@Credit("Fnafgameur")
@Getter
public abstract class CustomItems {

    @Setter
    private String name;
    @Setter
    private ItemStack itemStack;

    private final ArrayList<String> recipe;
    private final HashMap<Character, ItemStack> ingredients;
    private final String namespacedID;

    public CustomItems(ArrayList<String> recipe, HashMap<Character, ItemStack> ingredients, String namespacedID) {
        this.recipe = recipe;
        this.ingredients = ingredients;
        this.namespacedID = namespacedID;
    }
}
