package fr.communaywen.core.contest.menu;

import dev.xernas.menulib.utils.InventorySize;
import dev.xernas.menulib.utils.ItemBuilder;
import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.contest.managers.ContestManager;
import fr.communaywen.core.utils.constant.MessageManager;
import fr.communaywen.core.utils.constant.MessageType;
import fr.communaywen.core.utils.constant.Prefix;
import me.clip.placeholderapi.PlaceholderAPI;
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
    private final ContestManager contestManager;
    private final AywenCraftPlugin plugin;

    public ConfirmMenu(Player owner, AywenCraftPlugin plugins, String camp, String color, ContestManager manager) {
        super(owner);
        this.contestManager = manager;
        this.getCampName = camp;
        this.getColor = color;
        this.plugin= plugins;
    }

    @Override
    public @NotNull String getName() {
        return PlaceholderAPI.setPlaceholders(getOwner(), "§r§f%img_offset_-48%%img_contest_menu%");
    }

    @Override
    public @NotNull InventorySize getInventorySize() {
        return InventorySize.LARGE;
    }

    @Override
    public void onInventoryClick(InventoryClickEvent click) {}

    @Override
    public @NotNull Map<Integer, ItemStack> getContent() {
        Map<Integer, ItemStack> inventory = new HashMap<>();

        String campNameFinal = ContestManager.getString("contest", getCampName).join();;
        String campColor = ContestManager.getString("contest", getColor).join();;
        ChatColor colorFinal = ChatColor.valueOf(campColor);

        List<String> lore1 = new ArrayList<String>();
        lore1.add("§7Vous allez rejoindre " + colorFinal + "La Team " + campNameFinal);
        lore1.add("§c§lATTENTION! Vous ne pourrez changer de choix !");

        List<String> lore0 = new ArrayList<String>();
        lore0.add("§7Vous allez annuler votre choix : " + colorFinal + "La Team " + campNameFinal);

                inventory.put(11, new ItemBuilder(this, Material.RED_CONCRETE, itemMeta -> {
                    itemMeta.setDisplayName("§r§cAnnuler");
                    itemMeta.setLore(lore0);
                }).setOnClick(inventoryClickEvent -> {
                    VoteMenu menu = new VoteMenu(getOwner(), plugin, contestManager);
                    menu.open();
                }));

                inventory.put(15, new ItemBuilder(this, Material.GREEN_CONCRETE, itemMeta -> {
                    itemMeta.setDisplayName("§r§aConfirmer");
                    itemMeta.setLore(lore1);
                }).setOnClick(inventoryClickEvent -> {
                    String substring = this.getCampName.substring(this.getCampName.length() - 1);
                    contestManager.insertChoicePlayer(getOwner(), Integer.valueOf(substring));
                    getOwner().playSound(getOwner().getEyeLocation(), Sound.BLOCK_AMETHYST_BLOCK_RESONATE, 1.0F, 0.2F);
                    MessageManager.sendMessageType(getOwner(), "§7Vous avez bien rejoint : "+ colorFinal + "La Team " + contestManager.getString("contest", getCampName).join(), Prefix.CONTEST, MessageType.SUCCESS, false);
                    getOwner().closeInventory();
                }));
                return inventory;
    }
}
