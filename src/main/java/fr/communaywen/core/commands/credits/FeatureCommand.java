package fr.communaywen.core.commands.credits;

import fr.communaywen.core.credit.FeatureData;
import fr.communaywen.core.credit.FeatureManager;
import fr.communaywen.core.utils.CommandUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.*;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Command("feature")
@Description("Get information about features")
@CommandPermission("ayw.command.feature")
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
        CommandUtils.setSender(player);
        FeatureData featureData = featureManager.getFeature(feature);
        if (featureData == null) {
            CommandUtils.sendMessage(player, "Cette feature n'existe pas", true);
            return;
        }
        sendFeatureInfo(player, featureData);
    }

    @Subcommand("list")
    @Description("List all features")
    public void listFeatures(Player player) {
        CommandUtils.setSender(player);
        List<FeatureData> features = featureManager.getFeatures();
        for (FeatureData feature : features) {
            sendFeatureInfo(player, feature);
        }
    }

    @Subcommand("player")
    @Description("List all features of a player")
    public void playerFeatures(Player player, @Named("player") String target) {
        CommandUtils.setSender(player);
        CommandUtils.sendMessage(ChatColor.GOLD, "=====================", true);
        CommandUtils.sendMessage(ChatColor.GOLD, "Features de " + ChatColor.LIGHT_PURPLE + player.getName() + " :", true);
        for (FeatureData feature : featureManager.getFeatures()) {
            List<FeatureData> playerFeatures = new ArrayList<>();
            if (isDeveloper(target, feature)) {
                playerFeatures.add(feature);
            }
            if (isCollaborator(target, feature)) {
                playerFeatures.add(feature);
            }
            Collections.shuffle(playerFeatures);
            for (FeatureData playerFeature : playerFeatures) {
                if (isDeveloper(target, playerFeature)) {
                    CommandUtils.sendMessage(ChatColor.LIGHT_PURPLE, "DEV - " + playerFeature.getFeature(), true);
                } else {
                    CommandUtils.sendMessage(ChatColor.GRAY, "COL - " + playerFeature.getFeature(), false);
                }
            }
        }
    }

    private void sendFeatureInfo(Player player, FeatureData feature) {
        CommandUtils.sendMessage(ChatColor.GOLD, "=====================", true);
        CommandUtils.sendMessage(ChatColor.GOLD, "Feature : " + feature.getFeature(), true);
        CommandUtils.sendMessage(ChatColor.GOLD, "Par :", true);
        for (String credit : feature.getDevelopers()) {
            CommandUtils.sendMessage(ChatColor.LIGHT_PURPLE, " - " + credit, true);
        }
        if (feature.getCollaborators() != null) {
            CommandUtils.sendMessage(ChatColor.GRAY, "Mentions spéciales : ", false);
            for (String collaborator : feature.getCollaborators()) {
                CommandUtils.sendMessage(ChatColor.GRAY, collaborator, false);
            }
        }
    }

    private boolean isDeveloper(String target, FeatureData feature) {
        for (String developer : feature.getDevelopers()) {
            if (developer.equalsIgnoreCase(target)) {
                return true;
            }
        }
        return false;
    }

    private boolean isCollaborator(String target, FeatureData feature) {
        if (feature.getCollaborators() == null) {
            return false;
        }
        for (String collaborator : feature.getCollaborators()) {
            if (collaborator.equalsIgnoreCase(target)) {
                return true;
            }
        }
        return false;
    }

}
