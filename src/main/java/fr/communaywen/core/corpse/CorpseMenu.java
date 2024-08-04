package fr.communaywen.core.corpse;

import fr.communaywen.core.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class CorpseMenu {

    private final UUID owner;
    private final Inventory contents;
    private Inventory inv;

    private static final int INVENTORY_SIZE = 54;
    private static final String INVENTORY_TITLE = "§8Corps de §f";

    private static final Map<Integer, Integer> HOTBAR_SLOTS;
    private static final Map<Integer, Integer> INVENTORY_SLOTS;
    private static final Map<Integer, Integer> ARMOR_SLOTS;

    static {
        HOTBAR_SLOTS = new HashMap<>();
        HOTBAR_SLOTS.put(0, 45);
        HOTBAR_SLOTS.put(1, 46);
        HOTBAR_SLOTS.put(2, 47);
        HOTBAR_SLOTS.put(3, 48);
        HOTBAR_SLOTS.put(4, 49);
        HOTBAR_SLOTS.put(5, 50);
        HOTBAR_SLOTS.put(6, 51);
        HOTBAR_SLOTS.put(7, 52);
        HOTBAR_SLOTS.put(8, 53);

        INVENTORY_SLOTS = new HashMap<>();
        INVENTORY_SLOTS.put(27, 36);
        INVENTORY_SLOTS.put(28, 37);
        INVENTORY_SLOTS.put(29, 38);
        INVENTORY_SLOTS.put(30, 39);
        INVENTORY_SLOTS.put(31, 40);
        INVENTORY_SLOTS.put(32, 41);
        INVENTORY_SLOTS.put(33, 42);
        INVENTORY_SLOTS.put(34, 43);
        INVENTORY_SLOTS.put(35, 44);
        INVENTORY_SLOTS.put(18, 27);
        INVENTORY_SLOTS.put(19, 28);
        INVENTORY_SLOTS.put(20, 29);
        INVENTORY_SLOTS.put(21, 30);
        INVENTORY_SLOTS.put(22, 31);
        INVENTORY_SLOTS.put(23, 32);
        INVENTORY_SLOTS.put(24, 33);
        INVENTORY_SLOTS.put(25, 34);
        INVENTORY_SLOTS.put(26, 35);
        INVENTORY_SLOTS.put(9, 18);
        INVENTORY_SLOTS.put(10, 19);
        INVENTORY_SLOTS.put(11, 20);
        INVENTORY_SLOTS.put(12, 21);
        INVENTORY_SLOTS.put(13, 22);
        INVENTORY_SLOTS.put(14, 23);
        INVENTORY_SLOTS.put(15, 24);
        INVENTORY_SLOTS.put(16, 25);
        INVENTORY_SLOTS.put(17, 26);

        ARMOR_SLOTS = new HashMap<>();
        ARMOR_SLOTS.put(39, 5);  // Helmet
        ARMOR_SLOTS.put(38, 4);  // Chestplate
        ARMOR_SLOTS.put(37, 3);  // Leggings
        ARMOR_SLOTS.put(36, 2);  // Boots
        ARMOR_SLOTS.put(40, 6);  // Off-hand
    }

    public CorpseMenu(Player owner, Inventory contents) {
        this.owner = owner.getUniqueId();
        this.contents = contents;
        createInv();
    }

    private void createInv() {
        inv = Bukkit.createInventory(null, INVENTORY_SIZE, INVENTORY_TITLE + Bukkit.getPlayer(owner).getName());
        createItems(inv);
    }

    public void open(Player p) {
        p.openInventory(inv);
    }

    public boolean isOwner(Player p) {
        return p.getUniqueId().equals(owner);
    }

    public Inventory getInventory() {
        return inv;
    }

    private void createItems(Inventory inv) {
        for (int i = 0; i < INVENTORY_SIZE; i++) {
            inv.setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName(" ").toItemStack());
        }

        placeItems(contents, inv, HOTBAR_SLOTS);
        placeItems(contents, inv, INVENTORY_SLOTS);
        placeItems(contents, inv, ARMOR_SLOTS);
    }

    private void placeItems(Inventory fromInventory, Inventory toInventory, Map<Integer, Integer> slotsMap) {
        for (Map.Entry<Integer, Integer> entry : slotsMap.entrySet()) {
            int fromSlot = entry.getKey();
            int toSlot = entry.getValue();

            ItemStack item = fromInventory.getItem(fromSlot);
            if (item != null) {
                toInventory.setItem(toSlot, item);
            }
        }
    }

    public List<ItemStack> getContents(){
        return Arrays.stream(inv.getContents()).toList().stream().filter(item -> item.getType() != Material.BLACK_STAINED_GLASS_PANE).toList();
    }
}
