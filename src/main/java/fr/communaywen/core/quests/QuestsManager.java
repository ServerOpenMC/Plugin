package fr.communaywen.core.quests;

import fr.communaywen.core.AywenCraftPlugin;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class QuestsManager {

    private static final Map<UUID, PlayerQuests> playerQuests = new HashMap<>();
    private static final File questsFile = new File(AywenCraftPlugin.getInstance().getDataFolder(), "quests.yml");
    private static final FileConfiguration questsData = YamlConfiguration.loadConfiguration(questsFile);

    public enum QUESTS {
        BREAK_STONE(Material.STONE, 10000, TYPE.BREAK, "Casseur de pierres", "de stone", REWARD.MONEY, 2000, TIER.TIER_1),
        BREAK_DIAMOND(Material.DIAMOND, 100, TYPE.BREAK, "Richou", "diamants", REWARD.MONEY, 2500, TIER.TIER_1),
        KILL_PLAYERS(Material.IRON_SWORD, 10, TYPE.KILL, "Le pro du pvp", "joueurs", REWARD.MONEY, 5000, TIER.TIER_1),
        KILL_WARDENS(Material.SCULK_SHRIEKER, 1, TYPE.KILL, "L'explorateur des profondeurs", "wardens", REWARD.MONEY, 10000, TIER.TIER_1),
        CRAFT_RTP_WAND(Material.ENDER_PEARL, 1, TYPE.CRAFT, "Attention, ça va aller loin ...", "RTP wand", REWARD.MONEY, 500, TIER.TIER_1),
        EAT_KEBAB(Material.COOKED_BEEF, 10, TYPE.EAT, "Miam Miam", "Kebabs", REWARD.MONEY, 100, TIER.TIER_1),
        CRAFT_KEBAB(Material.BREAD, 1, TYPE.CRAFT, "Kebabier un jour, Kebabier toujours", "Kebab", REWARD.MONEY, 100, TIER.TIER_1),
        WALK_BLOCKS(Material.LEATHER_BOOTS, 20000, TYPE.WALK, "Le randonneur", "block", REWARD.MONEY, 10000, TIER.TIER_1),
        SMELT_IRON(Material.IRON_ORE, 256, TYPE.SMELT, "Chaud devant !", "de fer", REWARD.MONEY, 2000, TIER.TIER_1);
        private final Material material;
        private final int qt;
        private final TYPE type;
        private final String name;
        private final String desc;
        private final int rewardsQt;
        private final REWARD reward;
        private final Material rewardsMaterial;
        private final TIER tier;

        QUESTS(Material material, int qt, TYPE type, String name, String desc, REWARD reward, int rewardsQt, Material rewardsMaterial, TIER tier) {
            this.material = material;
            this.qt = qt;
            this.type = type;
            this.name = name;
            this.desc = desc;
            this.reward = reward;
            this.rewardsMaterial = rewardsMaterial;
            this.rewardsQt = rewardsQt;
            this.tier = tier;
        }

        QUESTS(Material material, int qt, TYPE type, String name, String desc, REWARD reward, int rewardsQt, TIER tier) {
            this(material, qt, type, name, desc, reward, rewardsQt, null, tier);
        }

        public Material getMaterial() {
            return material;
        }

        public int getQt() {
            return qt;
        }

        public TYPE getType() {
            return type;
        }

        public String getName() {
            return name;
        }

        public REWARD getReward() {
            return reward;
        }

        public int getRewardsQt() {
            return rewardsQt;
        }

        public Material getRewardsMaterial() {
            return rewardsMaterial;
        }

        public TIER getTier() {
            return tier;
        }

        public String getDesc() {
            switch (type) {
                case GET -> {
                    return "Récupérer " + qt + " " + desc;
                }
                case KILL -> {
                    return "Tuer " + qt + " " + desc;
                }
                case WALK -> {
                    return "Marcher " + qt + " " + desc;
                }
                case BREAK -> {
                    return "Casser " + qt + " " + desc;
                }
                case CRAFT -> {
                    return "Crafter " + qt + " " + desc;
                }
                case PLACE -> {
                    return "Placer " + qt + " " + desc;
                }
                case EAT -> {
                    return "Manger " + qt + " " + desc;
                }
                case ENCHANT -> {
                    return "Enchanter " + qt + " " + desc;
                }
                case SMELT -> {
                    return "Cuire " + qt + " " + desc;
                }
                default -> {
                    return "";
                }
            }
        }
    }

    private enum TYPE {
        BREAK,
        PLACE,
        GET,
        WALK,
        KILL,
        CRAFT,
        EAT,
        ENCHANT,
        SMELT
    }

    private enum REWARD {
        MONEY,
        ITEMS
    }

    private enum TIER {
        TIER_1,
        TIER_2,
        TIER_3,
        TIER_4,
        TIER_5
    }

    public static void loadPlayerData(Player player) {
        UUID playerId = player.getUniqueId();
        FileConfiguration data = YamlConfiguration.loadConfiguration(questsFile);

        PlayerQuests pq = new PlayerQuests();

        if (data.contains(playerId.toString())) {
            ConfigurationSection playerSection = data.getConfigurationSection(playerId.toString());

            for (String questName : playerSection.getKeys(false)) {
                try {
                    QUESTS quest = QUESTS.valueOf(questName);
                    int progress = playerSection.getInt(questName);
                    pq.getQuestsProgress().put(quest, progress);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        }

        playerQuests.put(playerId, pq);
    }


    public static void savePlayerData(Player player) {
        UUID playerId = player.getUniqueId();
        PlayerQuests pq = playerQuests.get(playerId);

        Map<String, Integer> questsProgressMap = new HashMap<>();
        for (Map.Entry<QUESTS, Integer> entry : pq.getQuestsProgress().entrySet()) {
            questsProgressMap.put(entry.getKey().name(), entry.getValue());
        }

        questsData.set(playerId.toString(), questsProgressMap);

        try {
            questsData.save(questsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadAllPlayersData() {
        if (!questsFile.exists()) {
            return;
        }

        FileConfiguration data = YamlConfiguration.loadConfiguration(questsFile);

        for (String playerIdString : data.getKeys(false)) {
            UUID playerId = UUID.fromString(playerIdString);
            ConfigurationSection playerSection = data.getConfigurationSection(playerIdString);

            PlayerQuests pq = new PlayerQuests();
            for (String questName : playerSection.getKeys(false)) {
                QUESTS quest = QUESTS.valueOf(questName);
                int progress = playerSection.getInt(questName);
                pq.getQuestsProgress().put(quest, progress);
            }

            playerQuests.put(playerId, pq);
        }
    }

    public static void saveAllPlayersData() {
        FileConfiguration data = new YamlConfiguration();

        for (Map.Entry<UUID, PlayerQuests> entry : playerQuests.entrySet()) {
            UUID playerId = entry.getKey();
            PlayerQuests pq = entry.getValue();

            ConfigurationSection playerSection = data.createSection(playerId.toString());
            for (Map.Entry<QUESTS, Integer> questEntry : pq.getQuestsProgress().entrySet()) {
                playerSection.set(questEntry.getKey().name(), questEntry.getValue());
            }
        }

        try {
            data.save(questsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static PlayerQuests getPlayerQuests(Player player) {
        return playerQuests.get(player.getUniqueId());
    }

    public static void manageQuestsPlayer(Player player, QUESTS quests, int amount, String actionBar) {
        PlayerQuests pq = getPlayerQuests(player);

        if (!pq.isQuestCompleted(quests)) {
            pq.addProgress(quests, amount);
            QuestsManager.savePlayerData(player);
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("» §7[§9§lQuêtes§7] §6" + pq.getProgress(quests) + "§l/§6" + quests.getQt() + " " + actionBar));
            if (pq.isQuestCompleted(quests)) {
                player.playSound(player, Sound.UI_TOAST_CHALLENGE_COMPLETE, 10, 10);
                player.sendTitle("§6QUETE COMPLETE", "§e" + quests.getName());
                player.sendMessage("» §7[§9§lQuêtes§7] §6Quête complétée: §e" + quests.getName());
                switch (quests.getReward()) {
                    case ITEMS -> {
                        player.getInventory().addItem(new ItemStack(quests.rewardsMaterial, quests.getRewardsQt()));
                        player.sendMessage("» §7[§9§lQuêtes§7] §6+ " + quests.rewardsQt + " " + quests.rewardsMaterial);
                    }
                    case MONEY -> {
                        AywenCraftPlugin.getInstance().economyManager.addBalance(player, quests.getRewardsQt());
                        player.sendMessage("» §7[§9§lQuêtes§7] §6+ " + quests.rewardsQt + "$");
                    }
                }
            }
        }
    }

}