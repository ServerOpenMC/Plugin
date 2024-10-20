package fr.communaywen.core.listeners;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.contest.cache.ContestCache;
import fr.communaywen.core.luckyblocks.managers.LBPlayerManager;
import fr.communaywen.core.luckyblocks.utils.LBReminder;
import fr.communaywen.core.managers.LeaderboardManager;
import fr.communaywen.core.managers.RegionsManager;
import fr.communaywen.core.utils.DraftAPI;
import fr.communaywen.core.utils.LinkerAPI;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.query.QueryOptions;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.sql.SQLException;

public class OnPlayers implements Listener {

    private LuckPerms luckPerms;
    private LinkerAPI linkerAPI;

    public void setLuckPerms(LuckPerms luckPerms) {
        this.luckPerms = luckPerms;
    }

    public void setLinkerAPI(LinkerAPI linkerAPI) {
        this.linkerAPI = linkerAPI;
    }

    public void addPermission(User user, String permission) {
        // Add the permission
        user.data().add(Node.builder(permission).build());

        // Now we need to save changes.
        luckPerms.getUserManager().saveUser(user);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) { // Donne une permissions en fonction du niveau
        if (event.joinMessage() == null) { return; }
        Player player = event.getPlayer();

        User userlp = AywenCraftPlugin.getInstance().api.getUserManager().getUser(player.getUniqueId());
        QueryOptions queryOptions = AywenCraftPlugin.getInstance().api.getContextManager().getQueryOptions(userlp).orElse(QueryOptions.defaultContextualOptions());

        event.setJoinMessage("§8[§a+§8] §r" + (userlp.getCachedData().getMetaData(queryOptions).getPrefix() != null ? userlp.getCachedData().getMetaData(queryOptions).getPrefix().replace("&", "§") : "") + "" + player.getName());

        Bukkit.getScheduler().runTaskAsynchronously(AywenCraftPlugin.getInstance(), () -> {
            long timePlayed = player.getStatistic(Statistic.PLAY_ONE_MINUTE);
            LeaderboardManager.setTimePlayed(player, timePlayed);

            DraftAPI draftAPI = new DraftAPI();

            JSONObject data = null;
            String discordPlayerId = null;
            JSONArray users = null;
            try {
                data = new JSONObject(draftAPI.getTop());

                users = data.getJSONArray("users");

                discordPlayerId = this.linkerAPI.getUserId(player);
            } catch (IOException | SQLException e) {
                throw new RuntimeException(e);
            }

            if (discordPlayerId.isEmpty()) {
                player.sendMessage("Profitez de récompenses en liant votre compte Discord à Minecraft");
            }

            LBPlayerManager playerManager = AywenCraftPlugin.getInstance().getManagers().getLbPlayerManager();
            LBReminder reminder = new LBReminder(player, playerManager, AywenCraftPlugin.getInstance());

            reminder.startReminder();

            if(ContestCache.getPhaseCache() == 2) {
                player.sendMessage(
                        "§8§m                                                     §r\n" +
                                "§7\n" +
                                "§6§lCONTEST!§r §7 Les votes ont été ouvert !§7" +
                                "§7\n" +
                                "§8§oon se retrouve au spawn pour pouvoir voter ou /contest...\n" +
                                "§7\n" +
                                "§8§m                                                     §r"
                );
                return;
            }
            if(ContestCache.getPhaseCache() == 3) {
                player.sendMessage(
                        "§8§m                                                     §r\n" +
                                "§7\n" +
                                "§6§lCONTEST!§r §7Les contributions ont commencé!§7" +
                                "§7\nEchanger des ressources contre des Coquillages de Contest. Récoltez en un max et déposez les\n" +
                                "§8§ovia la Borne des Contest ou /contest\n" +
                                "§7\n" +
                                "§8§m                                                     §r"
                );
                return;
            }

            for (int i = 0; i < users.length(); i++) {
                JSONObject user = users.getJSONObject(i);
                String discordId = user.getString("id");

                if (discordPlayerId.equals(discordId)) {
                    User lpPlayer = this.luckPerms.getPlayerAdapter(Player.class).getUser(player);

                    int level = user.getInt("level");
                    if (level < 10) {
                        break;
                    }

                    addPermission(lpPlayer, "ayw.levels.10");
                    if (level >= 20) {
                        addPermission(lpPlayer, "ayw.levels.20");
                    }
                    if (level >= 30) {
                        addPermission(lpPlayer, "ayw.levels.30");
                    }
                    if (level >= 40) {
                        addPermission(lpPlayer, "ayw.levels.40");
                    }
                    if (level >= 50) {
                        addPermission(lpPlayer, "ayw.levels.50");
                    }
                    return;
                }
            }
        });
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        User userlp = AywenCraftPlugin.getInstance().api.getUserManager().getUser(player.getUniqueId());
        QueryOptions queryOptions = AywenCraftPlugin.getInstance().api.getContextManager().getQueryOptions(userlp).orElse(QueryOptions.defaultContextualOptions());

        event.setQuitMessage("§8[§c-§8] §r" + (userlp.getCachedData().getMetaData(queryOptions).getPrefix() != null ? userlp.getCachedData().getMetaData(queryOptions).getPrefix().replace("&", "§") : "") + "" + player.getName());


        Bukkit.getScheduler().runTaskAsynchronously(AywenCraftPlugin.getInstance(), () -> {
            long timePlayed = player.getStatistic(Statistic.PLAY_ONE_MINUTE);
            LeaderboardManager.setTimePlayed(player, timePlayed);

            LBReminder reminder = new LBReminder(player, AywenCraftPlugin.getInstance().getManagers().getLbPlayerManager(), AywenCraftPlugin.getInstance());

            reminder.stopReminder();

        });
    }

}
