package fr.communaywen.core.listeners;

import fr.communaywen.core.AywenCraftPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

public class VpnListener implements Listener {

    private final AywenCraftPlugin plugin;

    public VpnListener(AywenCraftPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void OnPlayerJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        if (player.hasPermission("aywencraft.antivpn.bypass")) {
            return;
        }

        String ip = player.getAddress().getAddress().getHostAddress();


        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://ip-api.com/json/" + ip + "?fields=proxy");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");

                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String inputLine;
                    StringBuilder content = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        content.append(inputLine);
                    }

                    in.close();
                    connection.disconnect();

                    String response = content.toString();
                    if (response.contains("\"proxy\":true")) {
                        Bukkit.getScheduler().runTask(plugin, () -> {
                            player.kickPlayer(  "§l§cOpenMc - Sécurité\n" +
                                    "\n" +
                                    "§l§eConnexion non permise !§r\n" +
                                    "\n" +
                                    "§6Vous essayez actuellement de vous connecter au serveur\n" +
                                    "§6avec un vpn ou un proxy.\n" +
                                    "\n" +
                                    "§3Merci de le désactiver et de réessayer\n" +
                                    "\n" +
                                    "§cSi vous pensez qu'il s'agit d'une erreur, vous pouvez\n" +
                                    "§cavoir de l'assistance sur discord.gg/aywen");

                            Bukkit.broadcast(ChatColor.YELLOW + event.getPlayer().getName() + " a essayé de se connecter mais il avait un vpn.",
                                    "aywencraft.antivpn.notification");
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(plugin);
    }
}