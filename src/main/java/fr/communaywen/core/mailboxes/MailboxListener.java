package fr.communaywen.core.mailboxes;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.mailboxes.letter.LetterHead;
import fr.communaywen.core.mailboxes.menu.HomeMailbox;
import fr.communaywen.core.mailboxes.menu.PendingMailbox;
import fr.communaywen.core.mailboxes.menu.PlayerMailbox;
import fr.communaywen.core.mailboxes.menu.PlayersList;
import fr.communaywen.core.mailboxes.menu.letter.Letter;
import fr.communaywen.core.mailboxes.menu.letter.SendingLetter;
import fr.communaywen.core.mailboxes.utils.MailboxInv;
import fr.communaywen.core.mailboxes.utils.PaginatedMailbox;
import fr.communaywen.core.utils.database.DatabaseConnector;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;
import java.util.UUID;

import static fr.communaywen.core.mailboxes.utils.MailboxMenuManager.*;
import static fr.communaywen.core.mailboxes.utils.MailboxUtils.*;

public class MailboxListener extends DatabaseConnector implements Listener {
    private final AywenCraftPlugin plugin = AywenCraftPlugin.getInstance();

    /*
    public MailboxListener() {
        final int DELAY = 1; // in minutes
        BukkitRunnable runnable = new BukkitRunnable() {
            int i = 0;

            @Override
            public void run() {
                showBossBar(i++);
            }
        };
        runnable.runTaskTimer(plugin, DELAY, DELAY * 60L * 20L);
    }

    public void showBossBar(int i) {
        BossBar bossBar = BossBar.bossBar(getBossBarTitle(i), 1, BossBar.Color.GREEN, BossBar.Overlay.NOTCHED_10);
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.showBossBar(bossBar);
        }
        new BukkitRunnable() {
            int j = 1;

            @Override
            public void run() {
                bossBar.progress(1.0F - j++ * 0.1F);
                if (j > 10) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.hideBossBar(bossBar);
                    }
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 10L, 10L);
    }

    public Component getBossBarTitle(int i) {
        return Component.text("Envoi de lettre " + i, NamedTextColor.GOLD);
    }
    */

    @EventHandler
    public void onInventoryOpen(InventoryCloseEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();
        if (holder instanceof MailboxInv mailboxInv) mailboxInv.addInventory();
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();
        if (holder instanceof SendingLetter sendingLetter) sendingLetter.giveItems();
        if (holder instanceof MailboxInv mailboxInv) mailboxInv.removeInventory();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            Player player = event.getPlayer();
            try (PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) AS affected_rows, sender_id, items_count, id FROM mailbox_items WHERE receiver_id = ? AND refused = false;")) {
                statement.setString(1, player.getUniqueId().toString());
                try (ResultSet result = statement.executeQuery()) {
                    if (result.next()) {
                        Component message = null;
                        int affectedRows = result.getInt("affected_rows");
                        if (affectedRows > 1) {
                                message = Component.text("Vous avez reçu ", NamedTextColor.DARK_GREEN)
                                        .append(Component.text(affectedRows, NamedTextColor.GREEN))
                                        .append(Component.text(" lettres.", NamedTextColor.DARK_GREEN))
                                        .append(Component.text("\nCliquez-ici", NamedTextColor.YELLOW))
                                        .clickEvent(ClickEvent.runCommand("/mailbox"))
                                        .hoverEvent(getHoverEvent("Ouvrir ma boîte aux lettres"))
                                        .append(Component.text(" pour ouvrir les lettres", NamedTextColor.GOLD));
                        } else if (affectedRows == 1) {
                            int itemsCount = result.getInt("items_count");
                            int letterId = result.getInt("id");
                            UUID senderUUID = UUID.fromString(result.getString("sender_id"));
                            OfflinePlayer sender = Bukkit.getOfflinePlayer(senderUUID);
                            String senderName = sender.getName();
                            message = Component.text("Vous avez reçu ", NamedTextColor.DARK_GREEN)
                                    .append(Component.text(itemsCount, NamedTextColor.GREEN))
                                    .append(Component.text(" " + getItemCount(itemsCount) + " de la part de ", NamedTextColor.DARK_GREEN))
                                    .append(Component.text(senderName == null ? "Unknown" : senderName, NamedTextColor.GREEN))
                                    .append(Component.text("\nCliquez-ici", NamedTextColor.YELLOW))
                                    .clickEvent(getRunCommand("open " + letterId))
                                    .hoverEvent(getHoverEvent("Ouvrir la lettre #" + letterId))
                                    .append(Component.text(" pour ouvrir la lettre", NamedTextColor.GOLD));
                        }
                        if (message != null) sendSuccessMessage(player, message);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                sendFailureMessage(event.getPlayer(), "Une erreur est survenue.");
            }
        });
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        Inventory inv = event.getView().getTopInventory();
        InventoryHolder holder = inv.getHolder();
        Set<Integer> slots = event.getRawSlots();

        if (holder instanceof SendingLetter) {
            for (int slot : slots) {
                if (slot >= 54) continue;
                int row = slot / 9;
                if (row < 1 || row > 3) {
                    event.setCancelled(true);
                    return;
                }
            }
        } else if (holder instanceof MailboxInv) {
            for (int slot : slots) {
                if (slot >= holder.getInventory().getSize()) continue;
                event.setCancelled(true);
            }
        }
    }

    private void runTask(Runnable runnable) {
        plugin.getServer().getScheduler().runTask(plugin, runnable);
    }
    
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Inventory inv = event.getView().getTopInventory();
        InventoryHolder holder = inv.getHolder();
        ItemStack item = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();
        int slot = event.getRawSlot();
        int row = slot / 9;

        if (slot >= inv.getSize()) {
            if (event.isShiftClick()) {
                if (holder instanceof SendingLetter sendingLetter) {
                    if (sendingLetter.noSpace(item)) event.setCancelled(true);
                } else if (holder instanceof MailboxInv) event.setCancelled(true);
            }
            return;
        }

        if (holder instanceof SendingLetter) {
            if (row < 1 || row > 3) {
                event.setCancelled(true);
            }
        } else if (holder instanceof MailboxInv) event.setCancelled(true);

        //! Buttons actions
        if (cancelBtn(item) && holder instanceof MailboxInv) {
            runTask(player::closeInventory);
            return;
        } else if (item != null && item.getType() == Material.CHEST && slot == 45) {
            runTask(() -> HomeMailbox.openHomeMailbox(player, plugin));
            return;
        } else if (holder instanceof PaginatedMailbox<? extends ItemStack> menu) {
            if (nextPageBtn(item)) {
                runTask(menu::nextPage);
                return;
            } else if (previousPageBtn(item)) {
                runTask(menu::previousPage);
                return;
            }
        }

        if (holder instanceof SendingLetter sendingLetter) {
            if (sendBtn(item)) runTask(sendingLetter::sendLetter);
        } else if (holder instanceof PlayerMailbox playerMailbox) {
            if (item != null && item.getType() == Material.PLAYER_HEAD) {
                LetterHead letterHead = playerMailbox.getByIndex(slot);
                if (letterHead == null) return;
                runTask(() -> letterHead.openLetter(player));
            }
        } else if (holder instanceof Letter letter) {
            if (acceptBtn(item)) {
                runTask(letter::accept);
            } else if (refuseBtn(item)) {
                runTask(letter::refuse);
            }
        } else if (holder instanceof HomeMailbox) {
            if (slot == 3) {
                runTask(() -> HomeMailbox.openPendingMailbox(player));
            } else if (slot == 4) {
                runTask(() -> HomeMailbox.openPlayerMailbox(player));
            } else if (slot == 5) {
                runTask(() -> HomeMailbox.openPlayersList(player));
            } else if (slot == 8) {
                runTask(() -> {
	                try {
		                HomeMailbox.openSettings(player);
	                } catch (SQLException e) {
		                e.printStackTrace();
                        player.sendMessage(ChatColor.DARK_RED + "Impossible d'ouvrir le menu");
	                }
                });
            }
        } else if (holder instanceof PendingMailbox pendingMailbox) {
            if (item != null && item.getType() == Material.PLAYER_HEAD) {
                runTask(() -> pendingMailbox.clickLetter(slot));
            }
        } else if (holder instanceof PlayersList) {
            if (item != null && item.getType() == Material.PLAYER_HEAD) {
                SkullMeta meta = (SkullMeta) item.getItemMeta();
                OfflinePlayer receiver = meta.getOwningPlayer();
                if (receiver == null) return;
                runTask(() -> {
	                try {
		                HomeMailbox.openSendingMailbox(player, receiver, AywenCraftPlugin.getInstance());
	                } catch (SQLException e) {
		                e.printStackTrace();
	                }
                });
            }
        }
    }
}
