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
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class Timer extends BukkitRunnable{

    public static int time;
    public static int timeDeathmatch;
    public static boolean Dropping;
    public static boolean Dropped;

    private AutoWalls plugin;

    public Timer(AutoWalls plugin) {
        this.plugin = plugin;
    }

	public void run() {

			if (AutoWalls.gameInProgress)
			{
                time--;
                timeDeathmatch--;
				if (time==50*60)
				{
					announceCountdown("There are", "minutes remaining!");
				}
				if (time==40*60)
				{
					announceCountdown("There are", "minutes remaining!");
				}
				if (time==30*60)
				{
					announceCountdown("There are", "minutes remaining!");
				}
				if (time==20*60)
				{
					announceCountdown("There are", "minutes remaining!");
				}
				if (time==10*60)
				{
					announceCountdown("There are", "minutes remaining!");
				}
				else if (time==5*60)
				{
					announceCountdown("There are", "minutes remaining, all players have recieved 2 beef!");
					ItemStack beef = new ItemStack(Material.COOKED_BEEF, 2);
					for (Player p : AutoWalls.playing)
					{
						p.getInventory().addItem(beef);
					}
				}
				else if (time==3*60)
				{
					announceCountdown("There are", "minutes remaining!");
				}
				else if (time==2*60)
				{
					announceCountdown("There are", "minutes remaining!");
				}
				else if (time==60)
				{
					announceCountdown("There is", "minute remaining!");
				}
				else if (time==30)
				{
					announceCountdown("There are", "seconds remaining!");
				}
				else if (time==10)
				{
					announceCountdown("There are", "seconds remaining!");
				}
				else if (time==9)
				{
					announceCountdown("There are", "seconds remaining!");
				}
				else if (time==8)
				{
					announceCountdown("There are", "seconds remaining!");
				}
				else if (time==7)
				{
					announceCountdown("There are", "seconds remaining!");
				}
				else if (time==6)
				{
					announceCountdown("There are", "seconds remaining!");
				}
				else if (time==5)
				{
					announceCountdown("There are", "seconds remaining!");
				}
				else if (time==4)
				{
					announceCountdown("There are", "seconds remaining!");
				}
				else if (time==3)
				{
					announceCountdown("There are", "seconds remaining!");
				}
				else if (time==2)
				{
					announceCountdown("There are", "seconds remaining!");
				}
				else if (time==1)
				{
					announceCountdown("There is", "second remaining!");
				}
				else if (time==0)
				{
                    dropWalls();
                    timeDeathmatch = AutoWalls.config.getInt("time-until-deathmatch-in-minutes") * 60;
				}
                else if (time < 1 && time > -30) {
                    Dropping = true;
                }
                else if (time < -30){
                    Dropping = false;
                    if (!AutoWalls.deathmatches) {
                        Bukkit.getScheduler().cancelTask(AutoWalls.timerTask);
                    }
                }
                else if (timeDeathmatch == 5*60) {
                    announceDeathmatch("minutes.");
                }
                else if (timeDeathmatch == 1*60) {
                    announceDeathmatch("minute.");
                }
                else if (timeDeathmatch == 15) {
                    announceDeathmatch("seconds.");
                }
                else if (timeDeathmatch == 10) {
                    announceDeathmatch("seconds.");
                }
                else if (timeDeathmatch == 5) {
                    announceDeathmatch("seconds.");
                }
                else if (timeDeathmatch == 4) {
                    announceDeathmatch("seconds.");
                }
                else if (timeDeathmatch == 3) {
                    announceDeathmatch("seconds.");
                }
                else if (timeDeathmatch == 2) {
                    announceDeathmatch("seconds.");
                }
                else if (timeDeathmatch == 1) {
                    announceDeathmatch("second.");
                }
                else if (timeDeathmatch == 0) {
                    deathmatch();
                    Bukkit.getScheduler().cancelTask(AutoWalls.timerTask);
                }

			}

		}

    public void announceCountdown(final String prefix, final  String suffix)
	{
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin,new Runnable() {
            @Override
            public void run() {
                if (time >= 60) {
                    Bukkit.broadcastMessage(ChatColor.DARK_RED + prefix + " " + ChatColor.YELLOW + Timer.time/60 + ChatColor.DARK_RED + " " + suffix);
                } else {
                Bukkit.broadcastMessage(ChatColor.DARK_RED + prefix + " " + ChatColor.YELLOW + Timer.time + ChatColor.DARK_RED + " " + suffix);
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
                if (AutoWalls.mapNumber==1)
                {
                    new Location(AutoWalls.playing.get(0).getWorld(), 409, 108, -787).getBlock().setType(Material.BEDROCK);
                    new Location(AutoWalls.playing.get(0).getWorld(), 353, 108, -855).getBlock().setType(Material.BEDROCK);
                    new Location(AutoWalls.playing.get(0).getWorld(), 285, 108, -799).getBlock().setType(Material.BEDROCK);
                    new Location(AutoWalls.playing.get(0).getWorld(), 341, 108, -731).getBlock().setType(Material.BEDROCK);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                        @Override
                        public void run() {
                            new Location(AutoWalls.playing.get(0).getWorld(), 409, 110, -787).getBlock().setType(Material.BEDROCK);
                            new Location(AutoWalls.playing.get(0).getWorld(), 353, 110, -855).getBlock().setType(Material.BEDROCK);
                            new Location(AutoWalls.playing.get(0).getWorld(), 285, 110, -799).getBlock().setType(Material.BEDROCK);
                            new Location(AutoWalls.playing.get(0).getWorld(), 341, 110, -731).getBlock().setType(Material.BEDROCK);
                            Bukkit.broadcastMessage(ChatColor.DARK_RED + "DOWN WITH THE WALLS");
                        }
                    }, 20L);
                }
                else
                {
                    new Location(AutoWalls.playing.get(0).getWorld(), -794, 20, -173).getBlock().setType(Material.REDSTONE_TORCH_ON);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                        @Override
                        public void run() {
                            new Location(AutoWalls.playing.get(0).getWorld(), -794, 20, -173).getBlock().setType(Material.AIR);

                            Bukkit.broadcastMessage(ChatColor.DARK_RED + "DOWN WITH THE WALLS");
                        }
                }, 20L);

                }
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
