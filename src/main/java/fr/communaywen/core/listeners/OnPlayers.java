package fr.communaywen.core.listeners;

import fr.communaywen.core.utils.DraftAPI;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.data.DataMutateResult;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class OnPlayers implements Listener {

    private LuckPerms luckPerms;

    public void setLuckPerms(LuckPerms luckPerms) {
        this.luckPerms = luckPerms;
    }

    public void addPermission(User user, String permission) {
        // Add the permission
        user.data().add(Node.builder(permission).build());

        // Now we need to save changes.
        luckPerms.getUserManager().saveUser(user);
    }

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
                User lpPlayer = this.luckPerms.getPlayerAdapter(Player.class).getUser(player);

                int level = user.getInt("level");

                if (level < 10){ break; }

                int firstDigit = level % 10;

                String permissionNode = "ayw.levels." + (firstDigit * 10);
                addPermission(lpPlayer, permissionNode);
            }
        }
    }
}
