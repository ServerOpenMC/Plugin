package fr.communaywen.core.customitems.items;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import dev.lone.itemsadder.api.CustomStack;
import dev.lone.itemsadder.api.CustomBlock;
import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import fr.communaywen.core.customitems.objects.CustomItems;
import fr.communaywen.core.customitems.objects.CustomItemsEvents;
import fr.communaywen.core.customitems.utils.CustomItemsUtils;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

@Feature("Iron Hammer")
@Getter
@Credit("Fnafgameur")
/* Credit to
 * Dartsgame for the 3D model
 */
public class IronHammer extends CustomItems implements CustomItemsEvents {

    static AywenCraftPlugin plugin;
    public IronHammer(AywenCraftPlugin plugins) {
        super(
                new ArrayList<>() {{
                    add("BBB");
                    add("BSB");
                    add("XSX");
                }},
                new HashMap<>() {{
                    put('B', new ItemStack(Material.IRON_BLOCK));
                    put('S', new ItemStack(Material.STICK));
                }},
                "customitems:iron_hammer"
        );

        plugin = plugins;
    }

    @Override
    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockBreak(BlockBreakEvent event) {

        // WorldGuard
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(event.getBlock().getLocation()));

        if (!set.testState(null, (StateFlag) plugin.getCustomFlags().get(StateFlag.class).get("disable-hammer"))) {
            event.setCancelled(false);
            Block brokenBlock = event.getBlock();
            CustomBlock customBlock = CustomBlock.byAlreadyPlaced(brokenBlock);

            if (customBlock != null) {
                event.setCancelled(true);
                return;
            }

            Player player = event.getPlayer();
            BlockFace playerFacing = CustomItemsUtils.getDestroyedBlockFace(player);

            if (playerFacing == null) {
                return;
            }

            playerFacing = playerFacing.getOppositeFace();
            ItemStack itemToDamage = event.getPlayer().getInventory().getItemInMainHand();

            CustomItemsUtils.destroyArea(playerFacing, brokenBlock, 1, 0, itemToDamage, player);

        } else {
            event.setCancelled(true);
        }

    }
}
