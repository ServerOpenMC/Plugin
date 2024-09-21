package fr.communaywen.core.personalhome.listeners;

import dev.lone.itemsadder.api.CustomStack;
import fr.communaywen.core.personalhome.Home;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class BagInteraction implements Listener {
    public HashMap<UUID, Home> homes;

    public HashMap<UUID, Location> lastPosition = new HashMap<>();

    public BagInteraction(HashMap<UUID, Home> homes) {
        this.homes = homes;
    }

    @EventHandler
    public void enterBag(PlayerInteractEvent event) {
        if (!(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK))) { return; }
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        CustomStack stack = CustomStack.byItemStack(item);

        if (stack == null) { return;}
        if (!stack.getNamespacedID().equals("aywen:home_bag")) { return; }

        if (List.of("moon", "dreamworld").contains(player.getWorld().getName())) {
            player.sendMessage("§cVotre maison n'est pas accessible d'où vous êtes");
            return;
        }

        if (player.hasPermission("aywen.block.maisons")) {
            player.sendMessage("§cVotre maison n'est pas accessible !");
            return;
        }

        if (!player.getWorld().getName().equals("homes")) {
            Home home = homes.get(player.getUniqueId());

            lastPosition.put(player.getUniqueId(), player.getLocation());

            player.teleport(home.getSpawnpoint());
            player.playSound(player.getEyeLocation(), Sound.ENTITY_PLAYER_TELEPORT, 1, 1);
        } else {
            if (!lastPosition.containsKey(player.getUniqueId())) {
                lastPosition.put(player.getUniqueId(), Bukkit.getWorld("world").getSpawnLocation());
            }

            player.teleport(lastPosition.get(player.getUniqueId()));
            player.playSound(player.getEyeLocation(), Sound.ENTITY_PLAYER_TELEPORT, 1, 1);
            lastPosition.remove(player.getUniqueId());
        }
    }
}
