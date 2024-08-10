package fr.communaywen.core.customitems.guis;

import dev.xernas.menulib.PaginatedMenu;
import dev.xernas.menulib.utils.ItemBuilder;
import dev.xernas.menulib.utils.StaticSlots;
import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.customitems.managers.CustomItemsManager;
import fr.communaywen.core.customitems.objects.CustomItems;
import fr.communaywen.core.customitems.utils.CustomItemsUtils;
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

@Credit("Fnafgameur")
public class ShowcraftMainGUI extends PaginatedMenu {

    private final CustomItemsManager customItemsManager;

    public ShowcraftMainGUI(Player owner, CustomItemsManager customItemsManager) {
        super(owner);
        this.customItemsManager = customItemsManager;
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
        ArrayList<CustomItems> customItems = customItemsManager.getCustomItems();

        for (CustomItems customItem : customItems) {
            ItemStack itemStack = customItem.getItemStack();

            if (itemStack == null) {
                itemStack = new ItemBuilder(this, Material.AIR);
            } else {
                itemStack = new ItemBuilder(this, itemStack)
                        .setNextMenu(new ShowcraftMenuGUI(getOwner(), customItem));
            }

            content.add(itemStack);
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
        return "Showcraft";
    }

    @Override
    public void onInventoryClick(InventoryClickEvent inventoryClickEvent) {

    }
}
