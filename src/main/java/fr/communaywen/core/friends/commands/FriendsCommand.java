package fr.communaywen.core.friends.commands;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.friends.FriendsManager;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.annotation.Subcommand;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Command({"friends", "friend", "ami", "f"})
public class FriendsCommand {

    private final FriendsManager friendsManager;
    private final AywenCraftPlugin plugin;
    private final BukkitAudiences audiences;

    public FriendsCommand(FriendsManager friendsManager, AywenCraftPlugin plugin, BukkitAudiences audiences) {
        this.friendsManager = friendsManager;
        this.plugin = plugin;
        this.audiences = audiences;
    }

    @Subcommand("add")
    @Description("Ajoutez un joueur en tant qu'ami.")
    public void addFriend(Player player, Player target) {
        try {
            if (friendsManager.isRequestPending(player.getUniqueId().toString())) {
                player.sendMessage("§cTu as déjà envoyé une demande d'ami.");
                return;
            }

            if (friendsManager.areFriends(player.getUniqueId().toString(), target.getUniqueId().toString())) {
                player.sendMessage("§cTu es déjà ami avec §e" + target.getName());
                return;
            }

            if (player == target) {
                player.sendMessage("§cTu ne peux pas envoyer une demande d'ami à toi-même.");
                return;
            }

            friendsManager.addRequest(player.getUniqueId().toString(), target.getUniqueId().toString());
            player.sendMessage("§aDemande d'ami envoyée à §e" + target.getName() + "§a.");

            TextComponent accept = Component.text("§e" + player.getName() + " §avous a envoyé une demande d'ami !")
                    .hoverEvent(HoverEvent.showText(Component.text("§7[§aCliquez pour accepter§7]")))
                    .clickEvent(ClickEvent.runCommand("/friends accept " + player.getName()));

            audiences.player(target).sendMessage(accept);
        } catch (Exception e) {
            player.sendMessage("§cUne erreur s'est produite lors de l'envoi de la demande d'ami.");
            e.printStackTrace();
        }
    }

    @Subcommand("remove")
    @Description("Supprime un joueur de votre liste d'amis.")
    public void removeFriend(Player player, Player target) {
        try {
            if (!friendsManager.areFriends(player.getUniqueId().toString(), target.getUniqueId().toString())) {
                player.sendMessage("§cTu n'es pas ami avec §e" + target.getName());
                return;
            }

            friendsManager.removeFriend(player.getUniqueId().toString(), target.getUniqueId().toString());
            player.sendMessage("§aVous avez supprimé §e" + target.getName() + " §ade votre liste d'amis.");
        } catch (SQLException e) {
            player.sendMessage("§cUne erreur s'est produite lors de la suppression de l'ami.");
            e.printStackTrace();
        }
    }

    @Subcommand("list")
    @Description("Affiche tous vos amis.")
    public void listFriends(Player player) {
        friendsManager.getFriendsAsync(player.getUniqueId().toString()).thenAccept(friends -> {
            player.sendMessage("§eVos amis:");

            for (String friendUUID : friends) {
                OfflinePlayer friend = Bukkit.getOfflinePlayer(UUID.fromString(friendUUID));

                try {
                    String formattedDate = getFormattedDate(friendsManager.getTimestamp(player.getUniqueId().toString(), friendUUID));

                    TextComponent friendComponent = Component.text("- §e" + friend.getName() + " §7depuis le: §e" + formattedDate)
                            .hoverEvent(HoverEvent.showText(Component.text("§7[§cCliquez pour supprimer§7]")))
                            .clickEvent(ClickEvent.runCommand("/friends remove " + friend.getName()));

                    audiences.sender(player).sendMessage(friendComponent);
                } catch (SQLException e) {
                    player.sendMessage("§cErreur lors de la récupération des informations de l'ami: " + friend.getName());
                    e.printStackTrace();
                }
            }
        }).exceptionally(ex -> {
            player.sendMessage("§cUne erreur s'est produite lors de la récupération de votre liste d'amis.");
            ex.printStackTrace();
            return null;
        });
    }

    @Subcommand("accept")
    @Description("Accepte une demande d'ami.")
     public void acceptRequest(Player player, Player target) {
        try {
            if (!friendsManager.isRequestPending(player.getUniqueId().toString())) {
                player.sendMessage("§e" + target.getName() + " §cne vous a pas envoyé de demande d'ami.");
                return;
            }

            friendsManager.addFriend(player.getUniqueId().toString(), target.getUniqueId().toString());
            player.sendMessage("§aVous êtes maintenant amis avec §e" + target.getName() + "§a.");
            target.sendMessage("§aVous êtes maintenant amis avec §e" + player.getName() + "§a.");
        } catch (SQLException e) {
            player.sendMessage("§cUne erreur s'est produite lors de l'acceptation de la demande d'ami.");
            e.printStackTrace();
        }
    }

    @Subcommand("deny")
    @Description("Refuse une demande d'ami.")
    public void denyRequest(Player player, Player target) {
        try {
            if (!friendsManager.isRequestPending(player.getUniqueId().toString())) {
                player.sendMessage("§e" + target.getName() + " §cne vous a pas envoyé de demande d'ami.");
                return;
            }

            friendsManager.removeRequest(friendsManager.getRequest(player.getUniqueId().toString()));
            player.sendMessage("§cDemande d'ami de §e" + target.getName() + " §crefusée.");
        } catch (Exception e) {
            player.sendMessage("§cUne erreur s'est produite lors du refus de la demande d'ami.");
            e.printStackTrace();
        }
    }

    public String getFormattedDate(Timestamp timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date(timestamp.getTime());
        return sdf.format(date);
    }
}
