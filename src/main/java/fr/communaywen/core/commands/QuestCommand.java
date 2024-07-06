package fr.communaywen.core.commands;

import fr.communaywen.core.quests.Quest;
import fr.communaywen.core.quests.QuestManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class QuestCommand implements CommandExecutor {

    private QuestManager questManager;

    public QuestCommand(QuestManager questManager) {
        this.questManager = questManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length == 0) {
                player.sendMessage("Utilisez /quest list pour voir toutes les quêtes disponibles.");
                return true;
            }

            if (args[0].equalsIgnoreCase("list")) {
                player.sendMessage("Quêtes disponibles:");
                for (Quest quest : questManager.getAllQuests().values()) {
                    player.sendMessage("- " + quest.getName() + ": " + quest.getDescription() + " (Récompense: " + quest.getReward() + ")");
                }
                return true;
            }

            if (args[0].equalsIgnoreCase("accept") && args.length > 1) {
                String questName = args[1];
                Quest quest = questManager.getQuest(questName);
                if (quest != null) {
                    questManager.assignQuest(player.getName(), questName);
                    player.sendMessage("Vous avez accepté la quête: " + quest.getName());
                } else {
                    player.sendMessage("Cette quête n'existe pas.");
                }
                return true;
            }
        }

        return false;
    }
}
