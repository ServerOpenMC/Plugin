package fr.communaywen.core.corporation.guilds;

import fr.communaywen.core.corporation.guilds.data.MerchantData;
import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import fr.communaywen.core.economy.EconomyManager;
import fr.communaywen.core.utils.MethodState;
import fr.communaywen.core.utils.Queue;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Credit("Xernas")
@Feature("Guilds")
@Getter
public class GuildManager {

    private final List<Guild> guilds = new ArrayList<>();
    private final Queue<UUID, Guild> pendingApplications = new Queue<>(100);

    public void createGuild(String name, GuildOwner owner, EconomyManager economyManager) {
        guilds.add(new Guild(name, owner, economyManager));
    }

    public void applyToGuild(UUID player, Guild guild) {
        pendingApplications.add(player, guild);
    }

    public void acceptApplication(UUID player, Guild guild) {
        guild.addMerchant(player, new MerchantData());
        pendingApplications.remove(player);
    }

    public boolean hasPendingApplicationFor(UUID player, Guild guild) {
        return pendingApplications.get(player) == guild;
    }

    public void denyApplication(UUID player, Guild guild) {
        pendingApplications.remove(player);
    }

    public List<UUID> getPendingApplications(Guild guild) {
        List<UUID> players = new ArrayList<>();
        for (UUID player : pendingApplications.getQueue().keySet()) {
            if (hasPendingApplicationFor(player, guild)) {
                players.add(player);
            }
        }
        return players;
    }

    public boolean liquidateGuild(Guild guild) {
        if (!guild.getMerchants().isEmpty()) {
            fireAllMerchants(guild);
        }
        if (guild.getBalance() > 0) {
            return false;
        }
        guilds.remove(guild);
        return true;
    }

    public void fireAllMerchants(Guild guild) {
        for (UUID uuid : guild.getMerchants().keySet()) {
            guild.fireMerchant(uuid);
        }
    }

    public MethodState leaveGuild(UUID player) {
        Guild guild = getGuild(player);
        if (guild.isOwner(player)) {
            if (guild.getMerchants().isEmpty()) {
                if (guild.isUniqueOwner(player)) {
                    if (!liquidateGuild(guild)) {
                        return MethodState.WARNING;
                    }
                    return MethodState.SUCCESS;
                }
                return MethodState.SPECIAL;
            }
            return MethodState.FAILURE;
        }
        MerchantData data = guild.getMerchant(player);
        guild.removeMerchant(player);
        if (guild.getMerchants().isEmpty()) {
            if (!liquidateGuild(guild)) {
                guild.addMerchant(player, data);
                return MethodState.WARNING;
            }
        }
        return MethodState.SUCCESS;
    }

    public Guild getGuild(String name) {
        for (Guild guild : guilds) {
            if (guild.getName().equals(name)) {
                return guild;
            }
        }
        return null;
    }

    public Guild getGuild(UUID player) {
        for (Guild guild : guilds) {
            if (guild.getMerchants().containsKey(player)) {
                return guild;
            }
            GuildOwner owner = guild.getOwner();
            if (owner.isPlayer()) {
                if (owner.getPlayer().equals(player)) {
                    return guild;
                }
            }
            if (owner.isTeam()) {
                if (owner.getTeam().getPlayers().contains(player)) {
                    return guild;
                }
            }
        }
        return null;
    }

    public boolean isInGuild(UUID player) {
        return getGuild(player) != null;
    }

    public boolean isMerchantOfGuild(UUID player, Guild guild) {
        return guild.getMerchants().containsKey(player);
    }

    public boolean guildExists(String name) {
        return getGuild(name) != null;
    }

}
