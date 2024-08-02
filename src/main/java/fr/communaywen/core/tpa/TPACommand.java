package fr.communaywen.core.tpa;

import dev.xernas.menulib.MenuBuilder;
import dev.xernas.menulib.MenuItemClickContext;
import dev.xernas.menulib.MenuListener;
import dev.xernas.menulib.items.ClickableItem;
import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Named;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import java.util.ArrayList;
import java.util.List;

@Feature("TPA")
@Credit({"ddemile", "Axillity", "misieur", "process"})
public class TPACommand implements Listener {

    private final TPAQueue tpQueue = TPAQueue.INSTANCE;
    private final AywenCraftPlugin plugin;
    private static final String INVENTORY_TITLE = "Sélectionnez un joueur";
    private static final int PLAYERS_PER_PAGE = 45;

    public TPACommand(AywenCraftPlugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Command("tpa")
    @CommandPermission("ayw.command.tpa")
    public void onCommand(Player player, @Named("joueur") String targetName) {
        if (targetName == null || targetName.isEmpty()) {
            openPlayerListGUI(player, 0);
            return;
        }

        Player target = Bukkit.getPlayer(targetName);
        if (target == null) {
            player.sendMessage("§cLe joueur '" + targetName + "' n'est pas en ligne.");
            return;
        }

        sendTPARequest(player, target);
    }

    private void sendTPARequest(Player player, Player target) {
        if (player.equals(target)) {
            player.sendMessage("§cVous ne pouvez pas vous téléporter à vous-même.");
            return;
        }

        if (tpQueue.hasPendingRequest(player)) {
            player.sendMessage("§cVous avez déjà une demande de téléportation en attente...");
            return;
        }

        tpQueue.addRequest(player, target);
        player.sendMessage("§aDemande de téléportation envoyée à §f" + target.getName() + "§a.");

        final Component message = Component.text(player.getName() + " vous a envoyé une demande de téléportation. Tapez /tpaccept pour accepter.")
                .color(TextColor.color(30, 100, 150))
                .clickEvent(ClickEvent.runCommand("/tpaccept"))
                .hoverEvent(HoverEvent.showText(Component.text("§7[§aCliquez pour accepter§7]")));

        plugin.getAdventure().player(target).sendMessage(message);

        new BukkitRunnable() {
            @Override
            public void run() {
                tpQueue.expireRequest(player, target);
            }
        }.runTaskLater(plugin, 2400);
    }

    private void openPlayerListGUI(Player player, int page) {
        List<Player> onlinePlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
        int totalPlayers = onlinePlayers.size();
        int startIndex = page * PLAYERS_PER_PAGE;
        int endIndex = Math.min(startIndex + PLAYERS_PER_PAGE, totalPlayers);

        MenuBuilder menuBuilder = new MenuBuilder(INVENTORY_TITLE, 54)
                .withListener(new MenuListener() {
                    @Override
                    public void onItemClick(MenuItemClickContext context) {
                        ItemStack clickedItem = context.getClickedItem();
                        Player clicker = context.getClicker();
                        if (clickedItem == null) return;

                        Material type = clickedItem.getType();
                        if (type == Material.PLAYER_HEAD) {
                            SkullMeta meta = (SkullMeta) clickedItem.getItemMeta();
                            if (meta != null && meta.getOwningPlayer() != null) {
                                Player target = Bukkit.getPlayer(meta.getOwningPlayer().getUniqueId());
                                if (target != null) {
                                    sendTPARequest(clicker, target);
                                    clicker.closeInventory();
                                }
                            }
                        } else if (type == Material.ARROW) {
                            String itemName = clickedItem.getItemMeta().getDisplayName();
                            if (itemName.equals("§aPage suivante")) {
                                openPlayerListGUI(clicker, page + 1);
                            } else if (itemName.equals("§aPage précédente")) {
                                openPlayerListGUI(clicker, page - 1);
                            }
                        }
                    }
                });

        for (int i = startIndex; i < endIndex; i++) {
            Player onlinePlayer = onlinePlayers.get(i);
            if (!onlinePlayer.equals(player)) {
                ItemStack head = createPlayerHead(onlinePlayer);
                menuBuilder.addItem(new ClickableItem(head));
            }
        }

        if (page > 0) {
            menuBuilder.setItem(45, new ClickableItem(createNavigationItem("§aPage précédente", Material.ARROW)));
        }
        if (endIndex < totalPlayers) {
            menuBuilder.setItem(53, new ClickableItem(createNavigationItem("§aPage suivante", Material.ARROW)));
        }

        menuBuilder.build().open(player);
    }

    private ItemStack createPlayerHead(Player player) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        if (meta != null) {
            meta.setOwningPlayer(player);
            meta.setDisplayName("§a" + player.getName());
            head.setItemMeta(meta);
        }
        return head;
    }

    private ItemStack createNavigationItem(String name, Material material) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            item.setItemMeta(meta);
        }
        return item;
    }
}
