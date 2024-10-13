package fr.communaywen.core.settings;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.utils.constant.MessageManager;
import fr.communaywen.core.utils.constant.MessageType;
import fr.communaywen.core.utils.constant.Prefix;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.SQLException;

public class SettingsListener implements Listener {
	
	AywenCraftPlugin plugin;
	
	public SettingsListener(AywenCraftPlugin plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
			String uuid = e.getPlayer().getUniqueId().toString();
			PlayerSettings playerSettingsByUUID = this.plugin.getManagers().getSettingsManager().findPlayerSettingsByUUID(e.getPlayer());
			
			Bukkit.getScheduler().runTask(this.plugin, () -> {
				SettingsCache.settingsMap.put(uuid, playerSettingsByUUID);
				
				if (SettingsCache.settingsMap.get(uuid) == null) {
					SettingsCache.settingsMap.put(uuid, new PlayerSettings(uuid, 3, 3, 3));
				}
			});
			MessageManager.sendMessageType(e.getPlayer(), "Settings créés", Prefix.SETTINGS, MessageType.INFO, false);
		});
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
			String uuid = e.getPlayer().getUniqueId().toString();
			
			if (this.plugin.getManagers().getSettingsManager().findPlayerSettingsByUUID(e.getPlayer()) == null)
				this.plugin.getManagers().getSettingsManager().createPlayerSettings(SettingsCache.settingsMap.get(uuid));
			else
				this.plugin.getManagers().getSettingsManager().updatePlayerSettings(SettingsCache.settingsMap.get(uuid));
			
			Bukkit.getScheduler().runTask(this.plugin, () -> {
				SettingsCache.settingsMap.remove(uuid);
			});
		});
	}
}