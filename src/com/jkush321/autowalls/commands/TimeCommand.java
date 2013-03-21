package com.jkush321.autowalls.commands;

import com.jkush321.autowalls.AutoWalls;
import com.jkush321.autowalls.WallDropper;
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
        minutes = WallDropper.time / 60;
        seconds = WallDropper.time % 60;

        if (minutes==0 && seconds==0) {cmdSender.sendMessage(ChatColor.GRAY + "The Walls Already Dropped!"); return true;}
        cmdSender.sendMessage(ChatColor.GRAY + "The walls will drop in " + minutes + " minutes and " + seconds + " seconds!");
        return true;
    }
}