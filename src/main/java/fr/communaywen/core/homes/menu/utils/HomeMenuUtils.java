package fr.communaywen.core.homes.menu.utils;

import dev.lone.itemsadder.api.CustomStack;
import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.homes.Home;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class HomeMenuUtils {

    private static final List<HomeIcons> HOME_ICONS = Arrays.stream(HomeIcons.values()).toList();

    public static ItemStack getHomeButton(Home home) {
        String iconKey = getHomeIcon(home).getId();
        if(iconKey == null) {
            AywenCraftPlugin.getInstance().getLogger().warning("Icon key is null for home " + home.getName());
            return new ItemStack(Material.GRASS_BLOCK);
        }
        return CustomStack.getInstance(iconKey).getItemStack();
    }

    public static ItemStack getRandomsIcons() {
        String iconKey = String.valueOf(HOME_ICONS.get((int) (Math.random() * HOME_ICONS.size())).getId());
        return CustomStack.getInstance(iconKey).getItemStack();
    }

    public static HomeIcons getHomeIcon(Home home) {
        try {
            String homeName = home.getName().toLowerCase();
            if(home.getIcon() == null) {
                return HomeIcons.DEFAULT;
            }
            if (home.getIcon() != HomeIcons.DEFAULT) {
                return home.getIcon();
            }
            for (HomeIcons icon : HomeIcons.values()) {
                String[] usages = icon.getUsage().split("\\|");
                for (String usage : usages) {
                    if (homeName.contains(usage)) {
                        return icon;
                    }
                }
            }
            return HomeIcons.DEFAULT;
        } catch (Exception e) {
            AywenCraftPlugin.getInstance().getLogger().warning("Error while getting home icon for home " + home.getName());
            return HomeIcons.DEFAULT;
        }
    }

    public static HomeIcons getDefaultHomeIcon(String name) {
        return HOME_ICONS.stream()
                .filter(entry -> name.matches(".*" + entry.getUsage() + ".*"))
                .findFirst()
                .orElse(HomeIcons.DEFAULT);
    }

    public static HomeIcons getHomeIcon(String iconId) {
        return HOME_ICONS.stream()
                .filter(entry -> entry.getId().equals(iconId))
                .findFirst()
                .orElse(HomeIcons.DEFAULT);
    }
}
