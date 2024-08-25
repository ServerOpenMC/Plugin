package fr.communaywen.core.contest.menu;

import dev.lone.itemsadder.api.CustomStack;
import dev.xernas.menulib.Menu;
import dev.xernas.menulib.utils.InventorySize;
import dev.xernas.menulib.utils.ItemBuilder;
import fr.communaywen.core.contest.ContestManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

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

            String campName = ContestManager.getPlayerCampName(getOwner());
            ChatColor campColor = ContestManager.getPlayerCampChatColor(getOwner());

            Material shell_contest = CustomStack.getInstance("contest:shell_contest").getItemStack().getType();

            List<String> loreinfo = new ArrayList<String>();
            List<String> lore_trade = new ArrayList<String>();

            loreinfo.add("§7Apprenez en plus sur les Contest !");
            loreinfo.add("§7Le déroulement..., Les résultats, ...");
            loreinfo.add("§e§lCLIQUEZ ICI POUR EN VOIR PLUS!");

            lore_trade.add("§7Vendez un maximum de ressources");
            lore_trade.add("§7Contre des §bCoquillages de Contest");
            lore_trade.add("§7Pour faire gagner la "+ campColor + "Team " + campName);

            for(int i = 0; i < getInventorySize().getSize(); i++) {
                if(i==5) {
                    inventory.put(5, new ItemBuilder(this, shell_contest, itemMeta -> {
                        itemMeta.setDisplayName("§7Les Trades");
                        itemMeta.setLore(lore_trade);
                    }));
                } else if(i==35) {
                    inventory.put(35, new ItemBuilder(this, Material.EMERALD, itemMeta -> {
                        itemMeta.setDisplayName("§r§aPlus d'info !");
                        itemMeta.setLore(loreinfo);
                    }).setNextMenu(new MoreInfoMenu(getOwner())));
                } else {
                    inventory.put(i, new ItemBuilder(this, Material.GRAY_STAINED_GLASS_PANE, itemMeta -> itemMeta.setDisplayName(" ")));
                }
            }

            return inventory;
        }
}
