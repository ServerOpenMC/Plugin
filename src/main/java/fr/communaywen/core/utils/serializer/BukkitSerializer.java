package fr.communaywen.core.utils.serializer;

import lombok.Cleanup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class BukkitSerializer {
    public byte[] serializeItemStacks(ItemStack[] inv) throws Exception {
        try {
            @Cleanup
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            @Cleanup
            BukkitObjectOutputStream bos = new BukkitObjectOutputStream(os);
            bos.writeObject(inv);
            return os.toByteArray();
        } catch (IOException ex) {
            throw ex;
        }
    }

    public ItemStack[] deserializeItemStacks(byte[] b) throws Exception {
        try {
            @Cleanup
            ByteArrayInputStream bais = new ByteArrayInputStream(b);
            @Cleanup
            BukkitObjectInputStream bois = new BukkitObjectInputStream(bais);
            return (ItemStack[]) bois.readObject();
        } catch (IOException ex) {
            throw ex;
        }
    }
}