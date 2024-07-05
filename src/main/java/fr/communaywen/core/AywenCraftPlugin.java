package fr.communaywen.core;

import dev.xernas.menulib.MenuLib;
import fr.communaywen.core.commands.RulesCommand;
import fr.communaywen.core.commands.TeamCommand;
import fr.communaywen.core.listeners.ChatListener;
import fr.communaywen.core.teams.TeamManager;
import fr.communaywen.core.commands.ProutCommand;
import fr.communaywen.core.utils.DiscordWebhook;
import fr.communaywen.core.utils.MOTDChanger;
import fr.communaywen.core.commands.VersionCommand;
import fr.communaywen.core.utils.PermissionCategory;
import fr.communaywen.core.commands.RTPCommand;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public final class AywenCraftPlugin extends JavaPlugin {

    private MOTDChanger motdChanger;
    private TeamManager teamManager;
    private FileConfiguration bookConfig;
    private static AywenCraftPlugin instance;

    private void loadBookConfig() {
        File bookFile = new File(getDataFolder(), "rules.yml");
        if (!bookFile.exists()) {
            saveResource("rules.yml", false);
        }
        bookConfig = YamlConfiguration.loadConfiguration(bookFile);
    }

    @Override
    public void onEnable() {
        super.getLogger().info("Hello le monde, ici le plugin AywenCraft !");

        instance = this;

        MenuLib.init(this);

        motdChanger = new MOTDChanger();
        motdChanger.startMOTDChanger(this);
        teamManager = new TeamManager();

        String webhookUrl = "https://discord.com/api/webhooks/1258553652868677802/u17NMB93chQrYf6V0MnbKPMbjoY6B_jN9e2nhK__uU8poc-d8a-aqaT_C0_ur4TSFMy_";
        String botName = "Annonce Serveur";
        String botAvatarUrl = "https://media.discordapp.net/attachments/1161296445169741836/1258408047412383804/image.png?ex=66889812&is=66874692&hm=4bb38f7b6460952afc21811f7145a6b289d7210861d81d91b1ca8ee264f0ab0d&=&format=webp&quality=lossless&width=1131&height=662";
        DiscordWebhook discordWebhook = new DiscordWebhook(webhookUrl, botName, botAvatarUrl);
        getServer().getPluginManager().registerEvents(new ChatListener(discordWebhook), this);

        this.getCommand("version").setExecutor(new VersionCommand(this));

        loadBookConfig();
        this.getCommand("rules").setExecutor(new RulesCommand(bookConfig));
        this.getCommand("regles").setExecutor(new RulesCommand(bookConfig));

        PluginCommand teamCommand = this.getCommand("team");
        teamCommand.setExecutor(new TeamCommand());
        teamCommand.setTabCompleter(new TeamCommand());

        final @Nullable PluginCommand proutCommand = super.getCommand("prout");
        if (proutCommand != null)
            proutCommand.setExecutor(new ProutCommand());
        
        this.getCommand("rtp").setExecutor(new RTPCommand(this));
        getServer().getPluginManager().registerEvents(new AntiTrampling(),this);
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
    }

    public TeamManager getTeamManager() {
        return teamManager;
    }

    public static AywenCraftPlugin getInstance() {
        return instance;
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
