package fr.communaywen.core.homes;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.UUID;

@Getter
public class HomeLimit {

    private final UUID playerUUID;
    @Setter private int limit;

    public HomeLimit(UUID playerUUID, int limit) {
        this.playerUUID = playerUUID;
        this.limit = limit;
    }
}
