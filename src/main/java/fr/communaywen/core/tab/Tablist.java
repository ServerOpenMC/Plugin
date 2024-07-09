package fr.communaywen.core.tab;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import fr.communaywen.core.AywenCraftPlugin;
import net.luckperms.api.model.user.User;
import org.bukkit.entity.Player;

public class Tablist {

    ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();

    public void sendTabList(Player player, String header, String footer) {
        try {
            PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.PLAYER_LIST_HEADER_FOOTER);
            packet.getChatComponents().write(0, WrappedChatComponent.fromText(header))
                    .write(1, WrappedChatComponent.fromText(footer));
            protocolManager.sendServerPacket(player, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }

        updatePlayerPrefix(player);
    }

    private void updatePlayerPrefix(Player player) {
        User user = AywenCraftPlugin.getInstance().api.getUserManager().getUser(player.getUniqueId());
        if (user != null) {
            String prefix = user.getCachedData().getMetaData().getPrefix();
            if (prefix != null) {
                player.setPlayerListName(prefix + " " + player.getName().replace("&", "§"));
            } else {
                player.setPlayerListName(player.getName());
            }
        }
    }

}
