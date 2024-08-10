package fr.communaywen.core.space.rocket;

import dev.lone.itemsadder.api.CustomEntity;
import fr.communaywen.core.AywenCraftPlugin;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.joml.Math;

import java.util.Random;

public class LaunchAnim {

    private final CustomEntity rocket;
    private final Player player;

    public LaunchAnim(CustomEntity rocket, Player player) {
        this.rocket = rocket;
        this.player = player;
    }

    public void launch() {
        player.setFlySpeed(0);
        player.setGameMode(org.bukkit.GameMode.SPECTATOR);
        rocket.playAnimation("launch");

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
                    Random r = new Random();
                    int range = 1000;
                    int x = r.nextInt(range - (range * -1)) + (range * -1);
                    int y = r.nextInt(range - (range * -1)) + (range * -1);

                    Chunk chunk = Bukkit.getWorld("moon").getChunkAt(x, y);
                    if(!chunk.isLoaded()) {
                        chunk.load();
                    }
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            player.setGameMode(org.bukkit.GameMode.SURVIVAL);
                            player.setFlySpeed(0.1f);
                            player.teleport(new Location(Bukkit.getWorld("moon"), x, 100, y));
                            AywenCraftPlugin.getInstance().getLogger().info(player.getName()+" joined the moon dimension.");
                        }
                    }.runTaskLater(AywenCraftPlugin.getInstance(), 15);
                }
                Location loc = rocket.getLocation();
                double x = loc.getX() + 5;
                double y = loc.getY()+ticks/10.0;
                double z = loc.getZ();
                player.teleport(new Location(loc.getWorld(), x, y, z, 90, 0));
                ticks++;
            }
        }.runTaskTimer(AywenCraftPlugin.getInstance(), 0, 1);

    }
}
