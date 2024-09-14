package fr.communaywen.core.personalhome;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.utils.database.DatabaseConnector;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

import javax.annotation.Nullable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class Home extends DatabaseConnector {
    @Getter UUID owner;
    @Getter int id;
    @Nullable @Setter Location spawnpoint;

    public Home(UUID owner, int id) {
        this.owner = owner;
        this.id = id;
    }

    public Location getSpawnpoint() {
        if (spawnpoint == null) {
            return new Location(AywenCraftPlugin.getInstance().getManagers().getHomeManager().homeWorld, ((id - 1) * (11 * 16)) + 16,101,16);
        }
        return spawnpoint;
    }

    public boolean saveSpawnpoint() {
        if (spawnpoint == null) {
            return true;
        }

        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE personal_homes SET spawnpoint=? WHERE id=?");
            statement.setString(1, HomesUtils.serializeCoords(spawnpoint));
            statement.setInt(2, id);
            statement.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void save(Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO personal_homes VALUE (?, ?, NULL)");
        statement.setInt(1, id);
        statement.setString(2, owner.toString());

        statement.executeUpdate();
    }
}
