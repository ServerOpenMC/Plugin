package fr.communaywen.core.trade;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.utils.Queue;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.bukkit.annotation.CommandPermission;


/**
 * La commande /tradeaccept
 * <p>
 * Usage: /tradeaccept
 * Permission: PREFIX.command.trade
 */
public final class TradeAcceptCommand {
    private AywenCraftPlugin plugin;

    // player2: player1
    private static final Queue<Player, Player> pendingDemands = new Queue<>(20);

    public TradeAcceptCommand(AywenCraftPlugin plugin) {
        this.plugin = plugin;
    }

    @Command("tradeaccept")
    @Description("Accepter une demande de trade")
    @CommandPermission("ayw.command.trade")     // TODO
    public void onCommand(Player player2) {
        Player player1 = pendingDemands.get(player2);

        if (player1 == null) {
            player2.sendMessage("§6Tu n'as pas de demande de trade en cours !");
            return;
        }
        pendingDemands.remove(player2);

        Trade trade = Trade.makeNewTrade(player1, player2, 0, 0, plugin);

        trade.openOwnItems(true);
        trade.openOwnItems(false);

        final TextComponent textComponent =
                Component.text("§3Trade avec §9" + player1.getName())
                        .append(
                                Component.text("\n> §b[§cModifier les items§b] ")
                                        .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/trade items"))
                                        .hoverEvent(HoverEvent.showText(Component.text("Modifier les items")))
                                        .append(
                                                Component.text("\n> §b[§dModifier l'argent à envoyer§b] ")
                                                        .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/trade money 0"))
                                                        .hoverEvent(HoverEvent.showText(Component.text("Modifier l'argent à envoyer")))
                                                        .append(
                                                                Component.text("\n> §b[§dVérifier les items envoyés§b] ")
                                                                        .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/trade check"))
                                                                        .hoverEvent(HoverEvent.showText(Component.text("Vérifier les items envoyés par l'autre")))
                                                                        .append(
                                                                                Component.text("\n> §b[§aConclure le trade§b] ")
                                                                                        .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/trade conclude"))
                                                                                        .hoverEvent(HoverEvent.showText(Component.text("Accepter et conclure le trade")))
                                                                                        .append(
                                                                                                Component.text("\n> §b[§cAnnuler le trade§b] ")
                                                                                                        .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/trade cancel"))
                                                                                                        .hoverEvent(HoverEvent.showText(Component.text("Annuler le trade")))
                                                                                        )
                                                                        )
                                                        )
                                        )
                        );

        plugin.getAdventure().player(player1).sendMessage(textComponent);
        player1.playSound(player1.getEyeLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1);
        plugin.getAdventure().player(player2).sendMessage(textComponent);
        player2.playSound(player2.getEyeLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1);
    }

    public static void newPendingDemand(Player player1, Player player2) {
        pendingDemands.add(player2, player1);
    }
}
