package fr.communaywen.core.homes.menu.utils;

import dev.lone.itemsadder.api.CustomStack;
import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.homes.Home;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class HomeMenuUtils {

    private static final List<HomeIcons> HOME_ICONS = Arrays.stream(HomeIcons.values()).toList();

    public static ItemStack getHomeButton(Home home) {
        String iconKey = getHomeIcon(home).getId();
        return CustomStack.getInstance(iconKey).getItemStack();
    }

    public static ItemStack getRandomsIcons() {
        String iconKey = String.valueOf(HOME_ICONS.get((int) (Math.random() * HOME_ICONS.size())).getId());
        return CustomStack.getInstance(iconKey).getItemStack();
    }

    public static HomeIcons getHomeIcon(Home home) {
        return home.getIcon() == HomeIcons.DEFAULT ? getDefaultHomeIcon(home) : home.getIcon();
    }

    public static HomeIcons getDefaultHomeIcon(Home home) {
        return HOME_ICONS.stream()
                .filter(entry -> home.getName().matches(".*" + entry.getUsage() + ".*"))
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
