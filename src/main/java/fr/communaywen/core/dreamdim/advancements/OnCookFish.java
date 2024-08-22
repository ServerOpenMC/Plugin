package fr.communaywen.core.dreamdim.advancements;

import dev.lone.itemsadder.api.CustomStack;
import fr.communaywen.core.dreamdim.SimpleAdvancementRegister;
import io.papermc.paper.event.player.PlayerInventorySlotChangeEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class OnCookFish implements Listener {
    @EventHandler
    public void onCookFish(PlayerInventorySlotChangeEvent event) {
        Player player = event.getPlayer();
        ItemStack newIS = event.getNewItemStack();
        CustomStack customStack = CustomStack.byItemStack(newIS);

        if (customStack != null) {
            switch (customStack.getNamespacedID()) {
                case "aywen:cooked_poissonion":
                    SimpleAdvancementRegister.grantAdvancement(player, "aywen:fishing/comme_un_onion");
                    break;
                case "aywen:sun_fish":
                    SimpleAdvancementRegister.grantAdvancement(player, "aywen:fishing/poissoleil");
                    break;
            }
        }
    }
}
