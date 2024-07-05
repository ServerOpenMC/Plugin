package fr.communaywen.core.utils;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.PrefixNode;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TabListManager {

    private final LuckPerms luckPerms;

    public TabListManager() {
        this.luckPerms = LuckPermsProvider.get();
    }

    public void updateTabList() {
        List<Player> players = Bukkit.getOnlinePlayers().stream()
                .sorted(Comparator.comparingInt(this::getPlayerWeight).thenComparing(Player::getName))
                .collect(Collectors.toList());

        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            String prefix = getPlayerPrefix(player);
            String tabName = prefix + player.getName();
            player.setPlayerListName(tabName);
            player.setPlayerListHeaderFooter("IP: dev.openmc.fr");
        }
    }

    private String getPlayerPrefix(Player player) {
        User user = luckPerms.getUserManager().getUser(player.getUniqueId());
        if (user != null) {
            return user.getCachedData().getMetaData().getPrefix() != null ? user.getCachedData().getMetaData().getPrefix() : "";
        }
        return "";
    }

    private int getPlayerWeight(Player player) {
        User user = luckPerms.getUserManager().getUser(player.getUniqueId());
        if (user != null) {
            return user.getCachedData().getMetaData().getWeight() != null ? user.getCachedData().getMetaData().getWeight() : 0;
        }
        return 0;
    }
}
