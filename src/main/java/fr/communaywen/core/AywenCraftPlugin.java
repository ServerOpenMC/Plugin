package fr.communaywen.core;

import fr.communaywen.core.commands.ProutCommand;
import fr.communaywen.core.commands.VersionCommand;
import fr.communaywen.core.listeners.ChatListener;
import fr.communaywen.core.utils.DiscordWebhook;
import fr.communaywen.core.utils.MOTDChanger;
import fr.communaywen.core.utils.PermissionCategory;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class AywenCraftPlugin extends JavaPlugin {

    private MOTDChanger motdChanger;

    @Override
    public void onEnable() {
        super.getLogger().info("Hello le monde, ici le plugin AywenCraft !");

        motdChanger = new MOTDChanger();
        motdChanger.startMOTDChanger(this);

        this.getCommand("version").setExecutor(new VersionCommand(this));

        final @Nullable PluginCommand proutCommand = super.getCommand("prout");
        if (proutCommand != null) {
            proutCommand.setExecutor(new ProutCommand());
        }

        String webhookUrl = "https://discord.com/api/webhooks/1258553652868677802/u17NMB93chQrYf6V0MnbKPMbjoY6B_jN9e2nhK__uU8poc-d8a-aqaT_C0_ur4TSFMy_";
        DiscordWebhook discordWebhook = new DiscordWebhook(webhookUrl);
        getServer().getPluginManager().registerEvents(new ChatListener(discordWebhook), this);
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
     * @see PermissionCategory#PERMISSION_PREFIX
     */
    public static @NotNull String formatPermission(final @NotNull PermissionCategory category,
                                                   final @NotNull String suffix) {
        return category.formatPermission(suffix);
    }
}
