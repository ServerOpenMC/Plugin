package fr.communaywen.core;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class AywenCraftPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getServer().getLogger().info("Hello le monde, ici le plugin AywenCraft !");
        getServer().getPluginManager().registerEvents(new je_ne_sais_pas_quoi_mettre_comme_nom_mais_ca_permet_de_ne_pas_casser_le_champs_quand_on_saute_dessusje_ne_sais_pas_quoi_mettre_comme_nom_mais_ca_permet_de_ne_pas_casser_le_champs_quand_on_saute_dessus(),this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
