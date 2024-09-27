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

import javax.inject.Named;


public class DelhomesCommands {

    private final HomesManagers homesManagers;
    public DelhomesCommands(HomesManagers homesManagers) {
        this.homesManagers = homesManagers;
    }

    @Command("delhome")
    @Description("Supprimer un de tes homes")
    @AutoComplete("@homes *")
    public void delhome(Player player, @Named("homes") String name) {

        if(!player.getWorld().equals(AywenCraftPlugin.getInstance().getServer().getWorlds().get(0))) {
            MessageManager.sendMessageType(player, "§cTu ne peux définir un home que dans le monde principal.", Prefix.HOME, MessageType.ERROR, true);
            return;
        }
        List<Home> homes = HomesManagers.homes.stream()
                .filter(home -> home.getPlayer().equals(player.getUniqueId().toString()))
                .filter(home -> home.getName().equalsIgnoreCase(name))
                .toList();

        boolean homeFound = false;
        for(Home home : homes) {
            if(home.getPlayer().equalsIgnoreCase(player.getUniqueId().toString())) {
                homesManagers.removeHome(home);
                MessageManager.sendMessageType(player, "§aHome §e" + home.getName() + " §asupprimé avec succès !", Prefix.HOME, MessageType.SUCCESS, true);
                homeFound = true;
                return;
            }
        }

        if(!homeFound) {
            MessageManager.sendMessageType(player, "§cCe home n'existe pas.", Prefix.HOME, MessageType.ERROR, true);
        }
    }

}
