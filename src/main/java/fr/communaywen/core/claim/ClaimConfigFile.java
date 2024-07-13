package fr.communaywen.core.claim;

import fr.communaywen.core.AywenCraftPlugin;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.Arrays;

public class ClaimConfigFile {

    private File file;
    private YamlConfiguration conf;

    public ClaimConfigFile(AywenCraftPlugin plugin, String fileName) {
        file = new File(AywenCraftPlugin.getInstance().getDataFolder(), fileName);
        if (!file.exists())
            try {
                if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
                InputStream in = plugin.getResource(fileName);
                if (in != null) {
                    OutputStream out = new FileOutputStream(file);

                    byte[] buf = new byte[1024 * 4];
                    int len = in.read(buf);
                    while (len != -1) {
                        out.write(buf, 0, len);
                        len = in.read(buf);
                    }
                    out.close();
                    in.close();
                } else file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        reload();
    }

    public void addClaim(String teamName, String[] coordinates) {
        conf.set(teamName, Arrays.asList(coordinates));
        save();
    }

    public void removeClaim(String teamName) {
        conf.set(teamName, null);
        save();
    }

    public void reload() {
        try {
            conf = YamlConfiguration.loadConfiguration(new InputStreamReader(new FileInputStream(file), "UTF-8"));
        } catch (UnsupportedEncodingException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public YamlConfiguration get() {
        return conf;
    }

    public void save() {
        try {
            conf.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}