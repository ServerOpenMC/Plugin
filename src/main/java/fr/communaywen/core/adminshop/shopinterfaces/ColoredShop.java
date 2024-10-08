package fr.communaywen.core.adminshop.shopinterfaces;

import dev.xernas.menulib.Menu;
import dev.xernas.menulib.utils.InventorySize;
import dev.xernas.menulib.utils.ItemBuilder;
import fr.communaywen.core.adminshop.menu.buy.AdminShopBuy;
import fr.communaywen.core.adminshop.menu.category.colored.COLOR;
import fr.communaywen.core.customitems.utils.CustomItemsUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ColoredShop extends Menu {

    private final String name;
    private final String materialType;
    private final String materialName;

    public ColoredShop(Player player, String name, String materialType, String materialName) {
        super(player);
        this.name = name;
        this.materialType = materialType;
        this.materialName = materialName;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }
    @Override
    public @NotNull InventorySize getInventorySize() {
        return InventorySize.LARGEST;
    }
    @Override
    public void onInventoryClick(InventoryClickEvent inventoryClickEvent) {}

    @Override
    public @NotNull Map<Integer, ItemStack> getContent() {
        Map<Integer, ItemStack> content = new HashMap<>();

        for(int i = 0; i < getInventorySize().getSize(); i++) {
            if((i % 2) == 0) content.put(i, new ItemBuilder(this, Material.LIGHT_BLUE_STAINED_GLASS_PANE, itemMeta -> itemMeta.setDisplayName(" ")));
            else content.put(i, new ItemBuilder(this, Material.BLUE_STAINED_GLASS_PANE, itemMeta -> itemMeta.setDisplayName(" ")));
        }

        for(COLOR item : COLOR.values()) {

            List<String> lore = new ArrayList<>();

            lore.add("§aAcheter pour §e" + item.getPrize() + "$ §7/u");
            lore.add("§7");
            lore.add("§8■ §7Click gauche pour §aacheter");

            AdminShopBuy buy = new AdminShopBuy(getOwner(), item, materialType.toUpperCase());

            content.put(item.getSlots(), new ItemBuilder(this, Objects.requireNonNull(Material.getMaterial(item.named() + "_" + materialType.toUpperCase())), itemMeta ->  {
                    itemMeta.setDisplayName("§7" + materialName + " " + item.getName());
                    itemMeta.setLore(lore);
                }).setNextMenu(buy)
            );
        }

        ArrayList<ItemBuilder> navBtns = CustomItemsUtils.getNavigationButtons(this);

        content.put(45, navBtns.getFirst().setBackButton());
        content.put(53, navBtns.get(1).setCloseButton());

        return content;
    }
}
