package fr.communaywen.core.contest.menu;

import dev.lone.itemsadder.api.CustomStack;
import dev.xernas.menulib.Menu;
import dev.xernas.menulib.utils.InventorySize;
import dev.xernas.menulib.utils.ItemBuilder;
import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.contest.cache.ContestCache;
import fr.communaywen.core.contest.managers.ContestManager;
import fr.communaywen.core.mailboxes.MailboxManager;
import fr.communaywen.core.utils.ItemUtils;
import fr.communaywen.core.utils.constant.MessageManager;
import fr.communaywen.core.utils.constant.MessageType;
import fr.communaywen.core.utils.constant.Prefix;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

import static fr.communaywen.core.utils.ItemUtils.getSlotNull;
import static fr.communaywen.core.utils.ItemUtils.getNumberItemToStack;
import static fr.communaywen.core.utils.ItemUtils.splitAmountIntoStack;


public class TradeMenu extends Menu {
    private final ContestManager contestManager;
    private final ContestCache contestCache;
        public TradeMenu(Player owner, ContestManager manager) {
            super(owner);
            this.contestCache = AywenCraftPlugin.getInstance().getManagers().getContestCache();
            this.contestManager = manager;
        }

        @Override
        public @NotNull String getName() {
            return PlaceholderAPI.setPlaceholders(getOwner(), "§r§f%img_offset_-48%%img_contest_menu%");
        }

        @Override
        public @NotNull InventorySize getInventorySize() {
            return InventorySize.LARGE;
        }

        @Override
        public void onInventoryClick(InventoryClickEvent click) {
        }

    @Override
    public @NotNull Map<Integer, ItemStack> getContent() {
        Map<Integer, ItemStack> inventory = new HashMap<>();
        String campName = contestManager.getPlayerCampName(getOwner());
        ChatColor campColor = contestCache.getPlayerColorCache(getOwner());
        Material shell_contest = CustomStack.getInstance("contest:contest_shell").getItemStack().getType();

        List<String> loreinfo = Arrays.asList(
                "§7Apprenez en plus sur les Contest !",
                "§7Le déroulement..., Les résultats, ...",
                "§e§lCLIQUEZ ICI POUR EN VOIR PLUS!"
        );

        List<String> lore_trade = Arrays.asList(
                "§7Vendez un maximum de ressources",
                "§7Contre des §bCoquillages de Contest",
                "§7Pour faire gagner la " + campColor + "Team " + campName
        );

        inventory.put(4, new ItemBuilder(this, shell_contest, itemMeta -> {
            itemMeta.setDisplayName("§7Les Trades");
            itemMeta.setLore(lore_trade);
            itemMeta.setCustomModelData(10000);
        }));

        List<Map<String, Object>> selectedTrades = contestManager.getTradeSelected(true).stream()
                .sorted(Comparator.comparing(trade -> (String) trade.get("ress")))
                .collect(Collectors.toList());

        List<Integer> slot_trade = Arrays.asList(10, 11, 12, 13, 14, 15, 16, 20, 21, 22, 23, 24);

        for (int i = 0; i < selectedTrades.size(); i++) {
            Map<String, Object> trade = selectedTrades.get(i);
            Integer slot = slot_trade.get(i);

            Material m = Material.getMaterial((String) trade.get("ress"));
            List<String> lore_trades = Arrays.asList(
                    "§7Vendez §e" + trade.get("amount") + " de cette ressource §7pour §b" + trade.get("amount_shell") + " Coquillage(s) de Contest",
                    "§e§lCLIQUE-GAUCHE POUR VENDRE UNE FOIS",
                    "§e§lSHIFT-CLIQUE-GAUCHE POUR VENDRE TOUTE CETTE RESSOURCE"
            );

            inventory.put(slot, new ItemBuilder(this, m, itemMeta -> {
                itemMeta.setLore(lore_trades);
            }).setOnClick(inventoryClickEvent -> {
                String m1 = String.valueOf(inventoryClickEvent.getCurrentItem().getType());
                int amount = (int) trade.get("amount");
                int amount_shell = (int) trade.get("amount_shell");
                ItemStack shell_contestItem = CustomStack.getInstance("contest:contest_shell").getItemStack();
                if (inventoryClickEvent.isLeftClick() && inventoryClickEvent.isShiftClick()) {
                    int items = 0;
                    for (ItemStack is : getOwner().getInventory().getContents()) {
                        if (is != null && is.getType() == inventoryClickEvent.getCurrentItem().getType()) {
                            items = items + is.getAmount();
                        }
                    }

                    if (ItemUtils.hasEnoughItems(getOwner(), inventoryClickEvent.getCurrentItem().getType(), amount)) {
                        int amount_shell2 = (items / amount) * amount_shell;
                        int items1 = (amount_shell2 / amount_shell) * amount;
                        ItemUtils.removeItemsFromInventory(getOwner(), inventoryClickEvent.getCurrentItem().getType(), items1);
                        int slot_empty = getSlotNull(getOwner());
                        int stack_available = slot_empty * 64;
                        int additem = Math.min(amount_shell2, stack_available);
                        if (stack_available >=64) {
                            shell_contestItem.setAmount(additem);
                            for (ItemStack item : splitAmountIntoStack(shell_contestItem)) {
                                getOwner().getInventory().addItem(item);
                            }
                            int remain1 = amount_shell2 - additem;
                            if(remain1 != 0) {
                                int numbertoStack = getNumberItemToStack(getOwner(), shell_contestItem);
                                if (numbertoStack > 0) {
                                    shell_contestItem.setAmount(numbertoStack);
                                    getOwner().getInventory().addItem(shell_contestItem);
                                }

                                ItemStack newshell_contestItem = CustomStack.getInstance("contest:contest_shell").getItemStack();
                                int remain2 = remain1 - numbertoStack;
                                if (remain2 != 0) {
                                    newshell_contestItem.setAmount(remain2);
                                    List<ItemStack> itemlist = splitAmountIntoStack(newshell_contestItem);
                                    ItemStack[] shell_contest_array = itemlist.toArray(new ItemStack[itemlist.size()]);
                                    MailboxManager.sendItems(getOwner(), getOwner(), shell_contest_array);
                                }
                            }
                        } else {
                            shell_contestItem.setAmount(amount_shell2);
                            ItemStack[] shell_contest_array = new ItemStack[]{shell_contestItem, shell_contestItem};
                            for (ItemStack item : splitAmountIntoStack(shell_contestItem)) {
                                getOwner().getInventory().addItem(item);
                            }

                            MailboxManager.sendItems(getOwner(), getOwner(), shell_contest_array);
                        }

                        MessageManager.sendMessageType(getOwner(), "§7Vous avez échangé §e" + items1 + " " + m1 + " §7contre§b " + amount_shell2 + " Coquillages(s) de Contest", Prefix.CONTEST, MessageType.SUCCESS, true);
                    } else {
                        MessageManager.sendMessageType(getOwner(), "§cVous n'avez pas assez de cette ressource pour pouvoir l'échanger!", Prefix.CONTEST, MessageType.ERROR, true);
                    }
                } else if (inventoryClickEvent.isLeftClick()) {
                    if (ItemUtils.hasEnoughItems(getOwner(), inventoryClickEvent.getCurrentItem().getType(), amount)) {

                        //mettre dans l'inv ou boite mail?
                        if (Arrays.asList(getOwner().getInventory().getStorageContents()).contains(null)) {
                            shell_contestItem.setAmount(amount_shell);
                            for (ItemStack item : splitAmountIntoStack(shell_contestItem)) {
                                getOwner().getInventory().addItem(item);
                            }
                        } else {
                            shell_contestItem.setAmount(amount_shell);
                            ItemStack[] shell_contest_array = new ItemStack[]{shell_contestItem};
                            MailboxManager.sendItems(getOwner(), getOwner(), shell_contest_array);
                        }

                        ItemUtils.removeItemsFromInventory(getOwner(), inventoryClickEvent.getCurrentItem().getType(), amount);
                        MessageManager.sendMessageType(getOwner(), "§7Vous avez échangé §e" + amount + " " + m1 + " §7contre§b " + amount_shell + " Coquillages(s) de Contest", Prefix.CONTEST, MessageType.SUCCESS, true);
                    } else {
                        MessageManager.sendMessageType(getOwner(), "§cVous n'avez pas assez de cette ressource pour pouvoir l'échanger!", Prefix.CONTEST, MessageType.ERROR, true);
                    }
                }
            }));
        }

        inventory.put(27, new ItemBuilder(this, Material.ARROW, itemMeta -> {
            itemMeta.setDisplayName("§r§aRetour");
        }).setBackButton());

        inventory.put(35, new ItemBuilder(this, Material.EMERALD, itemMeta -> {
            itemMeta.setDisplayName("§r§aPlus d'info !");
            itemMeta.setLore(loreinfo);
        }).setNextMenu(new MoreInfoMenu(getOwner(), contestManager)));

        return inventory;
    }
}
