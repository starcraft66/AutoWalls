package com.jkush321.autowalls.commands;

import com.jkush321.autowalls.AutoWalls;
import com.jkush321.autowalls.Timer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ForceDropCommand implements CommandExecutor {

    private AutoWalls plugin;

    public ForceDropCommand(AutoWalls plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cmdSender, Command cmd, String label, String[] args) {

        if (cmdSender.hasPermission("walls.op") || cmdSender.isOp()) {
            if (Timer.time <= 5) {
                cmdSender.sendMessage(ChatColor.AQUA + "The walls have already dropped!");
            } else {
                Timer.time = 5;
            }
            return true;
        }
        cmdSender.sendMessage(ChatColor.RED + "No permission!");
        return false;
    }
}