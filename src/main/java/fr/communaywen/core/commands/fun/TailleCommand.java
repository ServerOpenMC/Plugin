package fr.communaywen.core.commands.fun;

import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.annotation.Named;
import revxrsal.commands.annotation.Range;

@Feature("Taille")
@Credit({"Gyro3630", "Gexary"})
public class TailleCommand {
    @Command({"taille", "size"})
    @Description("Change la taille du joueur (Par défaut 187cm)")
    public void onCommand(Player player, @Named("Taille (cm)") @Range(min = 100, max = 200) int size) {
        Component message = Component.text("Vous faites maintenant ", NamedTextColor.DARK_GREEN)
                                     .append(Component.text(size + "cm.", NamedTextColor.GREEN));
        player.sendMessage(message);
        double sizeRation = (double) size / 187;
        AttributeInstance playerAttribute = player.getAttribute(Attribute.GENERIC_SCALE);
        if (playerAttribute != null) playerAttribute.setBaseValue(sizeRation);
    }
}
