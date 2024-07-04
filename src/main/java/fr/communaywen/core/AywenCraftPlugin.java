package fr.communaywen.core;

import fr.communaywen.core.utils.MOTDChanger;
import fr.communaywen.core.utils.VersionCommand;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class AywenCraftPlugin extends JavaPlugin {

    private MOTDChanger motdChanger;

    @Override
    public void onEnable() {
        Bukkit.getServer().getLogger().info("Hello le monde, ici le plugin AywenCraft !");

        // Initialiser et démarrer le MOTDChanger
        motdChanger = new MOTDChanger();
        motdChanger.onEnable();

        // Enregistrer la commande /version
        this.getCommand("version").setExecutor(new VersionCommand(this));
    }

    @Override
    public void onDisable() {
        // Arrêter proprement le MOTDChanger
        if (motdChanger != null) {
            motdChanger.onDisable();
        }

        // Plugin shutdown logic
    }
}
