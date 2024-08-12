package fr.communaywen.core.quests.qenum;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum QUESTS {
    BREAK_STONE(Material.STONE, 10000, TYPE.BREAK, "Casseur de pierres", "de stone", REWARD.MONEY, 2000, TIER.TIER_1),
    BREAK_DIAMOND(Material.DIAMOND, 100, TYPE.BREAK, "Richou", "diamants", REWARD.MONEY, 2500, TIER.TIER_1),
    BREAK_EMERALD(Material.DEEPSLATE_EMERALD_ORE, 1, TYPE.BREAK, "Rare !", "minerais de deepslate émeraude", REWARD.MONEY, 2000, TIER.TIER_1),
    KILL_PLAYERS(Material.IRON_SWORD, 10, TYPE.KILL, "Le pro du pvp", "joueurs", REWARD.MONEY, 5000, TIER.TIER_1),
    PLAYER_DEATH(Material.POPPY, 100, TYPE.DEATH, "Sa fait beaucoup la non ?", "fois", REWARD.MONEY, 5000, TIER.TIER_1),
    KILL_WARDENS(Material.SCULK_SHRIEKER, 1, TYPE.KILL, "L'explorateur des profondeurs", "wardens", REWARD.MONEY, 10000, TIER.TIER_1),
    CRAFT_RTP_WAND(Material.ENDER_PEARL, 1, TYPE.CRAFT, "Attention, ça va aller loin ...", "RTP wand", REWARD.MONEY, 500, TIER.TIER_1),
    EAT_KEBAB(Material.COOKED_BEEF, 10, TYPE.EAT, "Miam Miam", "Kebabs", REWARD.MONEY, 100, TIER.TIER_1),
    CRAFT_KEBAB(Material.BREAD, 1, TYPE.CRAFT, "Kebabier un jour, Kebabier toujours", "Kebab", REWARD.MONEY, 62, TIER.TIER_1),
    WALK_BLOCKS(Material.LEATHER_BOOTS, 20000, TYPE.WALK, "Le randonneur", "block", REWARD.MONEY, 10000, TIER.TIER_1),
    //WALK_BLOCKS2(Material.LEATHER_BOOTS, 100000, TYPE.WALK, "Le randonneur de l'extrême", "block", REWARD.MONEY, 20000,TIER.TIER_1),
    WALK_MAX_HIGH(Material.SNOW_BLOCK, 1, TYPE.WALK, "En altitude", "fois en couche maximal.", REWARD.MONEY, 500,TIER.TIER_1),
    SMELT_IRON(Material.IRON_ORE, 256, TYPE.SMELT, "Chaud devant !", "de fer", REWARD.MONEY, 2000, TIER.TIER_1),
    ENCHANT_FIRST_ITEM(Material.ENCHANTED_BOOK, 1, TYPE.ENCHANT, "Abracadabra !", "item", REWARD.MONEY, 200, TIER.TIER_1),
    KILL_SUPER_CREEPER(Material.CREEPER_HEAD, 1, TYPE.KILL, "Le Boss des Creeper !", " Creeper super chargé", REWARD.MONEY, 1000, TIER.TIER_1),
    CRAFT_CAKE(Material.CAKE, 64, TYPE.CRAFT, "Le meilleur patissier !", " gateau", REWARD.MONEY, 100, TIER.TIER_1),
    KILL_ZOMBIE(Material.ZOMBIE_HEAD, 1000, TYPE.KILL, "Apocalypse zombie ?", " Zombie", REWARD.MONEY, 8000, TIER.TIER_1),
    KILL_CHIKEN(Material.COOKED_CHICKEN, 500, TYPE.KILL, "K  F  C", " Poulets", REWARD.MONEY, 5000, TIER.TIER_1),
    KILL_VILLAGER(Material.VILLAGER_SPAWN_EGG, 1, TYPE.KILL, "La maltraitance des villageois c'est mal !", " villageois", REWARD.MONEY, 100, TIER.TIER_1),
    PLACE_BLOCK(Material.GRASS_BLOCK, 2000, TYPE.PLACE, "Builder de l'extreme !", "Blocks", REWARD.MONEY, 250, TIER.TIER_1),
    CRAFT_FIREWORK(Material.FIREWORK_STAR, 1, TYPE.CRAFT, "Artificier en herbe !", "boule de feu d'artifice", REWARD.MONEY, 250, TIER.TIER_1),
    //    GET_TOTEM(Material.TOTEM_OF_UNDYING, 10, TYPE.GET, "Invincible !", "Avoir 10 totem", REWARD.MONEY, 300, TIER.TIER_1),
    BREAK_WOOD(Material.OAK_WOOD, 3000, TYPE.BREAK, "Casseur de bois", "de bois", REWARD.MONEY, 5000, TIER.TIER_1),
    MONEY_500K(Material.DIAMOND_BLOCK, 500000, TYPE.MONEY, "Devient Elon", "$", REWARD.ITEMS, 1, new ItemStack(Material.NETHERITE_INGOT), TIER.TIER_1),
    ;

    @Getter
    private final Material material;
    @Getter
    private final int qt;
    @Getter
    private final TYPE type;
    @Getter
    private final String name;
    private final String desc;
    @Getter
    private final int rewardsQt;
    @Getter
    private final REWARD reward;
    @Getter
    private final ItemStack rewardsMaterial;
    @Getter
    private final TIER tier;

    QUESTS(Material material, int qt, TYPE type, String name, String desc, REWARD reward, int rewardsQt, ItemStack rewardsMaterial, TIER tier) {
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

    public String getDesc() {
        switch (type) {
            case GET -> { return "Récupérer " + qt + " " + desc; }
            case KILL -> { return "Tuer " + qt + " " + desc; }
            case WALK -> { return "Marcher " + qt + " " + desc; }
            case BREAK -> { return "Casser " + qt + " " + desc; }
            case CRAFT -> { return "Crafter " + qt + " " + desc; }
            case PLACE -> { return "Placer " + qt + " " + desc; }
            case EAT -> { return "Manger " + qt + " " + desc; }
            case ENCHANT -> { return "Enchanter " + qt + " " + desc; }
            case SMELT -> { return "Cuire " + qt + " " + desc; }
            case MONEY -> { return "Avoir " + qt + " " + desc; }
            case DEATH -> { return "Mourrir " + qt + " " + desc; }
            default -> { return ""; }
        }
    }
}
