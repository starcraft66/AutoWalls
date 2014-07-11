package com.jkush321.autowalls.listeners;

import com.jkush321.autowalls.*;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Random;

public class PlayerConnectionListener implements Listener {

    private AutoWalls plugin;
    private int joinTeamIndex;

    public PlayerConnectionListener(AutoWalls plugin) {
        this.plugin = plugin;
        this.joinTeamIndex = 0;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e)
    {
        //e.setJoinMessage(ChatColor.AQUA + "+ " + ChatColor.DARK_AQUA + e.getPlayer().getName() + ChatColor.GRAY + " is now online");
        if (AutoWalls.gameInProgress) {
            plugin.spectate(e.getPlayer());
            for (Player p : AutoWalls.playing)
            {
                p.hidePlayer(e.getPlayer());
            }
        }
        e.getPlayer().setGameMode(GameMode.ADVENTURE);

        Arena arena = Arena.getInstance();
        e.getPlayer().teleport(new Location(Bukkit.getWorlds().get(0),arena.lobbySpawn[0],arena.lobbySpawn[1],arena.lobbySpawn[2]));
        e.getPlayer().getInventory().clear();
        e.getPlayer().getInventory().setArmorContents(null);
        e.getPlayer().setHealth(20);
        e.getPlayer().setFoodLevel(20);
        e.getPlayer().setFlying(false);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLogin(PlayerLoginEvent e)
    {
        final ConfigurationHelper ch = ConfigurationHelper.getInstance();
        if (AutoWalls.config.isSet("votes.players." + e.getPlayer().getName()) && AutoWalls.config.getInt("votes.players." + e.getPlayer().getName()) >= 20) { e.getPlayer().setDisplayName(ChatColor.DARK_AQUA + e.getPlayer().getName() + ChatColor.WHITE); }
        if (AutoWalls.config.isSet("votes.players." + e.getPlayer().getName()) && AutoWalls.config.getInt("votes.players." + e.getPlayer().getName()) >= 250) { e.getPlayer().setDisplayName(ChatColor.DARK_RED + e.getPlayer().getName() + ChatColor.WHITE); }

        if (AutoWalls.config.getBoolean("priorities") == true)
        {
            if (AutoWalls.config.isSet("votes.players." + e.getPlayer().getName())) { e.getPlayer().setDisplayName(ChatColor.YELLOW + "[" + AutoWalls.config.getInt("votes.players." + e.getPlayer().getName()) + "]" + ChatColor.GRAY + e.getPlayer().getDisplayName() + ChatColor.WHITE); }
            else e.getPlayer().setDisplayName(ChatColor.YELLOW + "[0]" + ChatColor.GRAY + e.getPlayer().getDisplayName() + ChatColor.WHITE);
        }
        if (e.getPlayer().hasPermission("walls.op")) e.getPlayer().setDisplayName(ChatColor.DARK_BLUE + "[" + ChatColor.DARK_GREEN + "Admin" + ChatColor.DARK_BLUE + "]" + ChatColor.DARK_RED + e.getPlayer().getName() + ChatColor.GRAY + ChatColor.WHITE);
        if (AutoWalls.config.isSet("prefix." + e.getPlayer().getName())) e.getPlayer().setDisplayName(        ChatColor.translateAlternateColorCodes('&', AutoWalls.config.getString("prefix." + e.getPlayer().getName())).replace("{pri}", AutoWalls.config.getInt("votes.players." + e.getPlayer().getName())+"") + e.getPlayer().getName() + ChatColor.WHITE);

        if (Bukkit.getOnlinePlayers().length == Bukkit.getMaxPlayers())
        {
            if (AutoWalls.config.isSet("votes.players." + e.getPlayer().getName()) && (AutoWalls.config.getBoolean("priorities") || AutoWalls.config.getInt("votes.players." + e.getPlayer().getName()) > 5))
            {
                int pl = AutoWalls.config.getInt("votes.players." + e.getPlayer().getName());
                int l = 999999;
                Player low = null;
                for (int i = Bukkit.getOnlinePlayers().length -  1; i > -1; i--)
                {
                    Player p = Bukkit.getOnlinePlayers()[i];
                    if (!AutoWalls.playing.contains(p))
                    {
                        if (!AutoWalls.config.isSet("votes.players." + p.getName()))
                        {
                            p.kickPlayer(AutoWalls.priorityKickMessage);
                            if (!e.getPlayer().isBanned())
                                if ((Bukkit.hasWhitelist() && e.getPlayer().isWhitelisted()) || !Bukkit.hasWhitelist())
                                    e.allow();
                            return;
                        }
                        if (AutoWalls.config.getInt("votes.players." + p.getName()) < l)
                        {
                            low = p;
                            l = AutoWalls.config.getInt("votes.players." + p.getName());
                        }
                    }
                }
                if (pl > l) { low.kickPlayer("Someone with higher priority joined!"); /*e.allow();*/ return; }

            }
            e.disallow(PlayerLoginEvent.Result.KICK_FULL, AutoWalls.fullKickMessage);
        }

        final Player p = e.getPlayer();

        e.getPlayer().getInventory().clear();
        e.getPlayer().getInventory().setArmorContents(new ItemStack[]{new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.AIR)});
        BukkitTask joinTeam = new BukkitRunnable() {
            @Override
            public void run() {
                for (int i = 0; i < plugin.arena.teamSize; i++) {
                    if (!AutoWalls.playing.contains(p)) {
                        if (plugin.redTeam.size() == i) {
                            plugin.joinTeam(p, "red");
                        } else if (plugin.blueTeam.size() == i) {
                            plugin.joinTeam(p, "blue");
                        } else if (plugin.greenTeam.size() == i) {
                            plugin.joinTeam(p, "green");
                        } else if (plugin.orangeTeam.size() == i) {
                            plugin.joinTeam(p, "orange");
                        }
                    }
                }

                //SHITTY!

                /* else {
                switch(randomInt) {
                    case 0:
                        if (plugin.redTeam.size()<plugin.arena.teamSize)
                            plugin.joinTeam(p, "red");
                        else if (plugin.blueTeam.size()<plugin.arena.teamSize)
                            plugin.joinTeam(p, "blue");
                        else if (plugin.greenTeam.size()<plugin.arena.teamSize)
                            plugin.joinTeam(p, "green");
                        else if (plugin.orangeTeam.size()<plugin.arena.teamSize)
                            plugin.joinTeam(p, "orange");
                        else p.sendMessage(ChatColor.RED + "Every team is full!");
                        break;
                    case 1:
                        if (plugin.blueTeam.size()<plugin.arena.teamSize)
                            plugin.joinTeam(p, "blue");
                        else if (plugin.greenTeam.size()<plugin.arena.teamSize)
                            plugin.joinTeam(p, "green");
                        else if (plugin.orangeTeam.size()<plugin.arena.teamSize)
                            plugin.joinTeam(p, "orange");
                        else if (plugin.redTeam.size()<plugin.arena.teamSize)
                            plugin.joinTeam(p, "red");
                        else p.sendMessage(ChatColor.RED + "Every team is full!");
                        break;
                    case 2:
                        if (plugin.greenTeam.size()<plugin.arena.teamSize)
                            plugin.joinTeam(p, "green");
                        else if (plugin.orangeTeam.size()<plugin.arena.teamSize)
                            plugin.joinTeam(p, "orange");
                        else if (plugin.redTeam.size()<plugin.arena.teamSize)
                            plugin.joinTeam(p, "red");
                        else if (plugin.blueTeam.size()<plugin.arena.teamSize)
                            plugin.joinTeam(p, "blue");
                        else p.sendMessage(ChatColor.RED + "Every team is full!");
                        break;
                    case 3:
                        if (plugin.orangeTeam.size()<plugin.arena.teamSize)
                            plugin.joinTeam(p, "orange");
                        else if (plugin.redTeam.size()<plugin.arena.teamSize)
                            plugin.joinTeam(p, "red");
                        else if (plugin.blueTeam.size()<plugin.arena.teamSize)
                            plugin.joinTeam(p, "blue");
                        else if (plugin.greenTeam.size()<plugin.arena.teamSize)
                            plugin.joinTeam(p, "green");
                        else p.sendMessage(ChatColor.RED + "Every team is full!");
                        break;
                }
                }*/

                p.sendMessage(ChatColor.YELLOW + "The current map is " + ch.getArenaName(plugin.getConfig().getInt("next-map")) + " by " + ch.getArenaAuthor(plugin.getConfig().getInt("next-map")) + ".");
            }
        }.runTaskLater(plugin, 10L);

    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e)
    {
        plugin.resetPlayer(e.getPlayer(), false);
        if (AutoWalls.playing.contains(e.getPlayer()) && !AutoWalls.gameInProgress) plugin.leaveTeam(e.getPlayer());
        if (AutoWalls.getLastEvent(e.getPlayer()) != 0) AutoWalls.lastEvent.remove(e.getPlayer());
        plugin.checkStats();
        Tags.refreshPlayer(e.getPlayer());
        e.setQuitMessage(ChatColor.AQUA + "- " + ChatColor.DARK_AQUA + e.getPlayer().getName() + ChatColor.GRAY + " has left");
    }
    
}
