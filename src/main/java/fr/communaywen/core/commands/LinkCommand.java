package fr.communaywen.core.commands;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import fr.communaywen.core.utils.LinkerAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Description;

import java.sql.SQLException;

@Feature("Link")
@Credit({"Axeno", "Gyro3630"})
public class LinkCommand {
    private final LinkerAPI linkerAPI;

    public LinkCommand(LinkerAPI linkerAPI) {
        this.linkerAPI = linkerAPI;
    }

    @Command("link")
    @Description("Lie un compte Minecraft à Discord")
    public void onCommand(Player player) {

        try {
            if (!linkerAPI.getUserId(player).isEmpty()) {
                player.sendMessage(ChatColor.RED + "Votre compte minecraft est déjà lié à un compte Discord.");
            } else if (!linkerAPI.playerAlreadyLinkTime(player)) {
                int code = linkerAPI.generateCode();

                do {
                    code = linkerAPI.generateCode();
                } while (linkerAPI.codeAlreadyExist(code));

                linkerAPI.linkWithCode(player, code);
                player.sendMessage("§aUtilise la commande §f/link " + code + " §asur discord pour lier votre compte.");

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        try {
                            linkerAPI.delayRemoveCode(player);
                            if (!linkerAPI.isVerified(player)) {
                                player.sendMessage("§cVous avez dépassé les 5 minutes.");
                            }
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }.runTaskLater(AywenCraftPlugin.getInstance(), 300 * 20);

            } else {
                player.sendMessage(ChatColor.RED + "Vous avez déjà un code de vérification.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
