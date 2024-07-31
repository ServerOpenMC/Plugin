package fr.communaywen.core.waypoints.commands;

import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.utils.database.Waypoints;
import fr.communaywen.core.waypoints.Waypoint;
import fr.communaywen.core.waypoints.managers.WaypointManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.*;

import java.util.List;

@Command({"waypoint", "waypoints", "wp", "balise"})
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

    @Subcommand("help")
    @Description("Affiche l'utilisation de la commande")
    public void help(Player player) {
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

        if (name.length() > 32) {
            player.sendMessage("§cLe nom du waypoint ne doit pas dépasser 32 caractères (" + name.length() + ")");
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

        boolean waypointExist = Waypoints.waypointExist(player.getUniqueId().toString(), name);

        if (!waypointExist) {
            player.sendMessage("§cLe waypoint '" + name + "' n'existe pas");
            return;
        }

        boolean successState = Waypoints.removeWaypoint(player.getUniqueId().toString(), name);

        if (successState) {
            player.sendMessage("§aWaypoint '" + name + "' supprimé avec succès");
            return;
        }
        player.sendMessage("§cErreur lors de la suppression du waypoint");
    }

    @Subcommand("list")
    @Description("Liste les waypoints")
    public void list(Player player) {

        List<Waypoint> waypoints = Waypoints.getWaypoints(player.getUniqueId().toString());

        if (waypoints.isEmpty()) {
            player.sendMessage("§cVous n'avez aucun waypoint");
            return;
        }

        int nbOfWaypoints = waypoints.size();
        int maxWaypoints = waypointManager.getMaxWaypoints();

        StringBuilder message = new StringBuilder("§7---------- §6§lWaypoints (")
                .append(nbOfWaypoints)
                .append("/")
                .append(maxWaypoints)
                .append(") §7----------\n");

        for (Waypoint waypoint : waypoints) {

            message.append("§7")
                    .append(waypoints.indexOf(waypoint))
                    .append(" -§b ")
                    .append(waypoint.getName())
                    .append(" §7->§e X: ")
                    .append(waypoint.getX())
                    .append(" §7-§e Y: ")
                    .append(waypoint.getY())
                    .append(" §7-§e Z: ")
                    .append(waypoint.getZ())
                    .append(" §7-§e Monde: ")
                    .append(waypoint.getWorldName())
                    .append("\n");
        }

        player.sendMessage(message.toString());
    }

    @Subcommand("goto")
    @Description("Ré-oriente le joueur vers un waypoint")
    public void gotoWaypoint(Player player, String name) {

        Waypoint waypoint = Waypoints.getWaypoint(player.getUniqueId().toString(), name);

        if (waypoint == null) {
            player.sendMessage("§cLe waypoint '" + name + "' n'existe pas");
            return;
        }

        if (!player.getWorld().getName().equals(waypoint.getWorldName())) {
            player.sendMessage("§cVous ne vous trouvez pas dans le même monde que le waypoint");
            return;
        }

        Material blockUnderPlayer = player.getLocation().add(0, -0.5, 0).getBlock().getType();

        if (blockUnderPlayer.equals(Material.AIR)) {
            player.sendMessage("§cVous ne pouvez pas utiliser cette commande dans les airs");
            return;
        }

        Location waypointLocation = new Location(player.getWorld(), waypoint.getX(), waypoint.getY(), waypoint.getZ());
        player.teleport(player.getLocation().setDirection(waypointLocation.toVector().subtract(player.getLocation().toVector())));
    }

    @Subcommand("credits")
    @Description("Affiche les crédits")
    public void credits(Player player) {
        player.sendMessage("""
                §9[Waypoint command] §7réalisé par §6§nFnafgameur
                §7- §bDiscord§7: §6@Fnafgameur
                §7- §bGitHub§7: §6https://github.com/Fnafgameur""");
    }

    private void sendHelpMessage(Player player) {

        String message = String.format("""
                §7---------- §6§lWaypoints §7----------
                §7Utilité: §fPermet de poser des balises pour marquer des coordonnées
                §7Aliases: §b/waypoint§7, §b/wp§7, §b/balise
                §7Nombre de waypoints MAX : §b%s
                §7- §bwaypoint §eadd [nom] §7: Ajoute un waypoint
                §7- §bwaypoint §eremove [nom] §7: Supprime un waypoint
                §7- §bwaypoint §elist §7: Liste les waypoints
                §7- §bwaypoint §egoto [nom] §7: Ré-oriente la caméra vers un waypoint
                §7- §bwaypoint §ecredits §7: Affiche les crédits
                """, waypointManager.getMaxWaypoints());

        player.sendMessage(message);
    }
}
