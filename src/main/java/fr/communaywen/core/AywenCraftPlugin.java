package fr.communaywen.core;

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import dev.xernas.menulib.Menu;
import dev.xernas.menulib.MenuLib;
import fr.communaywen.core.claim.ClaimConfigDataBase;
import fr.communaywen.core.claim.ClaimListener;
import fr.communaywen.core.claim.GamePlayer;
import fr.communaywen.core.claim.RegionManager;
import fr.communaywen.core.clockinfos.tasks.CompassClockTask;
import fr.communaywen.core.commands.contest.ContestCommand;
import fr.communaywen.core.commands.corpse.CorpseCommand;
import fr.communaywen.core.commands.credits.CreditCommand;
import fr.communaywen.core.commands.credits.FeatureCommand;
import fr.communaywen.core.commands.economy.AdminShopCommand;
import fr.communaywen.core.commands.economy.BaltopCommand;
import fr.communaywen.core.commands.economy.MoneyCommand;
import fr.communaywen.core.commands.economy.PayCommands;
import fr.communaywen.core.commands.explosion.ExplodeRandomCommand;
import fr.communaywen.core.commands.explosion.FBoomCommand;
import fr.communaywen.core.commands.fun.*;
import fr.communaywen.core.commands.homes.*;
import fr.communaywen.core.commands.teams.TeamClaim;
import fr.communaywen.core.commands.link.LinkCommand;
import fr.communaywen.core.commands.link.ManualLinkCommand;
import fr.communaywen.core.commands.randomEvents.RandomEventsCommand;
import fr.communaywen.core.commands.socials.DiscordCommand;
import fr.communaywen.core.commands.socials.GithubCommand;
import fr.communaywen.core.commands.staff.ReportCommands;
import fr.communaywen.core.commands.teams.TeamAdminCommand;
import fr.communaywen.core.commands.teams.TeamCommand;
import fr.communaywen.core.commands.teleport.RTPCommand;
import fr.communaywen.core.commands.teleport.SpawnCommand;
import fr.communaywen.core.commands.utils.*;
import fr.communaywen.core.contest.ContestIntractEvents;
import fr.communaywen.core.contest.ContestListener;
import fr.communaywen.core.contest.ContestManager;
import fr.communaywen.core.contest.FirerocketSpawnListener;
import fr.communaywen.core.customitems.commands.ShowCraftCommand;
import fr.communaywen.core.customitems.listeners.CIBreakBlockListener;
import fr.communaywen.core.customitems.listeners.CIEnchantListener;
import fr.communaywen.core.customitems.listeners.CIPrepareAnvilListener;
import fr.communaywen.core.elevator.ElevatorListener;
import fr.communaywen.core.fallblood.BandageRecipe;
import fr.communaywen.core.friends.commands.FriendsCommand;
import fr.communaywen.core.homes.world.DisabledWorldHome;
import fr.communaywen.core.homes.Home;
import fr.communaywen.core.homes.HomesManagers;
import fr.communaywen.core.levels.LevelsCommand;
import fr.communaywen.core.levels.LevelsListeners;
import fr.communaywen.core.listeners.*;
import fr.communaywen.core.luckyblocks.commands.LuckyBlockCommand;
import fr.communaywen.core.luckyblocks.listeners.LBBlockBreakListener;
import fr.communaywen.core.luckyblocks.listeners.LBEntityDeathListener;
import fr.communaywen.core.luckyblocks.listeners.LBPlayerInteractListener;
import fr.communaywen.core.luckyblocks.listeners.LBPlayerQuitListener;
import fr.communaywen.core.mailboxes.MailboxCommand;
import fr.communaywen.core.mailboxes.MailboxListener;
import fr.communaywen.core.managers.ChunkListManager;
import fr.communaywen.core.personalhome.HSCommand;
import fr.communaywen.core.quests.PlayerQuests;
import fr.communaywen.core.quests.QuestsListener;
import fr.communaywen.core.quests.QuestsManager;
import fr.communaywen.core.commands.staff.FreezeCommand;
import fr.communaywen.core.commands.staff.PlayersCommand;
import fr.communaywen.core.space.moon.MoonListener;
import fr.communaywen.core.space.rocket.RocketListener;
import fr.communaywen.core.tab.TabList;
import fr.communaywen.core.tpa.*;
import fr.communaywen.core.trade.TradeAcceptCommand;
import fr.communaywen.core.trade.TradeCommand;
import fr.communaywen.core.trade.TradeListener;
import fr.communaywen.core.utils.DiscordWebhook;
import fr.communaywen.core.utils.LinkerAPI;
import fr.communaywen.core.utils.MOTDChanger;
import fr.communaywen.core.utils.PermissionCategory;
import fr.communaywen.core.utils.command.InteractiveHelpMenu;
import lombok.Getter;
import lombok.SneakyThrows;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.luckperms.api.LuckPerms;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.autocomplete.SuggestionProvider;
import revxrsal.commands.bukkit.BukkitCommandHandler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.*;

public final class AywenCraftPlugin extends JavaPlugin {
    public static ArrayList<Player> frozenPlayers = new ArrayList<>();
    public static ArrayList<Player> playerClaimsByPass = new ArrayList<>();
    @Getter
    private static AywenCraftPlugin instance;
    @Getter
    private final Managers managers = new Managers();
    public EventsManager eventsManager; // TODO: include to Managers.java
    public LuckPerms api;
    public List<RegionManager> regions;
    public MultiverseCore mvCore;
    @Getter
    private BukkitAudiences adventure;
    @Getter
    private InteractiveHelpMenu interactiveHelpMenu;
    @Getter
    private BukkitCommandHandler handler;
    @Getter
    private TabList tabList;
    private HashMap<Class<?>, HashMap<String, Flag<?>>> flags;

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

    @Override
    public void onLoad() {
        this.registerFlags(new StateFlag("disable-thor-hammer", false));
        this.registerFlags(new StateFlag("disable-hammer", false));
        this.registerFlags(new StateFlag("disable-builder-wand", false));
        this.registerFlags(new StateFlag("disable-fly", false));
        this.registerFlags(new StateFlag("disable-spawn-grave", false));
    }

    @SneakyThrows
    @Override
    public void onEnable() {
        // Logs
        super.getLogger().info("Hello le monde, ici le plugin AywenCraft !");

        // Gardez les au début sinon ça pète tout
        instance = this;

        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            api = provider.getProvider();
        } else {
            getLogger().severe("LuckPerms not found !");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        MenuLib.init(this);
        managers.initConfig(this);
        managers.init(this);
        ClaimConfigDataBase.loadAllClaimsData();

        eventsManager = new EventsManager(this, loadEventsManager()); // TODO: include to Managers.java

        mvCore = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");

        LinkerAPI linkerAPI = new LinkerAPI(managers.getDatabaseManager());

        OnPlayers onPlayers = new OnPlayers();
        onPlayers.setLinkerAPI(linkerAPI);
        onPlayers.setLuckPerms(api);

        MOTDChanger motdChanger = new MOTDChanger();
        motdChanger.startMOTDChanger(this);

        this.adventure = BukkitAudiences.create(this);

        this.regions = new ArrayList<>();

        this.tabList = new TabList();

        /* ----- */

        String webhookUrl = getConfig().getString("discord.webhookURL");
        String botName = getConfig().getString("discord.webhookName");
        String botAvatarUrl = getConfig().getString("discord.webhookIconURL");
        DiscordWebhook discordWebhook = new DiscordWebhook(webhookUrl, botName, botAvatarUrl);

        /*  COMMANDS  */

        this.handler = BukkitCommandHandler.create(this);
        this.interactiveHelpMenu = InteractiveHelpMenu.create();
        this.handler.accept(interactiveHelpMenu);
        this.handler.getTranslator().setLocale(Locale.FRENCH);

        this.handler.getAutoCompleter().registerSuggestion("homes", (args, sender, command) -> {
            Player player = Bukkit.getPlayer(sender.getUniqueId());
            List<String> suggestions = new ArrayList<>();

            if(command.getName().equals("home")) {
                suggestions.add("upgrade");
            }

            assert player != null;
            if(!command.equals("renamehome")) {
                if(args.isEmpty()) {
                    if(player.hasPermission("ayw.home.teleport.others")) {
                        suggestions.addAll(Bukkit.getOnlinePlayers().stream()
                                .map(OfflinePlayer::getName)
                                .map(name -> name + ":")
                                .toList());

                    }
                    suggestions.addAll(managers.getHomesManagers().getHomeNamesByPlayer(player.getUniqueId()));
                } else {
                    String arg = args.getFirst();

                    if(arg.contains(":") && player.hasPermission("ayw.home.teleport.others")) {
                        String[] parts = arg.split(":", 2);
                        Player target = Bukkit.getPlayer(parts[0]);

                        if(target != null) {
                            String prefix = parts[0] + ":";
                            suggestions.addAll(managers.getHomesManagers().getHomeNamesByPlayer(target.getUniqueId())
                                    .stream()
                                    .map(home -> prefix + home)
                                    .toList());
                        }
                    } else {
                        if (player.hasPermission("ayw.home.teleport.others")) {
                            suggestions.addAll(Bukkit.getOnlinePlayers().stream()
                                    .map(OfflinePlayer::getName)
                                    .filter(name -> name.toLowerCase().startsWith(arg.toLowerCase()))
                                    .map(name -> name + ":")
                                    .toList());
                        }

                        suggestions.addAll(managers.getHomesManagers().getHomeNamesByPlayer(player.getUniqueId())
                                .stream()
                                .filter(home -> home.toLowerCase().startsWith(arg.toLowerCase()))
                                .toList());
                    }

                    return suggestions;
                }

                if(player.hasPermission("ayw.home.teleport.others")) {
                    suggestions.addAll(Bukkit.getOnlinePlayers().stream()
                            .map(OfflinePlayer::getName)
                            .map(name -> name + ":")
                            .toList());
                }
            }

            suggestions.addAll(HomesManagers.homes.stream()
                    .filter(home -> home.getPlayer().equals(sender.getUniqueId().toString()))
                    .map(Home::getName)
                    .toList());

            suggestions.add("upgrade");
            return suggestions;
        });

        this.handler.getAutoCompleter().registerSuggestion("featureName", SuggestionProvider.of(managers.getWikiConfig().getKeys(false)));
        this.handler.getAutoCompleter().registerSuggestion("lbEventsId", SuggestionProvider.of(managers.getLuckyBlockManager().getLuckyBlocksIds()));
        this.handler.getAutoCompleter().registerSuggestion("colorContest", SuggestionProvider.of(ContestManager.getColorContestList()));
        this.handler.getAutoCompleter().registerSuggestion("homeWorldsAdd", (args, sender, command) -> {

            List<String> allWorlds = new ArrayList<>(Bukkit.getWorlds().stream().map(World::getName).toList());
            allWorlds.removeAll(managers.getDisabledWorldHome().getDisabledWorlds());

            return allWorlds;
        });
        this.handler.getAutoCompleter().registerSuggestion("homeWorldsRemove", (args, sender, command) -> {
            DisabledWorldHome disabledWorldHome = managers.getDisabledWorldHome();

            return (List<String>) new ArrayList<String>(disabledWorldHome.getDisabledWorlds());
        });

        this.handler.getAutoCompleter().registerParameterSuggestions(OfflinePlayer.class, ((args, sender, command) -> {
            OfflinePlayer[] offlinePlayers = Bukkit.getServer().getOfflinePlayers();
            List<String> playerNames = new ArrayList<>();

            for (OfflinePlayer player : offlinePlayers) {
                String playerName = player.getName();
                if (playerName != null) {
                    playerNames.add(playerName);
                }
            }

            return playerNames;
        }));

        this.handler.register(
                new CorpseCommand(this),
                new HSCommand(getManagers().getHomeManager()),
                new ContestCommand(this, loadEventsManager()),
                new TeamAdminCommand(this),
                new SpawnCommand(this),
                new RulesCommand(managers.getBookConfig()),
                new TeamCommand(),
                new MoneyCommand(this),
                new ScoreboardCommand(),
                new ProutCommand(),
                new TPACommand(this),
                new TpacceptCommand(this),
                new TPAGUICommand(this),
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
                new PayCommands(),
                new FallBloodCommand(),
                new DiscordCommand(this),
                new ShowCraftCommand(managers.getCustomItemsManager()),
                new ReportCommands(),
                new ChatChannelCMD(),
                new MailboxCommand(),
                new RandomEventsCommand(this),
                new TeamClaim(),
                new LuckyBlockCommand(managers.getLbPlayerManager(), managers.getLuckyBlockManager()),
                new HomesCommands(managers.getHomeUpgradeManager(), managers.getHomesManagers()),
                new SethomesCommands(managers.getHomesManagers()),
                new DelhomesCommands(managers.getHomesManagers()),
                new RenameHomeCommands(managers.getHomesManagers()),
                new HomeDisabledWorldCommand(managers.getDisabledWorldHome())
        );

        /*  --------  */

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    Menu openedMenu = hasMenuOpened(player);
                    if (openedMenu != null) {
                        openedMenu.open();
                    }
                }
            }
        }.runTaskTimer(this, 0L, 100L);

        new CompassClockTask().runTaskTimer(this, 0L, 5L);

        /* LISTENERS */
        registerEvents(
                //new LeaderboardListener(this),
                new RocketListener(),
                new MoonListener(),
                new CustomFlagsEvents(this),
                new FirerocketSpawnListener(this),
                new ContestListener(this, loadEventsManager()),
                new ContestIntractEvents(),
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
                new Dream(this),
                new VpnListener(this),
                new ThorHammer(this),
                new FriendsListener(managers.getFriendsManager()),
                new TablistListener(this),
                new LevelsListeners(managers.getLevelsManager()),
                new CorpseListener(managers.getCorpseManager(), this),
                new TradeListener(),
                new QuestsListener(),
                new PasFraisListener(this),
                new ClaimListener(),
                new FarineListener(),
                new FallBloodListener(),
                new RTPCommand(this),
                new CIBreakBlockListener(managers.getCustomItemsManager()),
                new CIEnchantListener(managers.getCustomItemsManager()),
                new CIPrepareAnvilListener(managers.getCustomItemsManager()),
                new BabyFuzeListener(),
                new MailboxListener(),
                new ElevatorListener(),
                new ChunkListManager(),
                new LBBlockBreakListener(managers.getLuckyBlockManager()),
                new LBPlayerQuitListener(managers.getLuckyBlockManager()),
                new LBPlayerInteractListener(managers.getLuckyBlockManager()),
                new LBEntityDeathListener(managers.getLuckyBlockManager())
        );

        getServer().getPluginManager().registerEvents(eventsManager, this); // TODO: refactor

        /* --------- */

        saveDefaultConfig();

        createSandRecipe();
        createFarineRecipe();
        createCrazyPotion();

        for (Player player : Bukkit.getOnlinePlayers()) {
            new GamePlayer(player.getName());
            QuestsManager.loadPlayerData(player);
        }

        QuestsManager.initializeQuestsTable();
        ClaimConfigDataBase.processStoredClaimData();
        new BandageRecipe();

        //LeaderboardManager.createLeaderboard();
    }

    @SneakyThrows
    @Override
    public void onDisable() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerQuests pq = QuestsManager.getPlayerQuests(player.getUniqueId()); // Load quest progress
            QuestsManager.savePlayerQuestProgress(player, pq); // Save quest progress
            player.closeInventory(); // Close inventory
        }
        try {
            this.getConfig().save(new File(this.getDataFolder(), "config.yml"));
            loadEventsManager().save(new File(this.getDataFolder(), "events.yml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        managers.cleanup();
    }

    public void registerEvents(Listener... args) {
        for (Listener listener : args) {
            getServer().getPluginManager().registerEvents(listener, this);
        }
    }

    private void registerFlags(Flag<?> flag) {
        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
        if (this.flags == null) {
            this.flags = new HashMap<>();
        }
        try {
            registry.register(flag);
            if (flags.containsKey(flag.getClass())) {
                this.flags.get(flag.getClass()).put(flag.getName(), flag);
            } else {
                this.flags.put(flag.getClass(), new HashMap<>());
                this.flags.get(flag.getClass()).put(flag.getName(), flag);
            }
        } catch (FlagConflictException e) {
            Flag<?> existing = registry.get(flag.getName());
            if (existing instanceof StateFlag) {
                if (flags.containsKey(flag.getClass())) {
                    this.flags.get(flag.getClass()).put(flag.getName(), flag);
                } else {
                    this.flags.put(flag.getClass(), new HashMap<>());
                    this.flags.get(flag.getClass()).put(flag.getName(), flag);
                }
            } else {
                System.out.println("Flag: " + flag.getName() + " could not be registered!");
            }
        }
    }
    public HashMap<Class<?>, HashMap<String, Flag<?>>> getCustomFlags() {
        return flags;
    }

    public ArrayList<Player> getFrozenPlayers() {
        return frozenPlayers;
    }


    // Farine pour fabriquer du pain

    public int getBanDuration() {
        return getConfig().getInt("deco_freeze_nombre_de_jours_ban", 30);
    }

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

    private void createCrazyPotion() {
        ItemStack crazyPotion = new ItemStack(Material.POTION);
        PotionMeta meta = (PotionMeta) crazyPotion.getItemMeta();

        meta.setDisplayName("§k NEW §r §4 Mining Potion §r §k NEW");
        meta.addCustomEffect(new PotionEffect(PotionEffectType.SPEED, 4800, 9), true);
        meta.addCustomEffect(new PotionEffect(PotionEffectType.HASTE, 4800, 55), true);

        crazyPotion.setItemMeta(meta);

        NamespacedKey nmKey = new NamespacedKey(this, "crazypotion_craft");
        ShapedRecipe recipe = new ShapedRecipe(nmKey, crazyPotion);

        recipe.shape("BBB", "WGW", "IEI");

        recipe.setIngredient('B', Material.DIAMOND_BLOCK);
        recipe.setIngredient('G', Material.GLASS_BOTTLE);
        recipe.setIngredient('W', Material.WATER_BUCKET);
        recipe.setIngredient('E', Material.ENDER_PEARL);
        recipe.setIngredient('I', Material.IRON_INGOT);

        getServer().addRecipe(recipe);

    }

    private void createSandRecipe() {
        ItemStack sand = new ItemStack(Material.SAND, 4);

        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(this, "sand_craft"), sand);
        recipe.shape("A");
        recipe.setIngredient('A', Material.SANDSTONE);

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
}
