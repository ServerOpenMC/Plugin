package fr.communaywen.core.settings;

import dev.lone.itemsadder.api.CustomStack;
import dev.xernas.menulib.Menu;
import dev.xernas.menulib.utils.InventorySize;
import dev.xernas.menulib.utils.ItemBuilder;
import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.settings.menus.MailboxManagerMenu;
import fr.communaywen.core.utils.constant.MessageManager;
import fr.communaywen.core.utils.constant.MessageType;
import fr.communaywen.core.utils.constant.Prefix;
import lombok.Getter;
import lombok.Setter;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettingsMenu extends Menu {
	
	AywenCraftPlugin plugin;
	Player owner;
	SettingsManager manager;
	
	@Getter
	@Setter
	private int mailAccept, tradeAccept, tpaAccept;
	
	public SettingsMenu(AywenCraftPlugin plugin, Player owner, SettingsManager manager) {
		super(owner);
		this.plugin = plugin;
		this.owner = owner;
		this.manager = manager;
		mailAccept = SettingsCache.settingsMap.get(owner.getUniqueId().toString()).mailAccept();
		tradeAccept = SettingsCache.settingsMap.get(owner.getUniqueId().toString()).tradeAccept();
		tpaAccept = SettingsCache.settingsMap.get(owner.getUniqueId().toString()).tpaAccept();
	}
	
	@Override
	public @NotNull String getName() {
		return PlaceholderAPI.setPlaceholders(owner, "§r§f%img_offset_-8%%img_player_settings%");
	}
	
	@Override
	public @NotNull InventorySize getInventorySize() {
		return InventorySize.LARGEST;
	}
	
	@Override
	public void onInventoryClick(InventoryClickEvent inventoryClickEvent) {
	
	}
	
	@Override
	public @NotNull Map<Integer, ItemStack> getContent() {
		Map<Integer, ItemStack> map = new HashMap<>();
		
		map.put(22, new ItemBuilder(this, CustomStack.getInstance("settings:mailbox_settings_btn").getItemStack(), itemMeta -> {
			itemMeta.setDisplayName(ChatColor.GOLD + "Mailbox");
			itemMeta.setLore(List.of(SettingsUtils.getMailStatus(mailAccept)));
		}).setNextMenu(new MailboxManagerMenu(owner, this)));
		map.put(45, new ItemBuilder(this, CustomStack.getInstance("settings:close_btn").getItemStack(), itemMeta -> {
			itemMeta.setDisplayName(ChatColor.DARK_RED + "Fermer");
		}).setCloseButton());
		map.put(53, new ItemBuilder(this, CustomStack.getInstance("settings:save_btn").getItemStack(), itemMeta -> {
			itemMeta.setDisplayName(ChatColor.GREEN + "Sauvegarder");
		}).setOnClick(inventoryClickEvent -> {
				SettingsCache.settingsMap.replace(owner.getUniqueId().toString(), new PlayerSettings(owner.getUniqueId().toString(), mailAccept, tradeAccept, tpaAccept));
			MessageManager.sendMessageType(owner, "Settings enregistrés", Prefix.SETTINGS, MessageType.INFO, false);
		}));
		return map;
	}
}