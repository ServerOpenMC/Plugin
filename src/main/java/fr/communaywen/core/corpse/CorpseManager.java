package fr.communaywen.core.corpse;

import dev.lone.itemsadder.api.CustomBlock;
import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

@Feature("Tombe")
@Credit("Martinouxx")
public class CorpseManager implements Listener {

    private Map<CorpseBlock, CorpseMenu> corpses = new HashMap<>();

    public void addCorpse(Player p, Inventory inv) {
        CorpseMenu corpseMenu = new CorpseMenu(p, inv);

        CustomBlock block = CustomBlock.getInstance("omc_blocks:grave");

        if (block != null) {
            block.place(p.getLocation());

            CorpseBlock corpseBlock = new CorpseBlock(block, p.getLocation(), p.getUniqueId());
            corpses.put(corpseBlock, corpseMenu);

            new BukkitRunnable(){
                @Override
                public void run() {
                    if(!corpses.containsKey(corpseBlock)) return;

                    remove(corpseBlock);
                }
            }.runTaskLater(AywenCraftPlugin.getInstance(), 20*60*10);
        }

    }

    public void open(Location loc, Player p) {
        for (CorpseBlock corpseBlock : corpses.keySet()) {
            Location corpseLocation = corpseBlock.getLocation();

            if (areLocationsClose(loc, corpseLocation, 1.2)) {
                CorpseMenu corpseMenu = corpses.get(corpseBlock);

                if (corpseMenu != null && corpseMenu.isOwner(p)) {
                    corpseMenu.open(p);
                }
            }
        }
    }

    private boolean areLocationsClose(Location loc1, Location loc2, double tolerance) {
        return Math.abs(loc1.getX() - loc2.getX()) <= tolerance &&
                Math.abs(loc1.getZ() - loc2.getZ()) <= tolerance;
    }


    public void close(Inventory inv) {
        for (Map.Entry<CorpseBlock, CorpseMenu> entry : corpses.entrySet()) {
            if (entry.getValue().getInventory().equals(inv)) {
                if (isCorpseInventoryEmpty(entry.getValue().getInventory())) {
                    remove(entry.getKey());
                    return;
                }
            }
        }
    }

    public void remove(CorpseBlock corpseBlock){
        if (corpseBlock != null) {
            corpseBlock.remove();
        }
        corpses.remove(corpseBlock);
    }

    public void removeAll() {
        Iterator<CorpseBlock> iterator = corpses.keySet().iterator();
        while (iterator.hasNext()) {
            CorpseBlock corpseBlock = iterator.next();

            for(ItemStack it : corpses.get(corpseBlock).getInventory()){
                if(it.getType() == Material.BLACK_STAINED_GLASS_PANE) continue;

                corpseBlock.getLocation().getWorld().dropItemNaturally(corpseBlock.getLocation(), it);
            }

            corpseBlock.remove();
            iterator.remove();
        }
    }

    public CorpseMenu getCorpseMenuByPlayer(Player p) {
        for (Map.Entry<CorpseBlock, CorpseMenu> entry : corpses.entrySet()) {
            if (entry.getValue().isOwner(p)) {
                return entry.getValue();
            }
        }
        return null;
    }


    public boolean isCorpseInventoryEmpty(Inventory inv) {
        for (ItemStack item : inv.getContents()) {
            if (item != null && item.getType() != Material.AIR && !item.getType().equals(Material.BLACK_STAINED_GLASS_PANE)) {
                return false;
            }
        }
        return true;
    }

    public boolean isCorpseInventory(Inventory inv) {
        for (CorpseBlock corpseBlock : corpses.keySet()) {
            if (corpseBlock != null && corpseBlock.getBlock() != null) {
                return true;
            }
        }
        return false;
    }

    public List<Location> getGraveLocations() {
        List<Location> locations = new ArrayList<>();
        for (CorpseBlock corpseBlock : corpses.keySet()) {
            locations.add(corpseBlock.getLocation());
        }
        return locations;
    }

}
