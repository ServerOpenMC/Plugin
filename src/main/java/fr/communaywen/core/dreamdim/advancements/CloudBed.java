package fr.communaywen.core.dreamdim.advancements;

import dev.lone.itemsadder.api.CustomStack;
import fr.communaywen.core.dreamdim.AdvancementRegister;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

public class CloudBed implements Listener {
    AdvancementRegister register;
    ItemStack plank = CustomStack.getInstance("aywen:dream_planks").getItemStack();
    ItemStack cloud = CustomStack.getInstance("aywen:cloud").getItemStack();

    public CloudBed(AdvancementRegister register) {
        /* Accordée quand le joueur fabrique un lit avec des nuages */
        this.register = register;
    }

    @EventHandler
    public void onCraftItem(CraftItemEvent event) {
        if (event.getRecipe().getResult().getType() == Material.WHITE_BED) {
            CraftingInventory inventory = event.getInventory();
            ItemStack[] matrix = inventory.getMatrix();

            boolean isCorrectRecipe = true;
            for (ItemStack item : matrix) {
                if (item == null ) { continue; }
                if (!item.isSimilar(plank) && !item.isSimilar(cloud)) {
                    isCorrectRecipe = false;
                    break;
                }
            }

            if (isCorrectRecipe) {
                Player player = (Player) event.getWhoClicked();
                register.grantAdvancement(player, "aywen:sleep_on_cloud");
            }
        }
    }
}
