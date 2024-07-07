package fr.communaywen.core.tpa;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.teams.Team;
import fr.communaywen.core.teams.TeamManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CommandTPA implements CommandExecutor, TabCompleter {

    TPAQueue tpQueue = TPAQueue.INSTANCE;

    private AywenCraftPlugin plugin;

    public CommandTPA(AywenCraftPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("/tpa <joueur>");
            return false;
        }

        if (sender instanceof Player) {
            Player player = (Player) sender;
            Player receiver = Bukkit.getPlayerExact(args[0]);
            if (receiver == null) {
                player.sendMessage("Impossible de trouver le joueur «"+args[0]+"»");
                return false;
            }
            tpQueue.TPA_REQUESTS.put(receiver, player);
            tpQueue.TPA_REQUESTS2.put(player, receiver);
            player.sendMessage("Vous avez envoyé une demande de tpa à " + receiver.getName());

            final TextComponent textComponent = Component.text(player.getName() + " vous a envoyé un demande de téléportation faites /tpaccept pour l'accepter")
                    .color(TextColor.color(255,255,255))
                    .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "tpaccept"))
                    .hoverEvent(HoverEvent.showText(Component.text("§7[§aClique pour accepter§7]")));

            plugin.getAdventure().player(receiver).sendMessage(textComponent);
            return true;
        }

        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        List<String> list = new ArrayList<String>();
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getName().startsWith(strings[0])){
                list.add(p.getName());
            }
        }
        return list;
    }
}
