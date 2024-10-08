package fr.communaywen.core.settings.menus;

import dev.xernas.menulib.Menu;
import dev.xernas.menulib.utils.InventorySize;
import dev.xernas.menulib.utils.ItemBuilder;
import fr.communaywen.core.settings.SettingsMenu;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class MailboxManagerMenu extends Menu {
	
	Player player;
	SettingsMenu settingsMenu;
	
	public MailboxManagerMenu(Player owner, SettingsMenu settingsMenu) {
		super(owner);
		this.player = owner;
		this.settingsMenu = settingsMenu;
	}
	
	@Override
	public @NotNull String getName() {
		return PlaceholderAPI.setPlaceholders(player, "§r§f%img_offset_-8%%img_mailbox_settings%");
	}
	
	@Override
	public @NotNull InventorySize getInventorySize() {
		return InventorySize.SMALLEST;
	}
	
	@Override
	public void onInventoryClick(InventoryClickEvent inventoryClickEvent) {
	
	}
	
	@Override
	public @NotNull Map<Integer, ItemStack> getContent() {
		HashMap<Integer, ItemStack> map = new HashMap<>();
		
		map.put(1, new ItemBuilder(this, Material.RED_WOOL, itemMeta -> {
			itemMeta.setDisplayName(ChatColor.DARK_RED + "Personne");
			itemMeta.setEnchantmentGlintOverride(settingsMenu.getMail_accept() == 0);
		}).setOnClick(inventoryClickEvent -> {
			settingsMenu.setMail_accept(0);
			back();
		}));
//		map.put(3, new ItemBuilder(this, Material.ORANGE_WOOL, itemMeta -> {
//			itemMeta.setDisplayName(ChatColor.GOLD + "Amis seulement");
//			itemMeta.setEnchantmentGlintOverride(settingsMenu.getMail_accept() == 1);
//		}).setOnClick(inventoryClickEvent -> {
//			settingsMenu.setMail_accept(1);
//			back();
//		}));
//		map.put(5, new ItemBuilder(this, Material.YELLOW_WOOL, itemMeta -> {
//			itemMeta.setDisplayName(ChatColor.YELLOW + "Team seulement");
//			itemMeta.setEnchantmentGlintOverride(settingsMenu.getMail_accept() == 2);
//		}).setOnClick(inventoryClickEvent -> {
//			settingsMenu.setMail_accept(2);
//			back();
//		}));
		map.put(7, new ItemBuilder(this, Material.GREEN_WOOL, itemMeta -> {
			itemMeta.setDisplayName(ChatColor.GREEN + "Tout le monde");
			itemMeta.setEnchantmentGlintOverride(settingsMenu.getMail_accept() == 3);
		}).setOnClick(inventoryClickEvent -> {
			settingsMenu.setMail_accept(3);
			back();
		}));
		return map;
	}
}
