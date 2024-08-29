package fr.communaywen.core.contest.menu;

import dev.lone.itemsadder.api.CustomStack;
import dev.xernas.menulib.Menu;
import dev.xernas.menulib.utils.InventorySize;
import dev.xernas.menulib.utils.ItemBuilder;
import fr.communaywen.core.contest.ContestManager;
import fr.communaywen.core.contest.MaterialFromChatColor;
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


public class ContributionMenu extends Menu {
    public ContributionMenu(Player owner) {
        super(owner);
    }

    @Override
    public @NotNull String getName() {
        return "Le Contest - Les Contributions";
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
        Material m = MaterialFromChatColor.getMaterialFromColor(campColor);

        List<String> loreinfo = new ArrayList<String>();
        List<String> lore_randomevent = new ArrayList<String>();
        List<String> lore_contribute = new ArrayList<String>();
        List<String> lore_trade = new ArrayList<String>();

        loreinfo.add("§7Apprenez en plus sur les Contest !");
        loreinfo.add("§7Le déroulement..., Les résultats, ...");
        loreinfo.add("§e§lCLIQUEZ ICI POUR EN VOIR PLUS!");

        lore_randomevent.add("§b+x% §7Pêche Miraculeuse");
        lore_randomevent.add("§b+x% §7Nuit Terrifiante");
        lore_randomevent.add("§8Les probabilités qu'un event se produise plus souvent, sont choisis dès le début du Contest");

        Material shell_contest = CustomStack.getInstance("contest:contest_shell").getItemStack().getType();
        lore_contribute.add("§7Donner vos §bCoquillages de Contest");
        lore_contribute.add("§7Pour faire gagner votre"+ campColor +" Team!");
        lore_contribute.add("§e§lCliquez pour verser tout vos Coquillages");

        lore_trade.add("§7Faites des Trades contre des §bCoquillages de Contest");
        lore_trade.add("§7Utile pour faire gagner ta"+ campColor +" Team");
        lore_trade.add("§e§lCliquez pour acceder au Menu des trades");

        for(int i = 0; i < getInventorySize().getSize(); i++) {
            if(i==10) {
                inventory.put(10, new ItemBuilder(this, shell_contest, itemMeta -> {
                    itemMeta.setDisplayName("§7Les Trades");
                    itemMeta.setLore(lore_trade);
                    itemMeta.setCustomModelData(10000);
                }).setNextMenu(new TradeMenu(getOwner())));
            } else if(i==13) {
                inventory.put(13, new ItemBuilder(this, m, itemMeta -> {
                    itemMeta.setDisplayName("§r§7Contribuer pour la"+ campColor+ " Team " + campName);
                    itemMeta.setLore(lore_contribute);
                }));
            } else if(i==16) {
                inventory.put(16, new ItemBuilder(this, Material.OMINOUS_TRIAL_KEY, itemMeta -> {
                    itemMeta.setDisplayName("§r§1Boost d'Evenement!");
                    itemMeta.setLore(lore_randomevent);
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
