package fr.communaywen.core.settings;

import dev.xernas.menulib.Menu;
import dev.xernas.menulib.utils.InventorySize;
import dev.xernas.menulib.utils.ItemBuilder;
import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.settings.menus.MailboxManagerMenu;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class SettingsMenu extends Menu {
	
	AywenCraftPlugin plugin;
	Player owner;
	SettingsManager manager;
	PlayerSettings settings;
	
	@Getter
	@Setter
	private int mail_accept = 3, trade_accept = 3, tpa_accept = 3;
	
	public SettingsMenu(AywenCraftPlugin plugin, Player owner, SettingsManager manager) {
		super(owner);
		this.plugin = plugin;
		this.owner = owner;
		this.manager = manager;
	}
	
	@Override
	public @NotNull String getName() {
		return "Parametres (en developpement)";
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
		
		map.put(31, new ItemBuilder(this, Material.PAPER, itemMeta -> {
			itemMeta.setDisplayName(ChatColor.GOLD + "Mailbox");
		}).setNextMenu(new MailboxManagerMenu(owner, this)));
		map.put(45, new ItemBuilder(this, Material.BARRIER, itemMeta -> {
			itemMeta.setDisplayName(ChatColor.DARK_RED + "Fermer");
		}).setCloseButton());
		map.put(53, new ItemBuilder(this, Material.PAPER, itemMeta -> {
			itemMeta.setDisplayName(ChatColor.GREEN + "Sauvarder et fermer");
		}).setOnClick(inventoryClickEvent -> {
			try {
				if (plugin.getManagers().getSettingsManager().findPlayerSettingsByUUID(owner) == null) {
					manager.createPlayerSettings(new PlayerSettings(owner.getUniqueId().toString(), mail_accept, trade_accept, tpa_accept));
				} else {
					manager.updatePlayerSettings(new PlayerSettings(owner.getUniqueId().toString(), mail_accept, trade_accept, tpa_accept));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}));
		
		return map;
	}
}
