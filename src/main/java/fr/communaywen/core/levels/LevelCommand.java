package fr.communaywen.core.levels;

import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.annotation.Named;
import revxrsal.commands.annotation.Subcommand;

@Command("level")
@Description("manage xp")
public class LevelCommand {

    private final ExperienceManager experienceManager;

    public LevelCommand(ExperienceManager experienceManager) { this.experienceManager = experienceManager; }

    @Subcommand("add")
    public void add(Player player, @Named("Player") Player target, @Named("Amount") int amount) {

        experienceManager.addExperience(amount,target);
    }

    @Subcommand("remove")
    public void remove(Player player, @Named("Player") Player target, @Named("Amount") int amount) {
        experienceManager.removeExperience(amount,target);
    }

    @Subcommand("set")
    public void set(Player player, @Named("Player") Player target, @Named("Amount") int amount) {
        experienceManager.setExperience(amount,target);
    }
}
