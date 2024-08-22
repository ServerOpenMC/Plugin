package fr.communaywen.core.luckyblocks;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.GlowSquid;
import org.bukkit.entity.Player;

public class SpawnGlowSquid extends LuckyBlocksEvents{

    public SpawnGlowSquid() {
        super("SpawnGlowSquid", "Fait spawn un glow squid au dessus de nous", 0.5f);
    }

    @Override
    public void open(Player player, Block luckyBlock) {
        Location location = luckyBlock.getLocation();
        GlowSquid glowSquid = (GlowSquid) location.getWorld().spawn(location, GlowSquid.class);
    }
}
