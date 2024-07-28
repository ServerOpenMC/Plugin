package fr.communaywen.core.corporation.shops.menu;

import dev.xernas.menulib.Menu;
import dev.xernas.menulib.utils.InventorySize;
import dev.xernas.menulib.utils.ItemBuilder;
import fr.communaywen.core.corporation.guilds.GuildManager;
import fr.communaywen.core.corporation.shops.PlayerShopManager;
import fr.communaywen.core.corporation.shops.Shop;
import fr.communaywen.core.corporation.shops.ShopItem;
import fr.communaywen.core.economy.EconomyManager;
import fr.communaywen.core.utils.MethodState;
import fr.communaywen.core.utils.menu.ConfirmationMenu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShopMenu extends Menu {

    private final List<ShopItem> items = new ArrayList<>();
    private final GuildManager guildManager;
    private final PlayerShopManager playerShopManager;
    private final Shop shop;
    private final int itemIndex;

    private int amountToBuy = 1;

    public ShopMenu(Player owner, GuildManager guildManager, PlayerShopManager playerShopManager, Shop shop, int itemIndex) {
        super(owner);
        this.guildManager = guildManager;
        this.playerShopManager = playerShopManager;
        this.shop = shop;
        this.itemIndex = itemIndex;
        items.addAll(shop.getItems());
    }

    @Override
    public @NotNull String getName() {
        return shop.getName();
    }

    @Override
    public @NotNull InventorySize getInventorySize() {
        if (!shop.isOwner(getOwner().getUniqueId()))
            return InventorySize.LARGE;
        return InventorySize.LARGER;
    }

    @Override
    public void onInventoryClick(InventoryClickEvent inventoryClickEvent) {

    }

    @Override
    public @NotNull Map<Integer, ItemStack> getContent() {
        int previousItemSlot;
        int nextItemSlot;
        int closeMenuSlot;

        int purpleSetOne;
        int redRemoveTen;
        int redRemoveOne;
        int itemSlot;
        int greenAddOne;
        int greenAddTen;
        int purpleAddSixtyFour;
        if (shop.isOwner(getOwner().getUniqueId())) {
            previousItemSlot = 39;
            nextItemSlot = 41;
            closeMenuSlot = 40;
            purpleSetOne = 19;
            redRemoveTen = 20;
            redRemoveOne = 21;
            itemSlot = 22;
            greenAddOne = 23;
            greenAddTen = 24;
            purpleAddSixtyFour = 25;
        } else {
            previousItemSlot = 30;
            nextItemSlot = 32;
            closeMenuSlot = 31;
            purpleSetOne = 10;
            redRemoveTen = 11;
            redRemoveOne = 12;
            itemSlot = 13;
            greenAddOne = 14;
            greenAddTen = 15;
            purpleAddSixtyFour = 16;
        }

        Map<Integer, ItemStack> content = fill(Material.GRAY_STAINED_GLASS_PANE);
        content.put(previousItemSlot, new ItemBuilder(this, Material.RED_CONCRETE, itemMeta -> {
            itemMeta.setDisplayName(ChatColor.RED + "Item précédent");
        }).setNextMenu(new ShopMenu(getOwner(), guildManager, playerShopManager, shop, onFirstItem() ? itemIndex : itemIndex - 1)));
        content.put(nextItemSlot, new ItemBuilder(this, Material.LIME_CONCRETE, itemMeta -> {
            itemMeta.setDisplayName(ChatColor.GREEN + "Item suivant");
        }).setNextMenu(new ShopMenu(getOwner(), guildManager, playerShopManager, shop, onLastItem() ? itemIndex : itemIndex + 1)));
        content.put(closeMenuSlot, new ItemBuilder(this, Material.BARRIER, itemMeta -> {
            itemMeta.setDisplayName(ChatColor.GRAY + "Fermer");
        }).setCloseButton());
        if (shop.isOwner(getOwner().getUniqueId()))
            putOwnerItems(content);
        content.put(purpleSetOne, new ItemBuilder(this, Material.PURPLE_STAINED_GLASS_PANE, itemMeta -> {
            itemMeta.setDisplayName(ChatColor.DARK_PURPLE + "Définir à 1");
        }).setOnClick(inventoryClickEvent -> {
            if (getCurrentItem() == null) return;
            amountToBuy = 1;
            open();
        }));
        content.put(redRemoveTen, new ItemBuilder(this, Material.RED_STAINED_GLASS_PANE, itemMeta -> {
            itemMeta.setDisplayName(ChatColor.RED + "Retirer 10");
        }).setOnClick(inventoryClickEvent -> {
            if (getCurrentItem() == null) return;
            if (amountToBuy == 1) return;
            if (amountToBuy - 10 < 1) {
                amountToBuy = 1;
            } else {
                amountToBuy -= 10;
            }
            open();
        }));
        content.put(redRemoveOne, new ItemBuilder(this, Material.RED_STAINED_GLASS_PANE, itemMeta -> {
            itemMeta.setDisplayName(ChatColor.RED + "Retirer 1");
        }).setOnClick(inventoryClickEvent -> {
            if (getCurrentItem() == null) return;
            if (amountToBuy == 1) return;
            amountToBuy--;
            open();
        }));
        if (getCurrentItem() != null)
            content.put(itemSlot, new ItemBuilder(this, getCurrentItem().getItem(), itemMeta -> {
                itemMeta.setDisplayName(ChatColor.BOLD + "" + ChatColor.WHITE + ShopItem.getItemName(getOwner(), getCurrentItem().getItem()));
                List<String> lore = new ArrayList<>();
                lore.add(ChatColor.GRAY + "■ Prix: " + ChatColor.RED + (getCurrentItem().getPricePerItem() * amountToBuy) + "€");
                lore.add(ChatColor.GRAY + "■ En stock: " + EconomyManager.formatValue(getCurrentItem().getAmount()));
                lore.add(ChatColor.GRAY + "■ Cliquez pour en acheter " + ChatColor.WHITE + amountToBuy);
                itemMeta.setLore(lore);
            }).setNextMenu(new ConfirmationMenu(getOwner(), inventoryClickEvent -> {
                MethodState buyState = shop.buy(getCurrentItem(), amountToBuy, getOwner());
                if (buyState == MethodState.ERROR) {
                    getOwner().sendMessage(ChatColor.RED + "Vous n'avez pas assez d'argent pour acheter cet item");
                    getOwner().closeInventory();
                    return;
                }
                if (buyState == MethodState.WARNING) {
                    getOwner().sendMessage(ChatColor.RED + "Il n'y a pas assez de stock pour acheter cet item");
                    getOwner().closeInventory();
                    return;
                }
                if (buyState == MethodState.SPECIAL) {
                    getOwner().sendMessage(ChatColor.RED + "Vous n'avez pas assez de place dans votre inventaire");
                    getOwner().closeInventory();
                    return;
                }
                if (buyState == MethodState.ESCAPE) {
                    getOwner().sendMessage(ChatColor.RED + "Erreur lors de l'achat");
                    getOwner().closeInventory();
                    return;
                }
                getOwner().sendMessage(ChatColor.GREEN + "Vous avez bien acheté " + amountToBuy + " " + getCurrentItem().getItem().getItemMeta().getDisplayName());
            })));
        content.put(greenAddOne, new ItemBuilder(this, Material.LIME_STAINED_GLASS_PANE, itemMeta -> {
            itemMeta.setDisplayName(ChatColor.GREEN + "Ajouter 1");
        }).setOnClick(inventoryClickEvent -> {
            if (getCurrentItem() == null) return;
            amountToBuy++;
            open();
        }));
        content.put(greenAddTen, new ItemBuilder(this, Material.LIME_STAINED_GLASS_PANE, itemMeta -> {
            itemMeta.setDisplayName(ChatColor.GREEN + "Ajouter 10");
        }).setOnClick(inventoryClickEvent -> {
            if (getCurrentItem() == null) return;
            amountToBuy += 10;
            open();
        }));
        content.put(purpleAddSixtyFour, new ItemBuilder(this, Material.PURPLE_STAINED_GLASS_PANE, itemMeta -> {
            itemMeta.setDisplayName(ChatColor.DARK_PURPLE + "Ajouter 64");
        }).setOnClick(inventoryClickEvent -> {
            if (getCurrentItem() == null) return;
            if (amountToBuy == 1) amountToBuy = 64;
            else amountToBuy += 64;
            open();
        }));
        return content;
    }

    private void putOwnerItems(Map<Integer, ItemStack> content) {
        content.put(0, new ItemBuilder(this, Material.RED_DYE, itemMeta -> {
            itemMeta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Supprimer le shop");
        }).setNextMenu(new ConfirmationMenu(getOwner(), inventoryClickEvent -> {
            boolean isInGuild = guildManager.isInGuild(getOwner().getUniqueId());
            if (isInGuild) {
                MethodState deleteState = guildManager.getGuild(getOwner().getUniqueId()).deleteShop(getOwner(), shop.getUuid());
                if (deleteState == MethodState.ERROR) {
                    getOwner().sendMessage(ChatColor.RED + "Ce shop n'existe pas dans votre guilde");
                    return;
                }
                if (deleteState == MethodState.WARNING) {
                    getOwner().sendMessage(ChatColor.RED + "Ce shop n'est pas vide");
                    return;
                }
                if (deleteState == MethodState.SPECIAL) {
                    getOwner().sendMessage(ChatColor.RED + "Il vous faut au minimum le nombre d'argent remboursable pour supprimer un shop et obtenir un remboursement dans la banque de votre guilde");
                    return;
                }
                if (deleteState == MethodState.ESCAPE) {
                    getOwner().sendMessage(ChatColor.RED + "Caisse introuvable (appelez un admin)");
                }
                getOwner().sendMessage(ChatColor.GREEN + shop.getName() + " a été supprimé !");
                getOwner().sendMessage(ChatColor.GOLD + "[Shop]" + ChatColor.GREEN + " +75€ de remboursés sur la banque de la guilde");
            }
            else {
                MethodState methodState = playerShopManager.deleteShop(getOwner().getUniqueId());
                if (methodState == MethodState.WARNING) {
                    getOwner().sendMessage(ChatColor.RED + "Votre shop n'est pas vide");
                    return;
                }
                if (methodState == MethodState.ESCAPE) {
                    getOwner().sendMessage(ChatColor.RED + "Caisse introuvable (appelez un admin)");
                    return;
                }
                getOwner().sendMessage(ChatColor.GREEN + "Votre shop a bien été supprimé !");
                getOwner().sendMessage(ChatColor.GOLD + "[Shop]" + ChatColor.GREEN + " +400€ de remboursés sur votre compte personnel");
            }
            getOwner().closeInventory();
        })));
        content.put(3, new ItemBuilder(this, Material.PAPER, itemMeta -> {
            itemMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Vos ventes");
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "■ Ventes: " + ChatColor.WHITE + shop.getSales().size());
            lore.add(ChatColor.GRAY + "■ Cliquer pour voir vos ventes sur ce shop");
            itemMeta.setLore(lore);
        }));
        content.put(4, shop.getIcon(this, true));
        content.put(5, new ItemBuilder(this, Material.BARREL, itemMeta -> {
            itemMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Voir les stocks");
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "■ Stocks: " + ChatColor.WHITE + shop.getAllItemsAmount());
            lore.add(ChatColor.GRAY + "■ Cliquer pour voir les stocks de ce shop");
            itemMeta.setLore(lore);
        }));
        content.put(8, new ItemBuilder(this, Material.LIME_WOOL, itemMeta -> {
            itemMeta.setDisplayName(ChatColor.GREEN + "Ce shop vous appartient");
            if (shop.getOwner().isGuild()) {
                if (shop.getOwner().getGuild().getOwner().isTeam()) {
                    itemMeta.setLore(List.of(
                            ChatColor.GRAY + "■ Car vous faites partie de la team possédant la guilde"
                    ));
                }
            }
        }));
    }

    private ShopItem getCurrentItem() {
        if (itemIndex < 0 || itemIndex >= items.size()) {
            return null;
        }
        return items.get(itemIndex);
    }

    private boolean onFirstItem() {
        return itemIndex == 0;
    }

    private boolean onLastItem() {
        return itemIndex == items.size() - 1;
    }
}
