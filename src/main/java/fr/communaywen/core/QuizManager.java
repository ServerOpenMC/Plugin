package fr.communaywen.core;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.Debug;

import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class QuizManager {
    public Quiz currentQuiz;
    private ScheduledExecutorService timeoutExecutor;
    private ScheduledExecutorService executor;
    private List<Quiz> quizzes;
    public FileConfiguration config;
    private AywenCraftPlugin plugin;

    public QuizManager(AywenCraftPlugin plugin, FileConfiguration config) {
        this.plugin = plugin;
        this.config = config;
        this.quizzes = new ArrayList<>();

        for (LinkedHashMap i : (List<LinkedHashMap>) config.getList("quizzes")) {
            this.quizzes.add(
                    new Quiz((String) i.get("question"), (String) i.get("answer"))
            );
        };

        Runnable runnable = () -> {
            if (Bukkit.getOnlinePlayers().size() < 1) return;

            this.timeoutExecutor = Executors.newScheduledThreadPool(1);

            int index = new Random().nextInt(this.quizzes.size());

            currentQuiz = this.quizzes.get(index);
            Bukkit.broadcastMessage(
                    "Nouvelle question : " + currentQuiz.question + "\n" +
                    "Vous avez 30 seconds pour répondre" + "\n" +
                    "Le premier a répondre gagne !"
            );

            Runnable tellAnswer = () -> {
                Bukkit.broadcastMessage("Personne n'as trouvée la réponse au quizz après 30 secondes");
                Bukkit.broadcastMessage("La réponse était : " + currentQuiz.answer);
                currentQuiz = null;
            };

            this.timeoutExecutor.schedule(tellAnswer, 30, TimeUnit.SECONDS);
        };

        executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(runnable, 1, config.getInt("interval"), TimeUnit.MINUTES);
    }

    public void onPlayerChat(AsyncPlayerChatEvent event) {
        int money = new Random().nextInt(config.getInt("rewards.money.min"), config.getInt("rewards.money.max") - 1);

        if (currentQuiz == null) return;

        if (event.getMessage().toLowerCase().equals(currentQuiz.answer)) {
            String message = MessageFormat.format("{0} a trouvé la réponse en premier, il remporte {1} de monnaie", event.getPlayer().getDisplayName(), money);
            Bukkit.broadcastMessage(message);
            this.plugin.economyManager.addBalance(event.getPlayer(), money);
            currentQuiz = null;
            this.timeoutExecutor.shutdownNow();
            this.timeoutExecutor = Executors.newScheduledThreadPool(1);
        }
    }

    public void close() {
        executor.shutdownNow();
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
