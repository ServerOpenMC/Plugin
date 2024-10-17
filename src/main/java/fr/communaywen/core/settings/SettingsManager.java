package fr.communaywen.core.settings;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.utils.constant.MessageManager;
import fr.communaywen.core.utils.constant.MessageType;
import fr.communaywen.core.utils.constant.Prefix;
import fr.communaywen.core.utils.database.DatabaseConnector;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SettingsManager extends DatabaseConnector {
	
	AywenCraftPlugin plugin;
	
	public SettingsManager(AywenCraftPlugin plugin) {
		this.plugin = plugin;
	}
	
	public void init() {
		try {
			String sql = "SELECT * FROM settings";
			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet results = statement.executeQuery();
			while (results.next()) {
				PlayerSettings playerSettings = new PlayerSettings(
						results.getString("player"),
						results.getInt("mail_accept"),
						results.getInt("trade_accept"),
						results.getInt("tpa_accept"));
				SettingsCache.settingsMap.put(playerSettings.uuid(), playerSettings);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void saveSettings() {
		try {
			PreparedStatement clearAllStatement = plugin.getManagers().getDatabaseManager().getConnection().prepareStatement("DELETE FROM settings");
			clearAllStatement.executeUpdate();
			for (PlayerSettings settings : SettingsCache.settingsMap.values()) {
				createPlayerSettings(settings);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public PlayerSettings findPlayerSettingsByUUID(Player player) {
		try {
			String sql = "SELECT * FROM settings WHERE player = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, player.getUniqueId().toString());
			ResultSet results = statement.executeQuery();
			if (results.next()) {
				int mail_accept = results.getInt("mail_accept");
				int trade_accept = results.getInt("trade_accept");
				int tpa_accept = results.getInt("tpa_accept");
				PlayerSettings playerSettings = new PlayerSettings(player.getUniqueId().toString(), mail_accept, trade_accept, tpa_accept);
				statement.close();
				return playerSettings;
			}
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
			MessageManager.sendMessageType(player, "Impossible de charger les settings", Prefix.SETTINGS, MessageType.ERROR, false);
		}
		return null;
	}
	
	public void createPlayerSettings(PlayerSettings settings) {
		try {
			String sql = "INSERT INTO settings (player, mail_accept, trade_accept, tpa_accept) VALUES (?, ?, ?, ?)";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, settings.uuid());
			statement.setInt(2, settings.mail_accept());
			statement.setInt(3, settings.trade_accept());
			statement.setInt(4, settings.tpa_accept());
			
			statement.executeUpdate();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}