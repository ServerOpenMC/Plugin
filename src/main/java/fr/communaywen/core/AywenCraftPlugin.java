package fr.communaywen.core;

import com.onarandombox.MultiverseCore.MultiverseCore;
import dev.xernas.menulib.Menu;
import dev.xernas.menulib.MenuLib;
import fr.communaywen.core.claim.ClaimConfigDataBase;
import fr.communaywen.core.claim.ClaimListener;
import fr.communaywen.core.claim.GamePlayer;
import fr.communaywen.core.claim.RegionManager;
import fr.communaywen.core.commands.*;
import fr.communaywen.core.friends.commands.FriendsCommand;
import fr.communaywen.core.levels.LevelsCommand;
import fr.communaywen.core.levels.LevelsListeners;
import fr.communaywen.core.listeners.*;
import fr.communaywen.core.quests.PlayerQuests;
import fr.communaywen.core.quests.QuestsListener;
import fr.communaywen.core.quests.QuestsManager;
import fr.communaywen.core.quests.qenum.QUESTS;
import fr.communaywen.core.staff.freeze.FreezeCommand;
import fr.communaywen.core.staff.players.PlayersCommand;
import fr.communaywen.core.tpa.TPACommand;
import fr.communaywen.core.tpa.TpacceptCommand;
import fr.communaywen.core.tpa.TpcancelCommand;
import fr.communaywen.core.tpa.TpdenyCommand;
import fr.communaywen.core.trade.TradeAcceptCommand;
import fr.communaywen.core.trade.TradeCommand;
import fr.communaywen.core.trade.TradeListener;
import fr.communaywen.core.utils.*;
import fr.communaywen.core.utils.command.InteractiveHelpMenu;
import lombok.Getter;
import lombok.SneakyThrows;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.autocomplete.SuggestionProvider;
import revxrsal.commands.bukkit.BukkitCommandHandler;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

public final class AywenCraftPlugin extends JavaPlugin {
    public static ArrayList<Player> frozenPlayers = new ArrayList<>();

    @Getter
    private final Managers managers = new Managers();

    public EventsManager eventsManager; // TODO: include to Managers.java

    @Getter
    private static AywenCraftPlugin instance;
    public LuckPerms api;

    @Getter
    private BukkitAudiences adventure;
    @Getter
    private InteractiveHelpMenu interactiveHelpMenu;

    @Getter
    private BukkitCommandHandler handler;

    public List<RegionManager> regions;
    public MultiverseCore mvCore;
    @SneakyThrows
    @Override
    public void onEnable() {
        // Logs
        super.getLogger().info("Hello le monde, ici le plugin AywenCraft !");

        // Gardez les au début sinon ça pète tout
        instance = this;
        MenuLib.init(this);
        managers.initConfig(this);
        managers.init(this);

        eventsManager = new EventsManager(this, loadEventsManager()); // TODO: include to Managers.java

        mvCore = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");

        LinkerAPI linkerAPI = new LinkerAPI(managers.getDatabaseManager());

        OnPlayers onPlayers = new OnPlayers();
        onPlayers.setLinkerAPI(linkerAPI);

        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            api = provider.getProvider();
            onPlayers.setLuckPerms(api);
        }
        else {
            getLogger().severe("LuckPerms not found !");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        MOTDChanger motdChanger = new MOTDChanger();
        motdChanger.startMOTDChanger(this);

        this.adventure = BukkitAudiences.create(this);

        this.regions = new ArrayList<>();

        /* ----- */

        String webhookUrl = getConfig().getString("discord.webhookURL");
        String botName = getConfig().getString("discord.webhookName");
        String botAvatarUrl = getConfig().getString("discord.webhookIconURL");
        DiscordWebhook discordWebhook = new DiscordWebhook(webhookUrl, botName, botAvatarUrl);

        /*  COMMANDS  */

        this.handler = BukkitCommandHandler.create(this);
        this.interactiveHelpMenu = InteractiveHelpMenu.create();
        this.handler.accept(interactiveHelpMenu);

        this.handler.getAutoCompleter().registerSuggestion("featureName", SuggestionProvider.of(managers.getWikiConfig().getKeys(false)));

        this.handler.register(
                new TeamAdminCommand(this),
                new SpawnCommand(this),
                new VersionCommand(this),
                new RulesCommand(managers.getBookConfig()),
                new TeamCommand(),
                new MoneyCommand(this),
                new ScoreboardCommand(),
                new ProutCommand(),
                new FeedCommand(this),
                new TPACommand(this),
                new TpacceptCommand(),
                new TpcancelCommand(),
                new TpdenyCommand(),
                new CreditCommand(),
                new ExplodeRandomCommand(),
                new LinkCommand(linkerAPI),
                new ManualLinkCommand(linkerAPI),
                new RTPCommand(this),
                new FreezeCommand(),
                new PlayersCommand(),
                new FBoomCommand(),
                new BaltopCommand(this),
                new FriendsCommand(managers.getFriendsManager(), this, adventure),
                new PrivacyCommand(this),
                new LevelsCommand(managers.getLevelsManager()),
                new TailleCommand(),
                new WikiCommand(managers.getWikiConfig()),
                new GithubCommand(this),
                new TradeCommand(this),
                new TradeAcceptCommand(this),
                new QuestsCommands(),
                new RewardCommand(this),
                new FeatureCommand(managers.getFeatureManager()),
                new MineCommand(),
                new AdminShopCommand(),
                new PayCommands()
        );

        /*  --------  */

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (!managers.getScoreboardManager().disableSBPlayerList.contains(player)) {
                        managers.getScoreboardManager().setScoreboard(player);
                    }
                    Menu openedMenu = hasMenuOpened(player);
                    if (openedMenu != null) {
                        openedMenu.open();
                    }
                }
            }
        }.runTaskTimer(this, 0L, 100L);

        /* LISTENERS */
        registerEvents(
                new NoMoreLapins(),
                new KebabListener(this),
                new AntiTrampling(),
                new RTPWand(this),
                onPlayers,
                new ExplosionListener(),
                new SleepListener(),
                new ChatListener(this, discordWebhook),
                new FreezeListener(this),
                new WelcomeMessage(managers.getWelcomeMessageConfig()),
                new Insomnia(),
                new VpnListener(this),
                new ThorHammer(),
                new FriendsListener(managers.getFriendsManager()),
                new TablistListener(this),
                new LevelsListeners(managers.getLevelsManager()),
                new CorpseListener(managers.getCorpseManager()),
                new TradeListener(),
                new QuestsListener(),
                new PasFraisListener(this),
                new ClaimListener(),
                new FarineListener()
        );

        getServer().getPluginManager().registerEvents(eventsManager, this); // TODO: refactor
        
        /* --------- */

        saveDefaultConfig();

        createFarineRecipe();

        for (Player player : Bukkit.getOnlinePlayers()) {
            new GamePlayer(player.getName());
            QuestsManager.loadPlayerData(player);
        }

        QuestsManager.initializeQuestsTable();
        ClaimConfigDataBase.loadAllClaims();
    }

    @SneakyThrows
    @Override
    public void onDisable() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            for(QUESTS quests : QUESTS.values()) {
                PlayerQuests pq = QuestsManager.getPlayerQuests(player); // Load quest progress
                QuestsManager.savePlayerQuestProgress(player, quests, pq.getProgress(quests)); // Save quest progress
                player.closeInventory(); // Close inventory
            }
        }
        managers.cleanup();
    }

    public void registerEvents(Listener... args) {
        for (Listener listener : args) {
            getServer().getPluginManager().registerEvents(listener, this);
        }
    }

    public ArrayList<Player> getFrozenPlayers() {
        return frozenPlayers;
    }

    public int getBanDuration() {
        return getConfig().getInt("deco_freeze_nombre_de_jours_ban", 30);
    }


    // Farine pour fabriquer du pain

    private void createFarineRecipe() {
        ItemStack farine = new ItemStack(Material.SUGAR);
        ItemMeta meta = farine.getItemMeta();
        assert meta != null;
        meta.setDisplayName("Farine");
        farine.setItemMeta(meta);

        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(this, "farine"), farine);
        recipe.shape(" A ", " B ", "   ");
        recipe.setIngredient('A', Material.GUNPOWDER);
        recipe.setIngredient('B', Material.WHEAT);

        Bukkit.addRecipe(recipe);
    }

    private Menu hasMenuOpened(Player player) {
        Inventory inv = player.getOpenInventory().getTopInventory();
        if (inv.getHolder() instanceof Menu invMenu) {
            return invMenu;
        }
        return null;
    }

    // TODO: include to Managers.java
    private FileConfiguration loadEventsManager() {
        File eventsFile = new File(getDataFolder(), "events.yml");
        if (!eventsFile.exists()) {
            saveResource("events.yml", false);
        }
        return YamlConfiguration.loadConfiguration(eventsFile);
    }

    /**
     * Format a permission with the permission prefix.
     *
     * @param category the permission category
     * @param suffix   the permission suffix
     * @return The formatted permission.
     * @see PermissionCategory #PERMISSION_PREFIX
     */
    public static @NotNull String formatPermission(final @NotNull PermissionCategory category,
                                                   final @NotNull String suffix) {
        return category.formatPermission(suffix);
    }
}
