package fr.communaywen.core.trade;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import fr.communaywen.core.utils.Queue;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

@Feature("Trade")
@Credit("Armibule")
public class Trade implements Listener {
    // current trade requests (there is one entry for each player, static)
    public static final Queue<Player, Trade> tradesPlayer1 = new Queue<>(20);
    public static final Queue<Player, Trade> tradesPlayer2 = new Queue<>(20);

    private AywenCraftPlugin plugin;


    public static Trade makeNewTrade(Player player1, Player player2, double money1, double money2, AywenCraftPlugin plugin) {
        Trade trade = new Trade(player1, player2, money1, money2, plugin);

        tradesPlayer1.add(player1, trade);
        tradesPlayer2.add(player2, trade);

        return trade;
    }

    // trade instances
    public Player player1;
    public Player player2;

    public double money1;
    public double money2;

    public Inventory inventory1;
    public Inventory inventory2;

    public boolean accepted1 = false;
    public boolean accepted2 = false;

    private Trade(Player player1, Player player2, double money1, double money2, AywenCraftPlugin plugin) {
        this.player1 = player1;
        this.player2 = player2;

        this.money1 = money1;
        this.money2 = money2;

        this.inventory1 = Bukkit.createInventory(null, 9, "[Trade] Envoyer à " + player2.getName()/* + " 0$"*/);
        this.inventory2 = Bukkit.createInventory(null, 9, "[Trade] Envoyer à " + player1.getName()/* + " 0$"*/);

        this.plugin = plugin;
    }

    public boolean isLocked() {
        return accepted1 | accepted2;
    }

    public void openOwnItems(boolean isPlayer1) {
        if (isPlayer1) {
            player1.openInventory(inventory1);
        } else {
            player2.openInventory(inventory2);
        }
    }

    public void openOtherItems(boolean isPlayer1) {
        if (isPlayer1) {
            player1.sendMessage("Contenu de l'inventaire:");
            for (ItemStack item : inventory2.getContents()) {
                if (item != null) {
                    player1.sendMessage(item.getType().toString() + " x" + item.getAmount());
                }
            }
        } else {
            player2.sendMessage("Contenu de l'inventaire:");
            for (ItemStack item : inventory1.getContents()) {
                if (item != null) {
                    player2.sendMessage(item.getType().toString() + " x" + item.getAmount());
                }
            }
        }
    }

    public boolean setMoney1(double value) {
        boolean success = plugin.getManagers().getEconomyManager().withdrawBalance(player1, value - money1);

        if (!success) {
            return false;
        }

        money1 = value;
        return true;
    }

    public boolean setMoney2(double value) {
        boolean success = plugin.getManagers().getEconomyManager().withdrawBalance(player2, value - money1);

        if (!success) {
            return false;
        }

        money2 = value;
        return true;
    }

    public void cancel() {
        plugin.getManagers().getEconomyManager().addBalance(player1, money1);
        plugin.getManagers().getEconomyManager().addBalance(player2, money2);

        World player1World = player1.getWorld();
        Location player1Location = player1.getLocation();

        for (ItemStack stack : inventory1.getContents()) {
            if (stack != null) {
                Collection<ItemStack> leftItems = player1.getInventory().addItem(
                        stack
                ).values();

                if (leftItems.size() > 0) {
                    for (ItemStack leftStack : leftItems) {
                        player1World.dropItemNaturally(player1Location, leftStack);
                    }
                }
            }
        }


        World player2World = player2.getWorld();
        Location player2Location = player2.getLocation();

        for (ItemStack stack : inventory2.getContents()) {
            if (stack != null) {
                Collection<ItemStack> leftItems = player2.getInventory().addItem(
                        stack
                ).values();

                if (leftItems.size() > 0) {
                    for (ItemStack leftStack : leftItems) {
                        player2World.dropItemNaturally(player2Location, leftStack);
                    }
                }
            }
        }

        player1.playSound(player1.getEyeLocation(), Sound.ITEM_SHIELD_BREAK,  SoundCategory.PLAYERS, 1, 1);
        player2.playSound(player2.getEyeLocation(), Sound.ITEM_SHIELD_BREAK,  SoundCategory.PLAYERS, 1, 1);
        player1.sendMessage("§6Trade annulé !");
        player2.sendMessage("§6Trade annulé !");

        delete();
    }

    public void hasConcluded(boolean isPlayer1) {
        if (isPlayer1) {
            accepted1 = true;
        } else {
            accepted2 = true;
        }

        if (accepted1 && accepted2) {
            conclude();
        }
    }

    public void conclude() {
        plugin.getManagers().getEconomyManager().addBalance(player2, money1);
        plugin.getManagers().getEconomyManager().addBalance(player1, money2);


        World player1World = player1.getWorld();
        Location player1Location = player1.getLocation();

        for (ItemStack stack : inventory2.getContents()) {
            if (stack != null) {
                Collection<ItemStack> leftItems = player1.getInventory().addItem(
                        stack
                ).values();

                if (leftItems.size() > 0) {
                    for (ItemStack leftStack : leftItems) {
                        player1World.dropItemNaturally(player1Location, leftStack);
                    }
                }
            }
        }


        World player2World = player2.getWorld();
        Location player2Location = player2.getLocation();

        for (ItemStack stack : inventory1.getContents()) {
            if (stack != null) {
                Collection<ItemStack> leftItems = player2.getInventory().addItem(
                        stack
                ).values();

                if (leftItems.size() > 0) {
                    for (ItemStack leftStack : leftItems) {
                        player2World.dropItemNaturally(player2Location, leftStack);
                    }
                }
            }
        }

        player1.playSound(player1.getEyeLocation(), Sound.BLOCK_NOTE_BLOCK_FLUTE,  SoundCategory.PLAYERS,1, 1);
        player2.playSound(player2.getEyeLocation(), Sound.BLOCK_NOTE_BLOCK_FLUTE,  SoundCategory.PLAYERS,1, 1);
        player1.sendMessage("§aTrade réalisé !");
        player2.sendMessage("§aTrade réalisé !");

        delete();
    }

    private void delete() {
        inventory1.clear();
        inventory2.clear();
        player1.closeInventory();
        player2.closeInventory();

        money1 = 0;
        money2 = 0;
        inventory1 = null;
        inventory2 = null;

        tradesPlayer1.remove(player1);
        tradesPlayer2.remove(player2);
    }

    // On object destroy
    @Override
    protected void finalize() {
        if (inventory1 != null && inventory2 != null) {
            cancel();
        }
    }
}
