package fr.communaywen.core.commands.corporation;

import fr.communaywen.core.corporation.guilds.Guild;
import fr.communaywen.core.corporation.guilds.GuildManager;
import fr.communaywen.core.corporation.shops.PlayerShopManager;
import fr.communaywen.core.corporation.shops.ShopOwner;
import fr.communaywen.core.corporation.shops.menu.ShopManageMenu;
import fr.communaywen.core.corporation.shops.menu.ShopMenu;
import fr.communaywen.core.utils.MethodState;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.*;
import revxrsal.commands.bukkit.annotation.CommandPermission;

@Command({"shop", "shops"})
@Description("Manage shops")
@CommandPermission("ayw.command.shop")
public class ShopCommand {

    private final GuildManager guildManager;
    private final PlayerShopManager playerShopManager;

    public ShopCommand(GuildManager guildManager, PlayerShopManager playerShopManager) {
        this.guildManager = guildManager;
        this.playerShopManager = playerShopManager;
    }

    @DefaultFor("~")
    public void onCommand(Player player) {
        boolean isInGuild = guildManager.isInGuild(player.getUniqueId());
        if (isInGuild) {
            ShopManageMenu shopManageMenu = new ShopManageMenu(player, guildManager.getGuild(player.getUniqueId()), guildManager, playerShopManager);
            shopManageMenu.open();
            return;
        }
        if (!playerShopManager.hasShop(player.getUniqueId())) {
            player.sendMessage("Usage: /shop <create | manage | delete> <shop>");
            return;
        }
        ShopMenu shopMenu = new ShopMenu(player, guildManager, playerShopManager, playerShopManager.getShop(player.getUniqueId()), 0);
        shopMenu.open();
    }

    @Subcommand("create")
    @Description("Create a shop")
    public void createShop(Player player) {
        boolean isInGuild = guildManager.isInGuild(player.getUniqueId());
        if (isInGuild) {
            Guild guild = guildManager.getGuild(player.getUniqueId());
            if (!guild.isOwner(player.getUniqueId())) {
                player.sendMessage(ChatColor.RED + "Vous devez être un des propriétaires de la guilde pour créer un shop");
                return;
            }
            if (!guild.createShop(player)) {
                player.sendMessage(ChatColor.RED + "Vous n'avez pas assez d'argent dans la banque de votre guilde pour créer un shop (100€)");
                return;
            }
            player.sendMessage(ChatColor.GOLD + "[Shop]" + ChatColor.RED + " -100€ sur la banque de la guilde");
            player.sendMessage(ChatColor.GREEN + "Un shop a bien été crée pour votre guilde !");
            return;
        }
        if (playerShopManager.hasShop(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "Vous avez déjà un shop");
            return;
        }
        if (!playerShopManager.createShop(player)) {
            player.sendMessage(ChatColor.RED + "Vous n'avez pas assez d'argent pour créer un shop (500€)");
            return;
        }
        player.sendMessage(ChatColor.GOLD + "[Shop]" + ChatColor.RED + " -500€ sur votre compte personnel");
        player.sendMessage(ChatColor.GREEN + "Vous avez bien crée un shop !");
    }

    @Subcommand("delete")
    @Description("Delete a shop")
    public void deleteShop(Player player, @Named("shop") int shop) {
        boolean isInGuild = guildManager.isInGuild(player.getUniqueId());
        if (isInGuild) {
            if (!guildManager.getGuild(player.getUniqueId()).isOwner(player.getUniqueId())) {
                player.sendMessage(ChatColor.RED + "Vous devez être un des propriétaires de la guilde pour supprimer un shop");
                return;
            }
            MethodState deleteState = guildManager.getGuild(player.getUniqueId()).deleteShop(player, shop);
            if (deleteState == MethodState.ERROR) {
                player.sendMessage(ChatColor.RED + "Ce shop n'existe pas dans votre guilde");
                return;
            }
            if (deleteState == MethodState.WARNING) {
                player.sendMessage(ChatColor.RED + "Ce shop n'est pas vide");
                return;
            }
            player.sendMessage(ChatColor.GREEN + "Shop #" + shop + " supprimé !");
            player.sendMessage(ChatColor.GOLD + "[Shop]" + ChatColor.GREEN + " +75€ de remboursés sur la banque de la guilde");
        }
        if (!playerShopManager.hasShop(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "Vous n'avez pas de shop");
            return;
        }
        playerShopManager.deleteShop(player.getUniqueId());
        player.sendMessage(ChatColor.GREEN + "Votre shop a bien été supprimé !");
        player.sendMessage(ChatColor.GOLD + "[Shop]" + ChatColor.GREEN + " +400€ de remboursés sur votre compte personnel");
    }

    @Subcommand("manage")
    @Description("Manage a shop")
    public void manageShop(Player player) {
        boolean isInGuild = guildManager.isInGuild(player.getUniqueId());
        if (isInGuild) {
            ShopManageMenu shopManageMenu = new ShopManageMenu(player, guildManager.getGuild(player.getUniqueId()), guildManager, playerShopManager);
            shopManageMenu.open();
            return;
        }
        if (!playerShopManager.hasShop(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "Vous n'avez pas de shop");
            return;
        }
        ShopMenu shopMenu = new ShopMenu(player, guildManager, playerShopManager, playerShopManager.getShop(player.getUniqueId()), 0);
        shopMenu.open();
    }

}
