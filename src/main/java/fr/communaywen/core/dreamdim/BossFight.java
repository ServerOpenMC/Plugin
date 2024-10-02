package fr.communaywen.core.dreamdim;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.world.block.BlockState;
import com.sk89q.worldedit.world.block.BlockTypes;
import lombok.Getter;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class BossFight {
    @Getter private Player player;
    @Getter private Entity boss;
    @Getter private CuboidRegion room;
    @Getter private World world;

    public BossFight(Player player, Entity boss, CuboidRegion room, World world) {
        this.player = player;
        this.boss = boss;
        this.room = room;
        this.world = world;
    }

    public void clean() {
        try (EditSession editSession = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(this.world))) {
            for (BlockVector3 position : this.room) {
                BlockState blockState = editSession.getBlock(position);

                if (blockState.getBlockType() == BlockTypes.BEDROCK) {
                    editSession.setBlock(position, BlockTypes.DEEPSLATE.getDefaultState());
                }

                if (blockState.getBlockType() == BlockTypes.REINFORCED_DEEPSLATE) {
                    editSession.setBlock(position, BlockTypes.CHISELED_TUFF_BRICKS.getDefaultState());
                }
            }
        } catch (MaxChangedBlocksException e) {
            throw new RuntimeException(e);
        }
    }
}
