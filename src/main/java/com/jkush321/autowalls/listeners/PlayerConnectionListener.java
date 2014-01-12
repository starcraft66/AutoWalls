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

public class PlayerConnectionListener implements Listener {

    private AutoWalls plugin;

    public PlayerConnectionListener(AutoWalls plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e)
    {
        e.setJoinMessage(ChatColor.AQUA + "+ " + ChatColor.DARK_AQUA + e.getPlayer().getName() + ChatColor.GRAY + " is now online");
        if (AutoWalls.gameInProgress) {
            plugin.spectate(e.getPlayer());
            for (Player p : AutoWalls.playing)
            {
                p.hidePlayer(e.getPlayer());
            }
        }
        e.getPlayer().setGameMode(GameMode.ADVENTURE);
        if (e.getPlayer().hasPermission("walls.op"))
        {
            UpdateChecker.checkAndSendMessage(e.getPlayer());
        }
        Tabs.addPlayer(e.getPlayer());
        Arena arena = Arena.getInstance();
        e.getPlayer().teleport(new Location(Bukkit.getWorlds().get(0),arena.lobbySpawn[0],arena.lobbySpawn[1],arena.lobbySpawn[2]));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLogin(PlayerLoginEvent e)
    {
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

        e.getPlayer().getInventory().clear();
        e.getPlayer().getInventory().setArmorContents(new ItemStack[]{new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.AIR)});

    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e)
    {
        if (AutoWalls.playing.contains(e.getPlayer()) && AutoWalls.gameInProgress) e.getPlayer().setHealth(0);
        else if (AutoWalls.playing.contains(e.getPlayer()) && !AutoWalls.gameInProgress) plugin.leaveTeam(e.getPlayer());
        if (AutoWalls.getLastEvent(e.getPlayer()) != 0) AutoWalls.lastEvent.remove(e.getPlayer());
        plugin.checkStats();
        Tags.refreshPlayer(e.getPlayer());
        Tabs.removePlayer(e.getPlayer());
        e.setQuitMessage(ChatColor.AQUA + "- " + ChatColor.DARK_AQUA + e.getPlayer().getName() + ChatColor.GRAY + " has left");
    }
    
}
