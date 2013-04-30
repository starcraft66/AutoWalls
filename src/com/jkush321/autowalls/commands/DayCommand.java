package com.jkush321.autowalls.commands;

import com.jkush321.autowalls.AutoWalls;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class DayCommand implements CommandExecutor {

    private AutoWalls plugin;

    public DayCommand(AutoWalls plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cmdSender, Command cmd, String label, String[] args) {

        if (cmdSender.hasPermission("walls.op") || cmdSender.hasPermission("walls.mod") || cmdSender.isOp()) {
            Bukkit.getWorlds().get(0).setTime(1000);
            return true;
        }
        cmdSender.sendMessage(ChatColor.RED + "No permission!");
        return false;
    }
}