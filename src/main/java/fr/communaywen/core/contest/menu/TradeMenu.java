package fr.communaywen.core.contest.menu;

import dev.lone.itemsadder.api.CustomStack;
import dev.xernas.menulib.Menu;
import dev.xernas.menulib.utils.InventorySize;
import dev.xernas.menulib.utils.ItemBuilder;
import fr.communaywen.core.contest.ContestManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            try {

            String campName = ContestManager.getPlayerCampName(getOwner());
            ChatColor campColor = ContestManager.getPlayerCampChatColor(getOwner());

            ItemStack shell_contestItem = CustomStack.getInstance("contest:contest_shell").getItemStack();
            Material shell_contest = CustomStack.getInstance("contest:contest_shell").getItemStack().getType();

            List<String> loreinfo = new ArrayList<String>();
            List<String> lore_trade = new ArrayList<String>();

            loreinfo.add("§7Apprenez en plus sur les Contest !");
            loreinfo.add("§7Le déroulement..., Les résultats, ...");
            loreinfo.add("§e§lCLIQUEZ ICI POUR EN VOIR PLUS!");

            lore_trade.add("§7Vendez un maximum de ressources");
            lore_trade.add("§7Contre des §bCoquillages de Contest");
            lore_trade.add("§7Pour faire gagner la "+ campColor + "Team " + campName);

            inventory.put(4, new ItemBuilder(this, shell_contest, itemMeta -> {
                itemMeta.setDisplayName("§7Les Trades");
                itemMeta.setLore(lore_trade);
            }));
                ResultSet rs1 = ContestManager.getTradeSelected(true);
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


               while(rs1.next()) {
                    Integer row = rs1.getRow() - 1;
                    Integer slot = slot_trade.get(row);

                    Material m = Material.getMaterial(rs1.getString("ress"));

                   lore_trades.add("§7Vendez §e" + rs1.getInt("amount") +" de cette ressource §7pour §b"+ rs1.getInt("amount_shell") + " Coquillage(s) de Contest");
                   lore_trades.add("§e§lCLIQUE-GAUCHE POUR VENDRE UNE FOIS");
                   lore_trades.add("§e§lSHIFT-CLIQUE-GAUCHE POUR VENDRE TOUTE CETTE RESSOURCE");

                    inventory.put(slot, new ItemBuilder(this, m, itemMeta -> {
                        itemMeta.setLore(lore_trades);
                    }).setOnClick(inventoryClickEvent -> {
                        String m1 = String.valueOf(inventoryClickEvent.getCurrentItem().getType());
                        int amount = ContestManager.getIntWhere("contest_trades", "ress", m1, "amount");
                        int amount_shell = ContestManager.getIntWhere("contest_trades", "ress", m1, "amount_shell");
                        if (inventoryClickEvent.isLeftClick() && inventoryClickEvent.isShiftClick()) {
                            System.out.println("shift left click");

                        } else if (inventoryClickEvent.isLeftClick()) {
                            if (ContestManager.hasEnoughItems(getOwner(), inventoryClickEvent.getCurrentItem().getType(), amount)) {
                                getOwner().playSound(getOwner().getEyeLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1.0F, 1.8F);

                                ContestManager.removeItemsFromInventory(getOwner(),inventoryClickEvent.getCurrentItem().getType(),amount);
                                getOwner().sendMessage("§6§lCONTEST! §7Vous avez échangé §e"+ amount +" "+ m1 +" §7contre§b " + amount_shell + " Coquillages(s) de Contest");

                            } else {
                                getOwner().playSound(getOwner().getEyeLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0F, 0.2F);
                                getOwner().sendMessage("§6§lCONTEST! §cVous n'avez pas assez de cette ressource pour pouvoir l'échanger!");
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
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (Exception e) {
                System.out.println("Une erreur inattendue s'est produite : " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
}
