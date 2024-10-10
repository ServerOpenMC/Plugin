package fr.communaywen.core.settings;

import fr.communaywen.core.AywenCraftPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.SQLException;

public class SettingsListener implements Listener {
	
	AywenCraftPlugin plugin;
	
	public SettingsListener(AywenCraftPlugin plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) throws SQLException {
		String uuid = e.getPlayer().getUniqueId().toString();
		
		if (this.plugin.getManagers().getSettingsManager().findPlayerSettingsByUUID(uuid) == null) {
			this.plugin.getManagers().getSettingsManager().createPlayerSettings(new PlayerSettings(uuid, 3, 3, 3));
		}
	}
	
}
