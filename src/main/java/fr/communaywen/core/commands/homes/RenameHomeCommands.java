package fr.communaywen.core.commands.homes;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.homes.Home;
import fr.communaywen.core.homes.HomesManagers;
import fr.communaywen.core.utils.constant.MessageManager;
import fr.communaywen.core.utils.constant.MessageType;
import fr.communaywen.core.utils.constant.Prefix;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.AutoComplete;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Description;

import java.util.List;

public class RenameHomeCommands {

    private final HomesManagers homesManager;
    public RenameHomeCommands(HomesManagers homesManager) {
        this.homesManager = homesManager;
    }

    @Command("renamehome")
    @Description("Renommer un home")
    @AutoComplete("@homes *")
    public void renamehome(Player player, String home, String newName)  {

        if(!player.getWorld().equals(AywenCraftPlugin.getInstance().getServer().getWorlds().get(0))) {
            MessageManager.sendMessageType(player, "§cTu ne peux renommer un home que dans le monde principal.", Prefix.HOME, MessageType.ERROR, true);
            return;
        }
        if (newName.length() < 3) {
            MessageManager.sendMessageType(player, "§cLe nom de ton home doit contenir au moins 3 caractères.", Prefix.HOME, MessageType.ERROR, true);
            return;
        }
        if (newName.length() > 10) {
            MessageManager.sendMessageType(player, "§cLe nom de ton home ne doit pas dépasser 10 caractères.", Prefix.HOME, MessageType.ERROR, true);
            return;
        }
        if (!newName.matches("[a-zA-Z0-9]+")) {
            MessageManager.sendMessageType(player, "§cLe nom de ton home ne doit pas contenir de caractères spéciaux.", Prefix.HOME, MessageType.ERROR, true);
            return;
        }

        if (newName.equalsIgnoreCase("upgrade")) {
            MessageManager.sendMessageType(player, "§cTu ne peux pas nommer ton home comme ça.", Prefix.HOME, MessageType.ERROR, true);
            return;
        }

        List<Home> homes = HomesManagers.homes.stream()
                .filter(h -> h.getPlayer().equals(player.getUniqueId().toString()))
                .filter(h -> h.getName().equalsIgnoreCase(home))
                .toList();

        for(Home h : homes) {
            if (h.getName().equalsIgnoreCase(newName)) {
                MessageManager.sendMessageType(player, "§cTu as déjà un home avec ce nom.", Prefix.HOME, MessageType.ERROR, true);
                return;
            }
            if(h.getPlayer().equalsIgnoreCase(player.getUniqueId().toString())) {
                MessageManager.sendMessageType(player, "§aHome §e" + h.getName() + " §arenommé en §e" + newName + " §aavec succès !", Prefix.HOME, MessageType.SUCCESS, true);
                homesManager.renameHome(h, newName);
                return;
            }
        }

        MessageManager.sendMessageType(player, "§cCe home n'existe pas.", Prefix.HOME, MessageType.ERROR, true);
    }

}
