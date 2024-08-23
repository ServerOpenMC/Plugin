package fr.communaywen.core.claim;

import dev.xernas.menulib.Menu;
import dev.xernas.menulib.utils.InventorySize;
import dev.xernas.menulib.utils.ItemBuilder;
import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.teams.Team;
import fr.communaywen.core.utils.constant.MessageManager;
import fr.communaywen.core.utils.constant.MessageType;
import fr.communaywen.core.utils.constant.Prefix;
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
        return InventorySize.SMALLEST;
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {}

    @Override
    public @NotNull Map<Integer, ItemStack> getContent() {
        Map<Integer, ItemStack> content = new HashMap<>();

        for(int i = 0; i < getInventorySize().getSize(); i++) {
            content.put(i, new ItemBuilder(this, Material.GRAY_STAINED_GLASS_PANE, itemMeta -> itemMeta.setDisplayName(" ")));
        }

        content.put(2, new ItemBuilder(this, Material.RED_STAINED_GLASS_PANE, itemMeta -> {
            itemMeta.setDisplayName(ChatColor.RED + "Confirmer la suppression");
        }).setOnClick(event -> {
            ClaimConfigDataBase.removeClaims(claim.getTeam().getName(), claim.getClaimID());
            AywenCraftPlugin.getInstance().regions.remove(claim);
            MessageManager.sendMessageType(getOwner(), "§aLe claim a été supprimé avec succès.", Prefix.CLAIM, MessageType.SUCCESS, true);
            getOwner().closeInventory();
        }));

        content.put(6, new ItemBuilder(this, Material.GREEN_STAINED_GLASS_PANE, itemMeta -> {
            itemMeta.setDisplayName(ChatColor.GREEN + "Annuler");
        }).setNextMenu(new ClaimMenu(getOwner(), team)));

        return content;
    }
}