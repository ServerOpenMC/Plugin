package fr.communaywen.core.listeners;

import fr.communaywen.core.utils.FreezeUtils;
import fr.communaywen.core.utils.PlayersMenuUtils;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Objects;

public class PlayersMenuListener implements Listener {

	private static int currentPage = 0;

	@EventHandler
	public void onMenuClick(InventoryClickEvent e) {
		Player player = (Player) e.getWhoClicked();

		if (e.getView().getTitle().equalsIgnoreCase(ChatColor.BLUE + "Liste des joueurs")) {
			e.setCancelled(true);

			if (e.getCurrentItem() == null) return;

			if (e.getCurrentItem().getType() == Material.PLAYER_HEAD) {
				Player target = player.getServer().getPlayerExact(ChatColor.stripColor(Objects.requireNonNull(e.getCurrentItem().getItemMeta()).getDisplayName().replace("§f", "")));
				PlayersMenuUtils.openDetailsPlayersMenu(player, target);
			} else if (e.getCurrentItem().getType() == Material.PAPER) {
				String displayName = Objects.requireNonNull(e.getCurrentItem().getItemMeta()).getDisplayName();
				if (displayName.equals(ChatColor.GREEN + "Page précédente")) {
					if (currentPage > 0) {
						currentPage--;
						PlayersMenuUtils.openPlayersMenu(player, currentPage);
					}
				} else if (displayName.equals(ChatColor.GREEN + "Page suivante")) {
					currentPage++;
					PlayersMenuUtils.openPlayersMenu(player, currentPage);
				} else if(displayName.equals(ChatColor.RED + "Quitter")) {
					player.closeInventory();
				}
			}
		} else if (e.getView().getTitle().equalsIgnoreCase(ChatColor.BLUE + "Détails")) {
			Player target = player.getServer().getPlayerExact(ChatColor.stripColor(e.getClickedInventory().getItem(4).getItemMeta().getDisplayName()));
			if (Objects.requireNonNull(e.getCurrentItem()).getType() == Material.PAPER) {
				PlayersMenuUtils.openPlayersMenu(player, currentPage);
				return;
			} else if (e.getCurrentItem().getType() == Material.COMPASS) {
				assert target != null;
				player.teleport(target.getLocation());
				player.sendMessage(ChatColor.DARK_GREEN + "Vous avez été téléporté au joueur " + ChatColor.BLUE + target.getDisplayName() + ChatColor.DARK_GREEN + " !");
				player.closeInventory();
			} else if (e.getCurrentItem().getType() == Material.ENDER_EYE) {
				assert target != null;
				player.openInventory(target.getInventory());
			} else if (e.getCurrentItem().getType() == Material.PACKED_ICE) {
				FreezeUtils.switch_freeze(player, target);
				player.closeInventory();
			} else if (e.getCurrentItem().getType() == Material.WOODEN_AXE) {
				assert target != null;
				String name = target.getName();
				Bukkit.getServer().getBanList(BanList.Type.NAME).addBan(name, "Aucune raison spécifiée", null, player.getName());
				player.sendMessage(ChatColor.BLUE + name + ChatColor.DARK_RED + " a bien été banni !");
				target.kickPlayer("Vous avez été banni pour aucune raison spécifiée");
				player.closeInventory();
			}
			e.setCancelled(true);
		}
	}
}
