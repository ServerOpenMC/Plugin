package fr.communaywen.core.commands;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Description;

import java.util.*;

public final class ExplodeRandomCommand {

    private final HashMap<UUID, Long> cooldowns = new HashMap<>();
    private final HashMap<UUID, Long> lastExploded = new HashMap<>();
    private static final int COOLDOWN_TIME = 300; // 5 minutes in seconds

    private static final int IMMUNITY_TIME = 60 * 60; // 1 hour in seconds

    public static final HashSet<TNTPrimed> preventedExplosvies = new HashSet<>();

    public static final int COST = 32;

    @Command("exploderandom")
    @Description("Fait exploser un joueur aléatoire, coûte 32 diamants")
    public void onCommand(Player player) {
        try {
            UUID playerId = player.getUniqueId();
            long currentTime = System.currentTimeMillis() / 1000;

            if (cooldowns.containsKey(playerId)) {
                long lastUsed = cooldowns.get(playerId);
                long timeSinceLastUse = currentTime - lastUsed;

                if (timeSinceLastUse < COOLDOWN_TIME) {
                    long timeLeft = COOLDOWN_TIME - timeSinceLastUse;
                    player.sendMessage("Vous devez attendre encore " + timeLeft + " secondes avant d'utiliser cette commande à nouveau.");
                    return;
                }
            }

            List<Player> players = player.getWorld().getPlayers();
            players.remove(player);

            List<Player> untouchedPlayers = player.getWorld().getPlayers();
            untouchedPlayers.remove(player);

            for (Player currentPlayer : untouchedPlayers) {
                if (lastExploded.containsKey(playerId)) {
                    long lastUsed = lastExploded.get(playerId);
                    long timeSinceLastUse = currentTime - lastUsed;

                    if (timeSinceLastUse < IMMUNITY_TIME) {
                        players.remove(currentPlayer);
                    }
                }
            }

            if (players.isEmpty()) {
                player.sendMessage("Aucun joueur n'a été trouvé...");

                return;
            }

            PlayerInventory inventory = player.getInventory();

            if (player.getGameMode() != GameMode.CREATIVE) {
                int totalDiamonds = Arrays.stream(inventory.getContents())
                        .filter(stack -> stack != null && stack.getType() == Material.DIAMOND) //make the stream only be of gold stacks
                        .mapToInt(stack -> stack.getAmount()) //turn stacks into their amounts
                        .sum(); //add them up

                if (totalDiamonds < COST) {
                    player.sendMessage("Il vous manque " + (COST - totalDiamonds) + " diamants");

                    return;
                }

                List<ItemStack> diamondStacks = Arrays.stream(inventory.getContents())
                        .filter(stack -> stack != null && stack.getType() == Material.DIAMOND).toList();

                int remaining = COST;

                for (ItemStack stack : diamondStacks) {
                    int ammount = stack.getAmount();

                    if (ammount < remaining) {
                        remaining -= ammount;
                        stack.setAmount(0);
                        continue;
                    }

                    stack.setAmount(ammount - remaining);

                    break;
                }
            }

            int random = new Random().nextInt(players.size());

            Player chosenPlayer = players.get(random);
            chosenPlayer.sendMessage("Tu as été désigné.");
            chosenPlayer.sendMessage("REGARDE AU DESSUS DE TOI!");

            player.sendMessage(chosenPlayer.getDisplayName() + " va reçevoir un cadeau explosif");

            TNTPrimed tnt = (TNTPrimed) chosenPlayer.getWorld().spawnEntity(chosenPlayer.getLocation().add(0, 5, 0), EntityType.TNT);

            tnt.setFuseTicks(15);

            preventedExplosvies.add(tnt);

            lastExploded.put(playerId, currentTime);

            cooldowns.put(playerId, currentTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
