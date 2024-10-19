package fr.communaywen.core.commands.spawn.head;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import fr.communaywen.core.spawn.head.HeadManager;
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


@Feature("Heads")
@Credit("iambibi_")
@Command("heads")
@Description("Permet de vous donner les statistiques par rapport au tete")
public class HeadCommand {
    private final AywenCraftPlugin plugin;

    public HeadCommand(AywenCraftPlugin plugins) {
        plugin = plugins;
    }

    @Cooldown(4)
    @DefaultFor("~")
    public void defaultCommand(Player player) {
        MessageManager.sendMessageType(player, "§7Vous avez trouvé §d" + HeadManager.getNumberHeads(player) + " §8tête(s) sur §d" + HeadManager.getMaxHeads() + " §8têtes", Prefix.HEAD, MessageType.SUCCESS, true);
    }

    @Subcommand("add")
    @Description("Ajoute une tete")
    @CommandPermission("ayw.command.heads.add")
    public void add(Player player, Integer headId) {
        List<Map<?, ?>> headList = plugin.getConfig().getMapList("head.list");
        Boolean canPass = true;

        for (Map<?, ?> head : headList) {
            if (head.containsKey("id") && (int) head.get("id") == headId) {
                canPass = false;
                MessageManager.sendMessageType(player, "§cCet id est déja utilisé", Prefix.HEAD, MessageType.ERROR, true);
                break;
            }
        }

        if (canPass && (player.getTargetBlock(null, 100).getType() == Material.PLAYER_HEAD || player.getTargetBlock(null, 100).getType() == Material.PLAYER_WALL_HEAD)) {
            Location blockLocation = player.getTargetBlock(null, 100).getLocation();
            double posX = blockLocation.getX();
            double posY = blockLocation.getY();
            double posZ = blockLocation.getZ();
            String world = blockLocation.getWorld().getName();

            Map<String, Object> newHead = new HashMap<>();
            newHead.put("id", headId);
            newHead.put("posX", posX);
            newHead.put("posY", posY);
            newHead.put("posZ", posZ);

            headList.add(newHead);
            plugin.getConfig().set("head.list", headList);
            plugin.saveConfig();
            MessageManager.sendMessageType(player, "§7Tête ajoutée avec succès X=" + posX + " Y=" + posY + " Z=" + posZ + " WORLD = " + world, Prefix.HEAD, MessageType.SUCCESS, true);
        } else {
            MessageManager.sendMessageType(player, "§cVous devez viser une tête", Prefix.HEAD, MessageType.ERROR, true);
        }
    }
}
