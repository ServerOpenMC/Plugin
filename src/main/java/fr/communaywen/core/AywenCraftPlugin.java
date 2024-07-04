package fr.communaywen.core;

import fr.communaywen.core.commands.ProutCommand;
import fr.communaywen.core.utils.MOTDChanger;
import fr.communaywen.core.commands.VersionCommand;
import fr.communaywen.core.utils.PermissionCategory;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class AywenCraftPlugin extends JavaPlugin {

    private MOTDChanger motdChanger;

    @Override
    public void onEnable() {
        super.getLogger().info("Hello le monde, ici le plugin AywenCraft !");

        motdChanger = new MOTDChanger();
        motdChanger.startMOTDChanger(this);



        this.getCommand("version").setExecutor(new VersionCommand(this));

        final @Nullable PluginCommand proutCommand = super.getCommand("prout");
        if (proutCommand != null)
            proutCommand.setExecutor(new ProutCommand());
    }

    @Override
    public void onDisable() {
    }

    /**
     * Format a permission with the permission prefix.
     *
     * @param category the permission category
     * @param suffix the permission suffix
     * @return The formatted permission.
     * @see #PERMISSION_PREFIX
     */
    public static @NotNull String formatPermission(final @NotNull PermissionCategory category,
                                                   final @NotNull String suffix) {
        return category.formatPermission(suffix);
    }

}
