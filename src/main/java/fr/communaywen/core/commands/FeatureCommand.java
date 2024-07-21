package fr.communaywen.core.commands;

import fr.communaywen.core.credit.FeatureData;
import fr.communaywen.core.credit.FeatureManager;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.*;

import java.util.List;

@Command("feature")
@Description("Get information about features")
public class FeatureCommand {

    private final FeatureManager featureManager;

    public FeatureCommand(FeatureManager featureManager) {
        this.featureManager = featureManager;
    }

    @DefaultFor("~")
    public void feature(Player player) {
        player.sendMessage("/feature <get/list/player> <feature/player>");
    }

    @Subcommand("get")
    @Description("Get information about a feature")
    public void getFeature(Player player, @Named("feature") String feature) {

    }

    @Subcommand("list")
    @Description("List all features")
    public void listFeatures(Player player) {
        List<FeatureData> features = featureManager.getFeatures();
        for (FeatureData feature : features) {
            player.sendMessage("Feature: " + feature.getFeature());
            player.sendMessage("Developers: " + String.join(", ", feature.getDevelopers()));
            if (feature.getCollaborators() != null) {
                player.sendMessage("Collaborators: " + String.join(", ", feature.getCollaborators()));
            }
            player.sendMessage("");
        }
    }

}
