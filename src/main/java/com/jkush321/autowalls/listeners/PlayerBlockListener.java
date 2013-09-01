package com.jkush321.autowalls.listeners;

import com.jkush321.autowalls.AutoWalls;
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

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e)
    {
        Player p = e.getPlayer();
        if (e.getPlayer().hasPermission("walls.op")) return;
        if (!AutoWalls.playing.contains(e.getPlayer())) e.setCancelled(true);
        if (!AutoWalls.gameInProgress) e.setCancelled(true);
        if (AutoWalls.mapNumber==1)
        {
            if (e.getBlock().getX()==347) e.setCancelled(true);
            if (e.getBlock().getZ()==-793) e.setCancelled(true);
            if (e.getBlock().getX()>408) e.setCancelled(true);
            if (e.getBlock().getZ()<-853) e.setCancelled(true);
            if (e.getBlock().getX()<286) e.setCancelled(true);
            if (e.getBlock().getZ()>-731) e.setCancelled(true);
            if (e.getBlock().getY() > 137) {e.setCancelled(true); e.getPlayer().sendMessage(ChatColor.RED + "You can't build over the height limit. This prevents getting over walls."); }
        }
        else if (AutoWalls.mapNumber ==2)
        {
            if (e.getBlock().getZ()==-182) e.setCancelled(true);
            if (e.getBlock().getZ()==-164) e.setCancelled(true);
            if (e.getBlock().getX()==-785) e.setCancelled(true);
            if (e.getBlock().getX()==-803) e.setCancelled(true);
            if (e.getBlock().getZ()>-103) e.setCancelled(true);
            if (e.getBlock().getX()<-863) e.setCancelled(true);
            if (e.getBlock().getX()>-725) e.setCancelled(true);
            if (e.getBlock().getZ()<-243) e.setCancelled(true);
            if (e.getBlock().getY() > 94) {e.setCancelled(true); e.getPlayer().sendMessage(ChatColor.RED + "You can't build over the height limit. This prevents getting over walls."); }
        }
        if (e.getBlock() instanceof Sign)
        {
            if (AutoWalls.graves.contains((Sign) e.getBlock()))
            {
                e.setCancelled(true);
                e.getPlayer().sendMessage(ChatColor.AQUA + "You can not touch this grave!");
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e)
    {
        if (e.getPlayer().hasPermission("walls.op")) return;
        if (!AutoWalls.playing.contains(e.getPlayer())) e.setCancelled(true);
        if (!AutoWalls.gameInProgress) e.setCancelled(true);
        if (AutoWalls.mapNumber==1)
        {
            if (e.getBlock().getX()==347) e.setCancelled(true);
            if (e.getBlock().getZ()==-793) e.setCancelled(true);
            if (e.getBlock().getX()>408) e.setCancelled(true);
            if (e.getBlock().getZ()<-853) e.setCancelled(true);
            if (e.getBlock().getX()<286) e.setCancelled(true);
            if (e.getBlock().getZ()>-731) e.setCancelled(true);
            if (e.getBlock().getY() > 137) {e.setCancelled(true); e.getPlayer().sendMessage(ChatColor.RED + "You can't build over the height limit. This prevents getting over walls."); }
        }
        else if (AutoWalls.mapNumber ==2)
        {
            if (e.getBlock().getZ()==-182) e.setCancelled(true);
            if (e.getBlock().getZ()==-164) e.setCancelled(true);
            if (e.getBlock().getX()==-785) e.setCancelled(true);
            if (e.getBlock().getX()==-803) e.setCancelled(true);
            if (e.getBlock().getZ()>-103) e.setCancelled(true);
            if (e.getBlock().getX()<-863) e.setCancelled(true);
            if (e.getBlock().getX()>-725) e.setCancelled(true);
            if (e.getBlock().getZ()<-243) e.setCancelled(true);
            if (e.getBlock().getY() > 94) {e.setCancelled(true); e.getPlayer().sendMessage(ChatColor.RED + "You can't build over the height limit. This prevents getting over walls."); }
        }
    }

}
