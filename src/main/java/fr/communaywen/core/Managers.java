package fr.communaywen.core;

import fr.communaywen.core.commands.RewardCommand;
import fr.communaywen.core.corpse.CorpseManager;
import fr.communaywen.core.credit.FeatureManager;
import fr.communaywen.core.economy.EconomyManager;
import fr.communaywen.core.friends.FriendsManager;
import fr.communaywen.core.levels.LevelsDataManager;
import fr.communaywen.core.levels.LevelsManager;
import fr.communaywen.core.scoreboard.ScoreboardManager;
import fr.communaywen.core.teams.TeamManager;
import fr.communaywen.core.utils.ConfigUtils;
import fr.communaywen.core.utils.FallingBlocksExplosionManager;
import fr.communaywen.core.utils.database.Blacklist;
import fr.communaywen.core.utils.database.DatabaseManager;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.sql.SQLException;

@Getter
public class Managers {

    private AywenCraftPlugin plugin;

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

    private FileConfiguration bookConfig;
    private FileConfiguration wikiConfig;
    private FileConfiguration welcomeMessageConfig;
    private FileConfiguration levelsConfig;
    private FileConfiguration quizzesConfig;

    public void initConfig(AywenCraftPlugin plugin) {
        plugin.saveDefaultConfig();
        bookConfig = ConfigUtils.loadConfig(plugin, "rules.yml");
        wikiConfig = ConfigUtils.loadConfig(plugin, "wiki.yml");
        welcomeMessageConfig = ConfigUtils.loadConfig(plugin, "welcomeMessageConfig.yml");
        levelsConfig = ConfigUtils.loadConfig(plugin, "levels.yml");
        quizzesConfig = ConfigUtils.loadConfig(plugin, "quizzes.yml");
    }

    public void init(AywenCraftPlugin plugin) {
        this.plugin = plugin;

        // --------- MANAGERS --------- //

        this.teamManager = new TeamManager();
        featureManager = new FeatureManager();

        // Database
        databaseManager = new DatabaseManager(plugin, false);
        try {
            databaseManager.init(); // Créer les tables nécessaires
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            databaseManager.register(
                    // Utilisation : NomDeLaClasse.class,
                    // Dans la classe, ajouter : extends DatabaseConnector, et vous pourrez accéder à la base de données avec l'attribut "connection"
                    Blacklist.class,
                    RewardCommand.class
            );
        }
        // Database

        scoreboardManager = new ScoreboardManager();
        quizManager = new QuizManager(plugin, quizzesConfig);
        economyManager = new EconomyManager(plugin.getDataFolder());
        friendsManager = new FriendsManager(databaseManager, plugin);
        corpseManager = new CorpseManager();
        fbeManager = new FallingBlocksExplosionManager();
        levelsManager = new LevelsManager();

        LevelsDataManager.setLevelsFile(levelsConfig, new File(plugin.getDataFolder(), "levels.yml"));
        LevelsDataManager.setLevelsFile(levelsConfig, new File(plugin.getDataFolder(), "levels.yml"));
    }

    public void cleanup() {
        databaseManager.close();
        quizManager.close();
        corpseManager.removeAll();
    }

}
