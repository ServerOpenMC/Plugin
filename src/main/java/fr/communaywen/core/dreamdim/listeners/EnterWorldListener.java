package fr.communaywen.core.dreamdim.listeners;

import fr.communaywen.core.AywenCraftPlugin;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Objects;

public class EnterWorldListener implements Listener {

    World dreamworld;
    AywenCraftPlugin plugin;
    HashMap<Player, BossBar> bossbars = new HashMap<>();

    public EnterWorldListener(AywenCraftPlugin plugin) {
        this.plugin = plugin;
        dreamworld = Bukkit.getWorld("dreamworld");
    }

    public void teleportBack(Player p) {
        p.sendMessage("C'était juste un mauvais rêve");
        BossBar bossBar = bossbars.get(p);
        if (bossBar != null) {
            bossBar.removeAll();
            bossbars.remove(p);
        }

        p.clearActivePotionEffects();
        p.teleport(Objects.requireNonNullElse(
                p.getRespawnLocation(),
                Bukkit.getWorld("world").getSpawnLocation()
        ));
    }

    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent e) {
        if (e.getPlayer().getWorld() == dreamworld) { return; }
        Player p = e.getPlayer();
        BossBar bossBar = bossbars.get(p);
        if (bossBar == null) { return; }
        bossBar.removeAll();
        bossbars.remove(p);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        //Même chose que quand le joueur meurt
        teleportBack(e.getPlayer());
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        World world = p.getWorld();

        if (world.getName().equals("dreamworld")) {
            //TODO: Supprimer tout les objets qui viennent du rêves
            p.setHealth(2);
            teleportBack(p);
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onChangeWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        if (!player.getWorld().equals(dreamworld)){ return; }

        BossBar timer = Bukkit.createBossBar(
                "Rêve",
                BarColor.WHITE,
                BarStyle.SOLID
        );
        timer.addFlag(BarFlag.CREATE_FOG);
        timer.addFlag(BarFlag.DARKEN_SKY);
        timer.addPlayer(player);

        bossbars.put(player, timer);

        final int[] timeElapsed = {0};
        int totalTime = 300; //5min = 300s

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!bossbars.containsKey(player)) { this.cancel(); }

                if (timeElapsed[0] == totalTime){
                    teleportBack(player);
                    this.cancel();
                }

                double progress = (double)(totalTime - timeElapsed[0]) / totalTime;
                timer.setProgress(progress);
                timeElapsed[0] = timeElapsed[0] + 1;
            }
        }.runTaskTimer(this.plugin, 0, 20);
    }
}
