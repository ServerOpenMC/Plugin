package fr.communaywen.core.personalhome;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.utils.database.DatabaseConnector;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;

import javax.annotation.Nullable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;
import java.util.UUID;

public class Home extends DatabaseConnector {
    @Getter UUID owner;
    @Getter int id;
    @Nullable @Setter Location spawnpoint;
    boolean allow_visit;
    Biome biome;

    private World homeworld;

    public Home(UUID owner, int id) {
        this.owner = owner;
        this.id = id;

        this.homeworld = AywenCraftPlugin.getInstance().getManagers().getHomeManager().homeWorld;
    }

    public Biome getBiome() {
        return Objects.requireNonNullElse(biome, Biome.PLAINS);
    }

    public boolean setVisit(boolean visit) {
        allow_visit = visit;
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE personal_homes SET allow_visit=? WHERE id=?");
            statement.setBoolean(1, allow_visit);
            statement.setInt(2, id);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void setBiome(Biome biome) {
        this.biome = biome;

        int delta = (id-1)*13*16;

        for (int x = delta; x <= 32+delta; x++) {
            for (int z = 0; z <= 32; z++) {
                for (int y = -64; y <= 300; y++) {
                    homeworld.setBiome(x, y, z, biome);
                }
            }
        }
    }

    public boolean saveBiome() {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE personal_homes SET biome=? WHERE id=?");
            statement.setString(1, biome.name());
            statement.setInt(2, id);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Location getSpawnpoint() {
        if (spawnpoint == null) {
            if (id == 0) {
                return new Location(homeworld, 16, 101, 16);
            } else {
                return new Location(homeworld, ((id - 1) * (13 * 16)) + 16, 101, 16);
            }
        }
        return spawnpoint;
    }

    public boolean canVisit(){
        return allow_visit;
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
        PreparedStatement statement = connection.prepareStatement("INSERT INTO personal_homes VALUE (?, ?, NULL, 'PLAINS', FALSE)");
        statement.setInt(1, id);
        statement.setString(2, owner.toString());

        statement.executeUpdate();
    }
}
