package fr.communaywen.core.luckyblocks.commands;

import dev.xernas.menulib.Menu;
import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import fr.communaywen.core.luckyblocks.guis.LuckyBlockGUI;
import fr.communaywen.core.luckyblocks.managers.LBPlayerManager;
import fr.communaywen.core.luckyblocks.managers.LuckyBlockManager;
import fr.communaywen.core.luckyblocks.objects.LuckyBlockEvent;
import fr.communaywen.core.luckyblocks.utils.LBUtils;
import fr.communaywen.core.utils.constant.MessageManager;
import fr.communaywen.core.utils.constant.MessageType;
import fr.communaywen.core.utils.constant.Prefix;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import revxrsal.commands.annotation.*;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import java.util.Random;
import java.util.UUID;

@Feature("Lucky Blocks")
@Credit("Fnafgameur")
@Command({"luckyblock", "lb"})
public class LuckyBlockCommand {

    private final Random random = new Random();
    private final LBPlayerManager playerManager;
    private final LuckyBlockManager luckyBlockManager;

    public LuckyBlockCommand(LBPlayerManager playerManager, LuckyBlockManager luckyBlockManager) {
        this.playerManager = playerManager;
        this.luckyBlockManager = luckyBlockManager;
    }

    @DefaultFor("~")
    @CommandPermission("ayw.command.luckyblock")
    public void defaultCommand(Player player) {
        sendHelp(player);
    }

    @Subcommand("help")
    public void help(Player player) {
        sendHelp(player);
    }

    @Subcommand("claim")
    public void claim(Player player) {

        UUID uuid = player.getUniqueId();

        if (!playerManager.canClaimLuckyBlocks(uuid)) {
            StringBuilder sb = new StringBuilder();
            int timeHour = playerManager.getRemainingHours(uuid);
            int timeMinute = playerManager.getRemainingMinutes(uuid);
            int timeSecond = playerManager.getRemainingSeconds(uuid);

            sb.append(timeHour)
                    .append("h")
                    .append(timeMinute)
                    .append("m")
                    .append(timeSecond)
                    .append("s");

            player.sendMessage(sb.toString());
            MessageManager.sendMessageType(player, "Vous avez déjà réclamé vos lucky blocks du jour ! Temps restant : " + sb, Prefix.LUCKYBLOCK, MessageType.ERROR, true);
            return;
        }

        ItemStack luckyBlock = LBUtils.getLuckyBlockItem();
        int amount = random.nextInt(3) + 1;

        for (int i = 0; i < amount; i++) {

            if (player.getInventory().firstEmpty() == -1) {
                player.getWorld().dropItem(player.getLocation(), luckyBlock);
                continue;
            }

            player.getInventory().addItem(luckyBlock);
        }

        playerManager.setLuckyBlocksCooldown(uuid, System.currentTimeMillis());
        MessageManager.sendMessageType(player, "§aVous avez réclamé §6" + amount + " §alucky blocks !", Prefix.LUCKYBLOCK, MessageType.SUCCESS, true);
    }

    @Subcommand("event")
    @CommandPermission("ayw.command.luckyblock.admin")
    @AutoComplete("@lbEventsId")
    public void event(Player player, @Named("lbEventsId") String eventId) {

        LuckyBlockEvent event = luckyBlockManager.getEventById(eventId);

        if (event == null) {
            MessageManager.sendMessageType(player, "Cet événement n'existe pas !", Prefix.LUCKYBLOCK, MessageType.ERROR, true);
            return;
        }

        event.onOpen(player, player.getLocation().getBlock());
        MessageManager.sendMessageType(player, "Vous avez déclenché l'événement §6" + event.getName() + "§7 !", Prefix.LUCKYBLOCK, MessageType.SUCCESS, true);
    }

    @Subcommand("eventlist")
    @CommandPermission("ayw.command.luckyblock")
    public void eventList(Player player) {
        Menu menu = new LuckyBlockGUI(player, luckyBlockManager);
        menu.open();
    }

    private void sendHelp(Player player) {

        String help = """
                §7---------- §bLucky Blocks §7----------
                §8/§eluckyblock §bclaim §8- §7Permet de réclamer entre 1 et 3 lucky blocks par jour
                §8/§eluckyblock §beventlist §8- §7Permet de voir la liste des événements
                """;

        if (player.hasPermission("ayw.command.luckyblock.admin")) {
            help += """
                    §8/§eluckyblock §bevent §a<eventId> §8- §7Permet de déclencher un événement""";
        }

        player.sendMessage(help);
    }
}
