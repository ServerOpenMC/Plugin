package fr.communaywen.core.contest.menu;

import dev.xernas.menulib.utils.InventorySize;
import dev.xernas.menulib.utils.ItemBuilder;
import fr.communaywen.core.contest.ContestManager;
import fr.communaywen.core.utils.constant.MessageManager;
import fr.communaywen.core.utils.constant.MessageType;
import fr.communaywen.core.utils.constant.Prefix;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import dev.xernas.menulib.Menu;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ConfirmMenu extends Menu {
    private final String getCampName;
    private final String getColor;

    public ConfirmMenu(Player owner, String camp, String color) {
        super(owner);
        this.getCampName = camp;
        this.getColor = color;
    }

    @Override
    public @NotNull String getName() {
        return "Le Contest - Confirmation";
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

        String campNameFinal = ContestManager.getString("contest", getCampName);
        String campColor = ContestManager.getString("contest", getColor);
        ChatColor colorFinal = ChatColor.valueOf(campColor);

        List<String> lore1 = new ArrayList<String>();
        lore1.add("§7Vous allez rejoindre " + colorFinal + "La Team " + campNameFinal);
        lore1.add("§c§lATTENTION! Vous ne pourrez changer de choix !");

        List<String> lore0 = new ArrayList<String>();
        lore0.add("§7Vous allez annuler votre choix : " + colorFinal + "La Team " + campNameFinal);

        for(int i = 0; i < getInventorySize().getSize(); i++) {
            if(i==11) {
                inventory.put(11, new ItemBuilder(this, Material.RED_CONCRETE, itemMeta -> {
                    itemMeta.setDisplayName("§r§cAnnuler");
                    itemMeta.setLore(lore0);
                }).setOnClick(inventoryClickEvent -> {
                    VoteMenu menu = new VoteMenu(getOwner());
                    menu.open();
                }));
            } else if(i==15) {
                inventory.put(15, new ItemBuilder(this, Material.GREEN_CONCRETE, itemMeta -> {
                    itemMeta.setDisplayName("§r§aConfirmer");
                    itemMeta.setLore(lore1);
                }).setOnClick(inventoryClickEvent -> {
                    String substring = this.getCampName.substring(this.getCampName.length() - 1);
                    ContestManager.insertChoicePlayer(getOwner(), Integer.valueOf(substring));
                    getOwner().playSound(getOwner().getEyeLocation(), Sound.BLOCK_AMETHYST_BLOCK_RESONATE, 1.0F, 0.2F);
                    MessageManager.sendMessageType(getOwner(), "§7Vous avez bien rejoint : "+ colorFinal + "La Team " + ContestManager.getString("contest", getCampName), Prefix.CONTEST, MessageType.SUCCESS, false);
                    getOwner().closeInventory();
                }));
            } else {
                inventory.put(i, new ItemBuilder(this, Material.GRAY_STAINED_GLASS_PANE, itemMeta -> itemMeta.setDisplayName(" ")));
            }
        }
        return inventory;
    }
}
