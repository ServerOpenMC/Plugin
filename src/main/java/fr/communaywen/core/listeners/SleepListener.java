package fr.communaywen.core.listeners;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;

public class SleepListener implements Listener {

    @EventHandler
    public void onSleep(PlayerBedEnterEvent event) {
        if(event.getBedEnterResult() == PlayerBedEnterEvent.BedEnterResult.OK){
            int sleepNumber = 1;
            for(Player player : Bukkit.getOnlinePlayers()){
                if(player.isSleeping()) sleepNumber++;
            }
            if(sleepNumber >= getMinSleeper(event.getPlayer().getWorld())) passNight(event.getPlayer().getWorld());
        }
    }

    private int getMinSleeper(World world){
        int playersNumber = world.getPlayers().size();
        if(playersNumber < 2){
            return 1;
        }else if(playersNumber < 5){
            return 2;
        }else if(playersNumber < 7){
            return 3;
        }else if(playersNumber < 15){
            return 4;
        }else if(playersNumber < 20){
            return 5;
        }else if(playersNumber < 30){
            return 6;
        }else if(playersNumber < 40){
            return 7;
        }else if(playersNumber < 60){
            return 8;
        }else if(playersNumber < 90){
            return 9;
        }else if(playersNumber < 200){
            return 10;
        }else{
            return 11;
        }
    }

    private void passNight(World world){
        world.setStorm(false);
        world.setThundering(false);
        world.setTime(0);
    }
}
