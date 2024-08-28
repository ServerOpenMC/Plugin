package fr.communaywen.core.listeners;

import dev.lone.itemsadder.api.CustomEntity;
import dev.lone.itemsadder.api.Events.CustomEntityDeathEvent;
import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import fr.communaywen.core.utils.ItemBuilder;
import org.bukkit.Material;
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

                if(player.getUniqueId().equals(UUID.fromString("e5056dba-daa5-4b4e-a08c-eac27a4d2c07"))){
                    player.playSound(entity.getLocation(), "omc_sounds:babyfuze.martinouxx", 1f, 1f);
                } else {
                    player.playSound(entity.getLocation(), "omc_sounds:babyfuze.salut", 1f, 1f);
                }
                break;
            default:
                break;
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        if(e.isCancelled()) { return; }

        Player player = e.getEntity();
        if (player.getWorld().getName().equals("dreamworld")) { return; }

        if (entities.containsKey(player.getUniqueId())) {
            CustomEntity customEntity = entities.get(player.getUniqueId());

            if(customEntity != null) {
                customEntity.destroy();
            }
        }
    }

    @EventHandler
    public void onCustomEntityDeah(CustomEntityDeathEvent e){
        Entity entity = e.getEntity();

        if(e.getNamespacedID().equalsIgnoreCase("omc_entities:babyfuze")){
            entity.getLocation().getWorld().dropItemNaturally(entity.getLocation(), new ItemBuilder(Material.PLAYER_HEAD).setSkullOwner("FuzeIII").setName("§6§lTête de Fuze").toItemStack());
        }
    }

}
