package fr.communaywen.core.trade;

import fr.communaywen.core.AywenCraftPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.annotation.CommandPermission;


/**
 * La commande /trade
 * <p>
 * Usage: /trade [player]
 * Permission: PREFIX.command.trade
 */
@Command("trade")
@Description("Gestion des trades")
@CommandPermission("ayw.command.trade")
public final class TradeCommand {
    private AywenCraftPlugin plugin;

    public TradeCommand(AywenCraftPlugin plugin) {
        this.plugin = plugin;
    }

    @DefaultFor("~")
    @Description("Echanger des items et de l'argent avec un autre joueur")
    @CommandPermission("ayw.command.trade")
    public void onCommand(Player player1, Player player2) {

        if (Trade.tradesPlayer1.get(player1) != null || Trade.tradesPlayer2.get(player1) != null) {
            player1.sendMessage("§6Tu as déjà un trade en cours !");
            return;
        }

        if (player2 == null) {
            player1.sendMessage("§6Il faut spécifier un joueur !\nExemple: /trade Aywen");
            return;
        }

        if (player1 == player2) {
            player1.sendMessage("§6Tu ne peux pas te faire une demande de trade !");
            return;
        }

        final TextComponent textComponent = Component.text("§9" + player1.getName() + " §3vous a envoyé un demande de trade, /tradeaccept pour l'accepter")
                .color(TextColor.color(255, 255, 255))
                .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/tradeaccept"))
                .hoverEvent(HoverEvent.showText(Component.text("§7[§aClique pour accepter§7]")));

        TradeAcceptCommand.newPendingDemand(player1, player2);

        plugin.getAdventure().player(player2).sendMessage(textComponent);
        player2.playSound(player2.getEyeLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1);

        player1.sendMessage("§aLa demande de trade a été envoyée à " + player2.getName());
    }

    @Subcommand("money")
    @Description("Définir l'argent à envoyer")
    @CommandPermission("ayw.command.trade")
    public void moneyCmd(Player player, double moneytoPay) {

        boolean isPlayer1;

        Trade trade = Trade.tradesPlayer1.get(player);
        if (trade == null) {
            trade = Trade.tradesPlayer2.get(player);

            if (trade == null) {
                player.sendMessage("§6Tu n'as pas de trade en cours !");
                return;
            }

            isPlayer1 = false;
        } else {
            isPlayer1 = true;
        }

        if (trade.isLocked()) {
            player.sendMessage("§6L'autre joueur a bloqué le trade !");
            return;
        }

        /*if (moneyToPay == null) {
            player.sendMessage("§6Il faut spécifier une valeur !\nExemple: /trade money 200");
            return;
        }*/

        if (moneytoPay < 0) {
            player.sendMessage("§6Il faut un nombre positif !");
            return;
        }

        boolean success;

        if (isPlayer1) {
            success = trade.setMoney1(moneytoPay);
        } else {
            success = trade.setMoney2(moneytoPay);
        }

        if (!success) {
            player.sendMessage("§6Tu n'as pas assez d'argent ! (" + plugin.economyManager.getBalance(player) + ")");
            return;
        }

        player.sendMessage("§aL'argent à envoyer a bien été défini à " + moneytoPay + "$");

        if (isPlayer1) {
            trade.player2.sendMessage("§b" + player.getName() + " a modifié l'argent à envoyer " + moneytoPay + "$");
        } else {
            trade.player1.sendMessage("§b" + player.getName() + " a modifié l'argent à envoyer " + moneytoPay + "$");
        }
    }

    @Subcommand("items")
    @Description("Définir les items à envoyer")
    @CommandPermission("ayw.command.trade")
    public void itemsCmd(Player player) {

        boolean isPlayer1;

        Trade trade = Trade.tradesPlayer1.get(player);
        if (trade == null) {
            trade = Trade.tradesPlayer2.get(player);

            if (trade == null) {
                player.sendMessage("§6Tu n'as pas de trade en cours !");
                return;
            }

            isPlayer1 = false;
        } else {
            isPlayer1 = true;
        }

        if (trade.isLocked()) {
            player.sendMessage("§6L'autre joueur a bloqué le trade !");
            return;
        }

        trade.openOwnItems(isPlayer1);
    }

    @Subcommand("check")
    @Description("Vérifier les items envoyés par l'autre")
    @CommandPermission("ayw.command.trade")
    public void checkCmd(Player player) {

        boolean isPlayer1;

        Trade trade = Trade.tradesPlayer1.get(player);
        if (trade == null) {
            trade = Trade.tradesPlayer2.get(player);

            if (trade == null) {
                player.sendMessage("§6Tu n'as pas de trade en cours !");
                return;
            }

            isPlayer1 = false;
        } else {
            isPlayer1 = true;
        }

        if (isPlayer1) {
            trade.player1.sendMessage("$bArgent qui sera envoyé par " + trade.player2.getName() + " : §a" + trade.money2 + "$");
        } else {
            trade.player2.sendMessage("$bArgent qui sera envoyé par " + trade.player1.getName() + " : §a" + trade.money1 + "$");
        }

        trade.openOtherItems(isPlayer1);
    }

    @Subcommand("cancel")
    @Description("Annule le trade")
    @CommandPermission("ayw.command.trade")
    public void cancelCmd(Player player) {
        Trade trade = Trade.tradesPlayer1.get(player);
        if (trade == null) {
            trade = Trade.tradesPlayer2.get(player);

            if (trade == null) {
                player.sendMessage("§6Tu n'as pas de trade en cours !");
                return;
            }
        }

        trade.cancel();
    }

    @Subcommand("conclude")
    @Description("Conclus le trade")
    @CommandPermission("ayw.command.trade")
    public void concludeCmd(Player player) {
        boolean isPlayer1;

        Trade trade = Trade.tradesPlayer1.get(player);
        if (trade == null) {
            trade = Trade.tradesPlayer2.get(player);

            if (trade == null) {
                player.sendMessage("§6Tu n'as pas de trade en cours !");
                return;
            }

            isPlayer1 = false;
        } else {
            isPlayer1 = true;
        }

        trade.hasConcluded(isPlayer1);
    }
}
