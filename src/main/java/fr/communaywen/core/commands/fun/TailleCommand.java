package fr.communaywen.core.commands.fun;

import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
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
    @Description("Change la taille du joueur")
    public void onCommand(Player player, @Named("Taille (cm)") @Range(min = 100, max = 200) int size) {
        Component message = Component.text("Vous faites maintenant ", NamedTextColor.DARK_GREEN)
                                     .append(Component.text(size, NamedTextColor.GREEN, TextDecoration.BOLD))
                                     .append(Component.text("cm.", NamedTextColor.DARK_GREEN));
        player.sendMessage(message);
        double sizeRation = (double) size / 180;
        AttributeInstance playerAttribute = player.getAttribute(Attribute.GENERIC_SCALE);
        if (playerAttribute != null) playerAttribute.setBaseValue(sizeRation);
    }
}
