package fr.communaywen.core.commands;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import java.util.List;


public class RulesCommand {

    private final FileConfiguration bookConfig;

    public RulesCommand(FileConfiguration bookConfig) {
        this.bookConfig = bookConfig;
    }

    @Command({"rules", "regles", "reglement"})
    @Description("Affiche le règlement du serveur")
    @CommandPermission("ayw.command.rules")
    public void onCommand(Player player) {
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) book.getItemMeta();
        meta.setTitle(bookConfig.getString("title"));
        meta.setAuthor(bookConfig.getString("author"));
        List<String> pages = bookConfig.getStringList("pages");
        for (int i = 0; i < pages.size(); i++) {
            pages.set(i, pages.get(i).replace("\\n", "\n"));
        }
        meta.setPages(pages);
        book.setItemMeta(meta);
        player.openBook(book);
    }

}
