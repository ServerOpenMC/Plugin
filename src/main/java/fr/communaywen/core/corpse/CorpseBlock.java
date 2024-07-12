package fr.communaywen.core.corpse;

import dev.lone.itemsadder.api.CustomBlock;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;

import java.util.UUID;

//////////////////////////////
//       Credits            //
// Idée : sajiterre         //
// Développeur : Martinouxx //
// Aide : N/A               //
//////////////////////////////

public class CorpseBlock {

    private CustomBlock block;
    private TextDisplay textDisplay;
    private String inventoryName;
    private UUID ownerUUID;
    private Location location;

    public CorpseBlock(CustomBlock block, Location location, UUID ownerUUID) {
        this.block = block;
        this.location = location;
        this.ownerUUID = ownerUUID;
        this.inventoryName = "§aCorps de §6" + Bukkit.getOfflinePlayer(ownerUUID).getName();
        createTextDisplay();
    }

    private void createTextDisplay() {
        Location loc = block.getBlock().getLocation().add(0.5, 1.5, 0.5);

        textDisplay = (TextDisplay) block.getBlock().getWorld().spawnEntity(loc, EntityType.TEXT_DISPLAY);
        textDisplay.setText(inventoryName);
        textDisplay.setAlignment(TextDisplay.TextAlignment.CENTER);
        textDisplay.setSeeThrough(true);
        textDisplay.setBackgroundColor(Color.BLACK);
        textDisplay.setBillboard(Display.Billboard.VERTICAL);
        textDisplay.setViewRange(256f);
    }

    public CustomBlock getBlock() {
        return block;
    }

    public TextDisplay getTextDisplay() {
        return textDisplay;
    }

    public void remove() {
        textDisplay.remove();
        block.remove();
    }

    public String getInventoryName() {
        return inventoryName;
    }

    public Location getLocation() {
        return location;
    }
}
