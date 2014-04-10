package com.jkush321.autowalls.commands;

import com.jkush321.autowalls.AutoWalls;
import com.jkush321.autowalls.ConfigurationHelper;
import com.jkush321.autowalls.Timer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class TimeCommand implements CommandExecutor {

    private AutoWalls plugin;

    public TimeCommand(AutoWalls plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cmdSender, Command cmd, String label, String[] args) {

        int minutes = 0;
        int seconds = 0;
        if (!plugin.gameInProgress)
        {
            cmdSender.sendMessage(ChatColor.GRAY + "The game hasn't started yet!"); return true;
        }
        minutes = Timer.time / 60;
        seconds = Timer.time % 60;

        if (Timer.time < 1) {cmdSender.sendMessage(ChatColor.GRAY + "The Walls Already Dropped!"); return true;}
        cmdSender.sendMessage(ChatColor.GRAY + "The walls will drop in " + minutes + " minutes and " + seconds + " seconds!");
        return true;
    }
}