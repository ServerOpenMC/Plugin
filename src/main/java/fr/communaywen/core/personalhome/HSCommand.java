package fr.communaywen.core.personalhome;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.node.Node;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.SecretCommand;

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

    @Command("maison bypass")
    @SecretCommand
    public void maisonBypass(CommandSender sender) {
        if (!(sender instanceof Player player)) { return; }
        LuckPerms api = LuckPermsProvider.get();
        if (player.hasPermission("ayw.maisons.canbypass")) {
            if (player.hasPermission("ayw.maisons.bypass")) {
                api.getUserManager().modifyUser(player.getUniqueId(), user -> user.data().remove(Node.builder("ayw.maisons.bypass").build()));
                player.sendMessage("§aTu as désactivé le bypass des maisons");
            } else {
                api.getUserManager().modifyUser(player.getUniqueId(), user -> user.data().add(Node.builder("ayw.maisons.bypass").build()));
                player.sendMessage("§aTu as activé le bypass des maisons");
            }
        } else {
            player.sendMessage("§cTu dois spécifier une sous-commande !");
        }
    }

    @Command("maison gems biome")
    public void maisonGemsBiome(CommandSender sender) {
        if (!(sender instanceof Player player)) { return; }
        Home home = manager.getHomes().get(player.getUniqueId());

        home.setBiome(player.getLocation().getBlock().getBiome());

        if (home.saveBiome()) {
            player.sendMessage("§aLe biome de votre maison as été changé!");
        } else {
            player.sendMessage("§cImpossible de sauvegarde le biome");
        }
    }
}
