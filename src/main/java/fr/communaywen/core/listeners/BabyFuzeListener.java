package fr.communaywen.core.listeners;

import dev.lone.itemsadder.api.CustomEntity;
import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Feature("Bébé Fuze")
@Credit({"Martinouxx","WOLFLEVRAI"})
public class BabyFuzeListener implements Listener {

    private final Map<UUID, CustomEntity> entities = new HashMap<>();

    @EventHandler
    public void onVillagerDeath(EntityDeathEvent e) {
        if (!(e.getEntity() instanceof Villager)) return;
        if (e.getEntity().getKiller() == null) return;

        Player player = e.getEntity().getKiller();

        Random random = new Random();
        int alea = random.nextInt(9);

        switch (alea) {
            case 0:
                CustomEntity entity = CustomEntity.spawn("omc_entities:babyfuze", player.getLocation());
                entities.put(player.getUniqueId(), entity);
                break;
            default:
                break;
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();

        if (entities.containsKey(player.getUniqueId())) {
            CustomEntity customEntity = entities.get(player.getUniqueId());

            if(customEntity != null) {
                customEntity.destroy();
            }
        }
    }

}
