package fr.communaywen.core.listeners;

import dev.lone.itemsadder.api.CustomStack;
import dev.xernas.menulib.Menu;
import dev.xernas.menulib.utils.InventorySize;
import org.bukkit.*;
import org.bukkit.generator.structure.Structure;
import org.bukkit.generator.structure.StructureType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.packs.DataPack;
import org.bukkit.util.StructureSearchResult;
import org.jetbrains.annotations.Debug;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;
import java.util.*;

public class StructureCompass implements Listener {
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
            Player player = event.getPlayer();

            ItemStack item = player.getItemInHand();
            CustomStack customStack = CustomStack.byItemStack(item);

            assert customStack != null;
            assert customStack.getNamespacedID().equals("aywen:structure_compass");

            if (player.isSneaking()) {
//                if (item.getType().equals(Material.COMPASS)) {
                    useStructureCompass(player);
//                }
            } else {
                CompassMeta meta = (CompassMeta) item.getItemMeta();

                assert meta != null;

                if (meta.hasLodestone() && player.getCooldown(item.getType()) < 1) {
                    player.sendMessage(MessageFormat.format("La structure est à {0} block(s)", Math.round(meta.getLodestone().distance(player.getLocation()))));
                    player.setCooldown(item.getType(), 100);
                }
            }
        }
    }

    private void useStructureCompass(Player player) {
        Menu menu = new Menu(player) {
            @Override
            public @NotNull String getName() {
                return ChatColor.DARK_GREEN + "Selecteur de structure";
            }

            @Override
            public @NotNull InventorySize getInventorySize() {
                return InventorySize.LARGER;
            }

            @Override
            public void onInventoryClick(InventoryClickEvent inventoryClickEvent) {
                ItemStack item = inventoryClickEvent.getCurrentItem();
                RegisteredStructure[] structures = RegisteredStructure.values();

                assert item != null;

                List<RegisteredStructure> matches = Arrays.stream(structures).filter(structure -> structure.displayName.equals(item.getItemMeta().getDisplayName())).toList();
                if (matches.isEmpty()) return;

                RegisteredStructure structure = matches.getFirst();

                Structure structureType = Registry.STRUCTURE.get(NamespacedKey.fromString(structure.id));

                StructureSearchResult searchResult = player.getWorld().locateNearestStructure(player.getLocation(), structureType, 20000, false);

                if (searchResult == null) {
                    player.sendMessage(ChatColor.RED + "Aucune structure du type " + structureType.getKey()  + " n'as été trouvée");
                    player.closeInventory();
                    return;
                }

                ItemStack compass = player.getInventory().getItemInMainHand();
                CompassMeta meta = (CompassMeta) compass.getItemMeta();

                meta.setLodestoneTracked(false);
                meta.setLodestone(searchResult.getLocation());

                compass.setItemMeta(meta);

                player.sendMessage(ChatColor.GREEN + MessageFormat.format("La boussole pointe désormais vers la structure ({0}) la plus proche", structureType.getKey()));

                player.closeInventory();
            }

            @Override
            public @NotNull Map<Integer, ItemStack> getContent() {
                Map<Integer, ItemStack> content = new HashMap<>();

                for (int i = 0; i < RegisteredStructure.values().length; i++) {
                    System.out.println("Struct " + i);
//                    Structure structure = structures.values().stream().toList().get(i);
                    RegisteredStructure structure = RegisteredStructure.values()[i];
                    ItemStack item = new ItemStack(Material.PLAYER_HEAD);
                    SkullMeta meta = (SkullMeta) item.getItemMeta();

                    meta.setDisplayName(structure.getDisplayName());

                    item.setItemMeta(meta);
                    content.put(i % InventorySize.LARGER.getSize(), item);
                }

                return content;
            }

        };
        menu.open();
    }

    public enum RegisteredStructure {
        TNT_TRAP("struct:tnt_trap_", "Piège a TNT"),
        CLOUD("struct:nuage", "Nuage"),
        END_RETURN_PORTAL("struct:end_end_portal_", "Portail de retour de l'end");

        private final String id;
        private final String displayName;

        private RegisteredStructure(String structureId, String displayName) {
            this.id = structureId;
            this.displayName = displayName;
        }

        public String getId() {
            return this.id;
        }

        public String getDisplayName() {
            return this.displayName;
        }
    }
}
