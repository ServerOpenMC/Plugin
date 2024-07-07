package fr.communaywen.core.commands;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.utils.PermissionCategory;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Description;

public class VersionCommand {

    public static final @NotNull String PERMISSION = PermissionCategory.ADMIN.formatPermission("version");

    private final AywenCraftPlugin plugin;

    public VersionCommand(AywenCraftPlugin plugin) {
        this.plugin = plugin;
    }

    @Command("version")
    @Description("Affiche la version du plugin")
    public void onCommand(Player player) {

        if (!player.hasPermission(PERMISSION)) {
            player.sendMessage("Vous n'avez pas la permission d'utiliser cette commande.");
            return;
        }
        player.sendMessage("Version du plugin : " + plugin.getDescription().getVersion());
    }
}
