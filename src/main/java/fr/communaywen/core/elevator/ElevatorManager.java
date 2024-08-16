package fr.communaywen.core.elevator;

import dev.lone.itemsadder.api.CustomBlock;
import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.credit.Credit;
import fr.communaywen.core.credit.Feature;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;

@Credit("Gexary")
@Feature("Elevator")
public class ElevatorManager {
    private static final AywenCraftPlugin plugin = AywenCraftPlugin.getInstance();
    public static final NamespacedKey DATA_KEY = new NamespacedKey(plugin, "elevator_list");
    public static final String ELEVATOR_NAMESPACE = "gexary:elevator";

    public static boolean isElevator(Block block) {
        CustomBlock customBlock = CustomBlock.byAlreadyPlaced(block);
        return customBlock != null && customBlock.getNamespacedID().equals(ELEVATOR_NAMESPACE);
    }

    // todo: teleportation claims
}
