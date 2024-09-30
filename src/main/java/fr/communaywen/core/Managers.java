package fr.communaywen.core;

import fr.communaywen.core.commands.fun.RewardCommand;
import fr.communaywen.core.commands.randomEvents.RandomEventsData;
import fr.communaywen.core.contest.ContestManager;
import fr.communaywen.core.corpse.CorpseManager;
import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.FeatureManager;
import fr.communaywen.core.dreamdim.AdvancementRegister;
import fr.communaywen.core.dreamdim.DimensionManager;
import fr.communaywen.core.customitems.managers.CustomItemsManager;
import fr.communaywen.core.economy.EconomyManager;
import fr.communaywen.core.friends.FriendsManager;
import fr.communaywen.core.homes.HomeUpgradeManager;
import fr.communaywen.core.homes.HomesManagers;
import fr.communaywen.core.levels.LevelsDataManager;
import fr.communaywen.core.levels.LevelsManager;
import fr.communaywen.core.managers.LeaderboardManager;
import fr.communaywen.core.personalhome.Home;
import fr.communaywen.core.personalhome.HomeManager;
import fr.communaywen.core.luckyblocks.managers.LBPlayerManager;
import fr.communaywen.core.luckyblocks.managers.LuckyBlockManager;
import fr.communaywen.core.scoreboard.ScoreboardManager;
import fr.communaywen.core.space.moon.MoonDimensionManager;
import fr.communaywen.core.staff.report.ReportManager;
import fr.communaywen.core.teams.Team;
import fr.communaywen.core.teams.TeamManager;
import fr.communaywen.core.utils.ConfigUtils;
import fr.communaywen.core.utils.FallingBlocksExplosionManager;
import fr.communaywen.core.utils.ParticleRegionManager;
import fr.communaywen.core.utils.chatchannel.PlayerChatChannel;
import fr.communaywen.core.utils.database.Blacklist;
import fr.communaywen.core.utils.database.DatabaseManager;
import fr.communaywen.core.utils.database.TransactionsManager;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.sql.SQLException;

@Credit("Xernas")
@Getter
public class Managers {

    private AywenCraftPlugin plugin;
    private ParticleRegionManager particleRegionManager;
    private ContestManager contestManager;
    private LeaderboardManager leaderboardManager;
    private DimensionManager dreamdimManager;
    private MoonDimensionManager moonDimManager;
    private HomeManager homeManager;
    private TeamManager teamManager;
    private FeatureManager featureManager;
    private FriendsManager friendsManager;
    private CorpseManager corpseManager;
    private EconomyManager economyManager;
    private ScoreboardManager scoreboardManager;
    private DatabaseManager databaseManager;
    private QuizManager quizManager;
    private FallingBlocksExplosionManager fbeManager;
    private LevelsManager levelsManager;
    private TransactionsManager transactionsManager;
    private CustomItemsManager customItemsManager;
    private ReportManager reportManager;
    private PlayerChatChannel chatChannel;
    private LuckyBlockManager luckyBlockManager;
    private LBPlayerManager lbPlayerManager;
    private HomesManagers homesManagers;
    private HomeUpgradeManager homeUpgradeManager;

    private FileConfiguration bookConfig;
    private FileConfiguration wikiConfig;
    private FileConfiguration welcomeMessageConfig;
    private FileConfiguration levelsConfig;
    private FileConfiguration quizzesConfig;
    private FileConfiguration customItemsConfig;

    public void initConfig(AywenCraftPlugin plugin) {
        plugin.saveDefaultConfig();
        bookConfig = ConfigUtils.loadConfig(plugin, "rules.yml");
        wikiConfig = ConfigUtils.loadConfig(plugin, "wiki.yml");
        welcomeMessageConfig = ConfigUtils.loadConfig(plugin, "welcomeMessageConfig.yml");
        levelsConfig = ConfigUtils.loadConfig(plugin, "levels.yml");
        quizzesConfig = ConfigUtils.loadConfig(plugin, "quizzes.yml");
        customItemsConfig = ConfigUtils.loadConfig(plugin, "customitems.yml");
    }

    public void init(AywenCraftPlugin plugin) {
        this.plugin = plugin;

        // --------- MANAGERS --------- //

        featureManager = new FeatureManager();

        // Database
        databaseManager = new DatabaseManager(plugin, true);
        try {
            databaseManager.init(); // Créer les tables nécessaires
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            databaseManager.register(
                    // Utilisation : NomDeLaClasse.class,
                    // Dans la classe, ajouter : extends DatabaseConnector, et vous pourrez accéder à la base de données avec l'attribut "connection"
                    Blacklist.class,
                    RewardCommand.class,
                    TeamManager.class,
                    HomeManager.class,
                    Home.class,
                    Team.class,
                    TransactionsManager.class,
                    AdvancementRegister.class,
                    RandomEventsData.class
            );
        }
        // Database

        particleRegionManager = new ParticleRegionManager(plugin);
        leaderboardManager = new LeaderboardManager(plugin);
        dreamdimManager = new DimensionManager(plugin);
        moonDimManager = new MoonDimensionManager(plugin);
        contestManager = new ContestManager(plugin);
        this.teamManager = new TeamManager(plugin);
        scoreboardManager = new ScoreboardManager(plugin);
        dreamdimManager = new DimensionManager(plugin);
        homeManager = new HomeManager(plugin);
        quizManager = new QuizManager(plugin, quizzesConfig);
        economyManager = new EconomyManager(plugin.getDataFolder());
        friendsManager = new FriendsManager(databaseManager, plugin);
        corpseManager = new CorpseManager();
        fbeManager = new FallingBlocksExplosionManager();
        levelsManager = new LevelsManager();
        transactionsManager = new TransactionsManager();
        customItemsManager = new CustomItemsManager(plugin, customItemsConfig);
        chatChannel = new PlayerChatChannel();
        reportManager = new ReportManager();
        reportManager.loadReports();
        luckyBlockManager = new LuckyBlockManager();
        lbPlayerManager = new LBPlayerManager();
        homesManagers = new HomesManagers();
        homeUpgradeManager = new HomeUpgradeManager(homesManagers, plugin);


        LevelsDataManager.setLevelsFile(levelsConfig, new File(plugin.getDataFolder(), "levels.yml"));
        LevelsDataManager.setLevelsFile(levelsConfig, new File(plugin.getDataFolder(), "levels.yml"));

        dreamdimManager.init();
        homeManager.init();
        moonDimManager.init();
        homesManagers.loadHomes();
    }

    public void cleanup() {
        /* Besoin de la db */
        dreamdimManager.close();
        reportManager.saveReports();

        /* Plus besoin de la db */
        databaseManager.close();

        quizManager.close();
        corpseManager.removeAll();
        teamManager.getTeamCache().saveAllTeamsToDatabase();

    }
}
