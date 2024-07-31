package fr.communaywen.core.commands;

import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@Feature("Power")
@Credit("llex_tv")
public class PowerCommand implements Listener{
    @Command({"power"})
    @Description("Découvre et débloque des pouvoirs")
    public void onCommand(Player player) {
        inv = Bukkit.createInventory(null, 27, "Power");
        initializePower();
    }

    public void initializePower() {
      for (int i = 1; i <= 10; i = i + 1) {
        inv.addItem(createGuiItem(Material.GRAY_GLASS, "", "", ""));
      }
      inv.addItem(createGuiItem(Material.DIAMOND_SWORD, "§rMineur Power", "§aDebloque et améliore ton pouvoir de mineur", ""));
      inv.addItem(createGuiItem(Material.GRAY_GLASS, "", "", ""));
      inv.addItem(createGuiItem(Material.IRON_HELMET, "§bHunter Power", "§aDebloque et améliore ton pouvoir de hunter", ""));
      inv.addItem(createGuiItem(Material.GRAY_GLASS, "", "", ""));
      inv.addItem(createGuiItem(Material.IRON_HELMET, "§bFarmer Power", "§aDebloque et améliore ton pouvoir de farmer", ""));
      inv.addItem(createGuiItem(Material.GRAY_GLASS, "", "", ""));
      inv.addItem(createGuiItem(Material.IRON_HELMET, "§Builder Power", "§aDebloque et améliore ton pouvoir de buildeur", ""));
    }
  
    protected ItemStack createGuiItem(final Material material, final String name, final String... lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(name);

        meta.setLore(Arrays.asList(lore));

        item.setItemMeta(meta);

        return item;
    }
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if (!e.getInventory().equals(inv)) return;
        e.setCancelled(true);
        final ItemStack clickedItem = e.getCurrentItem();
        if (clickedItem == null || clickedItem.getType().isAir()) return;

        final Player p = (Player) e.getWhoClicked();
        if(e.getRawSlot() == 11){
          p.sendMessage("Debug 11");
          //inv = Bukkit.createInventory(null, 27, "Mineur Power");
        }
        p.sendMessage("You clicked at slot " + e.getRawSlot());
    }
}
