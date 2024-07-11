package fr.communaywen.core.quests;

import dev.xernas.menulib.Menu;
import dev.xernas.menulib.utils.InventorySize;
import fr.communaywen.core.AywenCraftPlugin;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestsMenu extends Menu {

    private int currentPage;
    private static final int QUESTS_PER_PAGE = 7;

    public QuestsMenu(Player player, int currentPage) {
        super(player);
        this.currentPage = currentPage;
    }
    public QuestsMenu(Player player) {
        this(player, 0);
    }

    @Override
    public @NotNull String getName() {
        return "§9§lQuêtes (Page " + (currentPage + 1) + ")";
    }

    @Override
    public @NotNull InventorySize getInventorySize() {
        return InventorySize.NORMAL;
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        int slot = event.getSlot();
        System.out.println("Slot clicked: " + slot);
        if (slot == 18) {
            if (currentPage > 0) {
                currentPage--;
                refresh();
            }
        } else if (slot == 26) {
            int totalPages = (int) Math.ceil((double) QuestsManager.QUESTS.values().length / QUESTS_PER_PAGE);
            if (currentPage < totalPages - 1) {
                currentPage++;
                refresh();
            }
        }
    }

    @Override
    public @NotNull Map<Integer, ItemStack> getContent() {
        Map<Integer, ItemStack> content = new HashMap<>();
        List<ItemStack> questItems = new ArrayList<>();

        for (QuestsManager.QUESTS quest : QuestsManager.QUESTS.values()) {
            ItemStack item = new ItemStack(quest.getMaterial());
            ItemMeta meta = item.getItemMeta();
            PlayerQuests pq = QuestsManager.getPlayerQuests(getOwner().getPlayer());
            if (meta != null) {
                if(pq.isQuestCompleted(quest)) {
                    meta.addEnchant(Enchantment.SHARPNESS, 5, true);
                    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                }
                meta.setDisplayName("§a" + quest.getName());
                List<String> lore = new ArrayList<>();
                lore.add("§7Description: §f" + quest.getDesc());
                lore.add("§7Récompense: §f" + (quest.getRewardsMaterial() != null ? "x" + quest.getRewardsQt() + " " + quest.getRewardsMaterial() : quest.getRewardsQt() + "$"));
                lore.add("§e" + (pq.isQuestCompleted(quest) ? "§aQuetes complété" : "En cours: " + pq.getProgress(quest) + "/" + quest.getQt()));
                meta.setLore(lore);
                item.setItemMeta(meta);
            }
            questItems.add(item);
        }

        int inventoryWidth = 9;
        int inventorySize = InventorySize.NORMAL.getSize();
        int startRow = 1;
        int endRow = inventorySize / inventoryWidth - 2;
        int padding = 1;

        ItemStack glassPane = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);

        for (int i = 0; i < inventoryWidth; i++) {
            content.put(i, glassPane);
            content.put(inventorySize - inventoryWidth + i, glassPane);
        }

        for (int row = startRow; row <= endRow; row++) {
            content.put(row * inventoryWidth, glassPane);
            content.put(row * inventoryWidth + inventoryWidth - 1, glassPane);
        }

        int startIndex = currentPage * QUESTS_PER_PAGE;
        int endIndex = Math.min(startIndex + QUESTS_PER_PAGE, questItems.size());

        System.out.println("currentPage: " + currentPage + ", startIndex: " + startIndex + ", endIndex: " + endIndex); // Debug message

        int slotIndex = startRow * inventoryWidth + padding;
        for (int i = startIndex; i < endIndex; i++) {
            content.put(slotIndex++, questItems.get(i));
        }

        ItemStack previousButton = new ItemStack(Material.ARROW);
        ItemMeta previousMeta = previousButton.getItemMeta();
        if (previousMeta != null) {
            previousMeta.setDisplayName("§aPrécédent");
            previousButton.setItemMeta(previousMeta);
        }
        content.put(18, previousButton);

        ItemStack nextButton = new ItemStack(Material.ARROW);
        ItemMeta nextMeta = nextButton.getItemMeta();
        if (nextMeta != null) {
            nextMeta.setDisplayName("§aSuivant");
            nextButton.setItemMeta(nextMeta);
        }
        content.put(26, nextButton);

        return content;
    }

    private void refresh() {
        getOwner().getPlayer().closeInventory();
        new QuestsMenu(getOwner().getPlayer(), currentPage).open();
    }
}