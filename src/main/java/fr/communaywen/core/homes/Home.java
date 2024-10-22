package fr.communaywen.core.homes;

import fr.communaywen.core.homes.menu.utils.HomeIcons;
import fr.communaywen.core.homes.menu.utils.HomeMenuUtils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

@Getter
public class Home {
    private final String player;
    @Setter private String name;
    private final Location location;
    @Getter @Setter private HomeIcons icon;

    public Home(String playerUUID, String name, Location location, HomeIcons icon) {
        this.player = playerUUID;
        this.name = name;
        this.location = location;
        this.icon = icon;
    }

    public String serializeLocation() {
        return location.getWorld().getName() + "," +
                location.getBlockX() + "," +
                location.getBlockY() + "," +
                location.getBlockZ() + "," +
                location.getYaw() + "," +
                location.getPitch();
    }

    public static Location deserializeLocation(String locString) {
        String[] loc = locString.split(",");
        return new Location(
                org.bukkit.Bukkit.getWorld(loc[0]),
                Double.parseDouble(loc[1]),
                Double.parseDouble(loc[2]),
                Double.parseDouble(loc[3]),
                Float.parseFloat(loc[4]),
                Float.parseFloat(loc[5])
        );
    }

    public ItemStack getCustomIcon() {
        ItemStack icons = HomeMenuUtils.getHomeButton(this);
        ItemMeta meta = icons.getItemMeta();
        meta.setDisplayName("§a" + name);
        meta.setLore(List.of(
                "§6Position:",
                "§6  W: §e" + location.getWorld().getName(),
                "§6  X: §e" + location.getBlockX(),
                "§6  Y: §e" + location.getBlockY(),
                "§6  Z: §e" + location.getBlockZ()
        ));
        icons.setItemMeta(meta);
        return icons;
    }
}
