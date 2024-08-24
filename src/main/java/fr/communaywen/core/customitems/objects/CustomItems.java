package fr.communaywen.core.customitems.objects;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

@Getter
public abstract class CustomItems {

    @Setter
    private String name;
    @Setter
    private ItemStack itemStack;

    private final ArrayList<String> recipe;
    private final HashMap<Character, ItemStack> ingredients;
    private final String namespacedID;

    public CustomItems(String name, ItemStack itemStack, ArrayList<String> recipe, HashMap<Character, ItemStack> ingredients, String namespacedID) {
        this.name = name;
        this.itemStack = itemStack;
        this.recipe = recipe;
        this.ingredients = ingredients;
        this.namespacedID = namespacedID;
    }
}
