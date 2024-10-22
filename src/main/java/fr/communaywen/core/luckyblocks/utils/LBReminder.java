package fr.communaywen.core.luckyblocks.utils;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.luckyblocks.managers.LBPlayerManager;
import fr.communaywen.core.utils.constant.MessageManager;
import fr.communaywen.core.utils.constant.MessageType;
import fr.communaywen.core.utils.constant.Prefix;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

import static fr.communaywen.core.mailboxes.utils.MailboxUtils.getHoverEvent;
import static fr.communaywen.core.mailboxes.utils.MailboxUtils.getRunCommand;

public class LBReminder {

    private final Player player;
    private final LBPlayerManager playerManager;
    private final AywenCraftPlugin plugin;

    public LBReminder(Player player, LBPlayerManager playerManager, AywenCraftPlugin plugin) {
        this.player = player;
        this.playerManager = playerManager;
        this.plugin = plugin;
    }

    public void startReminder() {
        UUID playerUUID = player.getUniqueId();

        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            if (!player.isOnline()) {
                return;
            }

            if (playerManager.canClaimLuckyBlocks(playerUUID)) {
                MessageManager.sendMessageType(player, "Vous pouvez réclamer vos Lucky Block Quotidien!", Prefix.LUCKYBLOCK, MessageType.INFO, true);
                Component message = Component.text("", NamedTextColor.GRAY)
                        .append(Component.text("Cliquez-ici", NamedTextColor.GRAY))
                        .clickEvent(ClickEvent.runCommand("/lb claim"))
                        .hoverEvent(getHoverEvent("Récuperer le Claim"))
                        .append(Component.text(" pour obtenir vos récompenses", NamedTextColor.LIGHT_PURPLE));
                player.sendMessage(message);
            }
        }, 0L, 5 * 60 * 20L);
    }

    public void stopReminder() {
        Bukkit.getScheduler().cancelTasks(plugin);
    }
}
