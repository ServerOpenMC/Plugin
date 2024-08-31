package fr.communaywen.core.dreamdim.listeners;

import dev.lone.itemsadder.api.CustomStack;
import dev.lone.itemsadder.api.ItemsAdder;
import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.dreamdim.AdvancementRegister;
import fr.communaywen.core.dreamdim.DreamUtils;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class EnterWorldListener implements Listener {

    World dreamworld;
    AywenCraftPlugin plugin;
    HashMap<Player, BossBar> bossbars = new HashMap<>();
    AdvancementRegister register;

    public EnterWorldListener(AywenCraftPlugin plugin, AdvancementRegister register) {
        this.plugin = plugin;
        this.register = register;
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

    public void death(Player p) {
        Inventory inv = p.getInventory();

        for (ItemStack itemStack : List.of(p.getInventory().getItemInMainHand(), p.getInventory().getItemInOffHand())) {
            CustomStack customStack = CustomStack.byItemStack(itemStack);
            if (customStack == null) { continue; }
            if (customStack.getNamespacedID().equals("aywen:totem_of_undreaming")) {
                int amount = itemStack.getAmount();

                if (amount == 1) {
                    inv.removeItemAnySlot(itemStack);
                } else {
                    itemStack.setAmount(amount - 1);
                }

                register.grantAdvancement(p, "aywen:cheatdeath");

                ItemsAdder.playTotemAnimation(p, "aywen:totem_of_undreaming");
                return;
            }
        }

        for (ItemStack itemStack : inv.getContents()) {
            if (itemStack == null) { continue; } else {
                Enchantment miragecurse = Registry.ENCHANTMENT.get(NamespacedKey.fromString("aywen:mirage_curse"));
                if (itemStack.hasEnchant(miragecurse)) {
                    inv.remove(itemStack);
                    continue;
                }
            }


            CustomStack customStack = CustomStack.byItemStack(itemStack);
            if (customStack != null) {
                if (List.of( // Listes de items qui seront supprimé lors de la mort
                        "aywen:stripped_dream_log",
                        "aywen:dream_log",
                        "aywen:cloud",
                        "aywen:dream_planks",
                        "aywen:dream_essence",
                        "aywen:cloud_soup",
                        "aywen:dockerfish",
                        "aywen:moon_fish",
                        "aywen:sun_fish",
                        "aywen:cooked_poissonion",
                        "aywen:poissonion").contains(customStack.getNamespacedID())) {
                    inv.remove(itemStack);
                    continue;
                }
            }

            if (DreamUtils.isFromDream(itemStack)) {
                inv.remove(itemStack);
            }
        }
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

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLeave(PlayerQuitEvent e) {
        //Même chose que quand le joueur meurt
        if (e.getPlayer().getWorld().getName().equals("dreamworld")) {
            death(e.getPlayer());
            teleportBack(e.getPlayer());
        }
    }

    @EventHandler(priority= EventPriority.HIGHEST)
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        World world = p.getWorld();

        if (world.getName().equals("dreamworld")) {
            register.grantAdvancement(p, "aywen:nightmare");
            death(e.getPlayer());
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
                    register.grantAdvancement(player, "aywen:bed_sweet_bed");
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
