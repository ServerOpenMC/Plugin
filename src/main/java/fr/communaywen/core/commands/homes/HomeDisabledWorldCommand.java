package fr.communaywen.core.commands.homes;

import fr.communaywen.core.homes.world.DisabledWorldHome;
import fr.communaywen.core.utils.constant.MessageManager;
import fr.communaywen.core.utils.constant.MessageType;
import fr.communaywen.core.utils.constant.Prefix;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.*;
import revxrsal.commands.bukkit.annotation.CommandPermission;

@Command("homeworld")
@CommandPermission("ayw.home.admin.world")
public class HomeDisabledWorldCommand {

    private final DisabledWorldHome disabledWorldHome;
    public HomeDisabledWorldCommand(DisabledWorldHome disabledWorldHome) {
        this.disabledWorldHome = disabledWorldHome;
    }

    @Subcommand("add")
    @Description("Set the world where homes are disabled")
    @CommandPermission("ayw.home.admin.world.add")
    @AutoComplete("@homeWorldsAdd *")
    public void setHomeDisabledWorld(Player player, String worldName) {
        World world = Bukkit.getWorld(worldName);
        if(world == null) {
            MessageManager.sendMessageType(player, "§cCe monde n'existe pas.", Prefix.HOME, MessageType.ERROR, true);
            return;
        }

        if(disabledWorldHome.isDisabledWorld(world)) {
            MessageManager.sendMessageType(player, "§cLe monde §e" + world.getName() + " §cest déjà dans la liste des mondes où les homes sont désactivés.", Prefix.HOME, MessageType.ERROR, true);
            return;
        }

        disabledWorldHome.addDisabledWorld(world, player);
        MessageManager.sendMessageType(player, "§aLe monde §e" + world.getName() + " §aa été ajouté à la liste des mondes où les homes sont §cdésactivés.", Prefix.HOME, MessageType.SUCCESS, true);
    }

    @Subcommand("remove")
    @Description("Remove the world where homes are disabled")
    @CommandPermission("ayw.home.admin.world.remove")
    @AutoComplete("@homeWorldsRemove *")
    public void removeWorld(Player player, String worldName) {
        World world = Bukkit.getWorld(worldName);
        if(world == null) {
            MessageManager.sendMessageType(player, "§cLe monde §e" + worldName + " §cn'existe pas.", Prefix.HOME, MessageType.ERROR, true);
            return;
        }

        if(disabledWorldHome.isDisabledWorld(world)) {
            disabledWorldHome.removeDisabledWorld(world);
            MessageManager.sendMessageType(player, "§aLe monde §e" + world.getName() + " §aa été retiré de la liste des mondes où les homes sont §cdésactivés.", Prefix.HOME, MessageType.SUCCESS, true);
        } else {
            MessageManager.sendMessageType(player, "§cLe monde §e " + world.getName() + " §cn'est pas dans la liste des mondes où les homes sont désactivés.", Prefix.HOME, MessageType.ERROR, true);
        }
    }

    @Subcommand("list")
    @Description("List the worlds where homes are disabled")
    @CommandPermission("ayw.home.admin.world.list")
    public void listWorlds(Player player) {
        MessageManager.sendMessage(player, "§eListe des mondes où les homes sont désactivés :", Prefix.HOME, MessageType.INFO);
        disabledWorldHome.getDisabledWorlds().forEach(worldName1 -> player.sendMessage("    §8- §e" + worldName1 + " §8(" + disabledWorldHome.getDisabledWorldInfo(worldName1) + "§8)"));
    }
}