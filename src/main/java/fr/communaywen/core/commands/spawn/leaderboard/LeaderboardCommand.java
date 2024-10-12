package fr.communaywen.core.commands.spawn.leaderboard;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import fr.communaywen.core.managers.LeaderboardManager;
import fr.communaywen.core.spawn.head.HeadManager;
import fr.communaywen.core.spawn.jump.JumpManager;
import fr.communaywen.core.utils.constant.MessageManager;
import fr.communaywen.core.utils.constant.MessageType;
import fr.communaywen.core.utils.constant.Prefix;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.*;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import java.io.IOException;


@Feature("Leaderboard")
@Credit("iambibi_")
@Command("leaderboard")
@Description("Commande admin")
public class LeaderboardCommand {
    private final AywenCraftPlugin plugin;
    private JumpManager jumpManager;

    public LeaderboardCommand(AywenCraftPlugin plugins, JumpManager manager) {
        plugin = plugins;
        jumpManager = manager;
    }

    @Subcommand("move")
    @Description("Déplace un leaderboard")
    @CommandPermission("ayw.command.leaderboard.move")
    @AutoComplete("@listLeaderboard")
    public void movestart(Player player, @Named("listLeaderboard") String lb) throws IOException {
        if (LeaderboardManager.getLbList().contains(lb)) {
            if (player.getTargetBlock(null, 100).getType() != Material.AIR) {
                Location blockLocation = player.getTargetBlock(null, 100).getLocation();
                double posX = blockLocation.getX();
                double posY = blockLocation.getY();
                double posZ = blockLocation.getZ();
                String world = blockLocation.getWorld().getName();

                plugin.getConfig().set("leaderboard." + lb + ".world", world);
                plugin.getConfig().set("leaderboard." + lb + ".posX", posX);
                plugin.getConfig().set("leaderboard." + lb + ".posY", posY);
                plugin.getConfig().set("leaderboard." + lb + ".posZ", posZ);
                plugin.saveConfig();

                LeaderboardManager.removeLeaderboardBalTop();
                LeaderboardManager.createLeaderboardBalTop();
                LeaderboardManager.updateLeaderboardBalTop();

                LeaderboardManager.removeLeaderboardTeamTop();
                LeaderboardManager.createLeaderboardTeamTop();
                LeaderboardManager.updateLeaderboardTeamTop();

                LeaderboardManager.removeLeaderboardContribution();
                LeaderboardManager.createLeaderboardContribution();
                LeaderboardManager.updateLeaderboardContribution();

                LeaderboardManager.removeLeaderboardPlayTime();
                LeaderboardManager.createLeaderboardPlayTime();
                LeaderboardManager.updateLeaderboardPlayTime();

                jumpManager.removeLeaderboardLeaderboardRecord();
                jumpManager.createLeaderboardLeaderboardRecord();
                jumpManager.updateLeaderboardLeaderboardRecord();
                MessageManager.sendMessageType(player, "§7Commencement du leaderboard " + lb + " déplacé X=" + posX + " Y=" + posY + " Z=" + posZ + " WORLD = " + world, Prefix.STAFF, MessageType.SUCCESS, true);
            } else {
                MessageManager.sendMessageType(player, "§cVous devez viser un bloc", Prefix.STAFF, MessageType.ERROR, true);
            }
        } else {
            MessageManager.sendMessageType(player, "§cVous devez mettre un nom de leaderboard valable", Prefix.STAFF, MessageType.ERROR, true);
        }
    }
}
