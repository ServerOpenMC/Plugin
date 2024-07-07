package fr.communaywen.core.commands;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Stream;

public final class ExplodeRandomCommand implements CommandExecutor {

    private final HashMap<UUID, Long> cooldowns = new HashMap<>();
    private static final int COOLDOWN_TIME = 300; // 5 minutes in seconds

    public static final HashSet<TNTPrimed> preventedExplosvies = new HashSet<>();

    public static final int COST = 32;

    @Override
    public boolean onCommand(final @NotNull CommandSender sender,
                             final @NotNull Command command,
                             final @NotNull String label,
                             final @NotNull String[] args) {
        if (sender instanceof Player player) {
            UUID playerId = player.getUniqueId();
            long currentTime = System.currentTimeMillis() / 1000;

            if (cooldowns.containsKey(playerId)) {
                long lastUsed = cooldowns.get(playerId);
                long timeSinceLastUse = currentTime - lastUsed;

                if (timeSinceLastUse < COOLDOWN_TIME) {
                    long timeLeft = COOLDOWN_TIME - timeSinceLastUse;
                    player.sendMessage("Vous devez attendre encore " + timeLeft + " secondes avant d'utiliser cette commande à nouveau.");
                    return true;
                }
            }

            List<Player> players = player.getWorld().getPlayers();

            players.remove(player);

            if (players.size() < 1) {
                player.sendMessage("Aucun joueur n'as été trouvée...");

                return true;
            }

            PlayerInventory inventory = player.getInventory();

            if (player.getGameMode() != GameMode.CREATIVE) {
                System.out.println("Before filter");

                int totalDiamonds = Arrays.stream(inventory.getContents())
                        .filter(stack -> stack != null && stack.getType() == Material.DIAMOND) //make the stream only be of gold stacks
                        .mapToInt(stack -> stack.getAmount()) //turn stacks into their amounts
                        .sum(); //add them up

                System.out.println("DIAMONDS");
                System.out.println(totalDiamonds);

                if (totalDiamonds < COST) {
                    player.sendMessage("Il vous manque " + (COST - totalDiamonds) + " diamants");

                    return true;
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

            cooldowns.put(playerId, currentTime);
        }

        return true;
    }
}
