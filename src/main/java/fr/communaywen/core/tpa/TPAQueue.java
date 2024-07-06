package fr.communaywen.core.tpa;

import java.util.HashMap;

import fr.communaywen.core.tpa.TPARequest;
import org.bukkit.entity.Player;
import org.bukkit.Location;

public class TPAQueue {
    public static final TPAQueue INSTANCE = new TPAQueue();
    private TPAQueue() {
    }
    public final HashMap<Player, TPARequest> TPA_REQUESTS = new HashMap<Player, TPARequest>();
}
