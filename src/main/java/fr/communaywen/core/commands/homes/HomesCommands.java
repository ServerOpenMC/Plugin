package fr.communaywen.core.commands.homes;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import fr.communaywen.core.utils.constant.MessageManager;
import fr.communaywen.core.utils.constant.MessageType;
import fr.communaywen.core.utils.constant.Prefix;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.homes.Home;
import fr.communaywen.core.homes.HomeUpgradeManager;
import fr.communaywen.core.homes.HomesManagers;
import fr.communaywen.core.homes.menu.HomesMenu;
import fr.communaywen.core.homes.menu.UpgradeHomesMenu;
import revxrsal.commands.annotation.AutoComplete;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.annotation.Named;

public class HomesCommands {

    private final HomeUpgradeManager upgradeManager;
    private final HomesManagers homesManagers;

    public HomesCommands(HomeUpgradeManager upgradeManager, HomesManagers homesManagers) {
        this.upgradeManager = upgradeManager;
        this.homesManagers = homesManagers;
    }

    @Command("home")
    @Description("Gestion des homes")
    @AutoComplete("@homes *")
    public void homes(Player player, @Default("~") @Named("homes") String name) {

        if(AywenCraftPlugin.frozenPlayers.contains(player)) {
            MessageManager.sendMessageType(player, "§cTu ne peux pas te téléporter à un home car tu es gelé.", Prefix.HOME, MessageType.ERROR, true);
            return;
        }

        if(AywenCraftPlugin.getInstance().getManagers().getDisabledWorldHome().isDisabledWorld(player.getWorld())) {
            MessageManager.sendMessageType(player, "§cTu ne peux pas te téléporter à un home dans ce monde.", Prefix.HOME, MessageType.ERROR, true);
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
                        player.teleport(home.getLocation());
                        MessageManager.sendMessageType(player, "§aTéléportation au home de §e" + target.getName() + "§a: §e" + homeName + "§a...", Prefix.HOME, MessageType.SUCCESS, true);
                        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
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

                HomesMenu menu = new HomesMenu(player, homes, target.getName());
                menu.open();

                MessageManager.sendMessage(player, "§aHome de §e" + target.getName() + "§a: " + homes.stream().map(Home::getName).collect(Collectors.joining(", ")), Prefix.HOME, MessageType.INFO);
            }
        } else {
            List<Home> homes = HomesManagers.homes.stream()
                    .filter(home -> home.getPlayer().equals(player.getUniqueId().toString()))
                    .toList();

            if(name.equals("~")) {
                if(homes.isEmpty()) {
                    MessageManager.sendMessageType(player, "§cTu n'as pas de home.", Prefix.HOME, MessageType.ERROR, true);
                    return;
                }

                HomesMenu menu = new HomesMenu(player, homes, player.getName());
                menu.open();
            } else if(name.equalsIgnoreCase("upgrade")) {
                new UpgradeHomesMenu(player, upgradeManager, homesManagers).open();
                UpgradeHomesMenu menu = new UpgradeHomesMenu(player, upgradeManager, homesManagers);
                menu.open();
            } else {
                boolean homeFound = false;
                for (Home home : homes) {
                    if (home.getName().equalsIgnoreCase(name)) {
                        player.teleport(home.getLocation());
                        MessageManager.sendMessageType(player, "§aTéléportation à votre home: §e" + name + "§a...", Prefix.HOME, MessageType.SUCCESS, true);
                        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
                        homeFound = true;
                        break;
                    }
                }
                if (!homeFound) {
                    MessageManager.sendMessageType(player, "§cCe home n'existe pas.", Prefix.HOME, MessageType.ERROR, true);
                }
            }
        }
    }
}
