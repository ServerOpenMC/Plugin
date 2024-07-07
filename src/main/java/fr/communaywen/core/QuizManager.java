package fr.communaywen.core;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class QuizManager {
    public static Quiz currentQuiz;

    public ScheduledExecutorService executor;

    private List<Quiz> quizzes;

    public FileConfiguration config;

    public QuizManager(FileConfiguration config) {
        this.config = config;
        this.quizzes = new ArrayList<>();

        for (LinkedHashMap i : (List<LinkedHashMap>) config.getList("quizzes")) {
            this.quizzes.add(
                    new Quiz((String) i.get("question"), (String) i.get("answer"))
            );
        };

        Runnable runnable = () -> {
            this.executor = Executors.newScheduledThreadPool(1);

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

            this.executor.schedule(tellAnswer, 30, TimeUnit.SECONDS);
        };

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(runnable, 5, config.getInt("interval"), TimeUnit.MINUTES);
    }

    public void onPlayerChat(AsyncPlayerChatEvent event) {
        int money = new Random().nextInt(config.getInt("rewards.money.min"), config.getInt("rewards.money.max") - 1);

        if (currentQuiz == null) return;

        if (event.getMessage().toLowerCase().equals(currentQuiz.answer)) {
            String message = MessageFormat.format("{0} a trouvé la réponse en premier, il remporte {1} de monnaie", event.getPlayer().getDisplayName(), money);
            Bukkit.broadcastMessage(message);
            currentQuiz = null;
            this.executor.shutdownNow();
            this.executor = Executors.newScheduledThreadPool(1);
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
