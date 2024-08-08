package fr.communaywen.core.commands.utils;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import fr.communaywen.core.utils.chatchannel.Channel;
import org.apache.commons.lang3.EnumUtils;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.annotation.Named;

@Feature("Chat Channel")
@Credit("Martinouxx")
public class ChatChannelCMD {

    @Command("channel")
    @Description("Changer votre channel dans le chat (global/team)")
    public void command(Player player, String channel){
        if(channel == null || channel.trim().isEmpty()) {
            player.sendMessage("§6ᴄʜᴀᴛ ᴄʜᴀɴɴᴇʟ §7» §cVous devez spécifier ");
        }

        if(!EnumUtils.isValidEnum(Channel.class, channel)) {
            StringBuilder channelsList = new StringBuilder();
            for (Channel ch : Channel.values()) {
                channelsList.append(ch.name()).append(", ");
            }

            if (!channelsList.isEmpty()) {
                channelsList.setLength(channelsList.length() - 2);
            }
            player.sendMessage("§6ᴄʜᴀᴛ ᴄʜᴀɴɴᴇʟ §7» §cLes channels existant sont §7: §c" + channelsList.toString());
            return;
        }

        AywenCraftPlugin.getInstance().getManagers().getChatChannel().changeChannel(player, Channel.valueOf(channel));
        player.sendMessage("§6ᴄʜᴀᴛ ᴄʜᴀɴɴᴇʟ §7» §aVous avez changé votre channel de chat en §b" + channel + " §a!");

    }

}
