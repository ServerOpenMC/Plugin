package fr.communaywen.core.utils.chatchannel;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerChatChannel {

    private final Map<UUID, Channel> players = new HashMap<>();

    public void changeChannel(Player player, Channel channel){
        if(players.containsKey(player.getUniqueId())){
            players.replace(player.getUniqueId(), channel);
        } else {
            players.put(player.getUniqueId(), channel);
        }
    }

    public Channel getChannel(Player player){
        return players.get(player.getUniqueId()) == null ? Channel.GLOBAL : players.get(player.getUniqueId());
    }

}
