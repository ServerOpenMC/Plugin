package fr.communaywen.core.mailboxes.menu.letter;

import fr.communaywen.core.AywenCraftPlugin;
import fr.communaywen.core.mailboxes.MailboxManager;
import fr.communaywen.core.mailboxes.utils.MailboxInv;
import fr.communaywen.core.mailboxes.utils.MailboxMenuManager;
import fr.communaywen.core.settings.SettingsCache;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static fr.communaywen.core.mailboxes.utils.MailboxMenuManager.*;
import static fr.communaywen.core.mailboxes.utils.MailboxUtils.getHead;
import static fr.communaywen.core.mailboxes.utils.MailboxUtils.sendFailureMessage;

public class SendingLetter extends MailboxInv {
	private final static String INV_NAME = "\uF990\uE003";
	private final OfflinePlayer receiver;
	private final AywenCraftPlugin plugin;
//	private final TeamManager teamManager;
//	private final List<String> playerFriends;   -> Temporaire
	
	int mail_accept;
	
	public SendingLetter(Player player, OfflinePlayer receiver, AywenCraftPlugin plugin) {
		super(player);
		this.receiver = receiver;
		this.plugin = plugin;
//		this.teamManager = plugin.getManagers().getTeamManager();
//		FriendsManager friendsManager = plugin.getManagers().getFriendsManager();
//		playerFriends = (List<String>) friendsManager.getFriendsAsync(player.getName());
		this.mail_accept = SettingsCache.settingsMap.get(receiver.getUniqueId().toString()).mail_accept();
		inventory = Bukkit.createInventory(this, 54, MailboxMenuManager.getInvTitle(INV_NAME));
		inventory.setItem(49, getHead(receiver));
		inventory.setItem(45, homeBtn());
		inventory.setItem(48, sendBtn());
		inventory.setItem(50, cancelBtn());
		
		for (int i = 0; i < 9; i++) inventory.setItem(i, transparentItem());
	}
	
	@Override
	public void openInventory() {
		player.openInventory(this.inventory);
	}
	
	public ItemStack[] getItems() {
		List<ItemStack> itemsList = new ArrayList<>(27);
		for (int slot = 9; slot < 36; slot++) {
			ItemStack item = inventory.getItem(slot);
			if (item != null && ! item.getType().isAir()) itemsList.add(item);
		}
		return itemsList.toArray(new ItemStack[0]);
	}
	
	public void sendLetter() {
		ItemStack[] items = getItems();
		inventory.clear();
		player.closeInventory();
		if (items.length == 0) {
			sendFailureMessage(player, "Vous ne pouvez pas envoyer de lettre vide");
			return;
		}
		plugin.getLogger().info("Mail Accept" + mail_accept);
		switch (mail_accept) {
			case 0:
				sendFailureMessage(player, "Ce joueur n'accepte pas les lettres");
				for (ItemStack item : items) player.getWorld().dropItemNaturally(player.getLocation(), item);
				break;

//			case 1:
//				if (! playerFriends.contains(receiver.getName())) {
//					sendFailureMessage(player, "Ce joueur n'accepte pas les lettres");
//				} else {
//					if (! MailboxManager.sendItems(player, receiver, items)) {
//						MailboxManager.givePlayerItems(player, items);
//					}
//				}
//				break;

//			case 2:
//				String playerTeamName = teamManager.getTeamByPlayer(player.getUniqueId()).getName();
//				String receiverTeamName = teamManager.getTeamByPlayer(receiver.getUniqueId()).getName();
//				if (! playerTeamName.equals(receiverTeamName)) {
//					sendFailureMessage(player, "Ce joueur n'accepte pas les lettres");
//				} else {
//					if (! MailboxManager.sendItems(player, receiver, items)) {
//						MailboxManager.givePlayerItems(player, items);
//					}
//				}
//				break;
			
			default:
				if (! MailboxManager.sendItems(player, receiver, items)) MailboxManager.givePlayerItems(player, items);
				break;
		}
	}
	
	public void giveItems() {
		MailboxManager.givePlayerItems(player, getItems());
	}
	
	public boolean noSpace(ItemStack item) {
		if (item == null || item.getType().isAir()) return false;
		int size = item.getAmount();
		for (int slot = 9; slot < 36; slot++) {
			ItemStack targetItem = inventory.getItem(slot);
			if (targetItem == null || targetItem.getType().isAir()) return false;
			if (targetItem.isSimilar(item)) size -= targetItem.getMaxStackSize() - targetItem.getAmount();
			if (size <= 0) return false;
		}
		return true;
	}
}
