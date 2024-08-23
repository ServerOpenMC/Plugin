package fr.communaywen.core.space.rocket;

import dev.lone.itemsadder.api.CustomEntity;
import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.utils.constant.MessageManager;
import fr.communaywen.core.utils.constant.MessageType;
import fr.communaywen.core.utils.constant.Prefix;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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
    }

    public void openMenu(Player player) {
        gui.openMenu(player);
    }


    static public Rocket getByEntity(CustomEntity entity) {
        return rockets.stream().filter(rocket -> rocket.entity.getId().equals(entity.getId())).findFirst().orElse(null);
    }

    public void remove() {
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
        if(coalCount < 1024) {
            MessageManager.sendMessageType(player, "Il n'y a pas assez de charbon dans la fusée pour démarrer", Prefix.SPACE, MessageType.ERROR, true);
            return;
        }

        player.closeInventory();
        getGui().getInventory().close();

        setCoalCount(0);
        player.playSound(player.getLocation(), "sons:effects.rocket_countdown", 1, 1);


        AtomicInteger i = new AtomicInteger(10);

        new BukkitRunnable() {
            @Override
            public void run() {
                player.sendTitle("§c"+i.get(), "§r", 0, 20, 0);
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
