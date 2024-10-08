package fr.communaywen.core.dreamdim.listeners;

import dev.lone.itemsadder.api.CustomStack;
import dev.lone.itemsadder.api.FontImages.FontImageWrapper;
import fr.communaywen.core.guideline.GuidelineManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class CodexSomnii implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();

        CustomStack customStack = CustomStack.byItemStack(event.getItem());

        if (!action.isRightClick()) { return; }
        if (customStack == null) { return; }
        if (!customStack.getNamespacedID().equals("aywen:codex_somnii")) { return; }

        GuidelineManager.getAPI().getAdvancement("dream:codex").grant(player);

        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) new ItemStack(Material.WRITTEN_BOOK).getItemMeta();

        String dandangicien = new FontImageWrapper("aywen:dandanmagicien").getString();

        /* INFO */
        meta.setTitle("Codex Somnii");
        meta.setAuthor("Le Dark Magicien Dandan");
        // 15 lignes max par page, environ 25 caractères par ligne
        /* A copier: meta.addPages(Component.text("")); */

        /* SOMMAIRE */
        meta.addPages(Component.text("  §l§kA§r §lCodex Somnii §kA§r\n\n\n    "+dandangicien+"\n\n\n\n\n\n\n\n\n Ecris par §lMagicien\n            Dandan"));
        meta.addPages(Component.text("§5§l1.§r Dimension\n").clickEvent(ClickEvent.changePage(3)).append(
                Component.text("    §d§l1.§r Flore\n").clickEvent(ClickEvent.changePage(5)).append(
                Component.text("    §d§l2.§r Pêche\n").clickEvent(ClickEvent.changePage(7)).append(
                Component.text("    §d§l3.§r Minéraux\n").clickEvent(ClickEvent.changePage(11))
        ))));
        /* DIMENSION */
        meta.addPages(Component.text("        §5§lDIMENSION§r\n\nLa dimension des rêves est un monde parallèle qui est accessible en dormant.\n\nSon sol est composé de sculk, puis de boue et enfin d'ardoise."));
        meta.addPages(Component.text("Il y fait toujours nuit, en conséquence, y rester est très dangereux.\n\nVous pouvez y trouver 3 types de créatures :\n- L'araignée géante\n- Le cheval-zombie\n- Le cheval-squelette"));

        /* FLORE */
        meta.addPages(Component.text("        §2§nLA FLORE§r\n\n\nIl n'y a qu'une seule espèce d'arbre dans la dimension des rêves."));
        meta.addPages(Component.text("Les arbres de rêves\n\nLes arbres de rêves ont des feuilles en nuages et des bûches en bois de rêves (très poussé niveau recherche)"));

        /* PÊCHE */
        meta.addPages(Component.text("        §5§lLA PÊCHE§r\n\nIl est possible de pêcher des poissons comme le poissonion, le poisson-lune et d'autres.\n\nSi vous êtes chanceux, vous pourrez pêcher des \"poissons\" encore vivants."));
        meta.addPages(Component.text("  §3§nLE POISSON DOCKER§r\n\nLe poisson Docker est un poisson atypique.\nIl ne peut pas être cuit et a un nom différent à chaque fois que vous le pêchez."));
        meta.addPages(Component.text("    §3§nLE POISSON LUNE§r\n\nLe poisson-lune ne peut être pêché que la nuit. À l'inverse du poisson Docker, il peut être cuit et vous donnera un poisson-soleil."));
        meta.addPages(Component.text("     §3§nLE POISSONION§r\n\nLe poissonion est un poisson qui peut s'apparenter à un oignon mais n'en est pas un. Il peut cependant être mélangé à de la salade, des tomates et du pain pour obtenir un bon kebab."));

        /* MINERAUX */
        meta.addPages(Component.text("      §8§lLES MINÉRAUX§r\n\n"));
        meta.addPages(Component.text("     §8§nESSENCE DE RÊVE§r\n\nL'essence de rêve est l'un des minéraux les plus utiles de la dimension. Il y est exclusif et a la même texture qu'un débris de netherite."));

        book.setItemMeta(meta);
        player.openBook(book);
    }
}
