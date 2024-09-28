package fr.communaywen.core.contest.menu;

import dev.lone.itemsadder.api.CustomStack;
import dev.xernas.menulib.Menu;
import dev.xernas.menulib.utils.InventorySize;
import dev.xernas.menulib.utils.ItemBuilder;
import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.contest.ColorConvertor;
import fr.communaywen.core.contest.ContestManager;
import fr.communaywen.core.utils.constant.MessageManager;
import fr.communaywen.core.utils.constant.MessageType;
import fr.communaywen.core.utils.constant.Prefix;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;


public class ContributionMenu extends Menu {
    private final AywenCraftPlugin plugin;

    public ContributionMenu(Player owner, AywenCraftPlugin plugin) {
        super(owner);
        this.plugin = plugin;
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
        ChatColor campColor = ContestManager.getPlayerColorCache(getOwner());
        Material m = ColorConvertor.getMaterialFromColor(campColor);

        List<String> loreinfo = new ArrayList<String>();
        List<String> lore_randomevent = new ArrayList<String>();
        List<String> lore_contribute = new ArrayList<String>();
        List<String> lore_trade = new ArrayList<String>();
        List<String> lore_rang = new ArrayList<String>();

        loreinfo.add("§7Apprenez en plus sur les Contest !");
        loreinfo.add("§7Le déroulement..., Les résultats, ...");
        loreinfo.add("§e§lCLIQUEZ ICI POUR EN VOIR PLUS!");

        FileConfiguration config = this.plugin.getConfig();
        ConfigurationSection boostEvents = config.getConfigurationSection("contest.boost_event");

        if  (boostEvents != null) {
            for (String event : boostEvents.getKeys(true)) {
                ConfigurationSection eventInfo = boostEvents.getConfigurationSection(event);

                if (eventInfo != null) {
                    lore_randomevent.add("§b+" + eventInfo.getInt("boost") + "% §7" + eventInfo.getString("name"));
                }
            }
        }

        lore_randomevent.add("§8Les probabilités qu'un event se produise plus souvent, sont choisis dès le début du Contest");

        Material shell_contest = CustomStack.getInstance("contest:contest_shell").getItemStack().getType();
        lore_contribute.add("§7Donner vos §bCoquillages de Contest");
        lore_contribute.add("§7Pour faire gagner votre"+ campColor +" Team!");
        lore_contribute.add("§e§lCliquez pour verser tout vos Coquillages");

        lore_trade.add("§7Faites des Trades contre des §bCoquillages de Contest");
        lore_trade.add("§7Utile pour faire gagner ta"+ campColor +" Team");
        lore_trade.add("§e§lCliquez pour acceder au Menu des trades");

        lore_rang.add(campColor + ContestManager.getRankContest(getOwner()) + campName);
        lore_rang.add("§7Progression §8: " + campColor + ContestManager.getPlayerPointsCache(getOwner()) + "§8/" + campColor + ContestManager.getRepPointsToRank(getOwner()));
        lore_rang.add("§e§lAUGMENTER DE RANG POUR VOIR DES RECOMPENSES MEILLEURES");

        for(int i = 0; i < getInventorySize().getSize(); i++) {
            if(i==8) {
                inventory.put(8, new ItemBuilder(this, Material.GOLD_BLOCK, itemMeta -> {
                    itemMeta.setDisplayName("§6§lVotre Grade");
                    itemMeta.setLore(lore_rang);
                }));
            }
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
                }).setOnClick(inventoryClickEvent -> {
                    try {
                        ItemStack shell_contestItem = CustomStack.getInstance("contest:contest_shell").getItemStack();
                        int shell = 0;
                        for (ItemStack is : getOwner().getInventory().getContents()) {
                            if (is != null && is.isSimilar(shell_contestItem)) {
                                shell = shell + is.getAmount();
                            }
                        }
                        if (ContestManager.hasEnoughItems(getOwner(), shell_contest, shell)) {
                            ContestManager.removeItemsFromInventory(getOwner(), shell_contest, shell);
                            ContestManager.addPointPlayer(shell + ContestManager.getPlayerPoints(getOwner()), getOwner());
                            ContestManager.updateColumnInt("contest", "points" + ContestManager.getPlayerCampsCache(getOwner()), shell + ContestManager.getInt("contest", "points" + ContestManager.getPlayerCampsCache(getOwner())));
                            MessageManager.sendMessageType(getOwner(), "§7Vous avez déposé§b " + shell + " Coquillage(s) de Contest§7 pour votre Team!", Prefix.CONTEST, MessageType.SUCCESS, true);
                        } else {
                            MessageManager.sendMessageType(getOwner(), "§cVous n'avez pas de Coquillage(s) de Contest§7", Prefix.CONTEST, MessageType.ERROR, true);
                        }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
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
            }
        }

        return inventory;
    }
}
