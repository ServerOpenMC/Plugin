package fr.communaywen.core.dreamdim.listeners;

import dev.lone.itemsadder.api.CustomCrop;
import fr.communaywen.core.AywenCraftPlugin;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public class HoeListener implements Listener {

    private final JavaPlugin plugin;

    public HoeListener() {
        this.plugin = AywenCraftPlugin.getInstance();
    }

    @EventHandler
    public void onCropBreaked(BlockBreakEvent event) {
        if (event.isCancelled()) return;

        Player player = event.getPlayer();
        ItemStack mainhand = player.getInventory().getItemInMainHand();
        Block block = event.getBlock();

        try {
            ItemMeta meta = mainhand.getItemMeta();
            if (meta == null || !meta.getPersistentDataContainer().get(NamespacedKey.fromString("replenish", plugin), PersistentDataType.BOOLEAN)) {
                return;
            }
        } catch (Exception e) {
            return;
        }

        if (block.getBlockData() instanceof Ageable crop) {
            CustomCrop customCrop = CustomCrop.byAlreadyPlaced(block);
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                if (customCrop != null) {
                    CustomCrop.place(customCrop.getSeed().getNamespacedID(), block.getLocation());
                } else {
                    block.setType(block.getType());

                    crop.setAge(0);
                    block.setBlockData(crop);
                }
            }, 2);
        }
    }

    private Material getSeedForCrop(Material cropType) {
        return switch (cropType) {
            case WHEAT -> Material.WHEAT_SEEDS;
            case CARROTS -> Material.CARROT;
            case POTATOES -> Material.POTATO;
            case BEETROOTS -> Material.BEETROOT_SEEDS;
            case NETHER_WART -> Material.NETHER_WART;
            default -> null;
        };
    }
}
