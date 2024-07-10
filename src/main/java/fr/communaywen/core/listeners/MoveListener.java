package fr.communaywen.core.listeners;

import fr.communaywen.core.commands.EnglishOrSpanishCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

public class MoveListener implements Listener {
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (EnglishOrSpanishCommand.players.contains(player) && !event.getFrom().setDirection(new Vector()).equals(player.getLocation().setDirection(new Vector()))) {
            if (event.getFrom().getX() != event.getTo().getX() || event.getFrom().getY() != event.getTo().getY() || event.getFrom().getZ() != event.getTo().getZ()) {
                EnglishOrSpanishCommand.players.remove(player);
                player.kickPlayer("Tu as bougé :'(");
            }
        }
    }
}
