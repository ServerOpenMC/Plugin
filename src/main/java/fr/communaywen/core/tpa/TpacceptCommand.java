package fr.communaywen.core.tpa;

import fr.communaywen.core.AywenCraftPlugin;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.bukkit.annotation.CommandPermission;

public class TpacceptCommand {
    private final TPAQueue tpQueue = TPAQueue.INSTANCE;
    private final AywenCraftPlugin plugin;

    public TpacceptCommand(AywenCraftPlugin plugin) {
        this.plugin = plugin;
    }

    @Command("tpaccept")
    @CommandPermission("ayw.command.tpa")
    public void onCommand(Player player) {
        Player requester = tpQueue.getRequester(player);
        if (requester == null) {
            player.sendMessage("§cVous n'avez pas de demande de téléportation.");
            return;
        }

        if (player.getWorld().getName().equals("dreamworld")) {
            player.sendMessage("§cVous ne pouvez pas vous téléportez dans un rêve");
            return;
        }

        tpQueue.removeRequest(player);

        player.sendMessage("§a" + requester.getName() + " sera téléporté à vous dans 5 secondes. Ne bougez pas !");
        requester.sendMessage("§aVous serez téléporté à " + player.getName() + " dans 5 secondes. Ne bougez pas !");

        new TeleportCountdown(plugin, requester, player).runTaskTimer(plugin, 0, 20);
    }

    private static class TeleportCountdown extends BukkitRunnable {
        private final AywenCraftPlugin plugin;
        private final Player requester;
        private final Player target;
        private int countdown = 5;
        private final double requesterInitialX;
        private final double requesterInitialY;
        private final double requesterInitialZ;

        TeleportCountdown(AywenCraftPlugin plugin, Player requester, Player target) {
            this.plugin = plugin;
            this.requester = requester;
            this.target = target;
            this.requesterInitialX = requester.getLocation().getX();
            this.requesterInitialY = requester.getLocation().getY();
            this.requesterInitialZ = requester.getLocation().getZ();
        }

        @Override
        public void run() {
            if (countdown <= 0) {
                requester.teleport(target);
                requester.sendMessage("§aTéléportation réussie !");
                target.sendMessage("§a" + requester.getName() + " a été téléporté à vous.");
                cancel();
                return;
            }

            if (requesterHasMoved()) {
                requester.sendMessage("§cTéléportation annulée car vous avez bougé.");
                target.sendMessage("§cTéléportation de " + requester.getName() + " annulée car il a bougé.");
                cancel();
                return;
            }

            countdown--;
        }

        private boolean requesterHasMoved() {
            return requester.getLocation().getX() != requesterInitialX ||
                    requester.getLocation().getY() != requesterInitialY ||
                    requester.getLocation().getZ() != requesterInitialZ;
        }
    }
}

