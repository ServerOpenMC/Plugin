package fr.communaywen.core.levels;

import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.*;
import revxrsal.commands.bukkit.annotation.CommandPermission;

@Command("level")
@Description("manage xp")
@Feature("Levels")
@Credit("Youn-T")
public class LevelsCommand {

    private final LevelsManager levelsManager;

    public LevelsCommand(LevelsManager levelsManager) {
        this.levelsManager = levelsManager;
    }

    @DefaultFor("~")
    public void defaultCommand(Player player) {
        int amount = levelsManager.getExperience(player);

        player.sendMessage("vous avez actuellement " + amount + " xp !");
    }

    @Subcommand("add")
    @CommandPermission("ayw.command.levels")
    public void add(Player player, @Named("Player") Player target, @Named("Amount") int amount) {
        levelsManager.addExperience(amount, target);
        int newAmount = levelsManager.getExperience(target);

        player.sendMessage(amount + " xp ajoutés à " + target.getName() + " , il a à présent " + newAmount);
    }

    @Subcommand("remove")
    @CommandPermission("ayw.command.levels")
    public void remove(Player player, @Named("Player") Player target, @Named("Amount") int amount) {
        levelsManager.removeExperience(amount, target);
        int newAmount = levelsManager.getExperience(target);

        player.sendMessage(amount + " xp enlevés à " + target.getName() + " , il a à présent " + newAmount + " xp");
    }

    @Subcommand("set")
    @CommandPermission("ayw.command.levels")
    public void set(Player player, @Named("Player") Player target, @Named("Amount") int amount) {
        levelsManager.setExperience(amount, target);
        int newAmount = levelsManager.getExperience(target);

        player.sendMessage("l'xp de " + target.getName() + " a été défini à " + newAmount + " xp");
    }

    @Subcommand("get")
    public void get(Player player, @Named("Player") Player target) {
        int amount = levelsManager.getExperience(target);

        player.sendMessage(target.getName() + " a actuellement " + amount + " xp !");
    }
}
