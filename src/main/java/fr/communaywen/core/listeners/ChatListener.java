package fr.communaywen.core.listeners;

import fr.communaywen.core.utils.DiscordWebhook;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.server.BroadcastMessageEvent;

public class ChatListener implements Listener {

    private final DiscordWebhook discordWebhook;

    public ChatListener(DiscordWebhook discordWebhook) {
        this.discordWebhook = discordWebhook;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        String username = event.getPlayer().getName();
        String avatarUrl = "https://minotar.net/helm/" + username;
        String message = event.getMessage();

        discordWebhook.sendMessage(username, avatarUrl, message);

        Bukkit.getOnlinePlayers().forEach(player -> {
            if (message.toLowerCase().contains(player.getName().toLowerCase())) {
                player.playSound(player.getEyeLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1);
            }
        });
    }

    @EventHandler
    public void onBroadcastMessage(BroadcastMessageEvent event) {
        discordWebhook.sendBroadcast(event.getMessage());
    }
}
