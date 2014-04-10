package com.jkush321.autowalls.commands;

import com.jkush321.autowalls.AutoWalls;
import com.jkush321.autowalls.Tabs;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaveCommand implements CommandExecutor {

    private AutoWalls plugin;

    public LeaveCommand(AutoWalls plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cmdSender, Command cmd, String label, String[] args) {

        if (cmdSender instanceof Player)
        {
            if (plugin.playing.contains((Player) cmdSender)){
                Bukkit.broadcastMessage(ChatColor.YELLOW + cmdSender.getName() + ChatColor.DARK_RED + " has left the game!");
                plugin.resetPlayer((Player) cmdSender);
                plugin.leaveTeam((Player) cmdSender);
                Tabs.updateAll();
            }
            else cmdSender.sendMessage(ChatColor.DARK_RED + "You aren't on a team");
        }
        return false;
    }
}