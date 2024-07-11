package fr.communaywen.core.levels;

import org.bukkit.entity.Player;
import revxrsal.commands.annotation.*;
import revxrsal.commands.bukkit.annotation.CommandPermission;

@Command("level")
@Description("manage xp")
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
    }

    @Subcommand("remove")
    @CommandPermission("ayw.command.levels")
    public void remove(Player player, @Named("Player") Player target, @Named("Amount") int amount) {
        levelsManager.removeExperience(amount, target);
    }

    @Subcommand("set")
    @CommandPermission("ayw.command.levels")
    public void set(Player player, @Named("Player") Player target, @Named("Amount") int amount) {
        levelsManager.setExperience(amount, target);
    }

    @Subcommand("get")
    public void get(Player player, @Named("Player") Player target) {
        int amount = levelsManager.getExperience(target);

        player.sendMessage(target.getName() + " a actuellement " + amount + " xp !");
    }
}
