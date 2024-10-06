package fr.communaywen.core.commands.homes;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.homes.Home;
import fr.communaywen.core.homes.HomesManagers;
import fr.communaywen.core.utils.constant.MessageManager;
import fr.communaywen.core.utils.constant.MessageType;
import fr.communaywen.core.utils.constant.Prefix;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.AutoComplete;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Description;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

        if(AywenCraftPlugin.getInstance().getManagers().getDisabledWorldHome().isDisabledWorld(player.getWorld())) {
            MessageManager.sendMessageType(player, "§cTu ne peux pas supprimer un home dans ce monde.", Prefix.HOME, MessageType.ERROR, true);
            return;
        }

        if (name.contains(":") && player.hasPermission("ayw.home.teleport.others")) {
            String[] parts = name.split(":");
            OfflinePlayer target = Bukkit.getOfflinePlayer(parts[0]);

            if (!target.hasPlayedBefore()) {
                MessageManager.sendMessageType(player, "§cLe joueur spécifié n'existe pas.", Prefix.HOME, MessageType.ERROR, true);
                return;
            }

            if (parts.length == 2) {
                String homeName = parts[1];

                if (target == null || !target.hasPlayedBefore()) {
                    MessageManager.sendMessageType(player, "§cLe joueur spécifié n'existe pas.", Prefix.HOME, MessageType.ERROR, true);
                    return;
                }

                UUID targetPlayerUUID = target.getUniqueId();
                List<Home> targetHomes = HomesManagers.homes.stream()
                        .filter(home -> home.getPlayer().equals(targetPlayerUUID.toString()))
                        .toList();

                for (Home home : targetHomes) {
                    if (home.getName().equalsIgnoreCase(homeName)) {
                        homesManagers.removeHome(home);
                        MessageManager.sendMessageType(player, "§aLe home de §e" + target.getName() + " avec le nom " + home.getName() + " §aà été supprimé avec succès ! ", Prefix.HOME, MessageType.SUCCESS, true);
                        return;
                    }
                }

                MessageManager.sendMessageType(player, "§cCe home n'existe pas pour le joueur spécifié.", Prefix.HOME, MessageType.ERROR, true);
            } else {
                List<Home> homes = HomesManagers.homes.stream()
                        .filter(home -> home.getPlayer().equals(target.getUniqueId().toString()))
                        .toList();

                if(homes.isEmpty()) {
                    MessageManager.sendMessageType(player, "§cLe joueur " + target.getName() + " n'a pas de home.", Prefix.HOME, MessageType.ERROR, true);
                    return;
                }

                MessageManager.sendMessage(player, "§aHome de §e" + target.getName() + "§a: " + homes.stream().map(Home::getName).collect(Collectors.joining(", ")), Prefix.HOME, MessageType.INFO);
            }
        } else {
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

}
