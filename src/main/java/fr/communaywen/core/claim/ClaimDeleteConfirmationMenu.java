package fr.communaywen.core.claim;

import dev.xernas.menulib.Menu;
import dev.xernas.menulib.utils.InventorySize;
import dev.xernas.menulib.utils.ItemBuilder;
import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.teams.Team;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class ClaimDeleteConfirmationMenu extends Menu {

    private final RegionManager claim;
    private final Team team;

    public ClaimDeleteConfirmationMenu(Player owner, RegionManager claim, Team team) {
        super(owner);
        this.claim = claim;
        this.team = team;
    }

    @Override
    public @NotNull String getName() {
        return "Confirmer la suppression";
    }

    @Override
    public @NotNull InventorySize getInventorySize() {
        return InventorySize.NORMAL;
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {}

    @Override
    public @NotNull Map<Integer, ItemStack> getContent() {
        Map<Integer, ItemStack> content = new HashMap<>();

        content.put(11, new ItemBuilder(this, Material.RED_CONCRETE, itemMeta -> {
            itemMeta.setDisplayName(ChatColor.RED + "Confirmer la suppression");
        }).setOnClick(event -> {
            ClaimConfigDataBase.removeClaims(claim.getTeam().getName(), claim.getClaimID());
            AywenCraftPlugin.getInstance().regions.remove(claim);
            getOwner().sendMessage(ChatColor.GREEN + "Le claim a été supprimé avec succès.");
            getOwner().closeInventory();
        }));

        content.put(15, new ItemBuilder(this, Material.GREEN_CONCRETE, itemMeta -> {
            itemMeta.setDisplayName(ChatColor.GREEN + "Annuler");
        }).setNextMenu(new ClaimMenu(getOwner(), team)));

        return content;
    }
}