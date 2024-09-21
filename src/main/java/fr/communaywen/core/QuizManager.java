package fr.communaywen.core;

import fr.communaywen.core.contest.ContestManager;
import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Feature("Quiz")
@Credit("ddemile")
public class QuizManager {
    public Quiz currentQuiz;
    private ScheduledExecutorService timeoutExecutor;
    private final ScheduledExecutorService executor;
    private final List<Quiz> quizzes;
    public FileConfiguration config;
    private final AywenCraftPlugin plugin;

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
            if (Bukkit.getOnlinePlayers().isEmpty()) return;

            this.timeoutExecutor = Executors.newScheduledThreadPool(1);

            currentQuiz = getRandomQuiz();

            int numberOfSpaces = Math.max(0, 23 - (currentQuiz.question.length() / 2));
            Bukkit.broadcastMessage(
                    "§8§m                                                     §r\n" +
                            "§7\n" +
                            "§6              Nouvelle question : \n§7" +
                            " ".repeat(numberOfSpaces) + currentQuiz.question + "\n" +
                            "§b     Vous avez 30 seconds pour répondre" + "\n" +
                            "§e         Le premier à répondre gagne !\n" +
                            "§7\n" +
                            "§8§m                                                     §r"
            );

            Runnable tellAnswer = () -> {
                Bukkit.broadcastMessage(

                        "§8§m                                                     §r\n" +
                                "§7\n" +
                                "§cAïe aïe aïe ! Personne n'a trouvé la réponse ... \n§7" +
                                "§7\n" +
                                "§bLa réponse était: §7" + currentQuiz.answer + "\n" +
                                "§7\n" +
                                "§8§m                                                     §r"
                );
                currentQuiz = null;
            };

            this.timeoutExecutor.schedule(tellAnswer, 30, TimeUnit.SECONDS);
        };

        executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(runnable, 1, config.getInt("interval"), TimeUnit.MINUTES);
    }

    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (currentQuiz == null) return;

        if (!event.getMessage().toLowerCase().equals(currentQuiz.answer)) return;

        int money = new Random().nextInt(config.getInt("rewards.money.min"), config.getInt("rewards.money.max"));
        int points = 10;
        // make a config file

        if (ContestManager.getInt("contest","phase") == 3) {
            Bukkit.broadcastMessage(
                    "§8§m                                                     §r\n" +
                            "§7\n" +
                            "§6Bravo à §7" + event.getPlayer().getDisplayName() + " §6qui a trouvé la réponse en premier ! \n§7" +
                            "§eLa réponse au quizz était §7" + currentQuiz.answer + ". \n§7" +
                            "§bIl remporte §7" + money + " §bde monnaie ainsi que §6" + points + "§b points pour son équipe!" + ". \n" +
                            "§7\n" +
                            "§8§m                                                     §r"
            );
            ContestManager.updateColumnInt("camps", "point_dep", points + ContestManager.getPlayerPoints(event.getPlayer()));
            ContestManager.updateColumnInt("contest", "points" + ContestManager.getPlayerCamp(event.getPlayer()), points + ContestManager.getInt("contest", "points" + ContestManager.getPlayerCamp(event.getPlayer())));
        } else {
            Bukkit.broadcastMessage(
                    "§8§m                                                     §r\n" +
                            "§7\n" +
                            "§6Bravo à §7" + event.getPlayer().getDisplayName() + " §6qui a trouvé la réponse en premier ! \n§7" +
                            "§eLa réponse au quizz était §7" + currentQuiz.answer + ". \n§7" +
                            "§bIl remporte §7" + money + " §bde monnaie !\n" +
                            "§7\n" +
                            "§8§m                                                     §r"
            );
        }

        event.setCancelled(true);
        this.plugin.getManagers().getEconomyManager().addBalance(event.getPlayer(), money);
        currentQuiz = null;
        this.timeoutExecutor.shutdownNow();
        this.timeoutExecutor = Executors.newScheduledThreadPool(1);
    }

    public Quiz getRandomQuiz() {
        boolean isPredefinedQuiz = new Random().nextInt(3) < 2;

        if (isPredefinedQuiz) {
            int index = new Random().nextInt(this.quizzes.size());

            return this.quizzes.get(index);
        } else {
            int type = new Random().nextInt(4);
            int a = new Random().nextInt(1, 10);

            return switch (type) {
                case 0 -> {
                    int b = new Random().nextInt(1, 10);
                    yield new Quiz(MessageFormat.format("Combien font {0} + {1} ?", a, b), String.valueOf(a + b));
                }
                case 1 -> {
                    int b = new Random().nextInt(1, 10);
                    yield new Quiz(MessageFormat.format("Combien font {0} * {1} ?", a, b), String.valueOf(a * b));
                }
                case 2 ->
                        new Quiz(MessageFormat.format("Quelle est la racine carrée de {0} ?", a * a), String.valueOf(a));
                case 3 -> new Quiz(MessageFormat.format("Quel est le carré de {0} ?", a), String.valueOf(a * a));
                default -> throw new IllegalStateException("Unexpected value: " + type);
            };
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
