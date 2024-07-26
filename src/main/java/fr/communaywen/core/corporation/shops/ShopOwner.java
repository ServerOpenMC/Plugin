package fr.communaywen.core.corporation.shops;

import fr.communaywen.core.corporation.guilds.Guild;
import lombok.Getter;

import java.util.UUID;

@Getter
public class ShopOwner {

    private final Guild guild;
    private final UUID player;

    public ShopOwner(Guild guild) {
        this.guild = guild;
        this.player = null;
    }

    public ShopOwner(UUID owner) {
        this.guild = null;
        this.player = owner;
    }

    public boolean isGuild() {
        return guild != null;
    }

    public boolean isPlayer() {
        return player != null;
    }

}

