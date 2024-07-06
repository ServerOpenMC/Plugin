package fr.communaywen.core.listeners;

import fr.communaywen.core.utils.LinkerAPI;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;

public class StarterKits implements Listener {

    private LuckPerms luckPerms;
    private LinkerAPI linkerAPI;

    public StarterKits(LuckPerms luckPerms, LinkerAPI linkerAPI) {
        this.luckPerms = luckPerms;
        this.linkerAPI = linkerAPI;
    }

    private void addPermission(User user, String permission) {
        // Add the permission
        user.data().add(Node.builder(permission).build());

        // Now we need to save changes.
        luckPerms.getUserManager().saveUser(user);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) throws SQLException { // Donne une permissions en fonction du niveau
        Player player = event.getPlayer();
        User lpPlayer = this.luckPerms.getPlayerAdapter(Player.class).getUser(player);

        if (player.hasPermission("ayw.starterpack.isclaimed")) { return; }
        String uid = linkerAPI.getUserId(player);
        if (uid.isBlank()){ return; }

        Inventory pInv = player.getInventory();
        if (player.hasPermission("ayw.levels.10")) {
            System.out.println("Give lv10 starter kits to "+player.getName());
            pInv.addItem(new ItemStack(Material.LEATHER_HELMET));
            pInv.addItem(new ItemStack(Material.LEATHER_CHESTPLATE));
            pInv.addItem(new ItemStack(Material.LEATHER_LEGGINGS));
            pInv.addItem(new ItemStack(Material.LEATHER_BOOTS));
            pInv.addItem(new ItemStack(Material.STONE_AXE));
            pInv.addItem(new ItemStack(Material.STONE_PICKAXE));
            pInv.addItem(new ItemStack(Material.STONE_SWORD));
            pInv.addItem(new ItemStack(Material.COOKED_BEEF, 16));

        }

        if (player.hasPermission("ayw.levels.20")) {
            System.out.println("Give lv20 starter kits to "+player.getName());
            pInv.addItem(new ItemStack(Material.COOKED_BEEF, 16));

        }

        if (player.hasPermission("ayw.levels.30")) { // Level 30+
            System.out.println("Give lv30 starter kits to "+player.getName());
            pInv.addItem(new ItemStack(Material.COOKED_BEEF, 32));
        }

        addPermission(lpPlayer, "ayw.starterpack.isclaimed");
    }
}
