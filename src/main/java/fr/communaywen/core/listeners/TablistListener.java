package fr.communaywen.core.listeners;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.tab.TabList;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class TablistListener implements Listener {

    private final AywenCraftPlugin plugin;

    public TablistListener(AywenCraftPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        updateTab(player);
        new BukkitRunnable() {
            @Override
            public void run() {
                updateTab(player);
            }
        }.runTaskTimer(plugin, 0L, 100L);
    }

    private void updateTab(Player player) {
        String header = PlaceholderAPI.setPlaceholders(player, "\n\n\n\n\n\n"+PlaceholderAPI.setPlaceholders(player, "%img_openmc%")+"\n\n  §eJoueurs en ligne §7: §6%server_online%§7/§e%server_max_players%  \n");
        String footer = "\n§dplay.openmc.fr\n";

        plugin.getTabList().updateHeaderFooter(player, header, footer);
    }
}
