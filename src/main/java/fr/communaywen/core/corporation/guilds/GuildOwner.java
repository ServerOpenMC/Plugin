package fr.communaywen.core.corporation.guilds;

import fr.communaywen.core.teams.Team;
import lombok.Getter;

import java.util.UUID;

@Getter
public class GuildOwner {

    private final Team team;
    private final UUID player;

    public GuildOwner(Team team) {
        this.team = team;
        this.player = null;
    }

    public GuildOwner(UUID owner) {
        this.team = null;
        this.player = owner;
    }

    public boolean isTeam() {
        return team != null;
    }

    public boolean isPlayer() {
        return player != null;
    }

}
