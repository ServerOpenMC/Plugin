package fr.communaywen.core.commands;

import fr.communaywen.core.utils.LinkerAPI;
import org.bukkit.entity.Player;
import org.checkerframework.checker.units.qual.N;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.annotation.Named;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import java.sql.SQLException;

public class ManualLinkCommand {

    private final LinkerAPI linkerAPI;

    public ManualLinkCommand(LinkerAPI linkerAPI) { this.linkerAPI = linkerAPI; }

    @Command("manuallink")
    @Description("Lie un compte Minecraft à Discord manuellement (sans verif)")
    @CommandPermission("ayw.command.manuallink")
    public void onCommand(Player player, @Named("user_id") String userId) {
        try {
            this.linkerAPI.setDatabase(player, userId);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
