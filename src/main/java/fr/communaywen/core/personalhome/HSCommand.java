package fr.communaywen.core.personalhome;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Named;
import revxrsal.commands.bukkit.annotation.CommandPermission;

public class HSCommand {
    HomeManager manager;

    public HSCommand(HomeManager manager) {
        this.manager = manager;
    }

    @Command("maison visit")
    @CommandPermission("ayw.maisons.visit")
    public void maisonVisit(CommandSender sender, @Named("player") OfflinePlayer target) {
        if (!(sender instanceof Player player)) { return; }
        Home home = manager.getHomes().get(target.getUniqueId());

        if (home == null) {
            player.sendMessage(Component.text("§cImpossible de trouver la maison de "+target.getName()));
            return;
        }

        if (!player.getWorld().getName().equals("homes")) {
            player.sendMessage("§cTu dois être dans ton sac pour visiter une maison");
            return;
        }

        if (home.canVisit()) {
            player.teleport(home.getSpawnpoint());
        } else {
            player.sendMessage("§cCe joueur n'accepte pas les visites");
        }
    }

    @Command("maison allowvisits")
    public void maisonAllowVisit(CommandSender sender, @Named("state") boolean state) {
        if (!(sender instanceof Player player)) { return; }

        Home home = manager.getHomes().get(player.getUniqueId());

        if (home == null) {
            sender.sendMessage("Impossible de trouver votre maison");
            return;
        }

        String stateString = "activé";

        if (!state) {
            stateString = "désactivé";
        }

        if (home.allow_visit == state) {
            player.sendMessage("Les visites sont déjà "+stateString);
            return;
        }

        if (home.setVisit(state)) {
            player.sendMessage("Les visites ont été "+stateString);
        } else {
            if (state) {
                player.sendMessage("§cImpossible d'activer les visites");
            } else {
                player.sendMessage("§cImpossible de désactiver les visites");
            }
        }
    }

    @Command("maison find")
    @CommandPermission("ayw.maisons.locate")
    public void maisonFind(CommandSender sender, @Named("player") OfflinePlayer target) {
        Home home = manager.getHomes().get(target.getUniqueId());

        if (home == null) {
            sender.sendMessage("Impossible de trouver une maison pour "+target.getName());
            return;
        }
        Block spawn = home.getSpawnpoint().getBlock();
        int x = spawn.getX();
        int y = spawn.getY();
        int z = spawn.getZ();
        int originX = (home.getId()-1)*208;
        originX = originX-16;

        sender.sendMessage("§6Maison de "+target.getName()+" (§4"+home.getId()+"§6)");
        sender.sendMessage(Component.text("§5Coords:§r ").append(Component.text(+x+" "+y+" "+z).clickEvent(ClickEvent.runCommand("/minecraft:tp @s "+x+" "+y+" "+z)).hoverEvent(HoverEvent.showText(Component.text("Cliquer pour s'y téléporté")))));
        sender.sendMessage(Component.text("§dOrigin: §r").append(Component.text(+originX+" 101 ").clickEvent(ClickEvent.runCommand("/minecraft:tp @s "+x+".0 101 0.0")).hoverEvent(HoverEvent.showText(Component.text("Cliquer pour s'y téléporté")))));
        sender.sendMessage("§2Biome: §a"+home.getBiome().name());
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

    @Command("maison setbiome")
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
