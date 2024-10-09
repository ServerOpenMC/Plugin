package fr.communaywen.core.quests.menu;

import dev.xernas.menulib.PaginatedMenu;
import dev.xernas.menulib.utils.ItemBuilder;
import dev.xernas.menulib.utils.StaticSlots;
import fr.communaywen.core.customitems.utils.CustomItemsUtils;
import fr.communaywen.core.quests.PlayerQuests;
import fr.communaywen.core.quests.QuestsManager;
import fr.communaywen.core.quests.qenum.QUESTS;
import fr.communaywen.core.quests.utils.QuestsUtils;
import fr.communaywen.core.utils.ItemUtils;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestsMenuTarget extends PaginatedMenu {

    private final OfflinePlayer target;
    private static PlayerQuests pq;

    public QuestsMenuTarget(Player player, OfflinePlayer target) {
        super(player);
        this.target = target;
        pq = QuestsManager.getPlayerQuests(target.getUniqueId());
    }

    @Override
    public @Nullable Material getBorderMaterial() {
        return Material.BLUE_STAINED_GLASS_PANE;
    }

    @Override
    public @NotNull List<Integer> getStaticSlots() {
        return StaticSlots.STANDARD;
    }

    @Override
    public Map<Integer, ItemStack> getButtons() {
        Map<Integer, ItemStack> content = new HashMap<>();
        ArrayList<ItemBuilder> navBtns = CustomItemsUtils.getNavigationButtons(this);

        content.put(48, navBtns.getFirst().setBackButton());
        content.put(49, navBtns.get(1).setCloseButton());
        content.put(50, navBtns.getLast().setNextPageButton());

        return content;
    }

    @Override
    public @NotNull String getName() {
        return "§9Quêtes de §e"+ target.getName() +" §8(§7Page " + (getPage() + 1) + "§8)";
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {}

    @Override
    public @NotNull List<ItemStack> getItems() {
        List<ItemStack> content = new ArrayList<>();
        List<QUESTS> quests = List.of(QUESTS.values());

        for (QUESTS quest : quests) {
            content.add(getQuestsItem(getOwner(), quest));
        }

        return content;
    }

    public static ItemStack getQuestsItem(Player player, QUESTS quest) {
        ItemStack item = new ItemStack(quest.getMaterial());
        ItemMeta meta = item.getItemMeta();
        QuestsUtils.createItems(quest, item, meta, pq, player);

        return item;
    }
}