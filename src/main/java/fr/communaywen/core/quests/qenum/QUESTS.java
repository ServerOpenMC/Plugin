package fr.communaywen.core.quests.qenum;

import fr.communaywen.core.AywenCraftPlugin;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum QUESTS {
    BREAK_STONE(Material.STONE, new int[]{10000, 30000, 80000}, TYPE.BREAK, "Casseur de pierres", "de stone", REWARD.MONEY, new int[]{2000, 4000, 6000}),
    BREAK_DIAMOND(Material.DIAMOND, new int[]{100, 400, 800}, TYPE.BREAK, "Richou", "diamants", REWARD.MONEY, new int[]{2500, 5000, 10000}),
    KILL_PLAYERS(Material.IRON_SWORD, new int[]{5, 20, 30}, TYPE.KILL, "Le pro du pvp", "joueurs", REWARD.MONEY, new int[]{2500, 5000, 10000}),
    KILL_WARDENS(Material.SCULK_SHRIEKER, new int[]{1, 2}, TYPE.KILL, "L'explorateur des profondeurs", "wardens", REWARD.MONEY, new int[]{10000, 20000}),
    CRAFT_RTP_WAND(Material.ENDER_PEARL, new int[]{1}, TYPE.CRAFT, "Attention, ça va aller loin ...", "RTP wand", REWARD.MONEY, new int[]{500}),
    EAT_KEBAB(Material.COOKED_BEEF, new int[]{10, 40, 80, 200}, TYPE.EAT, "Miam Miam", "Kebabs", REWARD.MONEY, new int[]{40, 80, 160, 1000}),
    CRAFT_KEBAB(Material.BREAD, new int[]{1}, TYPE.CRAFT, "Kebabier un jour, Kebabier toujours", "Kebab", REWARD.MONEY, new int[]{62}),
    WALK_BLOCKS(Material.LEATHER_BOOTS, new int[]{20000, 40000, 100000}, TYPE.WALK, "Le randonneur", "block", REWARD.MONEY, new int[]{10000, 15000, 20000}),
    SMELT_IRON(Material.IRON_ORE, new int[]{256, 512, 1536}, TYPE.SMELT, "Chaud devant !", "de fer", REWARD.MONEY, new int[]{2000, 4000, 8000}),
    ENCHANT_FIRST_ITEM(Material.ENCHANTING_TABLE, new int[]{1}, TYPE.ENCHANT, "Abracadabra !", "item", REWARD.MONEY, new int[]{200}),
    KILL_SUPER_CREEPER(Material.CREEPER_HEAD, new int[]{1}, TYPE.KILL, "Le Boss des Creeper !", " Creeper super chargé", REWARD.MONEY, new int[]{1000}),
    CRAFT_CAKE(Material.CAKE, new int[]{64, 128}, TYPE.CRAFT, "Le meilleur pâtissier !", " gateau", REWARD.MONEY, new int[]{100, 200}),
    KILL_ZOMBIE(Material.ZOMBIE_HEAD, new int[]{1000, 4000, 10000}, TYPE.KILL, "Apocalypse zombie ?", " Zombie", REWARD.MONEY, new int[]{8000, 10000, 15000}),
    PLACE_BLOCK(Material.GRASS_BLOCK, new int[]{2000, 8000, 40000}, TYPE.PLACE, "Builder de l'extreme !", "Blocks", REWARD.MONEY, new int[]{250, 1000, 2000}),
    MONEY_500K(Material.DIAMOND_BLOCK, new int[]{500000}, TYPE.MONEY, "Devient Elon", "$", REWARD.ITEMS, new int[]{1}, new ItemStack(Material.NETHERITE_INGOT)),
    HOLY_BREAD(Material.BREAD, new int[]{1}, TYPE.FISH, "Le pain sacré", "relique du pain sacré pendant l'event \"Pêche miraculeuse\"", REWARD.ITEMS, new int[]{16}, new ItemStack(Material.DIAMOND_ORE)),
    SAVE_THE_EARTH(Material.OAK_SAPLING, new int[]{10, 40, 100, 1000}, TYPE.PLACE, "Sauvons la planète !", "arbres et les faire grandir avec des poudres d'os", REWARD.ITEMS, new int[]{32, 64, 128, 256}, new ItemStack(Material.OAK_LOG)),
    CRAFT_ELEVATOR(Material.PAPER, new int[]{2, 8, 24}, TYPE.CRAFT, "Dépasse tous les niveaux !", "élévateur", REWARD.MONEY, new int[]{200, 400, 800}),;

    @Getter
    private final Material material;
    @Getter
    private final int[] qtTiers, rewardsQtTier;
    @Getter
    private final TYPE type;
    @Getter
    private final String name, desc;
    @Getter
    private final REWARD reward;
    @Getter
    private final ItemStack rewardsMaterial;

    QUESTS(Material material, int[] qtTiers, TYPE type, String name, String desc, REWARD reward, int[] rewardsQtTier, ItemStack rewardsMaterial) {
        this.material = material;
        this.qtTiers = qtTiers;
        this.type = type;
        this.name = name;
        this.desc = desc;
        this.reward = reward;
        this.rewardsMaterial = rewardsMaterial;
        this.rewardsQtTier = rewardsQtTier;
    }

    QUESTS(Material material, int[] qtTiers, TYPE type, String name, String desc, REWARD reward, int[] rewardsQtTier) {
        this(material, qtTiers, type, name, desc, reward, rewardsQtTier, null);
    }

    public String getDesc(int currentTier) {
        switch (type) {
            case GET -> { return "Récupérer " + qtTiers[currentTier] + " " + desc; }
            case KILL -> { return "Tuer " + qtTiers[currentTier] + " " + desc; }
            case WALK -> { return "Marcher " + qtTiers[currentTier] + " " + desc; }
            case BREAK -> { return "Casser " + qtTiers[currentTier] + " " + desc; }
            case CRAFT -> { return "Crafter " + qtTiers[currentTier] + " " + desc; }
            case PLACE -> { return "Placer " + qtTiers[currentTier] + " " + desc; }
            case EAT -> { return "Manger " + qtTiers[currentTier] + " " + desc; }
            case ENCHANT -> { return "Enchanter " + qtTiers[currentTier] + " " + desc; }
            case SMELT -> { return "Cuire " + qtTiers[currentTier] + " " + desc; }
            case MONEY -> { return "Avoir " + qtTiers[currentTier] + " " + desc; }
            case FISH -> { return "Pêcher " + qtTiers[currentTier] + " " + desc; }
            case OTHER -> { return desc; }
            default -> { return ""; }
        }
    }

    public int getQt(int currentTier) {
        if (currentTier < 0 || currentTier >= qtTiers.length)
            return qtTiers[qtTiers.length - 1];
        return qtTiers[currentTier];
    }

    public int getRewardsQt(int currentTier) {
        if(currentTier < 0 || currentTier >= rewardsQtTier.length)
            return rewardsQtTier[rewardsQtTier.length - 1];
        return rewardsQtTier[currentTier];
    }

}