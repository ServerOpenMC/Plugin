package fr.communaywen.core.datapacks;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.common.io.ByteSource;
import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.utils.DatapackUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.text.MessageFormat;
import java.util.*;
import java.util.logging.Level;

public class Applier {
    private AywenCraftPlugin plugin;

    private static final String separator = FileSystems.getDefault().getSeparator();

    private List<String> APPLIED_PACKS = Arrays.asList("open_mc_structures.zip");

    private final String worldDatapacksFolder;

    private YamlConfiguration config;

    public Applier(AywenCraftPlugin plugin) {
        this.plugin = plugin;

        Properties properties = new Properties();
        try {
            properties.load(Files.newInputStream(Paths.get("server.properties")));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load 'server.properties'!", e);
        }

        this.worldDatapacksFolder = this.plugin.getServer().getWorldContainer() + separator + properties.get("level-name") + separator
                + "datapacks";

        loadDatapacksFile();
    }

    public void apply() {
        boolean changed = false;
        for (String pack : APPLIED_PACKS) {
            this.plugin.saveResource("datapacks" + separator + pack, true);

            File packFile = new File(worldDatapacksFolder + separator + pack.replace(".zip", ""));

            try {
                String folderName = pack.replace(".zip", "");

                String filePath = this.plugin.getDataFolder() + separator + "datapacks" + separator + pack;
                String checksum = getFileChecksum(filePath);

                if (Objects.equals(config.getString("hashes." + folderName), checksum)) {
                    plugin.getLogger().log(Level.INFO, MessageFormat.format("Datapack {0} has not changed", pack));
                } else {
                    changed = true;

                    config.set("hashes." + folderName, checksum);
                    saveDatapacksFile();
                }

                if (packFile.exists()) {
                    DatapackUtils.deleteDirectory(packFile);
                }

                DatapackUtils.fileWalk(worldDatapacksFolder, new File(this.plugin.getDataFolder() + separator + "datapacks" + separator + pack), true, folderName);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        if (changed) {
            plugin.getLogger().log(Level.WARNING, "Some datapacks have changed, stopping...");
            Bukkit.shutdown();
        }
    }

    private String getFileChecksum(String filePath) throws IOException {
        ByteSource byteSource = com.google.common.io.Files.asByteSource(new File(filePath));
        HashCode hc = byteSource.hash(Hashing.md5());
        return hc.toString();
    }

    private void loadDatapacksFile() {
        File datapacksFile = new File(plugin.getDataFolder(), "datapacks.yml");
        if (!datapacksFile.exists()) {
            plugin.saveResource("datapacks.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(datapacksFile);
    }

    private void saveDatapacksFile() {
        try {
            File datapacksFile = new File(plugin.getDataFolder(), "datapacks.yml");
            config.save(datapacksFile);
        } catch (IOException ignored) { }
    }
}
