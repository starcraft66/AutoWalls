package com.jkush321.autowalls.commands;

import com.jkush321.autowalls.AutoWalls;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayingCommand implements CommandExecutor {

    private AutoWalls plugin;

    public PlayingCommand(AutoWalls plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cmdSender, Command cmd, String label, String[] args) {

        Player p = (Player) cmdSender;
        cmdSender.sendMessage(ChatColor.GRAY + "There are " + plugin.playing.size() + " people playing");
        String s = (ChatColor.GRAY + "Red: " + ChatColor.WHITE);
        for (Player pl : plugin.redTeam)
        {
            s+=pl.getName() + ", ";
        }
        cmdSender.sendMessage(s.substring(0,s.length()-2));
        s=(ChatColor.GRAY + "Blue: " + ChatColor.WHITE);
        for (Player pl : plugin.blueTeam)
        {
            s+=pl.getName() + ", ";
        }
        cmdSender.sendMessage(s.substring(0,s.length()-2));
        s=(ChatColor.GRAY + "Green: " + ChatColor.WHITE);
        for (Player pl : plugin.greenTeam)
        {
            s+=pl.getName() + ", ";
        }
        cmdSender.sendMessage(s.substring(0,s.length()-2));
        s=(ChatColor.GRAY + "Orange: " + ChatColor.WHITE);
        for (Player pl : plugin.orangeTeam)
        {
            s+=pl.getName() + ", ";
        }
        cmdSender.sendMessage(s.substring(0,s.length()-2));
        return true;
    }
}