package fr.communaywen.core.customitems.items;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.customitems.enums.ConfigNames;
import fr.communaywen.core.customitems.objects.CustomItems;
import fr.communaywen.core.customitems.utils.CustomItemsUtils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;

@Getter
@Setter
@Credit("Fnafgameur")
public class BuilderWand implements CustomItems {

    private String name;
    private ItemStack itemStack;
    private FileConfiguration customItemsConfig;
    private final int radius;
    private final ArrayList<String> recipe = new ArrayList<>() {{
        add("EDE");
        add("EBE");
        add("XBX");
    }};
    private final HashMap<Character, ItemStack> ingredients = new HashMap<>() {{
        put('D', new ItemStack(Material.DIAMOND_BLOCK));
        put('B', new ItemStack(Material.BLAZE_ROD));
        put('E', new ItemStack(Material.EMERALD_BLOCK));
    }};

    public BuilderWand(FileConfiguration customItemsConfig) {
        this.customItemsConfig = customItemsConfig;
        this.radius = customItemsConfig.getInt(ConfigNames.BUILDER_WAND_RADIUS.getName());
    }

    @Override
    public String getNamespacedID() {
        return "customitems:builder_wand";
    }

    @Override
    public void onInteract(PlayerInteractEvent event) {

        if (event.getHand() == null) {
            return;
        }

        if (event.getHand().equals(EquipmentSlot.OFF_HAND)) {
            return;
        }

        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            return;
        }

        Block interactedBlock = event.getClickedBlock();

        if (interactedBlock == null) {
            return;
        }

        Material blockType = interactedBlock.getType();

        if (blockType.isAir()) {
            return;
        }

        Player player = event.getPlayer();
        BlockFace blockFace = event.getBlockFace();

        if (!player.getGameMode().equals(GameMode.CREATIVE) && !player.getInventory().contains(blockType)) {
            return;
        }

        // Mettre un micro delay permet d'éviter que l'event se trigger 2 fois de suite
        new BukkitRunnable() {
            @Override
            public void run() {
                placeBlocks(player, interactedBlock, blockType, blockFace);
            }
        }.runTaskLater(AywenCraftPlugin.getInstance(), 1L);
    }

    private void placeBlocks(Player player, Block interactedBlock, Material blockType, BlockFace blockFace) {

        ItemStack builderWand = player.getInventory().getItemInMainHand();
        World world = player.getWorld();
        int x;
        int y;
        int z;

        switch (blockFace) {
            case UP:
            case DOWN:
                for (x = -radius; x <= radius; x++) {
                    for (z = -radius; z <= radius; z++) {

                        Location newLoc = interactedBlock.getLocation().add(x, 0, z);
                        boolean blockPlaced = tryPlaceBlock(world, newLoc, blockType, blockFace, player);

                        if (!blockPlaced) {
                            continue;
                        }

                        if (player.getGameMode().equals(GameMode.CREATIVE)) {
                            continue;
                        }

                        for (ItemStack item : player.getInventory().getContents()) {
                            if (item != null && item.getType() == blockType) {
                                item.setAmount(item.getAmount() - 1);
                                break;
                            }
                        }
                    }
                }
                break;
            case NORTH:
            case SOUTH:
               for (x = -radius; x <= radius; x++) {
                   for (y = -radius; y <= radius; y++) {

                       Location newLoc = interactedBlock.getLocation().add(x, y, 0);
                       boolean blockPlaced = tryPlaceBlock(world, newLoc, blockType, blockFace, player);

                       if (!blockPlaced) {
                           continue;
                       }

                       if (player.getGameMode().equals(GameMode.CREATIVE)) {
                           continue;
                       }

                       for (ItemStack item : player.getInventory().getContents()) {
                           if (item != null && item.getType() == blockType) {
                               item.setAmount(item.getAmount() - 1);
                               break;
                           }
                       }

                   }
               }
               break;
            case EAST:
            case WEST:
                for (z = -radius; z <= radius; z++) {
                    for (y = -radius; y <= radius; y++) {

                        Location newLoc = interactedBlock.getLocation().add(0, y, z);
                        boolean blockPlaced = tryPlaceBlock(world, newLoc, blockType, blockFace, player);

                        if (!blockPlaced) {
                            continue;
                        }

                        if (player.getGameMode().equals(GameMode.CREATIVE)) {
                            continue;
                        }

                        for (ItemStack item : player.getInventory().getContents()) {
                            if (item != null && item.getType() == blockType) {
                                item.setAmount(item.getAmount() - 1);
                                break;
                            }
                        }
                    }
                }
                break;
        }

        if (player.getGameMode().equals(GameMode.CREATIVE)) {
            return;
        }

        CustomItemsUtils.damageItem(builderWand, 1);
    }

    private boolean tryPlaceBlock(World world, Location newLoc, Material blockType, BlockFace blockFace, Player player) {

        Block adjacentBlock = world.getBlockAt(newLoc);
        Material adjacentBlockType = adjacentBlock.getType();

        if (adjacentBlock.getType().isAir() || adjacentBlockType != blockType) {
            return false;
        }

        Block blockToPlace = adjacentBlock.getRelative(blockFace);

        if (!blockToPlace.getType().isAir()) {
            return false;
        }

        if (!player.getGameMode().equals(GameMode.CREATIVE) && !player.getInventory().contains(blockType)) {
            return false;
        }

        blockToPlace.setType(blockType);
        return true;
    }
}
