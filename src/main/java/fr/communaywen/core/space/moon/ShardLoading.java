package fr.communaywen.core.space.moon;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ShardLoading {
    private Player player;

    private int percentage;

    private static final List<ShardLoading> shardLoadings = new ArrayList<>();

    public ShardLoading(Player player, int percentage) {
        this.player = player;
        this.percentage = percentage;

        if(getByPlayer(player) != null) {
            getByPlayer(player).remove();
        }
        shardLoadings.add(this);
    }

    public void set(int percentage) {
        this.percentage = percentage;
    }

    public int get() {
        return percentage;
    }

    public void remove() {
        shardLoadings.remove(this);
    }

    static public ShardLoading getByPlayer(Player player) {
        return shardLoadings.stream().filter(shardLoading -> shardLoading.player.equals(player)).findFirst().orElse(null);
    }


}
