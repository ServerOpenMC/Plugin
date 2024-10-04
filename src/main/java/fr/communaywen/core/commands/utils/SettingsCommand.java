package fr.communaywen.core.commands.utils;


import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import fr.communaywen.core.settings.SettingsManager;
import fr.communaywen.core.settings.SettingsMenu;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.annotation.Optional;
import revxrsal.commands.bukkit.annotation.CommandPermission;

@Feature("Settings")
@Credit("gab400")
public class SettingsCommand {
	
	AywenCraftPlugin plugin;
	
	public SettingsCommand(AywenCraftPlugin plugin) {
		this.plugin = plugin;
	}
	
	@Command("settings")
	@Description("Affiche la profile de la personne selectionnée")
	@CommandPermission("ayw.command.settings")
	public void onCommand(Player player) {
		SettingsMenu menu = new SettingsMenu(plugin, player, new SettingsManager(plugin));
		menu.open();
	}
	
}
