package fr.communaywen.core.guideline.listeners.dream;

import com.fren_gor.ultimateAdvancementAPI.UltimateAdvancementAPI;
import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.guideline.GuidelineManager;
import fr.communaywen.core.utils.database.DatabaseConnector;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.UUID;

public class HelldiversManager extends DatabaseConnector implements Listener {
    HashMap<UUID, Integer> spiders = new HashMap<>();
    HashMap<UUID, Integer> zombiehorse = new HashMap<>();
    HashMap<UUID, Integer> skeletonhorse = new HashMap<>();

    private void increment(HashMap<UUID, Integer> map, UUID uuid) {
        map.put(uuid, map.getOrDefault(uuid, 0) + 1);
    }

    public HelldiversManager() {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM advancements");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                UUID uuid = UUID.fromString(resultSet.getString("player"));
                switch (resultSet.getString("advancement")) {
                    case "spider":
                        spiders.put(uuid, resultSet.getInt("value"));
                        break;
                    case "zombie_horse":
                        zombiehorse.put(uuid, resultSet.getInt("value"));
                        break;
                    case "skeleton_horse":
                        skeletonhorse.put(uuid, resultSet.getInt("value"));
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            AywenCraftPlugin.getInstance().getLogger().severe("Unable to load advancements");
        }
    }

    private boolean hasReached(Player player, int value) {
        return  spiders.get(player.getUniqueId()) >= value &&
                zombiehorse.get(player.getUniqueId()) >= value &&
                skeletonhorse.get(player.getUniqueId()) >= value;
    }

    private void grantAdvancements(Player player) {
        UltimateAdvancementAPI api = GuidelineManager.getAPI();
        if (hasReached(player, 1)) {
            api.getAdvancement("dream:helldivers/1").grant(player);
        } else if (hasReached(player, 10000)) {
            api.getAdvancement("dream:helldivers/6").grant(player);
        } else if (hasReached(player, 1000)) {
            api.getAdvancement("dream:helldivers/5").grant(player);
        } else if (hasReached(player, 500)) {
            api.getAdvancement("dream:helldivers/4").grant(player);
        } else if (hasReached(player, 100)) {
            api.getAdvancement("dream:helldivers/3").grant(player);
        } else if (hasReached(player, 10)) {
            api.getAdvancement("dream:helldivers/2").grant(player);
        }
    }

    public void close() {
        saveAdvancementData(spiders, "spider");
        saveAdvancementData(zombiehorse, "zombie_horse");
        saveAdvancementData(skeletonhorse, "skeleton_horse");
    }

    private void saveAdvancementData(HashMap<UUID, Integer> advancements, String advancementType) {
        AywenCraftPlugin.getInstance().getLogger().warning("Saving " + advancementType + " advancements");
        for (UUID player : advancements.keySet()) {
            try {
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO advancements (player, advancement, value) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE value = ?");
                statement.setString(1, player.toString());
                statement.setString(2, advancementType);
                statement.setInt(3, advancements.get(player));
                statement.setInt(4, advancements.get(player));
                statement.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
                AywenCraftPlugin.getInstance().getLogger().severe("Unable to save " + advancementType + " advancements");
            }
        }
    }


    @EventHandler
    void onEntityDeath(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        if (!entity.getWorld().getName().equals("dreamworld")) return;

        if (!(event.getDamageSource().getCausingEntity() instanceof Player player)) return;
        switch (entity.getType()) {
            case SPIDER:
                increment(spiders, player.getUniqueId());
                break;
            case ZOMBIE_HORSE:
                increment(zombiehorse, player.getUniqueId());
                break;
            case SKELETON_HORSE:
                increment(skeletonhorse, player.getUniqueId());
                break;
        }

        grantAdvancements(player);
    }
}
