package com.jkush321.autowalls.listeners;

import com.jkush321.autowalls.Arena;
import com.jkush321.autowalls.AutoWalls;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;

public class WallDropper {

    private AutoWalls plugin;

    public WallDropper(AutoWalls plugin) {
        this.plugin = plugin;
    }

    Boolean dropped = false;

    public void dropWalls(int arenaid) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                final Arena arena = Arena.getInstance();
                new Location(Bukkit.getWorlds().get(0), arena.redstoneCircuitActivator[0], arena.redstoneCircuitActivator[1], arena.redstoneCircuitActivator[2]).getBlock().setType(Material.REDSTONE_BLOCK);
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                        new Location(Bukkit.getWorlds().get(0), arena.redstoneCircuitActivator[0], arena.redstoneCircuitActivator[1], arena.redstoneCircuitActivator[2]).getBlock().setType(Material.AIR);
                        Bukkit.broadcastMessage(ChatColor.DARK_RED + "DOWN WITH THE WALLS");
                    }
                }, 20L);
            }
        });
        dropped = true;
    }

}
