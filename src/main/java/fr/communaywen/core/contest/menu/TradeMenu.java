package fr.communaywen.core.contest.menu;

import dev.lone.itemsadder.api.CustomStack;
import dev.xernas.menulib.Menu;
import dev.xernas.menulib.utils.InventorySize;
import dev.xernas.menulib.utils.ItemBuilder;
import fr.communaywen.core.contest.ContestManager;
import fr.communaywen.core.mailboxes.MailboxManager;
import fr.communaywen.core.utils.constant.MessageManager;
import fr.communaywen.core.utils.constant.MessageType;
import fr.communaywen.core.utils.constant.Prefix;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static fr.communaywen.core.contest.ContestManager.getTradeSelected;
import static fr.communaywen.core.utils.ItemUtils.*;

public class TradeMenu extends Menu {

        public TradeMenu(Player owner) {
            super(owner);
        }

        @Override
        public @NotNull String getName() {
            return "Le Contest - Les Trades";
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
            if (getOwner().getOpenInventory().getTitle()!="Le Contest - Les Trades") {
                String campName = ContestManager.getPlayerCampName(getOwner());
                ChatColor campColor = ContestManager.getPlayerCampChatColor(getOwner());
                Material shell_contest = CustomStack.getInstance("contest:contest_shell").getItemStack().getType();

                List<String> loreinfo = new ArrayList<String>();
                List<String> lore_trade = new ArrayList<String>();

                loreinfo.add("§7Apprenez en plus sur les Contest !");
                loreinfo.add("§7Le déroulement..., Les résultats, ...");
                loreinfo.add("§e§lCLIQUEZ ICI POUR EN VOIR PLUS!");

                lore_trade.add("§7Vendez un maximum de ressources");
                lore_trade.add("§7Contre des §bCoquillages de Contest");
                lore_trade.add("§7Pour faire gagner la " + campColor + "Team " + campName);

                inventory.put(4, new ItemBuilder(this, shell_contest, itemMeta -> {
                    itemMeta.setDisplayName("§7Les Trades");
                    itemMeta.setLore(lore_trade);
                    itemMeta.setCustomModelData(10000);
                }));
                List<Map<String, Object>> selectedTrades = getTradeSelected(true);

                List<Integer> slot_trade = new ArrayList<Integer>();
                slot_trade.add(10);
                slot_trade.add(11);
                slot_trade.add(12);
                slot_trade.add(13);
                slot_trade.add(14);
                slot_trade.add(15);
                slot_trade.add(16);
                slot_trade.add(20);
                slot_trade.add(21);
                slot_trade.add(22);
                slot_trade.add(23);
                slot_trade.add(24);

                List<String> lore_trades = new ArrayList<String>();

                int index = 0;
                for (Map<String, Object> trade : selectedTrades) {
                     Integer slot = slot_trade.get(index);
                     index++;

                     Material m = Material.getMaterial((String) trade.get("ress"));

                     lore_trades.add("§7Vendez §e" + trade.get("amount") + " de cette ressource §7pour §b" + trade.get("amount_shell") + " Coquillage(s) de Contest");
                     lore_trades.add("§e§lCLIQUE-GAUCHE POUR VENDRE UNE FOIS");
                     lore_trades.add("§e§lSHIFT-CLIQUE-GAUCHE POUR VENDRE TOUTE CETTE RESSOURCE");

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

                             if (ContestManager.hasEnoughItems(getOwner(), inventoryClickEvent.getCurrentItem().getType(), amount)) {
                                           int amount_shell2 = (items / amount) * amount_shell;
                                           int items1 = (amount_shell2 / amount_shell) * amount;
                                           ContestManager.removeItemsFromInventory(getOwner(), inventoryClickEvent.getCurrentItem().getType(), items1);
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
                                       if (ContestManager.hasEnoughItems(getOwner(), inventoryClickEvent.getCurrentItem().getType(), amount)) {

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

                                           ContestManager.removeItemsFromInventory(getOwner(), inventoryClickEvent.getCurrentItem().getType(), amount);
                                           MessageManager.sendMessageType(getOwner(), "§7Vous avez échangé §e" + amount + " " + m1 + " §7contre§b " + amount_shell + " Coquillages(s) de Contest", Prefix.CONTEST, MessageType.SUCCESS, true);
                                       } else {
                                           MessageManager.sendMessageType(getOwner(), "§cVous n'avez pas assez de cette ressource pour pouvoir l'échanger!", Prefix.CONTEST, MessageType.ERROR, true);
                                       }
                                   }

                           }));

                           lore_trades.clear();
                }

                   inventory.put(35, new ItemBuilder(this, Material.EMERALD, itemMeta -> {
                       itemMeta.setDisplayName("§r§aPlus d'info !");
                       itemMeta.setLore(loreinfo);
                   }).setNextMenu(new MoreInfoMenu(getOwner())));

                   return inventory;
               }

            Inventory currentinv = getOwner().getOpenInventory().getTopInventory();
            Map<Integer, ItemStack> currentinv_map  = new HashMap<>();

            for (int i = 0; i < currentinv.getSize(); i++) {
                ItemStack item = currentinv.getItem(i);
                if (item != null) {
                    currentinv_map.put(i, item);
                }
            }
            return currentinv_map;
        }
}
