package fr.communaywen.core.commands.corporation;

import fr.communaywen.core.corporation.guilds.Guild;
import fr.communaywen.core.corporation.guilds.GuildManager;
import fr.communaywen.core.corporation.shops.PlayerShopManager;
import fr.communaywen.core.corporation.shops.Shop;
import fr.communaywen.core.corporation.shops.ShopItem;
import fr.communaywen.core.corporation.shops.menu.ShopManageMenu;
import fr.communaywen.core.corporation.shops.menu.ShopMenu;
import fr.communaywen.core.utils.MethodState;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import revxrsal.commands.annotation.*;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import java.util.Objects;
import java.util.UUID;

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
            player.sendMessage("Usage: /shop <create | manage | sell | unsell | delete> <shop>");
            return;
        }
        ShopMenu shopMenu = new ShopMenu(player, guildManager, playerShopManager, playerShopManager.getShop(player.getUniqueId()), 0);
        shopMenu.open();
    }

    @Subcommand("create")
    @Description("Create a shop")
    public void createShop(Player player) {
        boolean isInGuild = guildManager.isInGuild(player.getUniqueId());
        Block targetBlock = player.getTargetBlockExact(5);
        if (targetBlock == null || targetBlock.getType() != Material.BARREL) {
            player.sendMessage(ChatColor.RED + "Vous devez regarder un tonneau pour créer un shop");
            return;
        }
        Block aboveBlock = Objects.requireNonNull(targetBlock.getLocation().getWorld()).getBlockAt(targetBlock.getLocation().clone().add(0, 1, 0));
        if (aboveBlock.getType() != Material.AIR) {
            player.sendMessage(ChatColor.RED + "Vous devez liberer de l'espace au dessus de votre tonneau pour créer un shop");
            return;
        }
        if (isInGuild) {
            Guild guild = guildManager.getGuild(player.getUniqueId());
            if (!guild.isOwner(player.getUniqueId())) {
                player.sendMessage(ChatColor.RED + "Vous devez être un des propriétaires de la guilde pour créer un shop");
                return;
            }
            if (!guild.createShop(player, targetBlock, aboveBlock)) {
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
        if (!playerShopManager.createShop(player, targetBlock, aboveBlock)) {
            player.sendMessage(ChatColor.RED + "Vous n'avez pas assez d'argent pour créer un shop (500€)");
            return;
        }
        player.sendMessage(ChatColor.GOLD + "[Shop]" + ChatColor.RED + " -500€ sur votre compte personnel");
        player.sendMessage(ChatColor.GREEN + "Vous avez bien crée un shop !");
    }

    @Subcommand("sell")
    @Description("Sell an item in your shop")
    public void sellItem(Player player, @Named("price") double price) {
        boolean isInGuild = guildManager.isInGuild(player.getUniqueId());
        UUID shopUUID = Shop.getShopPlayerLookingAt(player, isInGuild);
        if (shopUUID == null) {
            player.sendMessage(ChatColor.RED + "Shop non reconnu");
            return;
        }
        if (isInGuild) {
            Shop shop = guildManager.getGuild(player.getUniqueId()).getShop(shopUUID);
            if (shop == null) {
                player.sendMessage(ChatColor.RED + "Ce shop n'appartient pas à votre guilde");
                return;
            }
            ItemStack item = player.getInventory().getItemInMainHand();
            boolean itemThere = shop.addItem(item, price);
            if (itemThere) {
                player.sendMessage(ChatColor.RED + "Cet item est déjà dans le shop");
                return;
            }
            player.sendMessage(ChatColor.GREEN + "L'item a bien été ajouté au shop !");
            return;
        }
        if (!playerShopManager.hasShop(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "Vous n'avez pas de shop");
            return;
        }
        Shop shop = playerShopManager.getShop(player.getUniqueId());
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null || item.getType() == Material.AIR) {
            player.sendMessage(ChatColor.RED + "Vous devez tenir un item dans votre main");
            return;
        }
        boolean itemThere = shop.addItem(item, price);
        if (itemThere) {
            player.sendMessage(ChatColor.RED + "Cet item est déjà dans le shop");
            return;
        }
        player.sendMessage(ChatColor.GREEN + "L'item a bien été ajouté au shop !");
    }

    @Subcommand("unsell")
    @Description("Unsell an item in your shop")
    public void unsellItem(Player player, @Named("item number") int itemIndex) {
        boolean isInGuild = guildManager.isInGuild(player.getUniqueId());
        UUID shopUUID = Shop.getShopPlayerLookingAt(player, isInGuild);
        if (shopUUID == null) {
            player.sendMessage(ChatColor.RED + "Shop non reconnu");
            return;
        }
        if (isInGuild) {
            Shop shop = guildManager.getGuild(player.getUniqueId()).getShop(shopUUID);
            if (shop == null) {
                player.sendMessage(ChatColor.RED + "Ce shop n'appartient pas à votre guilde");
                return;
            }
            if (itemIndex < 1 || itemIndex >= shop.getItems().size() + 1) {
                player.sendMessage(ChatColor.RED + "Cet item n'est pas dans le shop");
                return;
            }
            ShopItem item = shop.getItem(shop.getItems().get(itemIndex - 1));
            if (item == null) {
                player.sendMessage(ChatColor.RED + "Cet item n'est pas dans le shop");
                return;
            }
            shop.removeItem(item);
            player.sendMessage(ChatColor.GREEN + "L'item a bien été retiré du shop !");
            return;
        }
        if (!playerShopManager.hasShop(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "Vous n'avez pas de shop");
            return;
        }
        Shop shop = playerShopManager.getShop(player.getUniqueId());
        ShopItem item = shop.getItem(shop.getItems().get(itemIndex - 1));
        if (item == null) {
            player.sendMessage(ChatColor.RED + "Cet item n'est pas dans le shop");
            return;
        }
        shop.removeItem(item);
        player.sendMessage(ChatColor.GREEN + "L'item a bien été retiré du shop !");
    }

    @Subcommand("delete")
    @Description("Delete a shop")
    public void deleteShop(Player player) {
        boolean isInGuild = guildManager.isInGuild(player.getUniqueId());
        UUID shopUUID = Shop.getShopPlayerLookingAt(player, isInGuild);
        if (shopUUID == null) {
            player.sendMessage(ChatColor.RED + "Shop non reconnu");
            return;
        }
        if (isInGuild) {
            Shop shop = guildManager.getGuild(player.getUniqueId()).getShop(shopUUID);
            if (shop == null) {
                player.sendMessage(ChatColor.RED + "Ce shop n'appartient pas à votre guilde");
                return;
            }
            if (!guildManager.getGuild(player.getUniqueId()).isOwner(player.getUniqueId())) {
                player.sendMessage(ChatColor.RED + "Vous devez être un des propriétaires de la guilde pour supprimer un shop");
                return;
            }
            MethodState deleteState = guildManager.getGuild(player.getUniqueId()).deleteShop(player, shop.getUuid());
            if (deleteState == MethodState.ERROR) {
                player.sendMessage(ChatColor.RED + "Ce shop n'existe pas dans votre guilde");
                return;
            }
            if (deleteState == MethodState.WARNING) {
                player.sendMessage(ChatColor.RED + "Ce shop n'est pas vide");
                return;
            }
            if (deleteState == MethodState.SPECIAL) {
                player.sendMessage(ChatColor.RED + "Il vous faut au minimum le nombre d'argent remboursable pour supprimer un shop et obtenir un remboursement dans la banque de votre guilde");
                return;
            }
            if (deleteState == MethodState.ESCAPE) {
                player.sendMessage(ChatColor.RED + "Caisse introuvable (appelez un admin)");
            }
            player.sendMessage(ChatColor.GREEN + shop.getName() + " supprimé !");
            player.sendMessage(ChatColor.GOLD + "[Shop]" + ChatColor.GREEN + " +75€ de remboursés sur la banque de la guilde");
        }
        if (!playerShopManager.hasShop(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "Vous n'avez pas de shop");
            return;
        }
        MethodState methodState = playerShopManager.deleteShop(player.getUniqueId());
        if (methodState == MethodState.WARNING) {
            player.sendMessage(ChatColor.RED + "Votre shop n'est pas vide");
            return;
        }
        if (methodState == MethodState.ESCAPE) {
            player.sendMessage(ChatColor.RED + "Caisse introuvable (appelez un admin)");
            return;
        }
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
