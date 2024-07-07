package fr.communaywen.core;

import fr.communaywen.core.economy.EconomyManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class QuizManager implements Listener {

    private EconomyManager economyManager;
    private List<Quiz> quizzes;
    private ScheduledExecutorService executor;
    private FileConfiguration config;
    private AywenCraftPlugin plugin;
    private Quiz currentQuiz;

    public QuizManager(AywenCraftPlugin plugin, EconomyManager economyManager, FileConfiguration config) {
        this.plugin = plugin;
        this.economyManager = economyManager;
        this.config = config;
        this.quizzes = new ArrayList<>();

        // Charger les quizzes depuis la configuration
        for (LinkedHashMap<?, ?> i : (List<LinkedHashMap<?, ?>>) config.getList("quizzes")) {
            this.quizzes.add(new Quiz((String) i.get("question"), (String) i.get("answer")));
        }

        // Planifier l'exécution du quiz périodiquement
        Runnable quizTask = () -> {
            int index = new Random().nextInt(this.quizzes.size());
            this.currentQuiz = this.quizzes.get(index);

            Bukkit.broadcastMessage("Nouvelle question : " + currentQuiz.question);
            Bukkit.broadcastMessage("Vous avez 30 secondes pour répondre");
            Bukkit.broadcastMessage("Le premier à répondre gagne !");

            // Planifier l'affichage de la réponse après 30 secondes
            Runnable tellAnswer = () -> {
                Bukkit.broadcastMessage("Personne n'a trouvé la réponse au quiz après 30 secondes.");
                Bukkit.broadcastMessage("La réponse était : " + currentQuiz.answer);
                currentQuiz = null;
            };

            this.executor = Executors.newScheduledThreadPool(1);
            this.executor.schedule(tellAnswer, 30, TimeUnit.SECONDS);
        };

        // Planifier l'exécution initiale et répétée des quizzes
        this.executor = Executors.newScheduledThreadPool(1);
        this.executor.scheduleAtFixedRate(quizTask, 5, config.getInt("interval"), TimeUnit.MINUTES);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        economyManager.deposit(player, 100); // Déposer 100 unités dans le compte du joueur à son arrivée
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        economyManager.withdraw(player, 50); // Retirer 50 unités du compte du joueur lorsqu'il quitte
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (currentQuiz == null) return;

        String message = event.getMessage().toLowerCase();
        if (message.equals(currentQuiz.answer)) {
            int money = new Random().nextInt(config.getInt("rewards.money.min"), config.getInt("rewards.money.max"));
            String rewardMessage = MessageFormat.format("{0} a trouvé la réponse en premier, il remporte {1} de monnaie", event.getPlayer().getDisplayName(), money);
            Bukkit.broadcastMessage(rewardMessage);

            economyManager.deposit(event.getPlayer(), money); // Déposer la récompense dans le compte du joueur
            currentQuiz = null;
            if (executor != null) {
                executor.shutdownNow(); // Arrêter le planificateur du quiz en cours
                executor = null;
            }
        }
    }

    // Classe interne Quiz pour représenter une question et sa réponse
    private static class Quiz {
        public String question;
        public String answer;

        public Quiz(String question, String answer) {
            this.question = question;
            this.answer = answer.toLowerCase();
        }
    }
}

    public class Quiz {
        public String question;
        public String answer;

        public Quiz(String question, String answer) {
            this.question = question;
            this.answer = answer.toLowerCase();
        }
    }
}