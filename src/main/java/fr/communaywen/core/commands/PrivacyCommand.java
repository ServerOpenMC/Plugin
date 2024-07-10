package fr.communaywen.core.commands;

import com.sun.tools.jconsole.JConsoleContext;
import fr.communaywen.core.AywenCraftPlugin;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import fr.communaywen.core.utils.database.Blacklist;
import revxrsal.commands.annotation.*;
import revxrsal.commands.command.ExecutableCommand;
import revxrsal.commands.help.CommandHelp;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@Command("privacy")
@Description("Affiche un lien redirigeant vers le discord.")
public class PrivacyCommand {
    private AywenCraftPlugin plugin;
    private Blacklist blacklist;

    public PrivacyCommand(AywenCraftPlugin plugin) {
        this.blacklist = new Blacklist(plugin);
        this.plugin = plugin;
    }

    @DefaultFor("~")
    public void sendHelp(Player player, ExecutableCommand command, CommandHelp<Component> help, @Default("1") @Range(min = 1) int page) {
        player.sendMessage("hello");
    }

    @Subcommand("blacklist")
    @Description("Affiche votre blacklist")
    public void blacklist(Player player) throws SQLException {
        StringBuilder playersList = new StringBuilder();
        List<String> playersUUID = blacklist.getBlacklist(player);

        for (int i = 0; i < playersUUID.size(); i++) {
            OfflinePlayer blocked = Bukkit.getOfflinePlayer(UUID.fromString(playersUUID.get(i)));
            if (i != playersUUID.size() - 1) {
                playersList.append(blocked.getName()).append(", ");
            } else {
                playersList.append(blocked.getName());
            }
        }
        player.sendMessage("Blacklist: "+ playersList.toString());
    }

    @Subcommand("block")
    @Description("Ajoute quelqu'un à la blacklist")
    public void block(Player player, @Named("player") String blocked) throws SQLException {
        if (player.getName().equals(blocked)) {
            player.sendMessage("Vous ne pouvez pas vous bloquer vous-même");
            return;
        }
        blacklist.block(player, Bukkit.getOfflinePlayer(blocked));
    }

    @Subcommand("unblock")
    @Description("Retire quelqu'un à la blacklist")
    public void unblock(Player player, @Named("player") String blocked) throws SQLException {
        if (player.getName().equals(blocked)) {
            player.sendMessage("Vous ne pouvez pas vous débloquer vous-même");
            return;
        }
        blacklist.unblock(player, Bukkit.getOfflinePlayer(blocked));
    }
}