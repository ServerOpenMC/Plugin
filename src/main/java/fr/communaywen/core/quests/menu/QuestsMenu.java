package fr.communaywen.core.quests.menu;

import dev.lone.itemsadder.api.CustomStack;
import dev.xernas.menulib.Menu;
import dev.xernas.menulib.utils.InventorySize;
import fr.communaywen.core.quests.PlayerQuests;
import fr.communaywen.core.quests.QuestsManager;
import fr.communaywen.core.quests.qenum.QUESTS;
import fr.communaywen.core.quests.utils.QuestsUtils;
import fr.communaywen.core.utils.ItemUtils;
import fr.communaywen.core.utils.constant.MessageManager;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class QuestsMenu extends Menu {

    private int currentPage;
    private static final int QUESTS_PER_PAGE = 9;
    private static String TITLE;
    private static final ItemStack LEFT_ARROW;
    private static final ItemStack RIGHT_ARROW;
    private final int totalPages;

    static {
        LEFT_ARROW = CustomStack.getInstance("quests:quests_icon_left_arrow").getItemStack();
        ItemMeta leftArrowMeta = LEFT_ARROW.getItemMeta();
        leftArrowMeta.setDisplayName(ChatColor.GOLD + "⬅");
        LEFT_ARROW.setItemMeta(leftArrowMeta);

        RIGHT_ARROW = CustomStack.getInstance("quests:quests_icon_right_arrow").getItemStack();
        ItemMeta rightArrowMeta = RIGHT_ARROW.getItemMeta();
        rightArrowMeta.setDisplayName(ChatColor.GOLD + "➡");
        RIGHT_ARROW.setItemMeta(rightArrowMeta);
    }

    public QuestsMenu(Player player, int currentPage) {
        super(player);
        TITLE = PlaceholderAPI.setPlaceholders(null, "§r§f%img_offset_-25%%img_quests_menu%");
        this.currentPage = currentPage;
        this.totalPages = (int) Math.ceil((double) QUESTS.values().length / QUESTS_PER_PAGE);
        updateInventory();
    }

    public QuestsMenu(Player player) {
        this(player, 0);
        TITLE = PlaceholderAPI.setPlaceholders(null, "§r§f%img_offset_-25%%img_quests_menu%");
    }

    @Override
    public @NotNull String getName() {
        return TITLE;
    }

    @Override
    public @NotNull InventorySize getInventorySize() {
        return InventorySize.NORMAL;
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        int slot = event.getSlot();
        if (slot == 18) {
            if (currentPage > 0) {
                currentPage--;
                refresh();
            }
        } else if (slot == 26) {
            int totalPages = (int) Math.ceil((double) QUESTS.values().length / QUESTS_PER_PAGE);
            if (currentPage < totalPages - 1) {
                currentPage++;
                refresh();
            }
        }
    }

    private void updateInventory() {
        getInventory().clear();
        Map<Integer, ItemStack> content = getContent();
        for (Map.Entry<Integer, ItemStack> entry : content.entrySet()) {
            getInventory().setItem(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public Map<Integer, ItemStack> getContent() {
        Map<Integer, ItemStack> content = new HashMap<>();
        PlayerQuests pq = QuestsManager.getPlayerQuests(getOwner().getUniqueId());

        int startIndex = currentPage * QUESTS_PER_PAGE;
        int endIndex = Math.min(startIndex + QUESTS_PER_PAGE, QUESTS.values().length);

        int slotIndex = 9;
        for(int i = startIndex; i < endIndex; i++) {
            QUESTS quest = QUESTS.values()[i];
            ItemStack item = createQuestsItems(quest, pq);
            content.put(slotIndex++, item);
        }

       if(currentPage > 0) {
           content.put(18, LEFT_ARROW);
       }
       if(currentPage < totalPages - 1) {
           content.put(26, RIGHT_ARROW);
       }

        return content;
    }

    private ItemStack createQuestsItems(QUESTS quest, PlayerQuests pq) {
        ItemStack item = new ItemStack(quest.getMaterial());
        ItemMeta meta = item.getItemMeta();
        QuestsUtils.createItems(quest, item, meta, pq, getOwner());
        return item;
    }

    private void refresh() {
        updateInventory();
        new QuestsMenu(getOwner(), currentPage).open();
    }
}