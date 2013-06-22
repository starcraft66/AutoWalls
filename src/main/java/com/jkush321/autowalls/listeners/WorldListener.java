package com.jkush321.autowalls.listeners;

import com.jkush321.autowalls.AutoWalls;
import com.jkush321.autowalls.Grenades;
import com.jkush321.autowalls.WallDropper;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.weather.LightningStrikeEvent;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WorldListener implements Listener {

    private AutoWalls plugin;

    public WorldListener(AutoWalls plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority= EventPriority.HIGHEST)
    public void onEntitySpawn(CreatureSpawnEvent e)
    {
        if (e.getEntity().getType().equals(EntityType.CREEPER) || e.getEntity().getType().equals(EntityType.ENDERMAN) || e.getEntity().getType().equals(EntityType.SLIME) || e.getEntity().getType().equals(EntityType.SKELETON) || e.getEntity().getType().equals(EntityType.SPIDER) || e.getEntity().getType().equals(EntityType.ZOMBIE)) e.setCancelled(true);
    }

    @EventHandler( priority = EventPriority.HIGHEST, ignoreCancelled = true )
    public void onWeatherChange( WeatherChangeEvent event )
    {
        if( event.toWeatherState( ) )
        {
            event.setCancelled( true );
        }
    }

    @EventHandler( priority = EventPriority.HIGHEST, ignoreCancelled = true )
    public void onThunderChange( ThunderChangeEvent event )
    {
        if( event.toThunderState( ) )
        {
            event.setCancelled( true );
        }
    }

    @EventHandler( priority = EventPriority.HIGHEST, ignoreCancelled = true )
    public void onLightningStrike( LightningStrikeEvent event )
    {
        event.setCancelled( true );
    }
    @EventHandler
    public void onProjectileLand(ProjectileHitEvent e)
    {
        if (e.getEntityType() == EntityType.ARROW && AutoWalls.arrowLightning)
        {
            if (e.getEntity().getShooter() != null)
            {
                if (e.getEntity().getShooter() instanceof Player)
                {
                    Player shooter = (Player) e.getEntity().getShooter();
                    if (WallDropper.time <= 0)
                    {
                        Random r = new Random();
                        int rand = r.nextInt(AutoWalls.arrowLightningChance);
                        if (rand==0)
                        {
                            Bukkit.broadcastMessage(ChatColor.DARK_RED + shooter.getName() + ChatColor.RED + " Has Shot A Rare Lightning Arrow!");
                            e.getEntity().getWorld().strikeLightning(e.getEntity().getLocation());
                        }
                    }
                }
            }
        }
        else if (e.getEntity().getType() == EntityType.SNOWBALL)
        {
            if (e.getEntity().hasMetadata("grenade-type"))
            {
                Grenades.handleLanding(e, e.getEntity());
            }
            //e.getEntity().getWorld().createExplosion(e.getEntity().getLocation(), .8F, true);
        }
    }

    @EventHandler
    public void onPistonRetract (BlockPistonRetractEvent e)
    {
        if (e.getRetractLocation().getBlock().getType() == Material.SAND || e.getRetractLocation().getBlock().getType() == Material.GRAVEL) e.setCancelled(true);
    }
    @EventHandler
    public void onPistonExtend (BlockPistonExtendEvent e)
    {
        for (Block b : e.getBlocks())
        {
            if (b.getType()==Material.SAND || b.getType()==Material.GRAVEL) e.setCancelled(true);
        }
    }
    @EventHandler
    public void onExplode (EntityExplodeEvent e)
    {
        List<Block> newList = new ArrayList<Block>();
        newList.addAll(e.blockList());

        for (Block b : newList)
        {
            if (b.getType() == Material.SAND || b.getType() == Material.GRAVEL) { e.blockList().remove(b); }
        }
    }

    @EventHandler
    public void onSignUpdate(SignChangeEvent e)
    {
        if (ChatColor.stripColor(e.getLine(0).trim()).equalsIgnoreCase("[Join]") && !e.getPlayer().hasPermission("walls.op"))
        {
            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatColor.RED + "No placing special signs!");
        }
        if (ChatColor.stripColor(e.getLine(0).trim()).equalsIgnoreCase("[Kit]") && !e.getPlayer().hasPermission("walls.op"))
        {
            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatColor.RED + "No placing special signs!");
        }
    }
    @EventHandler
    public void onProjLaunch(ProjectileLaunchEvent e)
    {
        if (e.getEntity().getShooter().hasMetadata("last-grenade"))
        {
            e.getEntity().setMetadata("grenade-type", new FixedMetadataValue(plugin, e.getEntity().getShooter().getMetadata("last-grenade").get(0).asString()));
        }
    }

}
