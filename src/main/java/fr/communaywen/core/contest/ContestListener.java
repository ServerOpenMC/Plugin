package fr.communaywen.core.contest;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.managers.storage.StorageException;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import fr.communaywen.core.AywenCraftPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static fr.communaywen.core.contest.ContestManager.getTradeSelected;


public class ContestListener implements Listener {
    private BukkitRunnable eventRunnable;

    public ContestListener(AywenCraftPlugin plugin) {
        eventRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                String worldsName = "world";
                String regionsName = "spawn";
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E", Locale.FRENCH);
                DayOfWeek dayStartContestOfWeek = DayOfWeek.from(formatter.parse(ContestManager.getString("startdate")));

                if (ContestManager.getInt("contest","phase") == 1 && ContestManager.getCurrentDayOfWeek().getValue() == dayStartContestOfWeek.getValue()) {
                    ContestManager.updateColumnInt("contest", "phase", 2);
                    Bukkit.broadcastMessage(

                            "§8§m                                                     §r\n" +
                                    "§7\n" +
                                    "§6§lCONTEST!§r §7 Les votes sont ouverts !§7" +
                                    "§7\n" +
                                    "§8§o*on se retrouve au spawn pour pouvoir voter ou /contest...*\n" +
                                    "§7\n" +
                                    "§8§m                                                     §r"
                    );

                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.playSound(player.getEyeLocation(), Sound.BLOCK_AMETHYST_BLOCK_RESONATE, 1.0F, 0.2F);
                    }

                    org.bukkit.World world = Bukkit.getWorld(worldsName);
                    com.sk89q.worldedit.world.World wgWorld = BukkitAdapter.adapt(world);

                    RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
                    RegionManager regions = container.get(wgWorld);
                    ProtectedRegion region = regions.getRegion(regionsName);


                    region.setFlag(Flags.TIME_LOCK, "12700");

                    try {
                        regions.save();
                    } catch (StorageException e) {
                        throw new RuntimeException(e);
                    }
                }
                int dayStart = dayStartContestOfWeek.getValue() + 1;
                if (ContestManager.getInt("contest","phase") == 2 && ContestManager.getCurrentDayOfWeek().getValue() == dayStart) {
                    ResultSet rs1 = getTradeSelected(true);
                    try {
                        while(rs1.next()) {
                            ContestManager.updateColumnBooleanFromRandomTrades(false, rs1.getString("ress"));
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    ResultSet rs2 = getTradeSelected(false);
                    try {

                        while(rs2.next()) {
                            ContestManager.updateColumnBooleanFromRandomTrades(true, rs2.getString("ress"));
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }


                    ContestManager.updateColumnInt("contest", "phase", 3);
                    Bukkit.broadcastMessage(

                            "§8§m                                                     §r\n" +
                                    "§7\n" +
                                    "§6§lCONTEST!§r §7Les contributions commencent!§7" +
                                    "§7\nEchanger des ressources contre des Coquillages de Contest. Récoltez en un max et déposez les\n" +
                                    "§8§o*via la Borne des Contest ou /contest*\n" +
                                    "§7\n" +
                                    "§8§m                                                     §r"
                    );

                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.playSound(player.getEyeLocation(), Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, 1.0F, 0.3F);
                    }

                    org.bukkit.World world = Bukkit.getWorld(worldsName);
                    com.sk89q.worldedit.world.World wgWorld = BukkitAdapter.adapt(world);

                    RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
                    RegionManager regions = container.get(wgWorld);
                    ProtectedRegion region = regions.getRegion(regionsName);

                    region.setFlag(Flags.TIME_LOCK, "15000");

                    try {
                        regions.save();
                    } catch (StorageException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        };
        // tout les minutes
        eventRunnable.runTaskTimer(plugin, 0, 1200);
     };
}
