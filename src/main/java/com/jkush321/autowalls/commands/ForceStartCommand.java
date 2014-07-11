package com.jkush321.autowalls.commands;

import com.jkush321.autowalls.AutoWalls;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ForceStartCommand implements CommandExecutor {

    private AutoWalls plugin;

    public ForceStartCommand(AutoWalls plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cmdSender, Command cmd, String label, String[] args) {

        if (cmdSender.hasPermission("walls.forcestart") || cmdSender.hasPermission("walls.op") || cmdSender.isOp()) {
            if (plugin.playing.size() >= 2 && !plugin.gameInProgress) {
                Bukkit.broadcastMessage(ChatColor.DARK_RED + "FORCE STARTING GAME");
                plugin.startGame();
            }
            else
            {
                cmdSender.sendMessage(ChatColor.GRAY + "There have to be at least 2 players, and the game can not be started yet, and the join timer must be over!");
            }
            return true;

        }
        cmdSender.sendMessage(ChatColor.RED + "No permission!");
        return false;
    }

}
