package fr.communaywen.core.guideline.listeners.dream;

import dev.lone.itemsadder.api.CustomStack;
import fr.communaywen.core.guideline.GuidelineManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

public class TreeCraftAdvancements implements Listener {

    public boolean is(ItemStack stack, String namespacedId) {
        CustomStack cs = CustomStack.byItemStack(stack);
        return cs != null && cs.getNamespacedID().equals(namespacedId);
    }

    @EventHandler
    public void onCraft(CraftItemEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        Recipe recipe = event.getRecipe();
        ItemStack result = recipe.getResult();
        CraftingInventory inv = event.getInventory();

        CustomStack resultCS = CustomStack.byItemStack(result);

        if (resultCS != null) {
            if (resultCS.getNamespacedID().equals("aywen:dream_planks")) {
                GuidelineManager.getAPI().getAdvancement("dream:planks").grant(player);
            }
            if (resultCS.getNamespacedID().equals("aywen:cloud_soup")) {
                GuidelineManager.getAPI().getAdvancement("dream:cloudsoup").grant(player);
            }
            return;
        }

        if ((   is(inv.getItem(3), "aywen:dream_planks") &&
                is(inv.getItem(4), "aywen:dream_planks") &&
                is(inv.getItem(5), "aywen:dream_planks") &&
                is(inv.getItem(0), "aywen:cloud") &&
                is(inv.getItem(1), "aywen:cloud") &&
                is(inv.getItem(2), "aywen:cloud")
            ) || (is(inv.getItem(6), "aywen:dream_planks") &&
                  is(inv.getItem(7), "aywen:dream_planks") &&
                  is(inv.getItem(8), "aywen:dream_planks") &&
                  is(inv.getItem(3), "aywen:cloud") &&
                  is(inv.getItem(4), "aywen:cloud") &&
                  is(inv.getItem(5), "aywen:cloud"))) {
            GuidelineManager.getAPI().getAdvancement("dream:bed").grant(player);
        }
    }
}
