package com.jkush321.autowalls.listeners;

import com.jkush321.autowalls.AutoWalls;
import com.jkush321.autowalls.WallDropper;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {

    private AutoWalls plugin;

    public PlayerMoveListener(AutoWalls plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e)
    {
        AutoWalls.setLastEventToNow(e.getPlayer());

        Player p = e.getPlayer();

        //FINALLY! Prevent pretty much all forms of cheating by not allowing players to leave their quadrants.

        if (AutoWalls.gameInProgress) {
            if (WallDropper.time > 0) {
                //Game must be in progress
                if (AutoWalls.playing.contains(p)) {
                    //Only affect players
                    if(AutoWalls.redTeam.contains(p)) {
                        if (e.getTo().getX() > AutoWalls.redQuadrant[0] || e.getTo().getX() < AutoWalls.redQuadrant[1] || e.getTo().getZ() > AutoWalls.redQuadrant[2] || e.getTo().getZ() < AutoWalls.redQuadrant[3]) {
                            p.sendMessage(ChatColor.RED + "You cannot leave your quadrant now!");
                            p.teleport(new Location(e.getFrom().getWorld(),e.getFrom().getX(),e.getFrom().getY(),e.getFrom().getZ(),e.getFrom().getYaw(),e.getFrom().getPitch()));
                        }

                    }
                    else if (AutoWalls.blueTeam.contains(p)) {
                        if (e.getTo().getX() > AutoWalls.blueQuadrant[0] || e.getTo().getX() <  AutoWalls.blueQuadrant[1] || e.getTo().getZ() > AutoWalls.blueQuadrant[2] || e.getTo().getZ() < AutoWalls.blueQuadrant[3]) {
                            p.sendMessage(ChatColor.RED + "You cannot leave your quadrant now!");
                            p.teleport(new Location(e.getFrom().getWorld(),e.getFrom().getX(),e.getFrom().getY(),e.getFrom().getZ(),e.getFrom().getYaw(),e.getFrom().getPitch()));
                        }

                    }
                    else if (AutoWalls.greenTeam.contains(p)) {
                        if (e.getTo().getX() > AutoWalls.greenQuadrant[0] || e.getTo().getX() < AutoWalls.greenQuadrant[1] || e.getTo().getZ() > AutoWalls.greenQuadrant[2] || e.getTo().getZ() < AutoWalls.greenQuadrant[3]) {
                            p.sendMessage(ChatColor.RED + "You cannot leave your quadrant now!");
                            p.teleport(new Location(e.getFrom().getWorld(),e.getFrom().getX(),e.getFrom().getY(),e.getFrom().getZ(),e.getFrom().getYaw(),e.getFrom().getPitch()));
                        }

                    }
                    else if (AutoWalls.orangeTeam.contains(p)) {

                        if (e.getTo().getX() > AutoWalls.orangeQuadrant[0] || e.getTo().getX() < AutoWalls.orangeQuadrant[1] || e.getTo().getZ() > AutoWalls.orangeQuadrant[2] || e.getTo().getZ() < AutoWalls.orangeQuadrant[3]) {
                            p.sendMessage(ChatColor.RED + "You cannot leave your quadrant now!");
                            p.teleport(new Location(e.getFrom().getWorld(),e.getFrom().getX(),e.getFrom().getY(),e.getFrom().getZ(),e.getFrom().getYaw(),e.getFrom().getPitch()));
                        }

                    }
                }
            }
        }
    }
}
