
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class ClearLag{

  new BukkitRunnable() {
    @Override
    public void run() {
      ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
      Bukkit.dispatchCommand(console, "kill @e[type=item]");
    }
  }.runTaskTimer(this, 0L, 18000L);

}
