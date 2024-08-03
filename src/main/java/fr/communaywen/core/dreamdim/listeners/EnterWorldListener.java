package fr.communaywen.core.dreamdim.listeners;

import fr.communaywen.core.AywenCraftPlugin;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class EnterWorldListener implements Listener {

    World dreamworld;
    AywenCraftPlugin plugin;

    public EnterWorldListener(AywenCraftPlugin plugin) {
        this.plugin = plugin;
        dreamworld = Bukkit.getWorld("dreamworld");
    }

    @EventHandler
    public void onChangeWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        if (!player.getWorld().equals(dreamworld)){ return; }

        player.sendMessage("§4Mets ta luminosité au minimum pour une experience plus immersive");
    }
}
