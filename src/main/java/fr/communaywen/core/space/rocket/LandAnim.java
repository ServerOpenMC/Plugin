package fr.communaywen.core.space.rocket;

import dev.lone.itemsadder.api.CustomEntity;
import fr.communaywen.core.AywenCraftPlugin;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class LandAnim {

    private final CustomEntity rocket;
    private final Player player;

    public LandAnim(CustomEntity rocket, Player player) {
        this.rocket = rocket;
        this.player = player;
    }

    public void launch() {
        player.setFlySpeed(0);
        player.setGameMode(org.bukkit.GameMode.SPECTATOR);
        rocket.playAnimation("animation.rocket.land");

        //tp the player on each ticks for 3s
        new BukkitRunnable() {
            private int ticks = 0;
            @Override
            public void run() {
                if(ticks >= 85) {
                    this.cancel();
                    rocket.destroy();
                    return;
                }
                if(ticks == 65) {
                    player.sendTitle(PlaceholderAPI.setPlaceholders(player, "§0%img_screeneffect%"), "§r", 20, 10, 10);
                    int highestBlock = Bukkit.getWorld("moon").getHighestBlockYAt(rocket.getLocation().getBlockX(), rocket.getLocation().getBlockZ());
                    Location loc = new Location(Bukkit.getWorld("moon"), rocket.getLocation().getBlockX(), highestBlock+1, rocket.getLocation().getBlockZ());
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            this.cancel();
                            player.setGameMode(org.bukkit.GameMode.SURVIVAL);
                            player.setFlySpeed(0.1f);
                            player.teleport(loc);
                            CustomEntity.spawn("space:rocket", loc).playAnimation("animation.rocket.idle");

                        }
                    }.runTaskLater(AywenCraftPlugin.getInstance(), 15);
                }
                Location loc = rocket.getLocation();
                double x = loc.getX();
                double y = loc.getY()-ticks/7.0;
                double z = loc.getZ() - 5;
                player.teleport(new Location(loc.getWorld(), x, y, z, 0, 0));
                ticks++;
            }
        }.runTaskTimer(AywenCraftPlugin.getInstance(), 0, 1);

    }
}
