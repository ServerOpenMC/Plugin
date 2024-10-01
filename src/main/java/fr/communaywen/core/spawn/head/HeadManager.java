package fr.communaywen.core.spawn.head;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.utils.database.DatabaseConnector;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.UUID;

public class HeadManager extends DatabaseConnector  {
    static AywenCraftPlugin plugin;
    public HeadManager(AywenCraftPlugin plugins) {
        plugin = plugins;
    }

    public static void giveHead(Player player) {
        ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);

        SkullMeta skullMeta = (SkullMeta) playerHead.getItemMeta();

        UUID uuid = UUID.randomUUID();
        GameProfile profile = new GameProfile(uuid, null);

        profile.getProperties().put("textures", new Property("textures", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDcxZjQ2NWJlMDYwZGI5Nzg0ZTJjOTBiNjU5MDBmNGZmY2RjZWZlMmUxY2E3ZDI4ZjJmNjhiNDRmNWZmNjJmNSJ9fX0="));

        try {
            Field profileField = skullMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(skullMeta, profile);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        playerHead.setItemMeta(skullMeta);

        player.getInventory().addItem(playerHead);
    }
}
