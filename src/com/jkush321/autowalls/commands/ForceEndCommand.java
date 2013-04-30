package com.jkush321.autowalls.commands;

import com.jkush321.autowalls.AutoWalls;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ForceEndCommand implements CommandExecutor {

    private AutoWalls plugin;

    public ForceEndCommand(AutoWalls plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cmdSender, Command cmd, String label, String[] args) {

        if (cmdSender.hasPermission("walls.op") || cmdSender.isOp() && plugin.gameInProgress) {
            plugin.endGame("ADMINS", "No one.");
            return true;
        }
        cmdSender.sendMessage(ChatColor.RED + "No permission!");
        return false;
    }
}