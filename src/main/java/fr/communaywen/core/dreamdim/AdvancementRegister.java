package fr.communaywen.core.dreamdim;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.dreamdim.advancements.*;
import fr.communaywen.core.utils.Transaction;
import fr.communaywen.core.utils.database.DatabaseConnector;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class AdvancementRegister extends DatabaseConnector implements Listener {


    public HashMap<UUID, HashMap<String, Integer>> advancements = new HashMap<>();

    @Getter
    AywenCraftPlugin plugin;

    /**
     * @param player Le joueur qui recevra le succès
     * @param namespace Le NameSpace du succès
     * @param criteria Le critère à accordée
     * @return Si le succès a été accordée
     */
    public boolean grantAdvancement(Player player, String namespace, String criteria) {
        NamespacedKey key = NamespacedKey.fromString(namespace);
        if (key == null) { return false; }

        Advancement advancement = Bukkit.getAdvancement(key);
        if (advancement == null) { return false; }

        return player.getAdvancementProgress(advancement).awardCriteria(criteria);
    }

    /**
     * @param player Le joueur qui recevra le succès
     * @param namespace Le NameSpace du succès
     * @return Si le succès a été accordée
     */
    public boolean grantAdvancement(Player player, String namespace) {
        NamespacedKey key = NamespacedKey.fromString(namespace);
        if (key == null) { return false; }

        Advancement advancement = Bukkit.getAdvancement(key);
        if (advancement == null) { return false; }

        return player.getAdvancementProgress(advancement).awardCriteria("requirement");
    }

    public AdvancementRegister(AywenCraftPlugin plugin) {
        this.plugin = plugin;
        plugin.registerEvents(
                this,
                new CloudBed(this),
                new SweetChild(this),
                new Helldivers(this),
                new OnCookFish()
        );
    }

    /* REWARDS */
    @EventHandler
    public void onPlayerAchieve(PlayerAdvancementDoneEvent event) {
        String namespace = event.getAdvancement().getKey().toString(); //ex: aywen:root
        Player player = event.getPlayer();

        switch (namespace) {
            case "aywen:helldivers/hd1":
                giveMoney(player, 1, "Advancements HD1");
                break;
            case "aywen:helldivers/hd2":
                giveMoney(player, 10, "Advancements HD2");
                break;
            case "aywen:helldivers/hd3":
                giveMoney(player, 100, "Advancements HD3");
                break;
            case "aywen:helldivers/hd4":
                giveMoney(player, 500, "Advancements HD4");
                break;
            case "aywen:helldivers/hd5":
                giveMoney(player, 1000, "Advancements HD5");
                break;
            case "aywen:helldivers/hd6":
                giveMoney(player, 10000, "Advancements HD6");
                break;
            case "aywen:sleep_on_cloud":
                giveMoney(player, 500, "Advancements Sleep On Cloud");
                break;
            case "aywen:bed_sweet_bed":
                giveMoney(player, 100, "Advancements Bed Sweet Bed");
                break;
            case "aywen:pit_of_dreams":
                giveMoney(player, 500, "Advancements Pit Of Dreams");
                break;
            case "aywen:leave_earth":
                giveMoney(player, 500, "Advancements Leave Earth");
                break;
            case "aywen:dreamrush":
                giveMoney(player, 500, "Advancements Ruée vers le rêve");
                break;
            case "aywen:fishing/docker":
                giveMoney(player, 500, "Advancements Dockerfish");
                break;
            case "aywen:fishing/poissonion":
                giveMoney(player, 500, "Advancements Poissonion");
                break;
            case "aywen:fishing/moonfish":
                giveMoney(player, 500, "Advancements Moonfish");
                break;
            case "aywen:fishing/comme_un_onion":
                giveMoney(player, 500, "Advancements Comme un Onion");
                break;
            case "aywen:fishing/poissoleil":
                giveMoney(player, 500, "Advancements Poisson-soleil");
                break;

        }
    }

    public void giveMoney(Player recipient, Integer amount, String reason) {
        recipient.sendMessage("§aTu as complété un succès et récuperer "+amount.toString()+"$");
        plugin.getManagers().getTransactionsManager().addTransaction(new Transaction(
                recipient.getUniqueId().toString(),
                "CONSOLE",
                amount,
                reason
        ));
        plugin.getManagers().getEconomyManager().addBalance(recipient, amount);
    }

    /* SAUVEGARDE */
    public boolean savePlayer(UUID player) {
        HashMap<String, Integer> data = advancements.get(player);
        if (data == null) { return true; }

        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO advancements VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE value = VALUES(value);");
            for (String adv : data.keySet()) {
                statement.setString(1, player.toString());
                statement.setString(2, adv);
                statement.setInt(3, data.get(adv));

                statement.addBatch();
            }

            statement.executeBatch();
            statement.close();
            return true;
        } catch (Exception err) {
            err.printStackTrace();
            plugin.getLogger().severe("Failed to save advancement for " + player);
            return false;
        }
    }

    public void saveAll() {
        for (UUID player : advancements.keySet()) {
            savePlayer(player);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM advancements WHERE player = ?");
            statement.setString(1, player.toString());
            ResultSet rs = statement.executeQuery();
            advancements.put(player.getUniqueId(), new HashMap<>());

            if (rs.getFetchSize() == 0) {
                for (String key: List.of("zombie_horse", "spider", "skeleton_horse")){
                    advancements.get(player.getUniqueId()).put(key, 0);
                }
                return;
            }

            while (rs.next()) {
                advancements.get(player.getUniqueId()).put(rs.getString("advancement"), rs.getInt("value"));
            }
        } catch (Exception err) {
            err.printStackTrace();
            player.kick(Component.text("Impossible de charger vos succès, Veuillez réessayer"));
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        savePlayer(event.getPlayer().getUniqueId());
    }
    /* ---------- */
}
