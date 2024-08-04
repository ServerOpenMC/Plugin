package fr.communaywen.core.utils;


/*
 * ItemStack items = new ItemBuilder(Material.PLAYER_HEAD).setName("").toItemStack();
 */


import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;

public class ItemBuilder {
    private final ItemStack is;

    public ItemBuilder(Material m) {
        this(m, 1);
    }

    public ItemBuilder(ItemStack is) {
        this.is = is;
    }

    public ItemBuilder(Material m, int amount) {
        is = new ItemStack(m, amount);
    }

    public ItemBuilder(Material m, int amount, short meta) {
        is = new ItemStack(m, amount, meta);
    }

    public ItemBuilder clone() throws CloneNotSupportedException {
        ItemBuilder itemBuilder = (ItemBuilder) super.clone();
        return new ItemBuilder(is);
    }

    public ItemBuilder setDurability(short dur) {
        if (is.getItemMeta() != null) {
            Damageable damageable = (Damageable) is.getItemMeta();
            damageable.setDamage(dur);
            is.setItemMeta((ItemMeta) damageable);
        }
        return this;
    }

    public ItemBuilder setName(String name) {
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(name);
        is.setItemMeta(im);
        return this;
    }

    public ItemBuilder addUnsafeEnchantment(Enchantment ench, int level) {
        is.addUnsafeEnchantment(ench, level);
        return this;
    }

    public ItemBuilder removeEnchantment(Enchantment ench) {
        is.removeEnchantment(ench);
        return this;
    }

    public ItemBuilder setSkullOwner(String owner) {
        try {
            SkullMeta im = (SkullMeta) is.getItemMeta();
            assert im != null;
            im.setOwner(owner);
            is.setItemMeta(im);
        } catch (ClassCastException ignored) {
        }
        return this;
    }

    public ItemBuilder addEnchant(Enchantment ench, int level) {
        ItemMeta im = is.getItemMeta();
        assert im != null;
        im.addEnchant(ench, level, true);
        is.setItemMeta(im);
        return this;
    }

    public ItemBuilder addFlag(ItemFlag itemsflags) {
        ItemMeta im = is.getItemMeta();
        assert im != null;
        im.addItemFlags(itemsflags);
        is.setItemMeta(im);
        return this;
    }

    public ItemBuilder setInfinityDurability() {
        is.setDurability(Short.MAX_VALUE);
        return this;
    }

    public ItemBuilder setLore(String... lore) {
        ItemMeta im = is.getItemMeta();
        assert im != null;
        im.setLore(Arrays.asList(lore));
        is.setItemMeta(im);
        return this;
    }

    @SuppressWarnings("deprecation")
    public ItemBuilder setWoolColor(DyeColor color) {
        this.is.setDurability(color.getWoolData());
        return this;
    }

    public ItemBuilder setLeatherArmorColor(Color color) {
        try {
            LeatherArmorMeta im = (LeatherArmorMeta) is.getItemMeta();
            assert im != null;
            im.setColor(color);
            is.setItemMeta(im);
        } catch (ClassCastException ignored) {
        }
        return this;
    }

    public ItemBuilder setItemMeta(ItemMeta itemmeta) {
        is.setItemMeta(itemmeta);
        return this;
    }

    public ItemStack toItemStack() {
        return is;
    }
}