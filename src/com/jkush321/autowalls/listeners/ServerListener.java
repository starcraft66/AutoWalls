package com.jkush321.autowalls.listeners;

import com.jkush321.autowalls.AutoWalls;
import com.jkush321.autowalls.WallDropper;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class ServerListener implements Listener {

    private AutoWalls plugin;

    public ServerListener(AutoWalls plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPing(ServerListPingEvent e)
    {
        String message = "AutoWalls Server";
        if (!AutoWalls.gameInProgress && !AutoWalls.gameOver)
        {
            message=(ChatColor.DARK_GREEN + "Getting ready to start!");
        }
        else if (AutoWalls.gameInProgress && WallDropper.time > 0)
        {
            int mins = WallDropper.time / 60;
            int secs = WallDropper.time % 60;
            message=(ChatColor.DARK_GREEN + "Walls drop in "+ ChatColor.YELLOW + mins + ChatColor.DARK_RED + " mins, " + ChatColor.YELLOW + secs + ChatColor.DARK_RED + " secs!");
        }
        else if (AutoWalls.gameInProgress)
        {
            message=(ChatColor.YELLOW + "" + AutoWalls.playing.size() + ChatColor.DARK_RED + " players alive!");
        }
        else if (AutoWalls.gameOver && !AutoWalls.voting)
        {
            message=ChatColor.DARK_GREEN + "Game has ended!";
        }
        else {
            message=ChatColor.DARK_AQUA + "Voting for the next map!";
        }
        e.setMotd(message);
    }

}
