package fr.communaywen.core.settings;

import fr.communaywen.core.AywenCraftPlugin;
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
	public void onJoin(PlayerJoinEvent e) throws SQLException {
		String uuid = e.getPlayer().getUniqueId().toString();
		
		SettingsCache.settingsMap.put(uuid, this.plugin.getManagers().getSettingsManager().findPlayerSettingsByUUID(uuid));
		
		if (SettingsCache.settingsMap.get(uuid) == null) {
			SettingsCache.settingsMap.put(uuid, new PlayerSettings(uuid, 3, 3, 3));
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) throws SQLException {
		String uuid = e.getPlayer().getUniqueId().toString();
		
		this.plugin.getManagers().getSettingsManager().updatePlayerSettings(SettingsCache.settingsMap.get(uuid));
		
		SettingsCache.settingsMap.remove(uuid);
	}
	
}
