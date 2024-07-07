package fr.communaywen.core.commands;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.utils.LinkerAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Description;

import java.sql.SQLException;
import java.util.Random;

public class LinkCommand {
    private final LinkerAPI linkerAPI;

    public LinkCommand(LinkerAPI linkerAPI) { this.linkerAPI = linkerAPI; }

    @Command("link")
    @Description("Lie un compte Minecraft à Discord")
    public void onCommand(Player player) {

        try {
            if (!linkerAPI.getUserId(player).isEmpty()) {
                player.sendMessage(ChatColor.RED + "Votre compte minecraft est déjà lié à un compte Discord.");
            } else {
                int code = linkerAPI.generateCode();

                linkerAPI.linkWithCode(player, code);
                player.sendMessage("§aUtilise la commande §f/link " + code + " §asur discord pour lier votre compte.");

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        try {
                            linkerAPI.delayRemoveCode(player);
                            if(!linkerAPI.isVerified(player)) {
                                player.sendMessage("§cVous avez dépasser les 5 minutes.");
                            }
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }.runTaskLater(AywenCraftPlugin.getInstance(), 300 * 20);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
