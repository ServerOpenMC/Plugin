package fr.communaywen.core.luckyblocks.guis;

import dev.xernas.menulib.PaginatedMenu;
import dev.xernas.menulib.utils.ItemBuilder;
import dev.xernas.menulib.utils.StaticSlots;
import fr.communaywen.core.customitems.utils.CustomItemsUtils;
import fr.communaywen.core.luckyblocks.managers.LuckyBlockManager;
import fr.communaywen.core.luckyblocks.objects.LuckyBlockEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LuckyBlockGUI extends PaginatedMenu {

    private final LuckyBlockManager luckyBlockManager;

    public LuckyBlockGUI(Player owner, LuckyBlockManager luckyBlockManager) {
        super(owner);
        this.luckyBlockManager = luckyBlockManager;
    }

    @Override
    public @Nullable Material getBorderMaterial() {
        return Material.BLACK_STAINED_GLASS_PANE;
    }

    @Override
    public @NotNull List<Integer> getStaticSlots() {
        return StaticSlots.STANDARD;
    }

    @Override
    public @NotNull List<ItemStack> getItems() {

        List<ItemStack> content = new ArrayList<>();
        ArrayList<LuckyBlockEvent> luckyBlockEvents = luckyBlockManager.getLbEvents();

        for (LuckyBlockEvent luckyBlockEvent : luckyBlockEvents) {
            ItemStack itemStack = luckyBlockEvent.getIconItem();
            ItemBuilder itemBuilder = new ItemBuilder(this, itemStack);

            content.add(itemBuilder);
        }

        return content;
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
        return "Lucky Block Events";
    }

    @Override
    public void onInventoryClick(InventoryClickEvent inventoryClickEvent) {

    }
}
