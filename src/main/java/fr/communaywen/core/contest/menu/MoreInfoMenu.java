package fr.communaywen.core.contest.menu;

import dev.xernas.menulib.utils.InventorySize;
import dev.xernas.menulib.utils.ItemBuilder;
import fr.communaywen.core.contest.ContestManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import dev.xernas.menulib.Menu;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class MoreInfoMenu extends Menu {

    public MoreInfoMenu(Player owner) {
        super(owner);
    }

    @Override
    public @NotNull String getName() {
        return "Le Contest - Déroulement";
    }

    @Override
    public @NotNull InventorySize getInventorySize() {
        return InventorySize.NORMAL;
    }

    @Override
    public void onInventoryClick(InventoryClickEvent click) {}

    @Override
    public @NotNull Map<Integer, ItemStack> getContent() {
        Map<Integer, ItemStack> inventory = new HashMap<>();

        List<String> lore0 = new ArrayList<String>();
        List<String> lore1 = new ArrayList<String>();
        List<String> lore2 = new ArrayList<String>();

        lore0.add("§7Tout les vendredi, le Contest commence");
        lore0.add("§7Et les votes s'ouvrent, et il faut choisir");
        lore0.add("§7Entre 2 camps, une ambience se crée dans le spawn...");

        lore1.add("§7La nuit tombe sur le spawn pendant 2 jours");
        lore1.add("§7Que la Fête commence!");
        lore1.add("§7Des trades sont disponible");
        lore1.add("§7Donnant des Coquillages de Contest!");

        lore2.add("§7Le levé de Soleil sur le Spawn!");
        lore2.add("§7Les résultats tombent, et un camp gagnant");
        lore2.add("§7Sera élu. Et des récompenses sont attribué");
        lore2.add("§7A chacun.");

        int phase = ContestManager.getPhaseCache();

        boolean ench0;
        boolean ench1;
        if (phase==2) {
            ench1 = false;
            ench0 = true;
        } else if (phase==3) {
            ench0 = false;
            ench1 = true;
        } else {
            ench1 = false;
            ench0 = false;
        }


                inventory.put(11, new ItemBuilder(this, Material.BLUE_STAINED_GLASS_PANE, itemMeta -> {
                    itemMeta.setDisplayName("§r§1Les Votes - Vendredi");
                    itemMeta.setLore(lore0);
                    itemMeta.setEnchantmentGlintOverride(ench0);
                }));
                inventory.put(13, new ItemBuilder(this, Material.RED_STAINED_GLASS_PANE, itemMeta -> {
                    itemMeta.setDisplayName("§r§cL'Affrontement - Samedi-Dimanche");
                    itemMeta.setLore(lore1);
                    itemMeta.setEnchantmentGlintOverride(ench1);
                }));
                inventory.put(15, new ItemBuilder(this, Material.YELLOW_STAINED_GLASS_PANE, itemMeta -> {
                    itemMeta.setDisplayName("§r§eLes Résultats - Lundi");
                    itemMeta.setLore(lore2);
                }));
                inventory.put(18, new ItemBuilder(this, Material.ARROW, itemMeta -> {
                    itemMeta.setDisplayName("§r§aRetour");
                }).setBackButton());
        return inventory;
    }
}
