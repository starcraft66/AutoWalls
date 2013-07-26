package com.jkush321.autowalls.commands;

import com.jkush321.autowalls.AutoWalls;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlyCommand implements CommandExecutor {

    private AutoWalls plugin;

    public FlyCommand(AutoWalls plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cmdSender, Command cmd, String label, String[] args) {

        if (!plugin.gameInProgress) {
            cmdSender.sendMessage(ChatColor.RED + "The game did not start yet, there is no reason to fly.");
            return false;
        }
        if (plugin.playing.contains((Player) cmdSender)) {
            cmdSender.sendMessage(ChatColor.RED + "You are in game!");
            return false;
        }
        ((Player) cmdSender).setAllowFlight(true);
        cmdSender.sendMessage(ChatColor.YELLOW + "You are now able to fly!");
        return true;
    }
}