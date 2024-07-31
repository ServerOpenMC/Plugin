package fr.communaywen.core.waypoints.commands;

import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.utils.database.Waypoints;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.annotation.Subcommand;

import java.util.List;

@Command({"waypoint", "wp", "balise"})
@Credit("Fnafgameur")
public class WaypointCommand {

    private final WaypointManager waypointManager;

    public WaypointCommand(WaypointManager waypointManager) {
        this.waypointManager = waypointManager;
    }

    @DefaultFor("~")
    @Description("Gestion des waypoints")
    public void onCommand(Player player) {
        sendHelpMessage(player);
    }

    @Subcommand("add")
    @Description("Ajoute un waypoint")
    public void add(Player player, String name) {

        int nbOfWaypoints = Waypoints.getWaypointsCount(player.getUniqueId().toString());

        if (nbOfWaypoints >= waypointManager.getMaxWaypoints()) {
            player.sendMessage("§cVous avez atteint le nombre maximum de waypoints (" + waypointManager.getMaxWaypoints() + ")");
            return;
        }

        name = name.replace(" ", "_");

        if (Waypoints.waypointExist(player.getUniqueId().toString(), name)) {
            player.sendMessage("§cUn waypoint avec ce nom existe déjà");
            return;
        }

        boolean successState = Waypoints.addWaypoint(player.getUniqueId().toString(), name, player.getLocation());

        if (successState) {
            player.sendMessage("§aWaypoint '" + name + "' ajouté avec succès");
            return;
        }
        player.sendMessage("§cErreur lors de l'ajout du waypoint");
    }

    @Subcommand("remove")
    @Description("Supprime un waypoint")
    public void remove(Player player, String name) {

        boolean successState = Waypoints.removeWaypoint(player.getUniqueId().toString(), name);

        if (successState) {
            player.sendMessage("§aWaypoint '" + name + "' supprimé avec succès");
            return;
        }
        player.sendMessage("§cErreur lors de la suppression du waypoint, avez-vous bien écrit le nom ?");
    }

    @Subcommand("list")
    @Description("Liste les waypoints")
    public void list(Player player) {
        List<String> waypoints = Waypoints.getWaypoints(player.getUniqueId().toString());

        if (waypoints.isEmpty()) {
            player.sendMessage("§cVous n'avez aucun waypoint");
            return;
        }

        StringBuilder message = new StringBuilder("§7---------- §6§lWaypoints §7----------\n");

        for (String waypoint : waypoints) {

            message.append("§7")
                    .append(waypoints.indexOf(waypoint))
                    .append(" -§e ")
                    .append(waypoint)
                    .append("\n");
        }

        player.sendMessage(message.toString());
    }

    private void sendHelpMessage(Player player) {

        String message = """
                §7---------- §6§lWaypoints §7----------
                §7Utilisation: /waypoint <add|remove|list>
                §7- §eadd [nom] §7: Ajoute un waypoint
                §7- §eremove [nom] §7: Supprime un waypoint
                §7- §elist §7: Liste les waypoints""";

        player.sendMessage(message);
    }
}
