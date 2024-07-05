package fr.communaywen.core.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.List;


public class RulesCommand implements CommandExecutor {

    private final FileConfiguration bookConfig;

    public RulesCommand(FileConfiguration bookConfig) {
        this.bookConfig = bookConfig;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Seul un joueur peut executer cette commande !");
            return false;
        }

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

        ((Player) sender).openBook(book);

        return true;
    }
}
