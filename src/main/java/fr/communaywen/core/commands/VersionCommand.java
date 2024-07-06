package fr.communaywen.core.commands;

import fr.communaywen.core.utils.PermissionCategory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class VersionCommand implements CommandExecutor {

    public static final @NotNull String PERMISSION = PermissionCategory.ADMIN.formatPermission("version");

    private final JavaPlugin plugin;

    public VersionCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("version")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (!player.hasPermission(PERMISSION)) {
                    player.sendMessage("Vous n'avez pas la permission d'utiliser cette commande.");
                    return true;
                }
            }
            sender.sendMessage("Version du plugin : " + plugin.getDescription().getVersion());
            return true;
        }
        return false;
    }
}
