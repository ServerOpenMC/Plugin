package fr.communaywen.core.luckyblocks;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;

public class SpawnZombieNetherite extends LuckyBlocksEvents{

    public SpawnZombieNetherite() {
        super("SpawnZombieNetherite", "Fais spawn un zombie full netherite, items non dropables, et qui s'appelle Bob", 0.5f);
    }

    @Override
    public void open(Player player, Block luckyBlock) {
        Location location = luckyBlock.getLocation();
        Zombie zombie = location.getWorld().spawn(location, Zombie.class);
        zombie.setCustomNameVisible(true);
        zombie.setCustomName("Bob");
        zombie.getEquipment().setChestplate(new ItemStack(Material.NETHERITE_CHESTPLATE, 1));
        zombie.getEquipment().setChestplateDropChance(0);
        zombie.getEquipment().setHelmet(new ItemStack(Material.NETHERITE_HELMET, 1));
        zombie.getEquipment().setHelmetDropChance(0);
        zombie.getEquipment().setLeggings(new ItemStack(Material.NETHERITE_LEGGINGS, 1));
        zombie.getEquipment().setLeggingsDropChance(0);
        zombie.getEquipment().setBoots(new ItemStack(Material.NETHERITE_BOOTS, 1));
        zombie.getEquipment().setBootsDropChance(0);
        zombie.getEquipment().setItemInMainHand(new ItemStack(Material.NETHERITE_SWORD, 1));
        zombie.getEquipment().setItemInMainHandDropChance(0);


    }
}
