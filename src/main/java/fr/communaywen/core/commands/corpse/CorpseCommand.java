package fr.communaywen.core.commands.corpse;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.corpse.CorpseBlock;
import fr.communaywen.core.corpse.CorpseManager;
import fr.communaywen.core.credit.Credit;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.*;
import revxrsal.commands.bukkit.annotation.CommandPermission;

@Credit("iambibi_")
@Command("corpse")
@Description("Corpse")
public class CorpseCommand {
    private final AywenCraftPlugin plugin;
    public CorpseCommand(AywenCraftPlugin plugins) {
        plugin = plugins;
    }

    @Subcommand("remove")
    @Description("Supprimer une tombe")
    @CommandPermission("ayw.command.corpse.remove")
    public void remove(Player player) {
        CorpseBlock corpse = CorpseManager.getCorpseBlock(player.getTargetBlock(null, 100).getLocation());

        if (corpse != null) {
            CorpseManager.remove(corpse);
            player.sendMessage("§aTombe supprimé avec succès");
        } else {
            player.sendMessage("§cIl n'y a pas de tombe qui est target (Veuillez pointez la tombe)");
        }
    }

}
