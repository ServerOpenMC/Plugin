package fr.communaywen.core.managers;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.elevator.ElevatorChunk;
import fr.communaywen.core.elevator.ElevatorManager;
import lombok.Getter;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

@Getter
public class ChunkManager {
    private static final AywenCraftPlugin plugin = AywenCraftPlugin.getInstance();
    private final Chunk chunk;
    private ElevatorChunk elevatorManager;

    public ChunkManager(Chunk chunk) {
        this.chunk = chunk;
    }

    public ElevatorChunk getElevatorManager() {
        loadElevator();
        return elevatorManager;
    }

    public void unload() {
        if (elevatorManager != null) elevatorManager.unload(chunk);
    }

    public void placeElevator(Block block) {
        loadElevator();
        elevatorManager.placeElevator(block);
    }

    public void removeElevator(Block block) {
        loadElevator();
        elevatorManager.removeElevator(block);
    }

    public void loadElevator() {
        if (elevatorManager == null) {
            PersistentDataContainer container = chunk.getPersistentDataContainer();
            byte[] data = container.get(ElevatorManager.DATA_KEY, PersistentDataType.BYTE_ARRAY);
            elevatorManager = new ElevatorChunk(data);
        }
    }
}
