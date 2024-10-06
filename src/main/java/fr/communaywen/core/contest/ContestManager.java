package fr.communaywen.core.contest;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.world.weather.WeatherType;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.managers.storage.StorageException;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.economy.EconomyManager;
import fr.communaywen.core.luckyblocks.utils.LBUtils;
import fr.communaywen.core.mailboxes.MailboxManager;
import fr.communaywen.core.utils.database.DatabaseConnector;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.DayOfWeek;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static fr.communaywen.core.mailboxes.utils.MailboxUtils.*;

public class ContestManager extends DatabaseConnector {
    static FileConfiguration config;
    static AywenCraftPlugin plugins;
    private static final ArrayList<String> colorContest = new ArrayList<>();
    public ContestManager(AywenCraftPlugin plugin) {
        config = plugin.getConfig();
        plugins = plugin;
        colorContest.add("WHITE");
        colorContest.add("YELLOW");
        colorContest.add("LIGHT_PURPLE");
        colorContest.add("RED");
        colorContest.add("AQUA");
        colorContest.add("GREEN");
        colorContest.add("BLUE");
        colorContest.add("DARK_GRAY");
        colorContest.add("GRAY");
        colorContest.add("GOLD");
        colorContest.add("DARK_PURPLE");
        colorContest.add("DARK_AQUA");
        colorContest.add("DARK_RED");
        colorContest.add("DARK_GREEN");
        colorContest.add("DARK_BLUE");
        colorContest.add("BLACK");
    }

    //PHASE 1
    public static void initPhase1() {
        String worldsName = (String) config.get("contest.config.worldName");
        String regionsName = (String) config.get("contest.config.spawnRegionName");
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
        region.setFlag(Flags.WEATHER_LOCK, WeatherType.REGISTRY.get("clear"));

        try {
            regions.save();
        } catch (StorageException e) {
            throw new RuntimeException(e);
        }
        System.out.println("[CONTEST] Ouverture des votes");
    }
    //PHASE 2
    public static void initPhase2(JavaPlugin plugin, FileConfiguration eventConfig) {
        String worldsName = (String) config.get("contest.config.worldName");
        String regionsName = (String) config.get("contest.config.spawnRegionName");

        List<Map<String, Object>> selectedTrades = getTradeSelected(true);
        for (Map<String, Object> trade : selectedTrades) {
            ContestManager.updateColumnBooleanFromRandomTrades(false, (String) trade.get("ress"));
        }

        List<Map<String, Object>> unselectedTrades = getTradeSelected(false);
        for (Map<String, Object> trade : unselectedTrades) {
            ContestManager.updateColumnBooleanFromRandomTrades(true, (String) trade.get("ress"));
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

                    newProba = Double.parseDouble(df.format(newProba).replace(",", "."));

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
        region.setFlag(Flags.WEATHER_LOCK, WeatherType.REGISTRY.get("clear"));

        try {
            regions.save();
        } catch (StorageException e) {
            throw new RuntimeException(e);
        }
        System.out.println("[CONTEST] Ouverture des trades");
    }
    //PHASE 3
    public static void initPhase3(JavaPlugin plugin, FileConfiguration eventConfig) {
        String worldsName = (String) config.get("contest.config.worldName");
        String regionsName = (String) config.get("contest.config.spawnRegionName");
        ContestManager.updateColumnInt("contest", "phase", 4);

        // GET GLOBAL CONTEST INFORMATION
        String camp1Color = ContestManager.getColor1Cache();
        String camp2Color = ContestManager.getColor2Cache();
        ChatColor color1 = ColorConvertor.getReadableColor(ChatColor.valueOf(camp1Color));
        ChatColor color2 = ColorConvertor.getReadableColor(ChatColor.valueOf(camp2Color));
        String camp1Name = ContestManager.getCamp1Cache();
        String camp2Name = ContestManager.getCamp2Cache();

        //CREATE PART OF BOOK
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta bookMeta = (BookMeta) book.getItemMeta();
        bookMeta.setTitle("Les Résultats du Contest");
        bookMeta.setAuthor("Les Contest");

        List<String> lore = new ArrayList<String>();
        lore.add(color1 + camp1Name + " §7VS " + color2 + camp2Name);
        lore.add("§e§lOuvrez ce livre pour en savoir plus!");
        bookMeta.setLore(lore);

        // GET VOTE AND POINT TAUX
        int points1 = ContestManager.getInt("contest", "points1");
        int points2 = ContestManager.getInt("contest", "points2");
        int totalpoint = points1 + points2;
        int points1Taux = (int) (((double) points1 / totalpoint) * 100);
        DecimalFormat df = new DecimalFormat("#.#");
        points1Taux = Integer.valueOf(df.format(points1Taux));
        int points2Taux = (int) (((double) points2 / totalpoint) * 100);
        points2Taux = Integer.valueOf(df.format(points2Taux));
        int vote1 = ContestManager.getVoteTaux(1);
        int vote2 = ContestManager.getVoteTaux(2);
        int totalvote = vote1 + vote2;
        int vote1Taux = (int) (((double) vote1 / totalvote) * 100);
        int vote2Taux = (int) (((double) vote2 / totalvote) * 100);

        //PRINT ON BOOK
        if (points1 > points2) {
            bookMeta.addPage("§8§lStatistiques Globales \n§0Gagnant : " + color1 + camp1Name+ "\n§0Taux de vote : §8" + vote1Taux + "%\n§0Taux de Points : §8" + points1Taux + "%\n\n" + "§0Perdant : " + color2 + camp2Name+ "\n§0Taux de vote : §8" + vote2Taux + "%\n§0Taux de Points : §8" + points2Taux + "%\n\n\n§8§oProchaine page : Classement des 10 Meilleurs Contributeur");
        }
        if (points2 > points1) {
            bookMeta.addPage("§8§lStatistiques Globales \n§0Gagnant : " + color2 + camp2Name+ "\n§0Taux de vote : §8" + vote2Taux + "%\n§0Taux de Points : §8" + points2Taux + "%\n\n" + "§0Perdant : " + color1 + camp1Name+ "\n§0Taux de vote : §8" + vote1Taux + "%\n§0Taux de Points : §8" + points1Taux + "%\n\n\n§8§oProchaine page : Classement des 10 Meilleurs Contributeur");
        }

        //PRINT LEADERBOARD
        String leaderboard = "§8§lLe Classement du Contest (Jusqu'au 10eme)";
        int rankInt = 0;

        ResultSet rs2 = ContestManager.getAllPlayerOrdered();
        try {
            while (rs2.next()) {
                OfflinePlayer player2 = Bukkit.getOfflinePlayer(rs2.getString("name"));
                ChatColor playerCampColor2 = ColorConvertor.getReadableColor(ContestManager.getOfflinePlayerCampChatColor(player2));
                if (rankInt >= 10) {
                    break;
                }
                String rankStr = "\n§0#" + (rankInt+1) + " " + playerCampColor2 + rs2.getString("name") + " §8- §b" + rs2.getString("point_dep");
                leaderboard = leaderboard + rankStr;
                rankInt++;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        bookMeta.addPage(leaderboard);

        //PRINT FOR EACH PLAYER
        ResultSet rs1 = ContestManager.getAllPlayer();
        try {
            while(rs1.next()) {
                String playerName = rs1.getString("name");
                OfflinePlayer player = Bukkit.getOfflinePlayer(playerName);
                String playerCampName = ContestManager.getOfflinePlayerCampName(player);
                ChatColor playerCampColor = ColorConvertor.getReadableColor(ContestManager.getOfflinePlayerCampChatColor(player));

                bookMeta.addPage("§8§lStatistiques Personnelles\n§0Votre camp : " + playerCampColor+ playerCampName + "\n§0Votre Grade sur Le Contest §8: " + playerCampColor + ContestManager.getRankContestFroOffline(player) + playerCampName + "\n§0Votre Rang sur Le Contest : §8#" + ContestManager.getRankPlayerInContest(player)+ "\n§0Points Déposés : §b" + rs1.getString("point_dep"));

                int money = 0;
                int lucky = 0;
                ItemStack luckyblock = LBUtils.getLuckyBlockItem();
                if(ContestManager.hasWinInCampForOfflinePlayer(player)) {
                    int moneyMin = 12000;
                    int moneyMax = 14000;
                    double multi = ContestManager.getMultiMoneyFromRang(ContestManager.getRankContestFromOfflineInt(player));
                    moneyMin = (int) (moneyMin * multi);
                    moneyMax = (int) (moneyMax * multi);

                    money = ContestManager.giveRandomly(moneyMin, moneyMax);
                    EconomyManager.addBalanceOffline(player, money);

                    int luckyMin = 3;
                    int luckyMax = 6;
                    double multi2 = ContestManager.getMultiLuckyFromRang(ContestManager.getRankContestFromOfflineInt(player));

                    luckyMin = (int) (luckyMin * multi2);
                    luckyMax = (int) (luckyMax * multi2);

                    lucky = ContestManager.giveRandomly(luckyMin, luckyMax);
                    lucky = Math.round(lucky);
                } else {
                    int moneyMin = 4000;
                    int moneyMax = 6000;
                    double multi = ContestManager.getMultiMoneyFromRang(ContestManager.getRankContestFromOfflineInt(player));
                    moneyMin = (int) (moneyMin * multi);
                    moneyMax = (int) (moneyMax * multi);

                    money = ContestManager.giveRandomly(moneyMin, moneyMax);
                    EconomyManager.addBalanceOffline(player, money);

                    int luckyMin = 1;
                    int luckyMax = 3;
                    double multi2 = ContestManager.getMultiLuckyFromRang(ContestManager.getRankContestFromOfflineInt(player));

                    luckyMin = (int) (luckyMin * multi2);
                    luckyMax = (int) (luckyMax * multi2);

                    lucky = ContestManager.giveRandomly(luckyMin, luckyMax);
                    lucky = Math.round(lucky);

                    EconomyManager.addBalanceOffline(player, money);
                }
                bookMeta.addPage("§8§lRécompenses\n§0+ " + money + "$ §b(x"+ ContestManager.getMultiMoneyFromRang(ContestManager.getRankContestFromOfflineInt(player)) +")\n§0+ " + lucky + " §6Lucky Block§b (x"+ ContestManager.getMultiLuckyFromRang(ContestManager.getRankContestFromOfflineInt(player)) + ")");
                book.setItemMeta(bookMeta);

                luckyblock.setAmount(lucky);

                List<ItemStack> itemlist = new ArrayList<>();
                itemlist.add(book);
                itemlist.add(luckyblock);

                ItemStack[] items = itemlist.toArray(new ItemStack[itemlist.size()]);
                MailboxManager.sendItemsToAOfflinePlayer(player, items);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ContestManager.addOneToLastContest(ContestManager.getCamp1Cache());
        ContestManager.deleteTableContest("contest");
        ContestManager.deleteTableContest("camps");
        ContestManager.selectRandomlyContest();

        //REMOVE MULTIPLICATEUR CONTEST
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
                    DecimalFormat df1 = new DecimalFormat("#.#");
                    newProba = Double.parseDouble(df1.format(newProba).replace(",", "."));
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
        Component message = Component.text("Vous avez reçu la lettre du Contest", NamedTextColor.DARK_GREEN)
                .append(Component.text("\nCliquez-ici", NamedTextColor.YELLOW))
                .clickEvent(getRunCommand("mail"))
                .hoverEvent(getHoverEvent("Ouvrir la mailbox"))
                .append(Component.text(" pour ouvrir la mailbox", NamedTextColor.GOLD));
        Bukkit.broadcast(message);

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.playSound(player.getEyeLocation(), Sound.ENTITY_ENDER_DRAGON_DEATH, 1.0F, 2F);
            initPlayerDataCache(player);
        }

        World world = Bukkit.getWorld(worldsName);
        com.sk89q.worldedit.world.World wgWorld = BukkitAdapter.adapt(world);

        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(wgWorld);
        ProtectedRegion region = regions.getRegion(regionsName);


        region.setFlag(Flags.TIME_LOCK, null);
        region.setFlag(Flags.WEATHER_LOCK, null);

        try {
            regions.save();
        } catch (StorageException e) {
            throw new RuntimeException(e);
        }

        System.out.println("[CONTEST] Fermeture du Contest");
    }

    //import from axeno
    public static boolean hasEnoughItems(Player player, Material item, int amount) {
        int totalItems = 0;
        ItemStack[] contents = player.getInventory().getContents();

        for (ItemStack is : contents) {
            if (is != null && is.getType() == item) {
                totalItems += is.getAmount();
            }
        }

        if (amount == 0) return false;
        return totalItems >= amount;
    }

    public static void removeItemsFromInventory(Player player, Material item, int quantity) {
        ItemStack[] contents = player.getInventory().getContents();
        int remaining = quantity;

        for (int i = 0; i < contents.length && remaining > 0; i++) {
            ItemStack stack = contents[i];
            if (stack != null && stack.getType() == item) {
                int stackAmount = stack.getAmount();
                if (stackAmount <= remaining) {
                    player.getInventory().setItem(i, null);
                    remaining -= stackAmount;
                } else {
                    stack.setAmount(stackAmount - remaining);
                    remaining = 0;
                }
            }
        }
    }

    private static String camp1Cache = null;
    private static long lastCamp1Update = 0;
    private static String camp2Cache = null;
    private static long lastCamp2Update = 0;
    private static String color1Cache = null;
    private static long lastColor1Update = 0;
    private static String color2Cache = null;
    private static long lastColor2Update = 0;
    private static String startDateCache = null;
    private static long lastStartDateUpdate = 0;
    private static Integer phaseCache = null;
    private static long lastPhaseUpdate = 0;
    private static final Map<UUID, ContestPlayerCache> playerCache = new HashMap<>();
    private static final long cacheDuration = 120000;

    public static String getCamp1Cache() {
        long currentTime = System.currentTimeMillis();

        if (camp1Cache == null || (currentTime - lastCamp1Update) > cacheDuration) {
            camp1Cache = ContestManager.getString("contest", "camp1");
            lastCamp1Update = currentTime;
        }

        return camp1Cache;
    }

    public static String getCamp2Cache() {
        long currentTime = System.currentTimeMillis();

        if (camp2Cache == null || (currentTime - lastCamp2Update) > cacheDuration) {
            camp2Cache = ContestManager.getString("contest", "camp2");
            lastCamp2Update = currentTime;
        }

        return camp2Cache;
    }

    public static String getColor1Cache() {
        long currentTime = System.currentTimeMillis();

        if (color1Cache == null || (currentTime - lastColor1Update) > cacheDuration) {
            color1Cache = ContestManager.getString("contest", "color1");
            lastColor1Update = currentTime;
        }

        return color1Cache;
    }

    public static String getColor2Cache() {
        long currentTime = System.currentTimeMillis();

        if (color2Cache == null || (currentTime - lastColor2Update) > cacheDuration) {
            color2Cache = ContestManager.getString("contest", "color2");
            lastColor2Update = currentTime;
        }

        return color2Cache;
    }
    public static int getPhaseCache() {
        long currentTime = System.currentTimeMillis();
        if (phaseCache == null || (currentTime - lastPhaseUpdate) > cacheDuration) {
            phaseCache = ContestManager.getInt("contest", "phase");
            lastPhaseUpdate = currentTime;
        }
        return phaseCache;
    }
    public static String getStartDateCache() {
        long currentTime = System.currentTimeMillis();

        if (startDateCache == null || (currentTime - lastStartDateUpdate) > cacheDuration) {
            startDateCache = ContestManager.getString("contest", "startdate");
            lastStartDateUpdate = currentTime;
        }

        return startDateCache;
    }

    public static void initPlayerDataCache(Player player) {
        UUID playerUUID = player.getUniqueId();

        String sql = "SELECT * FROM camps WHERE minecraft_uuid = ?";
        try (PreparedStatement states = connection.prepareStatement(sql)) {
            states.setString(1, playerUUID.toString());
            ResultSet result = states.executeQuery();
            if (result.next()) {
                int points = result.getInt("point_dep");
                int camp = result.getInt("camps");
                String color = ContestManager.getString("contest","color" + camp);
                ChatColor campColor = ChatColor.valueOf(color);

                playerCache.put(playerUUID, new ContestPlayerCache(points, camp, campColor));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static int getPlayerPointsCache(Player player) {
        UUID playerUUID = player.getUniqueId();
        ContestPlayerCache cache = playerCache.get(playerUUID);

        if (cache != null && !cache.isCacheNull(cacheDuration)) {
            return cache.getPoints();
        } else {
            initPlayerDataCache(player);
            if (cache!=null) {
                return cache.getPoints();
            }
            return 0;
        }
    }

    public static int getPlayerPoints(Player player) {
        UUID playerUUID = player.getUniqueId();

        String sql = "SELECT * FROM camps WHERE minecraft_uuid = ?";
        try (PreparedStatement states = connection.prepareStatement(sql)) {
            states.setString(1, playerUUID.toString());
            ResultSet result = states.executeQuery();
            if (result.next()) {
                int points = result.getInt("point_dep");

                return points;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    public static int getPlayerCampsCache(Player player) {
        UUID playerUUID = player.getUniqueId();
        ContestPlayerCache cache = playerCache.get(playerUUID);

        if (cache != null && !cache.isCacheNull(cacheDuration)) {
            return cache.getCamp();
        } else {
            initPlayerDataCache(player);
            if (cache!=null) {
                return cache.getCamp();
            }
            return 0;
        }
    }
    public static ChatColor getPlayerColorCache(Player player) {
        UUID playerUUID = player.getUniqueId();
        ContestPlayerCache cache = playerCache.get(playerUUID);

        if (cache != null && !cache.isCacheNull(cacheDuration)) {
            return cache.getColor();
        } else {
            initPlayerDataCache(player);
            if (cache!=null) {
                return cache.getColor();
            }
            return null;
        }
    }

    public static int getInt(String table, String column) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM "+table);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt(column);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 999;
    }

    public static List<Map<String, Object>> getTradeSelected(boolean bool) {
        List<Map<?, ?>> contestTrades = config.getMapList("contest.contestTrades");

        List<Map<String, Object>> filteredTrades = contestTrades.stream()
                .filter(trade -> (boolean) trade.get("selected") == bool)
                .map(trade -> (Map<String, Object>) trade)
                .collect(Collectors.toList());
        Collections.shuffle(filteredTrades);

        return filteredTrades.stream().limit(12).collect(Collectors.toList());
    }

    public static String getString(String table, String column) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + table);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getString(column);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return "";
    }

    public static void updateColumnInt(String table, String column, int value) {
        String sql = "UPDATE " + table + " SET " + column + " = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, value);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateColumnBooleanFromRandomTrades(Boolean bool, String ress) {
        List<Map<String, Object>> contestTrades = (List<Map<String, Object>>) config.get("contest.contestTrades");

        for (Map<String, Object> trade : contestTrades) {
            if (trade.get("ress").equals(ress)) {
                trade.put("selected", bool);
            }
        }
        plugins.saveDefaultConfig();
    }

    public static void insertChoicePlayer(Player player, Integer camp) {

        String sql = "INSERT INTO camps (minecraft_uuid, name, camps, point_dep) VALUES (?, ?, ?, 0)";
        try (PreparedStatement states = connection.prepareStatement(sql)) {
            states.setString(1, player.getUniqueId().toString());
            states.setString(2, player.getName());
            states.setInt(3, camp);
            states.addBatch();
            states.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static DayOfWeek getCurrentDayOfWeek() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E", Locale.FRENCH);

        LocalDate currentDate = LocalDate.now();
        String currentDayString = currentDate.format(formatter);

        //conversion ex ven. => FRIDAY
        return DayOfWeek.from(formatter.parse(currentDayString));
    }

    public static ResultSet getAllPlayer() {
        try {
            PreparedStatement query = connection.prepareStatement("SELECT * FROM camps");
            ResultSet rs = query.executeQuery();
            return rs;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getPlayerCampName(Player player) {
        Integer campInteger = ContestManager.getPlayerCampsCache(player);
        String campName = ContestManager.getString("contest","camp" + campInteger);
        return campName;
    }
    public static Integer getOfflinePlayerCamp(OfflinePlayer player) {
        String sql = "SELECT * FROM camps WHERE minecraft_uuid = ?";
        try (PreparedStatement states = connection.prepareStatement(sql)) {
            states.setString(1, player.getUniqueId().toString());
            ResultSet result = states.executeQuery();
            if (result.next()) {
                return result.getInt("camps");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    public static Integer getVoteTaux(Integer camps) {
        String sql = "SELECT COUNT(*) FROM camps WHERE camps = ?";
        try (PreparedStatement states = connection.prepareStatement(sql)) {
            states.setInt(1, camps);
            ResultSet result = states.executeQuery();
            if (result.next()) {
                return result.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    public static String getOfflinePlayerCampName(OfflinePlayer player) {
        Integer campInteger = ContestManager.getOfflinePlayerCamp(player);
        String campName = ContestManager.getString("contest","camp" + campInteger);
        return campName;
    }
    public static ChatColor getOfflinePlayerCampChatColor(OfflinePlayer player) {
        Integer campInteger = ContestManager.getOfflinePlayerCamp(player);
        String color = ContestManager.getString("contest","color" + campInteger);
        ChatColor campColor = ChatColor.valueOf(color);
        return campColor;
    }

    public static Integer getRankPlayerInContest(OfflinePlayer player) {
        String sql = "SELECT COUNT(*) AS rank FROM camps WHERE point_dep > (SELECT point_dep FROM camps WHERE minecraft_uuid = ?);";
        try (PreparedStatement states = connection.prepareStatement(sql)) {
            states.setString(1, player.getUniqueId().toString());
            ResultSet result = states.executeQuery();
            if (result.next()) {
                return result.getInt("rank") + 1;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return -1;
    }

    public static ResultSet getAllPlayerOrdered() {
        try {
            PreparedStatement query = connection.prepareStatement("SELECT * FROM camps ORDER BY point_dep DESC");
            ResultSet rs = query.executeQuery();
            return rs;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getRankContest(Player player) {
        int points = getPlayerPointsCache(player);

        if(points >= 10000) {
            return "Dictateur en  ";
        } else if (points >= 2500) {
            return "Colonel en ";
        } else if (points >= 2000) {
            return "Addict en ";
        } else if (points >= 1500) {
            return "Dieu en ";
        } else if (points >= 1000) {
            return "Légende en ";
        } else if (points >= 750) {
            return "Sénior en ";
        } else if (points >= 500) {
            return "Pro en ";
        } else if (points >= 250) {
            return "Semi-Pro en ";
        } else if (points >= 100) {
            return "Amateur en ";
        } else if (points >= 0) {
            return "Noob en ";
        }

        return "";
    }

    public static int getRepPointsToRank(Player player) {
        int points = getPlayerPointsCache(player);

        if(points >= 10000) {
            return 0;
        } else if (points >= 2500) {
            return 10000;
        } else if (points >= 2000) {
            return 2500;
        } else if (points >= 1500) {
            return 2000;
        } else if (points >= 1000) {
            return 1500;
        } else if (points >= 750) {
            return 1000;
        } else if (points >= 500) {
            return 750;
        } else if (points >= 250) {
            return 500;
        } else if (points >= 100) {
            return 250;
        } else if (points >= 0) {
            return 100;
        }

        return 0;
    }

    public static String getRankContestFroOffline(OfflinePlayer player) {
        String sql = "SELECT * FROM camps WHERE minecraft_uuid = ?";
        int points = 0;
        try (PreparedStatement states = connection.prepareStatement(sql)) {
            states.setString(1, player.getUniqueId().toString());
            ResultSet result = states.executeQuery();
            if (result.next()) {
                points = result.getInt("point_dep");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if(points >= 10000) {
            return "Dictateur en  ";
        } else if (points >= 2500) {
            return "Colonel en ";
        } else if (points >= 2000) {
            return "Addict en ";
        } else if (points >= 1500) {
            return "Dieu en ";
        } else if (points >= 1000) {
            return "Légende en ";
        } else if (points >= 750) {
            return "Sénior en ";
        } else if (points >= 500) {
            return "Pro en ";
        } else if (points >= 250) {
            return "Semi-Pro en ";
        } else if (points >= 100) {
            return "Amateur en ";
        } else if (points >= 0) {
            return "Noob en ";
        }

        return "";
    }

    public static int getRankContestFromOfflineInt(OfflinePlayer player) {
        String sql = "SELECT * FROM camps WHERE minecraft_uuid = ?";
        int points = 0;
        try (PreparedStatement states = connection.prepareStatement(sql)) {
            states.setString(1, player.getUniqueId().toString());
            ResultSet result = states.executeQuery();
            if (result.next()) {
                points = result.getInt("point_dep");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if(points >= 10000) {
            return 10;
        } else if (points >= 2500) {
            return 9;
        } else if (points >= 2000) {
            return 8;
        } else if (points >= 1500) {
            return 7;
        } else if (points >= 1000) {
            return 6;
        } else if (points >= 750) {
            return 5;
        } else if (points >= 500) {
            return 4;
        } else if (points >= 250) {
            return 3;
        } else if (points >= 100) {
            return 2;
        } else if (points >= 0) {
            return 1;
        }

        return 0;
    }

    public static boolean hasWinInCampForOfflinePlayer(OfflinePlayer player) {
        String sql = "SELECT * FROM camps WHERE minecraft_uuid = ?";
        int playerCamp = 0;
        try (PreparedStatement states = connection.prepareStatement(sql)) {
            states.setString(1, player.getUniqueId().toString());
            ResultSet result = states.executeQuery();
            if (result.next()) {
                playerCamp = result.getInt("camps");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String sql2 = "SELECT * FROM contest";
        int points1 = 0;
        int points2 = 0;
        try (PreparedStatement states2 = connection.prepareStatement(sql2)) {
            ResultSet result = states2.executeQuery();
            if (result.next()) {
                points1 = result.getInt("points1");
                points2 = result.getInt("points2");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (points1 > points2 && playerCamp == 1) {
            return true;
        }
        if (points2 > points1 && playerCamp == 2) {
            return true;
        }
        return false;
    }

    public static int giveRandomly(Integer min, Integer max) {
        int moneyGive = new Random().nextInt(min, max);
        return moneyGive;
    }

    public static double getMultiMoneyFromRang(int rang) {
        if(rang == 10) {
            return 2.4;
        } else if (rang == 9) {
            return 2.0;
        } else if (rang == 8) {
            return 1.8;
        } else if (rang == 7) {
            return 1.7;
        } else if (rang == 6) {
            return 1.6;
        } else if (rang == 5) {
            return 1.5;
        } else if (rang == 4) {
            return 1.4;
        } else if (rang == 3) {
            return 1.3;
        } else if (rang == 2) {
            return 1.1;
        } else if (rang == 1) {
            return 1;
        }

        return 0;
    }
    public static double getMultiLuckyFromRang(int rang) {
        if(rang == 10) {
            return 4;
        } else if (rang == 9) {
            return 3.5;
        } else if (rang == 8) {
            return 3.2;
        } else if (rang == 7) {
            return 3;
        } else if (rang == 6) {
            return 2.7;
        } else if (rang == 5) {
            return 2.3;
        } else if (rang == 4) {
            return 1.8;
        } else if (rang == 3) {
            return 1.3;
        } else if (rang == 2) {
            return 1.1;
        } else if (rang == 1) {
            return 1;
        }

        return 0;
    }

    private static void updateSelected(String camp) {
        List<Map<?, ?>> contestList = config.getMapList("contest.contestList");
        List<Map<String, Object>> updatedContestList = new ArrayList<>();

        for (Map<?, ?> contest : contestList) {
            Map<String, Object> fusionContestList = new HashMap<>();

            for (Map.Entry<?, ?> entry : contest.entrySet()) {
                if (entry.getKey() instanceof String) {
                    fusionContestList.put((String) entry.getKey(), entry.getValue());
                }
            }

            if (fusionContestList.get("camp1").equals(camp)) {
                int selected = (int) fusionContestList.get("selected");
                fusionContestList.put("selected", selected + 1);
            }

            updatedContestList.add(fusionContestList);
        }
        config.set("contest.contestList", updatedContestList);
        plugins.saveDefaultConfig();
    }
    public static void addOneToLastContest(String camps) {
        List<Map<?, ?>> contestList = config.getMapList("contest.contestList");

        for (Map<?, ?> contest : contestList) {
            if (contest.get("camp1").equals(camps)) {
                Map<String, Object> result = new HashMap<>();
                for (Map.Entry<?, ?> entry : contest.entrySet()) {
                    if (entry.getKey() instanceof String) {
                        result.put((String) entry.getKey(), entry.getValue());
                    }
                }
                updateSelected(camps);
            }
        }
    }

    public static void selectRandomlyContest() {
        List<Map<?, ?>> contestList = config.getMapList("contest.contestList");
        List<Map<String, Object>> orderredContestList = new ArrayList<>();

        for (Map<?, ?> contest : contestList) {
            Map<String, Object> fusionContest = new HashMap<>();
            for (Map.Entry<?, ?> entry : contest.entrySet()) {
                if (entry.getKey() instanceof String) {
                    fusionContest.put((String) entry.getKey(), entry.getValue());
                }
            }
            orderredContestList.add(fusionContest);
        }

        orderredContestList.sort(Comparator.comparingInt(c -> (int) c.get("selected")));

        Map<String, Object> contest = orderredContestList.get(0);

        try (PreparedStatement states2 = connection.prepareStatement("INSERT INTO contest (phase, camp1, color1, camp2, color2, startdate, points1, points2) VALUES (1, ?, ?, ?, ?, ?, 0,0)")) {
            states2.setString(1, (String) contest.get("camp1"));
            states2.setString(2, (String) contest.get("color1"));
            states2.setString(3, (String) contest.get("camp2"));
            states2.setString(4, (String) contest.get("color2"));
            states2.setString(5, "ven.");
            states2.addBatch();
            states2.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteTableContest(String table) {
        String sql = "DELETE FROM " + table;
        try (PreparedStatement states = connection.prepareStatement(sql)) {
            states.addBatch();
            states.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addPointPlayer(Integer points_dep, Player player) {
        String sql = "UPDATE camps SET point_dep = ? WHERE minecraft_uuid = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, points_dep);
            stmt.setString(2, player.getUniqueId().toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public static List<String> getColorContestList() {
        List<String> color = new ArrayList<>();
        for (String colorName : colorContest) {
            color.add(colorName);
        }
        return color;
    }

    public static void insertCustomContest(String camp1, String color1, String camp2, String color2) {
        try (PreparedStatement states2 = connection.prepareStatement("INSERT INTO contest (phase, camp1, color1, camp2, color2, startdate, points1, points2) VALUES (1, ?, ?, ?, ?, ?, 0,0)")) {
            states2.setString(1, camp1);
            states2.setString(2, color1);
            states2.setString(3, camp2);
            states2.setString(4, color2);
            states2.setString(5, "ven.");
            states2.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
