package fr.communaywen.core.commands.teams;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.claim.ClaimListener;
import fr.communaywen.core.claim.ClaimMenu;
import fr.communaywen.core.teams.Team;
import fr.communaywen.core.teams.TeamManager;
import fr.communaywen.core.utils.CommandUtils;
import fr.communaywen.core.utils.constant.MessageManager;
import fr.communaywen.core.utils.constant.MessageType;
import fr.communaywen.core.utils.constant.Prefix;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.annotation.CommandPermission;

@Command("team claim")
public class TeamClaim {

    TeamManager teamManager = AywenCraftPlugin.getInstance().getManagers().getTeamManager();

    @Subcommand("add")
    @Description("Donne un baton de claim")
    public void claimCmd(Player player) {
        Team team = teamManager.getTeamByPlayer(player.getUniqueId());
        if (team == null || !team.isIn(player.getUniqueId())) {
            CommandUtils.sendMessage(player, "Vous n'êtes pas dans une team !", true);
            return;
        }

        if (player.getInventory().firstEmpty() == -1) {
            MessageManager.sendMessageType(player, "§cVotre inventaire est plein !", Prefix.CLAIM, MessageType.ERROR, true);
            return;
        }

        if(ClaimListener.getClaimStick(player)) {
            MessageManager.sendMessageType(player, "§cVous avez déjà un claim stick !", Prefix.CLAIM, MessageType.ERROR, true);
            return;
        }

        if(team.giveClaimStick(player)) {
            MessageManager.sendMessageType(player, "§aVous avez reçu un claim stick !", Prefix.CLAIM, MessageType.SUCCESS, true);
        } else {
            MessageManager.sendMessageType(player, "§4Une erreur est survenue !", Prefix.CLAIM, MessageType.ERROR, true);
        }

    }

    @Subcommand("list")
    @Description("Liste des claims de la team")
    public void claimList(Player player) {
        Team team = teamManager.getTeamByPlayer(player.getUniqueId());
        if (team == null || !team.isIn(player.getUniqueId())) {
            CommandUtils.sendMessage(player, "Vous n'êtes pas dans une team !", true);
            return;
        }

        new ClaimMenu(player, team).open();
    }

    @Subcommand("bypass")
    @Description("Bypass les claims")
    @CommandPermission("ayw.claims.bypass")
    public void claimBypass(Player player) {
        if(player.hasPermission("ayw.claims.bypass")) {
            if(AywenCraftPlugin.playerClaimsByPass.contains(player)) {
                AywenCraftPlugin.playerClaimsByPass.remove(player);
                MessageManager.sendMessageType(player, "§aVous n'êtes plus en bypass des claims de teams !", Prefix.CLAIM, MessageType.SUCCESS, true);
            } else {
                AywenCraftPlugin.playerClaimsByPass.add(player);
                MessageManager.sendMessageType(player, "§aVous êtes maintenant en bypass des claims de teams !", Prefix.CLAIM, MessageType.SUCCESS, true);
            }
        } else {
            MessageManager.sendMessageType(player, "§cVous n'avez pas la permission d'utiliser cette commande.", Prefix.CLAIM, MessageType.ERROR, true);
        }
    }

}
