package fr.communaywen.core.commands.utils;

import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.*;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import java.util.List;

/*
 * Comment ajouter une fonctionnalité ici:
 *
 * - Ajouter sa feature dans getIndex (tuto à la fin de la function)
 * - Ajouter une page pour la feature dans patchlogs (couleur vanilla §0-§f)
 * - Ajouter la page A LA FIN! (sinon les numéros de pages ne correspondent plus)
 *
 * Fréquence de remise à zero: 1 fois par semaine si c'est chargé/
 *                             1 fois par mois si c'est calme
 *
 *
 *
 * Comment reset les patchlogs:
 * - Changer getIndex() par Component.empty()
 * - Enlevez les pages sous getHeadPage ligne 66
 */


@Feature("Patchlogs")
@Credit("Gyro3630")
public class Patchlogs {
    private Component getIndex() {
        return Component.text("- Patchlogs").clickEvent(ClickEvent.changePage(2)).append(Component.text("\n- Spawn").clickEvent(ClickEvent.changePage(3)));

        // Examples
        // Component.text("- Feature cool").clickEvent(ClickEvent.changePage(1));
    }

    Book book;

    public Patchlogs() {
        ItemStack tmpbook = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) tmpbook.getItemMeta();
        meta.setTitle("Patchlogs");
        meta.setAuthor("");

        book = meta.pages(List.of(
                getHeadPage(),
                // Ajouter les pages en dessous
                buildPage("Patchlogs", NamedTextColor.AQUA,"Gyro3630",  "Les patchlogs permettent d'avoir un aperçu des dernières nouveautés du serveur\nElles sont écrites par les joueurs pour les joueurs"),
                buildPage("Le Spawn", NamedTextColor.LIGHT_PURPLE,"iambibi_",  "La Mise A Jour du Spawn !\nJ'ai donc rajouté des fonctionnalités autour du Spawn :\n- Des particules lors d'un Contest et par défaut.\n- Un Jump, Les Leaderboard, Les Tetes à Trouver")

        ));
    }

    @Command({"patchlogs", "news"})
    @Description("Affiche les dernières nouveautés du serveur")
    @CommandPermission("ayw.command.patchlogs")
    public void patchlogs(Player player) {
        player.openBook(book);
    }

    public static Component buildPage(String title, NamedTextColor titleColor, String author, String content) {
        return Component.text(StringUtils.center(title, 22)).color(titleColor).hoverEvent(HoverEvent.showText(Component.text("Par " + author)))
                .append(Component.newline())
                .append(Component.newline())
                .append(Component.text(content).color(NamedTextColor.BLACK));
    }

    private Component getHeadPage() {
        return Component.text(StringUtils.center("Patchlogs", 20)).color(NamedTextColor.GOLD).decoration(TextDecoration.BOLD, true)
                .append(Component.newline())
                .append(Component.newline())
                .append(getIndex().color(NamedTextColor.BLACK).decoration(TextDecoration.BOLD, false));
    }
}
