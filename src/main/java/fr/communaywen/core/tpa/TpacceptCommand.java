package fr.communaywen.core.tpa;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.spawn.jump.JumpManager;
import fr.communaywen.core.utils.constant.MessageManager;
import fr.communaywen.core.utils.constant.MessageType;
import fr.communaywen.core.utils.constant.Prefix;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
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
            MessageManager.sendMessageType(player, "§cVous n'avez pas de demande de téléportation.", Prefix.TPA, MessageType.ERROR, true);
            return;
        }

        if (player.getWorld().getName().equals("dreamworld")) {
            MessageManager.sendMessageType(player, "§cVous ne pouvez pas vous téléportez dans un rêve", Prefix.TPA, MessageType.ERROR, true);
            return;
        }

        if(JumpManager.isJumping(requester)) {
            MessageManager.sendMessageType(player, "§cLa personne concerné est en Jump, impossible de le tp", Prefix.TPA, MessageType.ERROR, true);
            return;
        }

        tpQueue.removeRequest(player);

        MessageManager.sendMessageType(player, "§a" + requester.getName() + " sera téléporté à vous dans 5 secondes. Ne bougez pas !", Prefix.TPA, MessageType.INFO, true);
        MessageManager.sendMessageType(requester, "§aVous serez téléporté à " + player.getName() + " dans 5 secondes. Ne bougez pas !", Prefix.TPA, MessageType.INFO, true);

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
                MessageManager.sendMessageType(requester, "§aTéléportation réussie !", Prefix.TPA, MessageType.SUCCESS, true);
                MessageManager.sendMessageType(target, "§a" + requester.getName() + " a été téléporté à vous.", Prefix.TPA, MessageType.SUCCESS, true);
                cancel();
                return;
            }

            if (requesterHasMoved()) {
                MessageManager.sendMessageType(requester, "§cTéléportation annulée car vous avez bougé.", Prefix.TPA, MessageType.ERROR, true);
                MessageManager.sendMessageType(target, "§cTéléportation de " + requester.getName() + " annulée car il a bougé.", Prefix.TPA, MessageType.ERROR, true);
                cancel();
                return;
            }

            if(JumpManager.isJumping(requester)) {
                MessageManager.sendMessageType(requester, "§cLe destinataire est en Jump, impossible de vous tp", Prefix.TPA, MessageType.ERROR, true);
                cancel();
                return;
            }

            if(JumpManager.isJumping(target)) {
                MessageManager.sendMessageType(target, "§cVous êtes en Jump, impossible de vous tp", Prefix.TPA, MessageType.ERROR, true);
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

