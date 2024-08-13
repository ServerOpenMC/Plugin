package fr.communaywen.core.dreamdim.listeners;

import dev.lone.itemsadder.api.CustomStack;
import fr.communaywen.core.dreamdim.AdvancementRegister;
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
    AdvancementRegister register;

    public CodexSomnii(AdvancementRegister register) {
        this.register = register;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();

        CustomStack customStack = CustomStack.byItemStack(event.getItem());

        if (!action.isRightClick()) { return; }
        if (customStack == null) { return; }
        if (!customStack.getNamespacedID().equals("aywen:codex_somnii")) { return; }

        this.register.grantAdvancement(player, "aywen:pit_of_dreams");

        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) new ItemStack(Material.WRITTEN_BOOK).getItemMeta();

        /* INFO */
        meta.setTitle("Codex Somnii");
        meta.setAuthor("Le Dark Magicien Dandan");
        // 15 lignes max par page, environ 25 caractères par ligne
        /* A copier: meta.addPages(Component.text("")); */

        /* SOMMAIRE */
        meta.addPages(Component.text("  §l§kA§r §lCodex Somnii §kA§r\n\nLivre qui sert à rien\npour l'instant\n\n\n\n\n\n\n\n\n\n     Ecris par §l§kDandan§r"));
        meta.addPages(Component.text("§5§l1.§r Dimension").clickEvent(ClickEvent.changePage(3)));

        /* DIMENSION */
        meta.addPages(Component.text("        §5DIMENSION§r\n\nLa dimension des rêves est un monde parallèle qui est accessible en dormant.\n\nSon sol est composé de sculk puis de boue et enfin d'ardoise."));
        meta.addPages(Component.text("Il y fais toujours nuit en conséquence, y rester est très dangereux.\n\nLes créatures qui y vivent sont des cauchemars qui peuvent vous tuer"));

        book.setItemMeta(meta);
        player.openBook(book);
    }
}
