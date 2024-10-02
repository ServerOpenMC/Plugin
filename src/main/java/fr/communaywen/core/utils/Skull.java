package fr.communaywen.core.utils;

import org.apache.commons.codec.binary.Base64;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

/**
 * Represents some special mob heads, also support creating player skulls and custom skulls.
 *
 * @author xigsag, SBPrime
 */
public enum Skull {

    ARROW_LEFT("MHF_ArrowLeft"),
    ARROW_RIGHT("MHF_ArrowRight"),
    ARROW_UP("MHF_ArrowUp"),
    ARROW_DOWN("MHF_ArrowDown"),
    QUESTION("MHF_Question"),
    EXCLAMATION("MHF_Exclamation"),
    CAMERA("FHG_Cam"),

    ZOMBIE_PIGMAN("MHF_PigZombie"),
    PIG("MHF_Pig"),
    SHEEP("MHF_Sheep"),
    BLAZE("MHF_Blaze"),
    CHICKEN("MHF_Chicken"),
    COW("MHF_Cow"),
    SLIME("MHF_Slime"),
    SPIDER("MHF_Spider"),
    SQUID("MHF_Squid"),
    VILLAGER("MHF_Villager"),
    OCELOT("MHF_Ocelot"),
    HEROBRINE("MHF_Herobrine"),
    LAVA_SLIME("MHF_LavaSlime"),
    MOOSHROOM("MHF_MushroomCow"),
    GOLEM("MHF_Golem"),
    GHAST("MHF_Ghast"),
    ENDERMAN("MHF_Enderman"),
    CAVE_SPIDER("MHF_CaveSpider"),

    CACTUS("MHF_Cactus"),
    CAKE("MHF_Cake"),
    CHEST("MHF_Chest"),
    MELON("MHF_Melon"),
    LOG("MHF_OakLog"),
    PUMPKIN("MHF_Pumpkin"),
    TNT("MHF_TNT"),
    DYNAMITE("MHF_TNT2");

    private static final Base64 base64 = new Base64();
    private String id;

    private Skull(String id) {
        this.id = id;
    }



    /**
     * Return a skull of a player.
     *
     * @param name player's name
     * @return itemstack
     */
    public static ItemStack getPlayerSkull(String name) {
        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD, 1, (short) 3);
        SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
        meta.setOwner(name);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    /**
     * Return the skull's id.
     *
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * Return the skull of the enum.
     *
     * @return itemstack
     */
    public ItemStack getSkull() {
        return getPlayerSkull(id);
    }

    private static final UUID RANDOM_UUID = UUID.fromString("92864445-51c5-4c3b-9039-517c9927d1b4"); // We reuse the same "random" UUID all the time

    private static PlayerProfile getProfile(String url) {
        PlayerProfile profile = Bukkit.createPlayerProfile(RANDOM_UUID); // Get a new player profile
        PlayerTextures textures = profile.getTextures();
        URL urlObject;
        try {
            urlObject = new URL(url); // The URL to the skin, for example: https://textures.minecraft.net/texture/18813764b2abc94ec3c3bc67b9147c21be850cdf996679703157f4555997ea63a
        } catch (MalformedURLException exception) {
            throw new RuntimeException("Invalid URL", exception);
        }
        textures.setSkin(urlObject); // Set the skin of the player profile to the URL
        profile.setTextures(textures); // Set the textures back to the profile
        return profile;
    }


    /**
     * Return a skull that has a custom texture specified by url.
     *
     * @param url skin url
     * @return itemstack
     */
    public static ItemStack getCustomSkull(String url) {
        PlayerProfile profile = getProfile(url);
        System.out.println(profile.getTextures().getSkin().toString());
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.setOwnerProfile(profile); // Set the owning player of the head to the player profile
        head.setItemMeta(meta);
        return head; // Return the player head
    }

}
