/*
 * AutoWalls by jkush321 is licensed under the
 * Creative Commons Attribution-NonCommercial 3.0 Unported License
 * 
 * You are fully allowed to modify the source code for your own network
 * of servers, but you may not distribute the modified code outside of
 * your servers.
 * 
 * AutoWalls was originally a personal project that was standalone for
 * my own private server, and it slowly accumulated into a giant plugin.
 * 
 * AutoWalls is for dedicated servers that are willing to run just Walls.
 * 
 * The license requires attribution and you have to give credit to jkush321
 * no matter how many changes were made to the code. In some clearly stated
 * way everyone who goes on the server must be able to easily see and be aware
 * of the fact that this code originated from jkush321 and was modified by
 * you or your team.
 * 
 * For more information visit http://bit.ly/AutoWalls
 * 
 */

package com.jkush321.autowalls;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Timer extends BukkitRunnable{

    public static int time;
    public static int timeDeathmatch;
    public static boolean Dropped;

    private AutoWalls plugin;

    public Timer(AutoWalls plugin) {
        this.plugin = plugin;
    }

	public void run() {

			if (AutoWalls.gameInProgress)
			{
                Countdown();
                time--;
			}

		}

    public void Countdown()
	{
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin,new Runnable() {
            @Override
            public void run() {
                if (time >= 60 && time % 60 == 0) {
                    Integer mins = time/60;
                    Bukkit.broadcastMessage(ChatColor.DARK_RED + "The walls drop in " + mins.toString() + " minutes!");
                } else if (time == 30 || time == 15 || time == 10 || time == 5 || time == 4 || time == 3 || time == 2 || time == 1) {
                    Bukkit.broadcastMessage(ChatColor.DARK_RED + "The walls drop in " + time + " seconds!");
                } else if (time == 0) {
                    dropWalls();
                    Bukkit.getScheduler().cancelTask(AutoWalls.timerTask);
                }
            }
        });
	}

    public void announceDeathmatch(final String unit) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin,new Runnable() {
            @Override
            public void run() {
                if (timeDeathmatch >= 60) {
                    Bukkit.broadcastMessage(ChatColor.DARK_RED + "Deathmatch in " + ChatColor.YELLOW + Timer.time/60 + ChatColor.DARK_RED + " " + unit);
                } else {
                    Bukkit.broadcastMessage(ChatColor.DARK_RED + "Deathmatch in " + ChatColor.YELLOW + Timer.time + ChatColor.DARK_RED + " " + unit);
                }
            }
        });
    }
	
	public void dropWalls()
	{
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                Bukkit.broadcastMessage(ChatColor.DARK_RED + "The walls are falling!");
                Arena arena = Arena.getInstance();
                final Block redstoneActivator = new Location(Bukkit.getWorlds().get(0),arena.redstoneCircuitActivator[0],arena.redstoneCircuitActivator[1],arena.redstoneCircuitActivator[2]).getBlock();
                final Material formerBlock = redstoneActivator.getType();
                redstoneActivator.setType(Material.REDSTONE_BLOCK);
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                        redstoneActivator.setType(formerBlock);
                    }
                },20L);
            }
        });
        Dropped = true;

	}

    public void deathmatch() {
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {

                Bukkit.broadcastMessage(ChatColor.GOLD + "It is time for a " + ChatColor.AQUA + "deathmatch" + ChatColor.GOLD + "!");

                if (AutoWalls.mapNumber == 1) {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.teleport(new Location(Bukkit.getWorlds().get(0),492,150,-848));
                    }

                } else if (AutoWalls.mapNumber == 2) {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.teleport(new Location(Bukkit.getWorlds().get(0),-850,114,-308));
                    }
                } else {
                    Bukkit.getLogger().severe("Invaid map number!");
                }
            }
        });
    }

}
