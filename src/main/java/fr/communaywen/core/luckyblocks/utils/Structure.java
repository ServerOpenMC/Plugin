package fr.communaywen.core.luckyblocks.utils;

import com.flowpowered.nbt.*;
import com.flowpowered.nbt.stream.NBTInputStream;
import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.claim.RegionManager;
import fr.communaywen.core.luckyblocks.tasks.DelayStructureTask;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class Structure {

    /**
     * The function that places a structure into a world from an NBT file. You can generate this file using the in-game
     * <a href="https://minecraft.gamepedia.com/Structure_Block">MC Structure Block docs</a>
     * Credit : <a href="https://github.com/rodiconmc/Rodiblock/blob/master/src/main/java/com/rodiconmc/rodiblock/Structure.java">rodiconmc</a>
     * @param file NBT file of the structure
     * @param target The lowest value location of where you want to place the structure.
     * @param mirrorX Whether or not to mirror the structure on the X coordinate. (Does not change block rotation values)
     * @param mirrorZ Whether or not to mirror the structure on the Z coordinate. (Does not change block rotation values)
     * @param player The player that is placing the structure
     * @throws IOException Exception thrown if the NBT file is formatted incorrectly.
     */
    public static void placeStructure(File file, Location target, boolean mirrorX, boolean mirrorZ, Player player) throws IOException {
        new DelayStructureTask(file, target, mirrorX, mirrorZ, player).runTaskLater(AywenCraftPlugin.getInstance(), 1);
    }

    public static void tryPlacingStructure(File file, Location target, boolean mirrorX, boolean mirrorZ, Player player) throws IOException {
        NBTInputStream input = new NBTInputStream(new FileInputStream(file));

        Tag baseCompound = input.readTag();
        int[] size;

        if (!baseCompound.getType().equals(TagType.TAG_COMPOUND)) throw new IOException("NBT File does not start in a Compound Tag");
        CompoundMap compound = ((CompoundTag) baseCompound).getValue();

        if (!compound.containsKey("size")) throw new IOException("NBT File does not start contain a size");
        Tag sizeTag = compound.get("size");
        if (!sizeTag.getType().equals(TagType.TAG_LIST)) throw new IOException("size is not a List Tag");
        ListTag sizeList = (ListTag) sizeTag;
        if (!(sizeList.getElementType().equals(IntTag.class))) throw new IOException("size List Tag is not of type Int Tag");
        if (sizeList.getValue().size() != 3) throw new IOException("size List Tag is not of type size 3");
        size = new int[]{((IntTag) sizeList.getValue().get(0)).getValue(), ((IntTag) sizeList.getValue().get(1)).getValue(), ((IntTag) sizeList.getValue().get(2)).getValue()};

        if (!compound.containsKey("palette")) throw new IOException("NBT File does not start contain a palette");
        Tag paletteTag = compound.get("palette");
        if (!paletteTag.getType().equals(TagType.TAG_LIST)) throw new IOException("palette is not a List Tag");
        ListTag paletteList = (ListTag) paletteTag;
        if (!(paletteList.getElementType().equals(CompoundTag.class))) throw new IOException("palette List Tag is not of type Compound Tag");

        BlockData[] states = new BlockData[paletteList.getValue().size()];
        for (int stateNum = 0; stateNum < states.length; stateNum++) {
            Object oTag = paletteList.getValue().get(stateNum);
            CompoundMap blockTag = ((CompoundTag) oTag).getValue();
            StringBuilder blockStateString = new StringBuilder();

            if (!blockTag.keySet().contains("Name")) throw new IOException("palette state does not have a name.");
            blockStateString.append(blockTag.get("Name").getValue()); //String now looks like "minecraft:log"

            if (blockTag.keySet().contains("Properties")) {
                blockStateString.append("[");
                if (!blockTag.get("Properties").getType().equals(TagType.TAG_COMPOUND)) throw new IOException("palette block properties is not a Compound Tag");
                CompoundMap properties = ((CompoundTag) blockTag.get("Properties")).getValue();

                Set<Map.Entry<String, Tag<?>>> properySet =  properties.entrySet();
                Iterator<Map.Entry<String, Tag<?>>> propertyIterator = properySet.iterator();
                for (int p = 0; p < properySet.size(); p++) {
                    if (p > 0) blockStateString.append(",");
                    Tag property = propertyIterator.next().getValue();
                    blockStateString.append(property.getName()).append("=").append(property.getValue());
                }
                blockStateString.append("]");
            } //If the block had properties, it now looks like "minecraft:log[axis=z]

            states[stateNum] = Bukkit.createBlockData(blockStateString.toString());
        }

        if (!compound.containsKey("blocks")) throw new IOException("NBT File does not start contain a blocks list");
        Tag blocksTag = compound.get("blocks");
        if (!blocksTag.getType().equals(TagType.TAG_LIST)) throw new IOException("Blocks is not a List Tag");
        ListTag blocksList = (ListTag) blocksTag;
        if (!(blocksList.getElementType().equals(CompoundTag.class))) throw new IOException("Blocks List Tag is not of type Compound Tag");

        List<Block> blocks = new ArrayList<>();
        for (Object oTag : blocksList.getValue()) {
            CompoundMap blockTag = ((CompoundTag) oTag).getValue();
            if (!blockTag.containsKey("pos")) throw new IOException("Blocks list does not contain a pos tag");
            Tag posTag = blockTag.get("pos");
            if (!posTag.getType().equals(TagType.TAG_LIST)) throw new IOException("pos is not a List Tag");
            ListTag posListTag = (ListTag) posTag;
            if (!(posListTag.getElementType().equals(IntTag.class))) throw new IOException("pos List Tag is not of type Int Tag");
            List posList = posListTag.getValue();
            if (posListTag.getValue().size() != 3) throw new IOException("pos List Tag is not of size 3");
            int x = ((IntTag) posList.get(0)).getValue();
            int y = ((IntTag) posList.get(1)).getValue();
            int z = ((IntTag) posList.get(2)).getValue();

            if (mirrorX) x = (size[0]-1) - x;
            if (mirrorZ) z = (size[2]-1) - z;

            if (!blockTag.containsKey("state")) throw new IOException("Blocks list does not contain a state tag.");
            Tag stateTag = blockTag.get("state");
            if (!stateTag.getType().equals(TagType.TAG_INT)) throw new IOException("state is not a Int Tag");
            IntTag stateIntTag = (IntTag) stateTag;
            if (stateIntTag.getValue() > states.length) throw new IOException("Invalid state");
            Block block = new Location(target.getWorld(), target.getX() + x, target.getY() + y, target.getZ() + z).getBlock();

            if (!block.getType().isAir() && block.getType().isSolid()) {
                continue;
            }

            if (!AywenCraftPlugin.getInstance().regions.isEmpty()) {
                for (RegionManager region : AywenCraftPlugin.getInstance().regions) {
                    if (region.isInArea(block.getLocation()) && !region.isTeamMember(player.getUniqueId())) {
                        continue;
                    }

                    block.setBlockData(states[stateIntTag.getValue()], false);
                    blocks.add(block);
                }
            } else {
                block.setBlockData(states[stateIntTag.getValue()], false);
                blocks.add(block);
            }
        }
    }

    /**
     * Permet de récupérer un fichier de structure à partir de son nom
     * @param name Nom du fichier
     * @return Fichier de structure
     */
    public static File getStructureFile(String name) {
        name = name.replace(".nbt", "");
        return new File("world/generated/aywencraft/structures/" + name + ".nbt");
    }
}
