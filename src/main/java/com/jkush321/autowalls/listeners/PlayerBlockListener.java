package com.jkush321.autowalls.listeners;

import com.jkush321.autowalls.Arena;
import com.jkush321.autowalls.AutoWalls;
import com.jkush321.autowalls.Timer;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class PlayerBlockListener implements Listener {

    private AutoWalls plugin;

    public PlayerBlockListener(AutoWalls plugin) {
        this.plugin = plugin;
    }

    Arena arena = Arena.getInstance();

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        if (!e.getPlayer().hasPermission("walls.op")) {
            if (AutoWalls.playing.contains(e.getPlayer())) {
                if (AutoWalls.gameInProgress) {
                    if (Timer.Dropped) {
                        if ((e.getBlock().getX() < arena.mapLimits[0])
                                || (e.getBlock().getZ() < arena.mapLimits[1])
                                || (e.getBlock().getX() > arena.mapLimits[2])
                                || (e.getBlock().getZ() > arena.mapLimits[3])
                                || (e.getBlock().getY() < arena.height[0])
                                || (e.getBlock().getY() > arena.height[1])) {
                            e.setCancelled(true);
                        }
                    } else {
                        if (AutoWalls.redTeam.contains(p)) {
                            if (e.getBlock().getX() > arena.redQuadrant[0]
                                    || e.getBlock().getZ() > arena.redQuadrant[1]
                                    || e.getBlock().getX() < arena.redQuadrant[2]
                                    || e.getBlock().getZ() < arena.redQuadrant[3]) {
                                e.setCancelled(true);
                            }
                        } else if (AutoWalls.blueTeam.contains(p)) {
                            if (e.getBlock().getX() > AutoWalls.arena.blueQuadrant[0]
                                    || e.getBlock().getZ() > arena.blueQuadrant[1]
                                    || e.getBlock().getX() < AutoWalls.arena.blueQuadrant[2]
                                    || e.getBlock().getZ() < AutoWalls.arena.blueQuadrant[3]) {
                                e.setCancelled(true);
                            }
                        } else if (AutoWalls.greenTeam.contains(p)) {
                            if (e.getBlock().getX() > AutoWalls.arena.greenQuadrant[0]
                                    || e.getBlock().getZ() > arena.greenQuadrant[1]
                                    || e.getBlock().getX() < arena.greenQuadrant[2]
                                    || e.getBlock().getZ() < arena.greenQuadrant[3]) {
                                e.setCancelled(true);
                            }
                        } else if (AutoWalls.orangeTeam.contains(p)) {
                            if (e.getBlock().getX() > AutoWalls.arena.orangeQuadrant[0]
                                    || e.getBlock().getZ() > arena.orangeQuadrant[1]
                                    || e.getBlock().getX() < arena.orangeQuadrant[2]
                                    || e.getBlock().getZ() < arena.orangeQuadrant[3]) {
                                e.setCancelled(true);
                            }
                        }
                    }
                }
            }
        }

        if (e.getBlock() instanceof Sign) {
            if (AutoWalls.graves.contains((Sign) e.getBlock())) {
                e.setCancelled(true);
                e.getPlayer().sendMessage(ChatColor.AQUA + "You can not touch this grave!");
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        if (!e.getPlayer().hasPermission("walls.op")) {
            if (AutoWalls.playing.contains(e.getPlayer())) {
                if (AutoWalls.gameInProgress) {
                    if (Timer.Dropped) {
                        if ((e.getBlock().getX() < arena.mapLimits[0])
                                || (e.getBlock().getZ() < arena.mapLimits[1])
                                || (e.getBlock().getX() > arena.mapLimits[2])
                                || (e.getBlock().getZ() > arena.mapLimits[3])
                                || (e.getBlock().getY() < arena.height[0])
                                || (e.getBlock().getY() > arena.height[1])) {
                            e.setCancelled(true);
                        }
                    } else {
                        if (AutoWalls.redTeam.contains(p)) {
                            if (e.getBlock().getX() > arena.redQuadrant[0]
                                    || e.getBlock().getZ() > arena.redQuadrant[1]
                                    || e.getBlock().getX() < arena.redQuadrant[2]
                                    || e.getBlock().getZ() < arena.redQuadrant[3]) {
                                e.setCancelled(true);
                            }
                        } else if (AutoWalls.blueTeam.contains(p)) {
                            if (e.getBlock().getX() > AutoWalls.arena.blueQuadrant[0]
                                    || e.getBlock().getZ() > arena.blueQuadrant[1]
                                    || e.getBlock().getX() < AutoWalls.arena.blueQuadrant[2]
                                    || e.getBlock().getZ() < AutoWalls.arena.blueQuadrant[3]) {
                                e.setCancelled(true);
                            }
                        } else if (AutoWalls.greenTeam.contains(p)) {
                            if (e.getBlock().getX() > AutoWalls.arena.greenQuadrant[0]
                                    || e.getBlock().getZ() > arena.greenQuadrant[1]
                                    || e.getBlock().getX() < arena.greenQuadrant[2]
                                    || e.getBlock().getZ() < arena.greenQuadrant[3]) {
                                e.setCancelled(true);
                            }
                        } else if (AutoWalls.orangeTeam.contains(p)) {
                            if (e.getBlock().getX() > AutoWalls.arena.orangeQuadrant[0]
                                    || e.getBlock().getZ() > arena.orangeQuadrant[1]
                                    || e.getBlock().getX() < arena.orangeQuadrant[2]
                                    || e.getBlock().getZ() < arena.orangeQuadrant[3]) {
                                e.setCancelled(true);
                            }
                        }
                    }
                }
            }
        }
    }

}
