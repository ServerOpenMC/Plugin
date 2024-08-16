package fr.communaywen.core.managers;

import fr.communaywen.core.AywenCraftPlugin;
import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

import java.util.HashMap;
import java.util.Map;

public class ChunkListManager implements Listener {
    private static final AywenCraftPlugin plugin = AywenCraftPlugin.getInstance();
    private static final Map<Chunk, ChunkManager> chunkList = new HashMap<>();

    public static ChunkManager getChunk(Chunk chunk) {
        return chunkList.computeIfAbsent(chunk, c -> new ChunkManager(chunk));
    }

    public static void unloadChunk(Chunk chunk) {
        ChunkManager chunkManager = chunkList.remove(chunk);
        if (chunkManager != null) chunkManager.unload();
    }

    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent event) {
        unloadChunk(event.getChunk());
    }

    @EventHandler
    public void onPluginDisable(PluginDisableEvent event) {
        if (event.getPlugin().equals(plugin)) {
            plugin.getLogger().info("Unloading all chunks...");
            for (ChunkManager chunkManager : chunkList.values()) chunkManager.unload();
            chunkList.clear();
        }
    }
}
