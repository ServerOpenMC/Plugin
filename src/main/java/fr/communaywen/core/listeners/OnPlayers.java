package fr.communaywen.core.listeners;

import fr.communaywen.core.utils.DraftAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class OnPlayers implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) throws IOException { // Donne une permissions en fonction du niveau
        Player player = event.getPlayer();
        DraftAPI draftAPI = new DraftAPI();

        JSONObject data = new JSONObject(draftAPI.getTop());
        JSONArray users = data.getJSONArray("users");

        for (int i = 0; i < users.length(); i++) {
            JSONObject user = users.getJSONObject(i);
            String discordUsername = user.getString("username");

            if (player.getName().equalsIgnoreCase(discordUsername)){
                int firstDigit = Character.getNumericValue(String.valueOf(user.getInt("level")).charAt(0)); // booouuuuh c'est cracra
                switch(firstDigit){
                    case 1: // Niveau 1x
                        player.sendMessage("Tu as le droit au récompense de niveau 1");
                        break;
                    case 2:
                        player.sendMessage("Tu as le droit au niveau 2");
                        break;
                    case 3:
                        player.sendMessage("Tu as le droit au niveau 3");
                        break;
                    case 4:
                        player.sendMessage("Tu as le droit au niveau 4");
                        break;
                    case 5:
                        player.sendMessage("Tu as le droit au niveau 5");
                        break;
                }
            }
        }
    }
}
