package fr.communaywen.core.listeners;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import dev.lone.itemsadder.api.CustomStack;
import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import fr.communaywen.core.utils.FallingBlocksExplosion;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

@Feature("Marteau de Thor")
@Credit("ri1_")
public class ThorHammer implements Listener {

    static AywenCraftPlugin plugin;
    public ThorHammer(AywenCraftPlugin plugins) {
        plugin = plugins;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock() != null) {

            if (event.getMaterial() == CustomStack.getInstance("thor:hammer").getItemStack().getType()) {
                Player player = event.getPlayer();

                if (!player.isSneaking()) return;

                // WorldGuard
                RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
                RegionQuery query = container.createQuery();
                ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(event.getClickedBlock().getLocation()));
                if (!set.testState(null, (StateFlag) plugin.getFlags().get(StateFlag.class).get("disable-thor-hammer"))) {
                    event.setCancelled(false);
                    useThorHammer(player, event.getClickedBlock().getLocation());

                } else {
                    event.setCancelled(true);
                }
            }
        }
    }


    @EventHandler
    public void on(EntityDamageByEntityEvent event) {
        if (event.getDamager().getType() == EntityType.PLAYER) {
            Player player = (Player) event.getDamager();

            Location loc = new Location(event.getEntity().getWorld(), event.getEntity().getLocation().getBlockX(), event.getEntity().getLocation().getBlockY(), event.getEntity().getLocation().getBlockZ());

            useThorHammer(player, loc);

        }
    }

    @SuppressWarnings("deprecation")
    private void useThorHammer(Player player, Location loc) {

        ItemStack item = player.getItemInHand();
        CustomStack customStack = CustomStack.byItemStack(item);

        if (customStack != null && customStack.getNamespacedID().equals("thor:hammer")) {
            if (player.getCooldown(item.getType()) > 0) return;
            if (player.getGameMode() != GameMode.CREATIVE) item.setDurability((short) (item.getDurability() + 1));
            if (item.getDurability() >= item.getType().getMaxDurability()) {
                player.getInventory().remove(item);
                player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK,  SoundCategory.BLOCKS, 1, 1);
                return;
            }
            if (player.getGameMode() != GameMode.CREATIVE) player.setCooldown(item.getType(), 1200);
            World world = loc.getWorld();
            assert world != null;
            world.strikeLightning(loc);
            new FallingBlocksExplosion(3, loc, false);
            world.playSound(loc, Sound.ITEM_TOTEM_USE,  SoundCategory.HOSTILE, 1, 0);
            world.spawnParticle(Particle.CLOUD, loc, 3, 0.02d, 1d, 0.02d, 0.09d);
        }
    }
}
