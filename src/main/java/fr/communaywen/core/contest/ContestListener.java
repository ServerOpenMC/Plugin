package fr.communaywen.core.contest;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.managers.storage.StorageException;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import dev.lone.itemsadder.api.FontImages.FontImageWrapper;
import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.mailboxes.MailboxManager;
import fr.communaywen.core.mailboxes.letter.LetterHead;
import fr.communaywen.core.mailboxes.menu.PlayerMailbox;
import fr.communaywen.core.mailboxes.utils.MailboxMenuManager;
import fr.communaywen.core.utils.serializer.BukkitSerializer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.WritableBookMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.DayOfWeek;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static fr.communaywen.core.contest.ContestManager.getTradeSelected;
import static org.bukkit.Bukkit.getOfflinePlayers;


public class ContestListener implements Listener {
    private BukkitRunnable eventRunnable;;

    public ContestListener(AywenCraftPlugin plugin, FileConfiguration eventConfig) {
        eventRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                String worldsName = "world";
                String regionsName = "spawn";
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E", Locale.FRENCH);
                DayOfWeek dayStartContestOfWeek = DayOfWeek.from(formatter.parse(ContestManager.getString("startdate")));

                System.out.println(DayOfWeek.from(formatter.parse("lun.")).getValue());
                System.out.println(DayOfWeek.from(formatter.parse("dim.")).getValue());

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

                    World world = Bukkit.getWorld(worldsName);
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
                    System.out.println("[CONTEST] Ouverture des votes");
                }
                int dayStart = dayStartContestOfWeek.getValue() + 1;
                if (dayStart==8) {dayStart=1;}
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

                    FileConfiguration config = plugin.getConfig();
                    ConfigurationSection boostEvents = config.getConfigurationSection("contest.boost_event");

                    if  (boostEvents != null) {
                        for (String event : boostEvents.getKeys(false)) {
                            ConfigurationSection eventInfo = boostEvents.getConfigurationSection(event);
                            Random random = new Random();
                            int boost = random.nextInt(5,25);
                            plugin.getConfig().set("contest.boost_event."+ event +".boost", boost);
                            try {
                                plugin.getConfig().save(new File(plugin.getDataFolder(), "config.yml"));
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                            String probaCode = null;
                            if (eventInfo != null) {
                                probaCode = eventInfo.getString("probaCode");
                            }

                            if (probaCode != null) {
                                double currentProba = Double.parseDouble(eventConfig.get(probaCode).toString());
                                double addboost = (double) boost / 100;
                                double newProba = currentProba + addboost;
                                DecimalFormat df = new DecimalFormat("#.#");
                                newProba = Double.valueOf(df.format(newProba));
                                eventConfig.set(eventInfo.getString("probaCode"), newProba);
                                try {
                                    eventConfig.save(new File(plugin.getDataFolder(), "events.yml"));
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }


                            }
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

                    World world = Bukkit.getWorld(worldsName);
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
                    System.out.println("[CONTEST] Ouverture des trades");
                }
                int dayEnd = dayStart + 2;
                if (dayEnd>=8) {
                    dayEnd=1;
                } //attention ne pas modifier les valeurs de départ des contest sinon le systeme va broke
                if (ContestManager.getInt("contest","phase") == 3 && ContestManager.getCurrentDayOfWeek().getValue() == dayEnd) {
                    ContestManager.updateColumnInt("contest", "phase", 4); //tempo mettre sql qui delete lai ligne

                    ResultSet rs1 = ContestManager.getAllPlayer();
                    try {
                        while(rs1.next()) {
                            OfflinePlayer player = Bukkit.getOfflinePlayer(rs1.getString("name"));
                            String playerCampName = ContestManager.getOfflinePlayerCampName(player);
                            ChatColor playerCampColor = ContestManager.getOfflinePlayerCampChatColor(player);
                            String camp1Name = ContestManager.getString("camp1");
                            String camp2Name = ContestManager.getString("camp2");
                            String camp1Color = ContestManager.getString("color1");
                            String camp2Color = ContestManager.getString("color2");
                            ChatColor color1 = ChatColor.valueOf(camp1Color);
                            ChatColor color2 = ChatColor.valueOf(camp2Color);



                            ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
                            BookMeta bookMeta = (BookMeta) book.getItemMeta();
                            bookMeta.setTitle("Les Résultats du Contest");
                            bookMeta.setAuthor("Les Contest");

                            List<String> lore = new ArrayList<String>();
                            lore.add(color1 + camp1Name + " §7VS " + color2 + camp2Name);
                            lore.add("§e§lOuvrez ce livre pour en savoir plus!");
                            bookMeta.setLore(lore);
                            int points1 = ContestManager.getInt("contest", "points1");
                            int points2 = ContestManager.getInt("contest", "points2");
                            int totalpoint = points1 + points2;
                            int points1Taux = (points1/totalpoint)*100;
                            DecimalFormat df = new DecimalFormat("#.#");
                            points1Taux = Integer.valueOf(df.format(points1Taux));
                            int points2Taux = (points2/totalpoint)*100;
                            points2Taux = Integer.valueOf(df.format(points2Taux));
                            int vote1 = ContestManager.getVoteTaux(1);
                            int vote2 = ContestManager.getVoteTaux(2);
                            int totalvote = vote1 + vote2;
                            int vote1Taux = (vote1/totalvote)*100;
                            int vote2Taux = (vote2/totalvote)*100;


                            if (points1 > points2) {
                                bookMeta.addPage("§8§lStatistiques Globales \n§0Gagnant : " + color1 + camp1Name+ "\n§0Taux de vote : §8" + vote1Taux + "%\n§0Taux de Points : §8" + points1Taux + "%\n\n" + "§0Perdant : " + color2 + camp2Name+ "\n§0Taux de vote : §8" + vote2Taux + "%\n§0Taux de Points : §8" + points2Taux + "%\n\n\n§8§oProchaine page : Classement des 10 Meilleurs Contributeur");
                            }
                            if (points2 > points1) {
                                bookMeta.addPage("§8§lStatistiques Globales \n§0Gagnant : " + color2 + camp2Name+ "\n§0Taux de vote : §8" + vote2Taux + "%\n§0Taux de Points : §8" + points2Taux + "%\n\n" + "§0Perdant : " + color1 + camp1Name+ "\n§0Taux de vote : §8" + vote1Taux + "%\n§0Taux de Points : §8" + points1Taux + "%\n\n\n§8§oProchaine page : Classement des 10 Meilleurs Contributeur");
                            }

                            String leaderboard = "§8§lLe Classement du Contest (Jusqu'au 10eme)";
                            int rankInt = 0;

                            ResultSet rs2 = ContestManager.getAllPlayerOrdered();
                            try {
                                while (rs2.next()) {
                                    System.out.println(rs2.getString("name"));
                                    OfflinePlayer player2 = Bukkit.getOfflinePlayer(rs2.getString("name"));
                                    ChatColor playerCampColor2 = ContestManager.getOfflinePlayerCampChatColor(player2);
                                    if (rankInt >= 10) {
                                        break;
                                    }
                                    String rankStr = "\n§0#" + ContestManager.getRankPlayerInContest(player2)+ " " + playerCampColor2 + rs2.getString("name") + " §8- §b" + rs2.getString("point_dep");
                                    leaderboard = leaderboard + rankStr;
                                    rankInt++;
                                }
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                            bookMeta.addPage(leaderboard);
                            bookMeta.addPage("§8§lStatistiques Personnelles\n§0Votre camp : " + playerCampColor+ playerCampName + "\n§0Votre Rang sur Le Contest : §8#" + ContestManager.getRankPlayerInContest(player)+ "\n§0Points Déposés : §b" + rs1.getString("point_dep"));
                            book.setItemMeta(bookMeta);

                            List<ItemStack> itemlist = new ArrayList<>();
                            itemlist.add(book);

                            //ajouter les recompenses comme argent, loot box, lucky block
                            ItemStack[] items = itemlist.toArray(new ItemStack[itemlist.size()]);
                            MailboxManager.sendItemsToAOfflinePlayer(player, items);
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    //delete camps, contest
                    //add 1 selected to all_contest
                    //select contest

                    FileConfiguration config = plugin.getConfig();
                    ConfigurationSection boostEvents = config.getConfigurationSection("contest.boost_event");

                    if  (boostEvents != null) {
                        for (String event : boostEvents.getKeys(false)) {
                            ConfigurationSection eventInfo = boostEvents.getConfigurationSection(event);
                            // reset
                            String probaCode = null;
                            if (eventInfo != null) {
                                probaCode = eventInfo.getString("probaCode");
                            }

                            if (probaCode != null) {
                                int boost = Integer.parseInt(plugin.getConfig().get("contest.boost_event."+event+".boost").toString());
                                double currentProba = Double.parseDouble(eventConfig.get(probaCode).toString());
                                double removeboost = (double) boost / 100;
                                double newProba = currentProba - removeboost;
                                DecimalFormat df = new DecimalFormat("#.#");
                                newProba = Double.valueOf(df.format(newProba));
                                eventConfig.set(eventInfo.getString("probaCode"), newProba);
                                try {
                                    eventConfig.save(new File(plugin.getDataFolder(), "events.yml"));
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }

                            plugin.getConfig().set("contest.boost_event."+ event +".boost", 0);
                            try {
                                plugin.getConfig().save(new File(plugin.getDataFolder(), "config.yml"));
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }

                    Bukkit.broadcastMessage(

                            "§8§m                                                     §r\n" +
                                    "§7\n" +
                                    "§6§lCONTEST!§r §7Time over! §7" +
                                    "§7\nFin du Contest, retrouvez vos récompenses et le bilan de ce Contest\n" +
                                    "§7sous forme de livre\n" +
                                    "§8§o*/contest pour voir quand le prochain contest arrive*\n" +
                                    "§7\n" +
                                    "§8§m                                                     §r"
                    );

                    System.out.println("[CONTEST] Fermeture du Contest");
                }
            }
        };
        // tout les minutes
        eventRunnable.runTaskTimer(plugin, 0, 1200);
     };
}
