package fr.communaywen.core.tpa;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.Location;

public class TPAQueue {
    public static final TPAQueue INSTANCE = new TPAQueue();
    private TPAQueue() {
    }
    public final HashMap<Player, Player> TPA_REQUESTS = new HashMap<Player, Player>();
    public final HashMap<Player, Player> TPA_REQUESTS2 = new HashMap<Player, Player>();
    public final HashMap<Player, Long> TPA_REQUESTS_TIME = new HashMap<Player, Long>();
}
