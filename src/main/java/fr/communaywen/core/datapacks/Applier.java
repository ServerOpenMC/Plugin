package fr.communaywen.core.datapacks;

import fr.communaywen.core.AywenCraftPlugin;
import org.bukkit.Bukkit;
import org.bukkit.packs.DataPack;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class Applier {
    private AywenCraftPlugin plugin;

    private static final String separator = FileSystems.getDefault().getSeparator();

    private List<String> APPLIED_PACKS = Arrays.asList("open_mc_pack.zip");

    private final String worldDatapacksFolder;

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
    }

    public void apply() {
        System.out.println("APPLYING PACK");
//        for (String pack : APPLIED_PACKS) {
//            this.plugin.saveResource("datapacks" + separator + pack, true);
//
//            System.out.println("Extracted pack!");
//
//            new File(this.plugin.getDataFolder() + separator + "datapacks" + separator + pack)
//                    .renameTo(new File(worldDatapacksFolder + separator + pack));
//
//        }

    }
}
