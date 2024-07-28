package fr.communaywen.core.utils;

import org.bukkit.block.BlockFace;

public enum Yaw {

    NORTH,
    EAST,
    SOUTH,
    WEST;

    public Yaw getOpposite() {
        return switch (this) {
            case NORTH -> SOUTH;
            case EAST -> WEST;
            case SOUTH -> NORTH;
            case WEST -> EAST;
        };
    }

    public BlockFace toBlockFace() {
        return switch (this) {
            case NORTH -> BlockFace.NORTH;
            case EAST -> BlockFace.EAST;
            case SOUTH -> BlockFace.SOUTH;
            case WEST -> BlockFace.WEST;
        };
    }

}
