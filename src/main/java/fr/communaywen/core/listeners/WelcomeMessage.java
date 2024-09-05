package fr.communaywen.core.listeners;

import fr.communaywen.core.AywenCraftPlugin;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Objects;

// show messages/title at the player's connection
// configuration file -> welcomeMessage.Config.yml

public class WelcomeMessage implements Listener {

    private final FileConfiguration welcomeMessageConfig;

    public WelcomeMessage(FileConfiguration welcomeMessageConfig) {
        this.welcomeMessageConfig = welcomeMessageConfig;
    }

    @EventHandler
    public void OnPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        final var messages = welcomeMessageConfig.getKeys(false);

        // for each message of the config file we check if it is a chat message or a title and if we show it only at the first player's connection
        for (String message : messages) {

            String type = welcomeMessageConfig.getString(message + ".type"); // it can be a chat message or a title
            boolean executeOnfirstJoin = welcomeMessageConfig.getBoolean(message + ".execute_onfisrt_join");

            if (type == null) continue;

            if (executeOnfirstJoin == (!player.hasPlayedBefore())) {

                if (type.equals("title")) {
                    player.sendTitle(welcomeMessageConfig.getString(message + ".config.title"),
                            welcomeMessageConfig.getString(message + ".config.subtitle"),
                            welcomeMessageConfig.getInt(message + ".config.fadeIn"),
                            welcomeMessageConfig.getInt(message + ".config.stay"),
                            welcomeMessageConfig.getInt(message + ".config.fadeOut"));
                } else if (type.equals("chat")) {
                    String messageContent = welcomeMessageConfig.getString(message + ".config.content");
                    if (messageContent != null) {
                        player.sendMessage(messageContent);
                    }
                }
            }
        }

        if (event.getJoinMessage() != null) {
            event.setJoinMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(event.getJoinMessage())));
        }

        AywenCraftPlugin.getInstance().getManagers().getScoreboardManager().setScoreboard(player);
    }
}
