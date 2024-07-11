package fr.communaywen.core.levels;

import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.annotation.Named;
import revxrsal.commands.annotation.Subcommand;

@Command("level")
@Description("manage xp")
public class LevelsCommand {

    private final LevelsManager levelsManager;

    public LevelsCommand(LevelsManager levelsManager) {
        this.levelsManager = levelsManager;
    }

    @Subcommand("add")
    public void add(Player player, @Named("Player") Player target, @Named("Amount") int amount) {

        levelsManager.addExperience(amount, target);
    }

    @Subcommand("remove")
    public void remove(Player player, @Named("Player") Player target, @Named("Amount") int amount) {
        levelsManager.removeExperience(amount, target);
    }

    @Subcommand("set")
    public void set(Player player, @Named("Player") Player target, @Named("Amount") int amount) {
        levelsManager.setExperience(amount, target);
    }

    @Subcommand("get")
    public void get(Player player, @Named("Player") Player target) {
        int amount = levelsManager.getExperience(target);

        player.sendMessage(target.getName() + " a actuellement " + amount + " xp !");
    }
}
