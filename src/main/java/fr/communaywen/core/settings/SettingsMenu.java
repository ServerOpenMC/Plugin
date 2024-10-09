package fr.communaywen.core.settings;

import dev.xernas.menulib.Menu;
import dev.xernas.menulib.utils.InventorySize;
import dev.xernas.menulib.utils.ItemBuilder;
import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.settings.menus.MailboxManagerMenu;
import lombok.Getter;
import lombok.Setter;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettingsMenu extends Menu {
	
	AywenCraftPlugin plugin;
	Player owner;
	SettingsManager manager;
	
	@Getter
	@Setter
	private int mail_accept, trade_accept, tpa_accept;
	
	public SettingsMenu(AywenCraftPlugin plugin, Player owner, SettingsManager manager) throws SQLException {
		super(owner);
		this.plugin = plugin;
		this.owner = owner;
		this.manager = manager;
		this.mail_accept = manager.findPlayerSettingsByUUID(owner).mail_accept();
		this.trade_accept = manager.findPlayerSettingsByUUID(owner).trade_accept();
		this.tpa_accept = manager.findPlayerSettingsByUUID(owner).tpa_accept();
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
		
		map.put(22, new ItemBuilder(this, Material.PAPER, itemMeta -> {
			itemMeta.setDisplayName(ChatColor.GOLD + "Mailbox");
			itemMeta.setCustomModelData(8000);
			// itemMeta.setLore(List.of(SettingsUtils.getMailStatus(this.mail_accept)));
		}).setNextMenu(new MailboxManagerMenu(owner, this)));
		map.put(45, new ItemBuilder(this, Material.BARRIER, itemMeta -> {
			itemMeta.setDisplayName(ChatColor.DARK_RED + "Fermer");
			itemMeta.setCustomModelData(8100);
		}).setCloseButton());
		map.put(53, new ItemBuilder(this, Material.PAPER, itemMeta -> {
			itemMeta.setDisplayName(ChatColor.GREEN + "Sauvegarder");
			itemMeta.setCustomModelData(8001);
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
