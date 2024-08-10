package fr.communaywen.core.space.rocket;

import com.destroystokyo.paper.event.entity.EntityJumpEvent;
import dev.lone.itemsadder.api.CustomEntity;
import dev.lone.itemsadder.api.CustomStack;
import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.teams.menu.TeamMenu;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class RocketListener implements Listener {

    @EventHandler
    public void onRocketInteract(PlayerInteractEntityEvent event) {
        if(CustomEntity.isCustomEntity(event.getRightClicked())) {
            CustomEntity rocket = CustomEntity.byAlreadySpawned(event.getRightClicked());
            if(rocket != null) {


                //  §r§f%img_offset_-9%%img_rocket_menu%
                //  54

                //create a gui
                RocketMenu rocketMenu = new RocketMenu(event.getPlayer(), rocket);
                rocketMenu.open();

                //LaunchAnim launchAnim = new LaunchAnim(rocket, event.getPlayer());
                //launchAnim.launch();
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getView().title().equals(PlaceholderAPI.setPlaceholders((Player) e.getWhoClicked(), "§r§f%img_offset_-9%%img_rocket_menu%"))) {
            ItemStack clickedItem = e.getCurrentItem();

            if (clickedItem != null && clickedItem.getType() == Material.BLACK_STAINED_GLASS_PANE) {
                e.setCancelled(true);
            } /*else if (e.getCurrentItem() != null && e.getCurrentItem().getType() == Material.PAPER) {
                Player p = (Player) e.getWhoClicked();

                Inventory corpseInventory = e.getClickedInventory();
                Inventory playerInventory = p.getInventory();

                CorpseMenu corpseMenu = corpseManager.getCorpseMenuByPlayer(p);

                if(corpseMenu == null){
                    return;
                }

                corpseMenu.swapContents(playerInventory, corpseInventory);

                p.sendMessage("§aVous avez récupéré tout le stuff de la tombe.");
                p.closeInventory();
            }*/
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (!player.getWorld().getName().equals("moon")){ return; }

        //TODO : Créer l'acier
        /*if (block.getType().equals(Material.IRON_BLOCK)) {
            event.setDropItems(false);
            player.getWorld().dropItemNaturally(block.getLocation(),
                    CustomStack.getInstance("space:steel").getItemStack());
        }*/
        if(block.getType().equals(Material.END_STONE)) {
            event.setDropItems(false);
            player.getWorld().dropItemNaturally(block.getLocation(),
                    CustomStack.getInstance("space:moon_shard").getItemStack());
            if(Math.random() < 0.1) {
                player.getWorld().dropItemNaturally(block.getLocation(),
                        CustomStack.getInstance("space:moon_shard").getItemStack());
            }
        }
    }


    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent e) {
        if(!e.getLocation().getWorld().getName().equals("moon")) return;
        if(List.of(EntityType.ARMOR_STAND, EntityType.BLOCK_DISPLAY, EntityType.ITEM_DISPLAY, EntityType.TEXT_DISPLAY, EntityType.ITEM, EntityType.WOLF, EntityType.PARROT, EntityType.OCELOT, EntityType.CAT, EntityType.TROPICAL_FISH, EntityType.ARROW, EntityType.ENDER_PEARL).contains(e.getEntity().getType())) return;
        if(!List.of(EntityType.ZOMBIE, EntityType.SKELETON).contains(e.getEntity().getType())) {
            e.setCancelled(true);
        }
        EntityEquipment ee = ((LivingEntity) e.getEntity()).getEquipment();
        assert ee != null;
        ee.setHelmet(new ItemStack(Material.GLASS));
        ((LivingEntity) e.getEntity()).getAttribute(Attribute.GENERIC_GRAVITY).setBaseValue(0.013211);
        ((LivingEntity) e.getEntity()).getAttribute(Attribute.GENERIC_SAFE_FALL_DISTANCE).setBaseValue(10);
    }

    @EventHandler
    public void onDimChange(PlayerChangedWorldEvent e) {
        if(e.getPlayer().getWorld() == Bukkit.getWorld("moon")) {
            // Alors le produit en croix : La gravité sur la terre d'mc c'est 0.08, la gravité sur terre irl c'est 9.81. La gravité sur la lune irl c'est 1.62. (1.62*0.08)/9.81 = environ 0.013211
            e.getPlayer().getAttribute(Attribute.GENERIC_GRAVITY).setBaseValue(0.013211);
            e.getPlayer().getAttribute(Attribute.GENERIC_SAFE_FALL_DISTANCE).setBaseValue(10);
        }
        if(e.getFrom() == Bukkit.getWorld("moon")) {
            e.getPlayer().getAttribute(Attribute.GENERIC_GRAVITY).setBaseValue(0.08);
            e.getPlayer().getAttribute(Attribute.GENERIC_SAFE_FALL_DISTANCE).setBaseValue(3);
        }
    }

}
