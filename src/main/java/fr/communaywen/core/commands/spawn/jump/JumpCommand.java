package fr.communaywen.core.commands.spawn.jump;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import fr.communaywen.core.spawn.head.HeadManager;
import fr.communaywen.core.spawn.jump.JumpManager;
import fr.communaywen.core.utils.constant.MessageManager;
import fr.communaywen.core.utils.constant.MessageType;
import fr.communaywen.core.utils.constant.Prefix;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.*;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Feature("Jump")
@Credit("iambibi_")
@Command("jump")
@Description("Permet de vous donner vos statistiques par rapport au jump")
public class JumpCommand {
    private final AywenCraftPlugin plugin;
    private JumpManager jumpManager;

    public JumpCommand(AywenCraftPlugin plugins, JumpManager manager) {
        plugin = plugins;
        jumpManager = manager;
    }

    @Subcommand("end")
    @Description("Arrete le jump que vous etes entrain de faire")
    public void end(Player player) {
        if (jumpManager.isJumping(player)) {
            jumpManager.endJump(player);
            MessageManager.sendMessageType(player, "§7Vous avez §carreter votre jump", Prefix.JUMP, MessageType.SUCCESS, true);
        } else {
            MessageManager.sendMessageType(player, "§cVous devez etre entrain de jump pour faire cela!", Prefix.JUMP, MessageType.ERROR, true);
        }
    }

    @Subcommand("move start")
    @Description("Déplace le démarrage du jump")
    @CommandPermission("ayw.command.jump.move.start")
    public void movestart(Player player) {
        if (player.getTargetBlock(null, 100).getType() == Material.HEAVY_WEIGHTED_PRESSURE_PLATE) {
            Location blockLocation = player.getTargetBlock(null, 100).getLocation();
            double posX = blockLocation.getX();
            double posY = blockLocation.getY();
            double posZ = blockLocation.getZ();
            String world = blockLocation.getWorld().getName();

            plugin.getConfig().set("jump.world", world);
            plugin.getConfig().set("jump.start.posX", posX);
            plugin.getConfig().set("jump.start.posY", posY);
            plugin.getConfig().set("jump.start.posZ", posZ);
            plugin.saveConfig();
            jumpManager.removeDisplayJumpStart();
            jumpManager.createDisplayJumpStart();
            MessageManager.sendMessageType(player, "§7Commencement du jump déplacé X=" + posX + " Y=" + posY + " Z=" + posZ + " WORLD = " + world, Prefix.JUMP, MessageType.SUCCESS, true);
        } else {
            MessageManager.sendMessageType(player, "§cVous devez viser plaque de pression de fer", Prefix.JUMP, MessageType.ERROR, true);
        }
    }

    @Subcommand("move end")
    @Description("Déplace la fin du jump")
    @CommandPermission("ayw.command.jump.move.end")
    public void moveend(Player player) {
        if (player.getTargetBlock(null, 100).getType() == Material.HEAVY_WEIGHTED_PRESSURE_PLATE) {
            Location blockLocation = player.getTargetBlock(null, 100).getLocation();
            double posX = blockLocation.getX();
            double posY = blockLocation.getY();
            double posZ = blockLocation.getZ();
            String world = blockLocation.getWorld().getName();

            plugin.getConfig().set("jump.world", world);
            plugin.getConfig().set("jump.end.posX", posX);
            plugin.getConfig().set("jump.end.posY", posY);
            plugin.getConfig().set("jump.end.posZ", posZ);
            plugin.saveConfig();
            jumpManager.removeDisplayJumpEnd();
            jumpManager.createDisplayJumpEnd();
            MessageManager.sendMessageType(player, "§7Fin du jump déplacé X=" + posX + " Y=" + posY + " Z=" + posZ + " WORLD = " + world, Prefix.JUMP, MessageType.SUCCESS, true);
        } else {
            MessageManager.sendMessageType(player, "§cVous devez viser plaque de pression de fer", Prefix.JUMP, MessageType.ERROR, true);
        }
    }
}
