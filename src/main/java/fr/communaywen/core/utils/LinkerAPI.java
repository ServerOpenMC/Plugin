package fr.communaywen.core.utils;

import fr.communaywen.core.utils.database.DatabaseManager;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

public class LinkerAPI {

    private final DatabaseManager dbmanager;

    public LinkerAPI(DatabaseManager dbmanager) {
        this.dbmanager = dbmanager;
    }

    public boolean setDatabase(Player player, String uid) throws SQLException { // Ajoute un lien à la DB
        try {
            String playerUUID = player.getUniqueId().toString();
            Connection connection = dbmanager.getConnection();

            PreparedStatement statement = connection.prepareStatement("INSERT INTO link (discord_id, minecraft_uuid) VALUES (?, ?)");

            statement.setString(1, uid);
            statement.setString(2, playerUUID);

            statement.executeUpdate();

            return true;
        } catch (Exception e){
            System.out.println(e.toString());
            return false;
        }
    }

    public boolean linkWithCode(Player player, int code) throws SQLException { // Ajoute un lien à la DB
        try {
            String playerUUID = player.getUniqueId().toString();
            Connection connection = dbmanager.getConnection();

            PreparedStatement statement = connection.prepareStatement("INSERT INTO link_verif (minecraft_uuid, code) VALUES (?, ?)");

            statement.setString(1, playerUUID);
            statement.setString(2, String.valueOf(code));

            statement.executeUpdate();

            return true;
        } catch (Exception e){
            System.out.println(e.toString());
            return false;
        }
    }

    public boolean delayRemoveCode(Player player) throws SQLException { // Ajoute un lien à la DB
        try {
            String playerUUID = player.getUniqueId().toString();
            Connection connection = dbmanager.getConnection();

            PreparedStatement statement = connection.prepareStatement("DELETE FROM link_verif WHERE minecraft_uuid = ?");

            statement.setString(1, playerUUID);

            statement.executeUpdate();

            return true;
        } catch (Exception e){
            System.out.println(e.toString());
            return false;
        }
    }

    public String getUserId(Player player) throws SQLException { // Lis la DB est retourne l'uid (discord) du joueur, "" si introuvable
        try {
            String uuid = player.getUniqueId().toString();
            Connection connection = dbmanager.getConnection();

            String sql = "SELECT * FROM link WHERE minecraft_uuid = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, uuid);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                System.out.println("Discord ID found for "+player.getName()+": "+resultSet.getString("discord_id"));
                return resultSet.getString("discord_id");
            } else {
                return "";
            }

        } catch (Exception e) {
            System.out.println(e.toString());
            return "";
        }
    }

    public boolean isVerified(Player player) throws SQLException {
        try {
            String uuid = player.getUniqueId().toString();
            Connection connection = dbmanager.getConnection();

            String sql = "SELECT * FROM link WHERE minecraft_uuid = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, uuid);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            System.out.println(e.toString());
            return false;
        }
    }

    public int generateCode() {
        return new Random().nextInt(9000) + 1000;
    }

}
