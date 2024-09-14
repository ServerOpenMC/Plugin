package fr.communaywen.core.personalhome;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;

public class HSCommand {
    HomeManager manager;

    public HSCommand(HomeManager manager) {
        this.manager = manager;
    }

    @Command("maison sethome")
    public void maisonSetHome(CommandSender sender) {
        if (!(sender instanceof Player player)) { return; }
        Home home = manager.getHomes().get(player.getUniqueId());

        if (!player.getWorld().getName().equals("homes") || HomesUtils.isOnPlatform(player.getLocation(), home.getId())) {
            player.sendMessage("§cTu doit être dans ta maison pour effectuer cette commande");
            return;
        }

        home.setSpawnpoint(player.getLocation());
        if (!home.saveSpawnpoint()) {
            player.sendMessage("§cImpossible de sauvegarder le point d'apparition!");
        } else {
            player.sendMessage("§aVotre point d'apparition as été changée");
        }
    }
}
