package fr.communaywen.core.homes.world;

import fr.communaywen.core.AywenCraftPlugin;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DisabledWorldHome {

    // TODO: Liste du .YML se reset quand le plugin est désactivé

    private final AywenCraftPlugin plugin;
    private final File file;
    private FileConfiguration config;
    private Map<String, WorldDisableInfo> disabledWorlds;

    public DisabledWorldHome(AywenCraftPlugin plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "home_disabled_world.yml");
        this.disabledWorlds = new HashMap<>();
        loadConfig();
    }

    public void loadConfig() {
        if(!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (Exception e) {
                AywenCraftPlugin.getInstance().getLogger().severe("Error while creating disabled worlds config: " + e.getMessage());
                e.printStackTrace();
            }
        }
        config = YamlConfiguration.loadConfiguration(file);
        loadDisabledWorlds();
    }

    private void loadDisabledWorlds() {
        disabledWorlds.clear();
        ConfigurationSection sections = config.getConfigurationSection("disabled-worlds");
        if(sections != null) {
            for(String key : sections.getKeys(false)) {
                ConfigurationSection section = sections.getConfigurationSection(key);
                if(section != null) {
                    String addedBy = section.getString("added-by", "unknown");
                    long addedOn = section.getLong("added-on", 0);
                    disabledWorlds.put(key, new WorldDisableInfo(addedBy, addedOn));
                }
            }
        }
    }

    public void saveConfig() {
        plugin.getLogger().info("Saving disabled worlds config...");
        config.set("disabled-worlds", null);
        for(Map.Entry<String, WorldDisableInfo> entry : disabledWorlds.entrySet()) {
            String key = entry.getKey();
            WorldDisableInfo info = entry.getValue();
            config.set("disabled-worlds." + key + ".added-by", info.addedBy());
            config.set("disabled-worlds." + key + ".added-on", info.addedOn());
        }
        try {
            config.save(file);
        } catch (Exception e) {
            AywenCraftPlugin.getInstance().getLogger().severe("Error while saving disabled worlds config: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void addDisabledWorld(World world, Player player) {
        if(!disabledWorlds.containsKey(world.getName())) {
            disabledWorlds.put(world.getName(), new WorldDisableInfo(player.getName(), System.currentTimeMillis()));
            saveConfig();
        }
    }

    public void removeDisabledWorld(World world) {
        if(disabledWorlds.remove(world.getName()) != null) {
            saveConfig();
        }
    }

    public boolean isDisabledWorld(World world) {
        return disabledWorlds.containsKey(world.getName());
    }

    public List<String> getDisabledWorlds() {
        return new ArrayList<>(disabledWorlds.keySet());
    }

    public String getDisabledWorldInfo(String world) {
        WorldDisableInfo info = disabledWorlds.get(world);
        if(info != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            return "§7Ajouté par §e" + info.addedBy() + " §7le §e" + sdf.format(info.addedOn());
        }
        return null;
    }
}
