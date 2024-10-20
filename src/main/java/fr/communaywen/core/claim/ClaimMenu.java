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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ClaimMenu extends Menu {

    private final List<RegionManager> claims;
    private final Team team;

    public ClaimMenu(Player owner, Team team) {
        super(owner);
        this.team = team;
        this.claims = AywenCraftPlugin.getInstance().regions.stream()
                .filter(c -> c.getTeam().equals(team))
                .collect(Collectors.toList());
    }

    @Override
    public @NotNull String getName() {
        return "Liste des Claims de la team" + team.getName();
    }

    @Override
    public @NotNull InventorySize getInventorySize() {
        return InventorySize.SMALLEST;
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) { }

    @Override
    public @NotNull Map<Integer, ItemStack> getContent() {
        Map<Integer, ItemStack> content = new HashMap<>();

        for(int i = 0; i < getInventorySize().getSize(); i++) {
            content.put(i, new ItemBuilder(this, Material.GRAY_STAINED_GLASS_PANE, itemMeta -> itemMeta.setDisplayName(" ")));
        }

        int firstSlot = (9 - Math.min(claims.size(), 7)) / 2;

        for (int i = 0; i < Math.min(claims.size(), 7); i++) {
            RegionManager claim = claims.get(i);
            int slots = firstSlot + i;
            int finalI = i;
            content.put(slots, new ItemBuilder(this, Material.GRASS_BLOCK, itemMeta -> {
                itemMeta.setDisplayName(ChatColor.GOLD + "Claim #" + (finalI + 1));
                itemMeta.setLore(List.of(
                        ChatColor.GOLD + "Team: " + ChatColor.YELLOW + claim.getTeam().getName(),
                        ChatColor.GOLD + "ID: " + ChatColor.YELLOW + claim.getClaimID(),
                        ChatColor.GOLD + "Position: ",
                        ChatColor.DARK_GRAY + "  ■ " + ChatColor.GOLD + " X: " + ChatColor.YELLOW + claim.getMiddle().getBlockX(),
                        ChatColor.DARK_GRAY + "  ■ " + ChatColor.GOLD + " Z: " + ChatColor.YELLOW + claim.getMiddle().getBlockZ(),
                        "",
                        ChatColor.RED + "Cliquez gauche pour supprimer",
                        ChatColor.GREEN + "Cliquez droit pour voir le claim",
                        "§bⓘ §7Pour plus d'information sur le claim",
                        "§7faites un clic droit sur le bloc",
                        "§7avec un §eClaim Info Wand"
                ));
            }).setOnClick(event -> {
                if(event.isLeftClick()) {
                    new ClaimDeleteConfirmationMenu(getOwner(), claim, team).open();
                } else if (event.isRightClick()) {
                    getOwner().closeInventory();
                    ClaimListener.toggleClaimParticles(getOwner(), claim);
                }
            }));
        }

        content.put(8, new ItemBuilder(this, Material.BARRIER, itemMeta -> {
            itemMeta.setDisplayName(ChatColor.RED + "Fermer");
        }).setCloseButton());

        return content;
    }
}