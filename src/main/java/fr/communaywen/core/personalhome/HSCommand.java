package fr.communaywen.core.personalhome;

import org.bukkit.block.Biome;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;

public class HSCommand {
    HomeManager manager;

    public HSCommand(HomeManager manager) {
        this.manager = manager;
    }

    @Command("maison setspawn")
    public void maisonSetHome(CommandSender sender) {
        if (!(sender instanceof Player player)) { return; }
        Home home = manager.getHomes().get(player.getUniqueId());

        if (!player.getWorld().getName().equals("homes")) {
            player.sendMessage("§cTu dois être dans ta maison pour effectuer cette commande");
            return;
        }

        home.setSpawnpoint(player.getLocation());
        if (!home.saveSpawnpoint()) {
            player.sendMessage("§cImpossible de sauvegarder le point d'apparition!");
        } else {
            player.sendMessage("§aVotre point d'apparition as été changée");
        }
    }

    @Command("maison gems biome")
    public void maisonGemsBiome(CommandSender sender) {
        if (!(sender instanceof Player player)) { return; }
        Home home = manager.getHomes().get(player.getUniqueId());

        Biome biome = player.getLocation().getBlock().getBiome();
        home.setBiome(biome);

        if (home.saveBiome()) {
            player.sendMessage("§aLe biome de votre maison as été changé en "+biome.name());
        } else {
            player.sendMessage("§cImpossible de sauvegarde le biome");
        }
    }
}
