package fr.communaywen.core.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PlayersMenuUtils {
	
	public static String state = "§2Unfreeze";
	
	public static void openPlayersMenu(Player player) {
		ArrayList<Player> list = new ArrayList<>(player.getServer().getOnlinePlayers());
		Inventory playersgui = Bukkit.createInventory(player, 45, ChatColor.BLUE + "Liste des joueurs");
		
		for (Player target : list) {
			ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
			SkullMeta meta = (SkullMeta) playerHead.getItemMeta();
			
			ArrayList<String> lore = new ArrayList<>();
			lore.add(ChatColor.GOLD + "Vie : " + ChatColor.RED + target.getHealth());
			lore.add(ChatColor.GOLD + "XP : " + ChatColor.AQUA + target.getExp());
			
			String name = target.getName();
			
			if (meta != null) {
				meta.setDisplayName(ChatColor.WHITE + name);
				meta.setOwner(name);
				meta.setLore(lore);
			}
			
			playerHead.setItemMeta(meta);
			
			playersgui.addItem(playerHead);
		}
		player.openInventory(playersgui);
	}
	
	public static void openDetailsPlayersMenu(Player player, Player target) {
		Inventory playerDetails = Bukkit.createInventory(player, 9, ChatColor.BLUE + "Détails");
		
		// Creating items
		ItemStack back = new ItemStack(Material.BARRIER);
		ItemStack position = new ItemStack(Material.COMPASS);
		ItemStack health = new ItemStack(Material.APPLE);
		ItemStack hunger = new ItemStack(Material.COOKED_BEEF);
		ItemStack head = new ItemStack(Material.PLAYER_HEAD);
		ItemStack exp = new ItemStack(Material.EXPERIENCE_BOTTLE);
		ItemStack inventory = new ItemStack(Material.ENDER_EYE);
		ItemStack freeze = new ItemStack(Material.PACKED_ICE);
		ItemStack ban = new ItemStack(Material.WOODEN_AXE);
		
		// Meta items
		ItemMeta back_meta = back.getItemMeta();
		ItemMeta position_meta = position.getItemMeta();
		ItemMeta health_meta = health.getItemMeta();
		ItemMeta hunger_meta = hunger.getItemMeta();
		SkullMeta head_meta = (SkullMeta) head.getItemMeta();
		ItemMeta exp_meta = exp.getItemMeta();
		ItemMeta inventory_meta = inventory.getItemMeta();
		ItemMeta freeze_meta = freeze.getItemMeta();
		ItemMeta ban_meta = ban.getItemMeta();
		
		if (head_meta != null) {
			head_meta.setOwner(target.getName());
		}
		
		// Change Meta items
		Objects.requireNonNull(back_meta).setDisplayName(ChatColor.DARK_GREEN + "Retour");
		Objects.requireNonNull(position_meta).setDisplayName(ChatColor.DARK_GREEN + "Position/Cliquer pour se téléporter");
		Objects.requireNonNull(health_meta).setDisplayName(ChatColor.DARK_GREEN + "Vie");
		Objects.requireNonNull(hunger_meta).setDisplayName(ChatColor.DARK_GREEN + "Faim");
		Objects.requireNonNull(head_meta).setDisplayName(ChatColor.DARK_GREEN + target.getDisplayName());
		Objects.requireNonNull(exp_meta).setDisplayName(ChatColor.DARK_GREEN + "Expérience");
		Objects.requireNonNull(inventory_meta).setDisplayName(ChatColor.DARK_GREEN + "Voir l'inventaire");
		Objects.requireNonNull(freeze_meta).setDisplayName(ChatColor.DARK_GREEN + "Geler le joueur");
		Objects.requireNonNull(ban_meta).setDisplayName(ChatColor.DARK_GREEN + "Bannir le joueur");
		
		// Change lore of items
		position_meta.setLore(List.of(ChatColor.BLUE + String.valueOf(target.getLocation().getBlockX()) + ", " + target.getLocation().getBlockY() + ", " + target.getLocation().getBlockZ()));
		health_meta.setLore(List.of(ChatColor.BLUE + String.valueOf(target.getHealth())));
		hunger_meta.setLore(List.of(ChatColor.BLUE + String.valueOf(target.getFoodLevel())));
		exp_meta.setLore(List.of(ChatColor.BLUE + "Niveau " + target.getLevel(), "Xp : " + target.getExp()));
		freeze_meta.setLore(List.of(ChatColor.BLUE + "État : " + state));
		
		// Set in items
		back.setItemMeta(back_meta);
		position.setItemMeta(position_meta);
		health.setItemMeta(health_meta);
		hunger.setItemMeta(hunger_meta);
		head.setItemMeta(head_meta);
		exp.setItemMeta(exp_meta);
		inventory.setItemMeta(inventory_meta);
		freeze.setItemMeta(freeze_meta);
		ban.setItemMeta(ban_meta);
		
		// Add in inventory
		playerDetails.addItem(back);
		playerDetails.addItem(position);
		playerDetails.addItem(health);
		playerDetails.addItem(hunger);
		playerDetails.addItem(head);
		playerDetails.addItem(exp);
		playerDetails.addItem(inventory);
		playerDetails.addItem(freeze);
		playerDetails.addItem(ban);
		
		player.openInventory(playerDetails);
	}
}
