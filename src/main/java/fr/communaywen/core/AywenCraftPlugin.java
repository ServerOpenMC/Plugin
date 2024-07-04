package fr.communaywen.core;

import fr.communaywen.core.utils.MOTDChanger;
import fr.communaywen.core.commands.VersionCommand;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class AywenCraftPlugin extends JavaPlugin {

    /**
     * Permission prefix.
     * <br>
     * Permissions in the plugin <b>SHOULD</b> be prefixed with this prefix. E.g. <code>ayw.command.prout</code>
     */
    public static final @NotNull String PERMISSION_PREFIX = "ayw";

    private MOTDChanger motdChanger;

    @Override
    public void onEnable() {
        super.getLogger().info("Hello le monde, ici le plugin AywenCraft !");

        motdChanger = new MOTDChanger();
        motdChanger.startMOTDChanger(this);



        this.getCommand("version").setExecutor(new VersionCommand(this));
    }

    @Override
    public void onDisable() {
    }

    /**
     * Format a permission with the permission prefix.
     *
     * @param suffix The permission suffix.
     * @return The formatted permission.
     * @see #PERMISSION_PREFIX
     */
    public static @NotNull String formatPermission(final @NotNull String suffix) {
        return PERMISSION_PREFIX + "." + suffix;
    }

}
