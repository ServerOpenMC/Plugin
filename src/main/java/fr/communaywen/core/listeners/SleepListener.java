package fr.communaywen.core.listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class SleepListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        World world = event.getPlayer().getWorld();
        world.setGameRule(GameRule.PLAYERS_SLEEPING_PERCENTAGE, getPercentage(world.getPlayers().size()+1));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        World world = event.getPlayer().getWorld();
        world.setGameRule(GameRule.PLAYERS_SLEEPING_PERCENTAGE, getPercentage(world.getPlayers().size()-1));
    }

    @EventHandler
    public void onChangeWorld(PlayerChangedWorldEvent event){
        World oldWorld = event.getFrom();
        World newWorld = event.getPlayer().getWorld();
        oldWorld.setGameRule(GameRule.PLAYERS_SLEEPING_PERCENTAGE, getPercentage(oldWorld.getPlayers().size()));
        newWorld.setGameRule(GameRule.PLAYERS_SLEEPING_PERCENTAGE, getPercentage(newWorld.getPlayers().size()));
    }

    private int getPercentage(int playersNumber){
        if(playersNumber < 4){
            return 51;
        }else if(playersNumber < 10){
            return 43;
        }else if(playersNumber < 20){
            return 31;
        }else if(playersNumber < 27){
            return 26;
        }else if(playersNumber < 35){
            return 23;
        }else if(playersNumber < 39){
            return 21;
        }else if(playersNumber < 45){
            return 18;
        }else if(playersNumber < 57){
            return 16;
        }else if(playersNumber < 61){
            return 15;
        }else if(playersNumber < 65){
            return 14;
        }else if(playersNumber < 70){
            return 13;
        }else if(playersNumber < 76){
            return 12;
        }else if(playersNumber < 91){
            return 11;
        }else if(playersNumber < 101){
            return 10;
        }else{
            return 9;
        }
    }
}