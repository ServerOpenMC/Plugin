package fr.communaywen.core;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerEvent;

import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class QuizManager implements Listener {
    private final AywenCraftPlugin plugin;
    private List<Quiz> quizzes;
    private FileConfiguration config;
    public static Quiz currentQuiz;
    private ScheduledExecutorService executor;

    public QuizManager(AywenCraftPlugin plugin, FileConfiguration config) {
        this.plugin = plugin;
        this.config = config;
        this.quizzes = new ArrayList<>();
        
        for (LinkedHashMap i : (List<LinkedHashMap>) config.getList("quizzes")) {
            this.quizzes.add(new Quiz((String) i.get("question"), (String) i.get("answer")));
        }

        Runnable runnable = () -> {
            this.executor = Executors.newScheduledThreadPool(1);

            int index = new Random().nextInt(this.quizzes.size());

            currentQuiz = this.quizzes.get(index);
            Bukkit.broadcastMessage(
                "Nouvelle question : " + currentQuiz.question + "\n" +
                "Vous avez 30 secondes pour répondre" + "\n" +
                "Le premier à répondre gagne !"
            );

            Runnable tellAnswer = () -> {
                Bukkit.broadcastMessage("Personne n'a trouvé la réponse au quiz après 30 secondes.");
                Bukkit.broadcastMessage("La réponse était : " + currentQuiz.answer);
                currentQuiz = null;
            };

            this.executor.schedule(tellAnswer, 30, TimeUnit.SECONDS);
        };

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(runnable, 5, config.getInt("interval"), TimeUnit.MINUTES);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        int money = new Random().nextInt(config.getInt("rewards.money.min"), config.getInt("rewards.money.max"));

        if (currentQuiz == null) return;

        if (event.getMessage().equalsIgnoreCase(currentQuiz.answer)) {
            String message = MessageFormat.format("{0} a trouvé la réponse en premier, il remporte {1} de monnaie", event.getPlayer().getDisplayName(), money);
            Bukkit.broadcastMessage(message);
            this.plugin.economyManager.addBalance(event.getPlayer(), money);
            currentQuiz = null;
            this.executor.shutdownNow();
            this.executor = Executors.newScheduledThreadPool(1);
        }
    }

    @EventHandler
    public void onQuizCompletion(PlayerEvent event) {
        // Exemple d'utilisation de l'économie
        double money = 10.0; // Par exemple, l'argent à donner au joueur après avoir complété le quiz
        this.plugin.economyManager.addBalance(event.getPlayer(), money);
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
