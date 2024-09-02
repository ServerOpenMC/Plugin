package fr.communaywen.core.space.rocket;

import dev.lone.itemsadder.api.CustomEntity;
import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.credit.Collaborators;
import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import fr.communaywen.core.utils.constant.MessageManager;
import fr.communaywen.core.utils.constant.MessageType;
import fr.communaywen.core.utils.constant.Prefix;
import lombok.Getter;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Feature("Fusée")
@Credit("ri1_")
@Collaborators("Dartsgame")
public class Rocket {
    @Getter
    private final CustomEntity entity;

    @Getter
    private RocketMenu gui;

    private int coalCount = 0;

    private boolean isLaunched = false;

    private static final List<Rocket> rockets = new ArrayList<>();

    public Rocket(CustomEntity entity) {
        this.entity = entity;
        this.gui = new RocketMenu(this);
        this.isLaunched = false;
        rockets.add(this);
        AywenCraftPlugin.getInstance().getLogger().info("Rocket at " + this.getEntity().getLocation().getX() + " " + this.getEntity().getLocation().getY() + " " + this.getEntity().getLocation().getZ() + " in " + this.getEntity().getLocation().getWorld().getName() + " created. There's now " + rockets.size() + " rockets");
    }

    public void openMenu(Player player) {
        gui.openMenu(player);
    }


    static public Rocket getByEntity(CustomEntity entity) {
        return rockets.stream().filter(rocket -> rocket.entity.getEntity().getUniqueId().equals(entity.getEntity().getUniqueId())).findFirst().orElse(null);
    }

    public void remove() {
        AywenCraftPlugin.getInstance().getLogger().info("Rocket at " + this.getEntity().getLocation().getX() + " " + this.getEntity().getLocation().getY() + " " + this.getEntity().getLocation().getZ() + " in " + this.getEntity().getLocation().getWorld().getName() + " removed");
        rockets.remove(this);
    }

    public void setCoalCount(int coalCount) {
        this.coalCount = coalCount;
    }

    public int getCoalCount() {
        return coalCount;
    }

    public void addCoalCount(int coalCount) {
        this.coalCount += coalCount;
    }

    public boolean isLaunched() {
        return isLaunched;
    }

    public void launch(Player player) {
        if(player == null) {
            AywenCraftPlugin.getInstance().getLogger().warning("Unable to launch rocket: player is null");
            return;
        }
        if(isLaunched) {
            MessageManager.sendMessageType(player, "Cette fusée est déjà partie.", Prefix.SPACE, MessageType.ERROR, true);
            return;
        }

        if(!player.getWorld().getName().equals("world") && !player.getWorld().getName().equals("moon")) {
            MessageManager.sendMessageType(player, "Cette planète n'a pas de lune, essayez depuis la terre.", Prefix.SPACE, MessageType.ERROR, true);
            return;
        }

        if(coalCount < 1024) {
            MessageManager.sendMessageType(player, "Il n'y a pas assez de charbon dans la fusée pour démarrer", Prefix.SPACE, MessageType.ERROR, true);
            return;
        }

        if(entity.getLocation().getWorld().getHighestBlockAt(entity.getLocation()).getY() > entity.getLocation().getY()) {
            MessageManager.sendMessageType(player, "Il y a un bloc sur la trajectoire de la fusée, impossible de décoller", Prefix.SPACE, MessageType.ERROR, true);
            return;
        }

        player.closeInventory();
        getGui().getInventory().close();

        setCoalCount(0);
        player.playSound(player.getLocation(), "sons:effects.rocket_countdown", 1, 1);


        AtomicInteger i = new AtomicInteger(10);

        player.setGameMode(GameMode.SPECTATOR);
        Location loc = entity.getLocation().add(0, -1, -4);
        loc.setYaw((float) -1078.95);
        loc.setPitch((float) -28.5);
        player.setFlySpeed(0);
        //-4
        player.teleport(loc);

        new BukkitRunnable() {
            @Override
            public void run() {
                player.teleport(loc);
                player.sendTitle("§c"+i.get(), "§r", 0, 20, 0);
                if(i.get() == 1) {
                    entity.playAnimation("animation.rocket.launch");
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            player.sendTitle(PlaceholderAPI.setPlaceholders(player, "§0%img_screeneffect%"), "§r", 20, 10, 10);

                            if(Bukkit.getWorld("moon") == null) {
                                AywenCraftPlugin.getInstance().getLogger().warning("Moon world is null");
                                MessageManager.sendMessageType(player, "Une erreur est survenue lors du chargement de la lune", Prefix.SPACE, MessageType.ERROR, true);
                                return;
                            }
                            int x = entity.getLocation().getBlockX();
                            int z = entity.getLocation().getBlockZ();

                            Chunk chunk = (entity.getLocation().getWorld().equals(Bukkit.getWorld("moon")) ? Bukkit.getWorld("world") : Bukkit.getWorld("moon")).getChunkAt(x, z);
                            if(!chunk.isLoaded()) {
                                chunk.load();
                            }
                            int highestBlock = (entity.getLocation().getWorld().equals(Bukkit.getWorld("moon")) ? Bukkit.getWorld("world") : Bukkit.getWorld("moon")).getHighestBlockYAt(x, z);
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    //this.cancel();
                                    player.teleport(new Location(player.getLocation().getWorld().equals(Bukkit.getWorld("moon")) ? Bukkit.getWorld("world") : Bukkit.getWorld("moon"), x, highestBlock, z-4, (float) -1078.95, (float) -28.5));
                                    new BukkitRunnable() {
                                        @Override
                                        public void run() {
                                            player.teleport(entity.getLocation());
                                            player.setGameMode(GameMode.SURVIVAL);
                                            player.setFlySpeed(0.1f);
                                            isLaunched = false;
                                            gui = new RocketMenu(Rocket.this);
                                        }
                                    }.runTaskLater(AywenCraftPlugin.getInstance(), 80);
                                }
                            }.runTaskLater(AywenCraftPlugin.getInstance(), 15);
                            entity.teleport(new Location(entity.getLocation().getWorld().equals(Bukkit.getWorld("moon")) ? Bukkit.getWorld("world") : Bukkit.getWorld("moon"), x, highestBlock+20, z));
                            entity.playAnimation("animation.rocket.land");
                        }
                    }.runTaskLater(AywenCraftPlugin.getInstance(), 65);
                }
                if(i.get() == 0) {
                    player.sendTitle("§cDécollage !", "§r", 0, 20, 0);
                    this.cancel();
                    return;
                }
                i.getAndDecrement();
            }
        }.runTaskTimer(AywenCraftPlugin.getInstance(), 0, 20);


        isLaunched = true;
    }


}
