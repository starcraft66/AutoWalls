package com.jkush321.autowalls.commands;

import com.jkush321.autowalls.AutoWalls;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeamCommand implements CommandExecutor {

    private AutoWalls plugin;

    public TeamCommand(AutoWalls plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cmdSender, Command cmd, String label, String[] args) {

        if (cmdSender instanceof Player) {
            Player p = (Player) cmdSender;
            if (!plugin.playing.contains(p)) {
                p.sendMessage(ChatColor.YELLOW + "You are not in game and have no team!");
                return true;
            } else {
                if (plugin.redTeam.contains(p)) {
                    p.sendMessage(ChatColor.YELLOW + "You are on the red team with...");
                    for (Player pl : plugin.redTeam) {
                        if (pl != p) p.sendMessage(ChatColor.YELLOW + "-" + pl.getName());
                    }
                    if (plugin.redTeam.size() == 1) p.sendMessage(ChatColor.YELLOW + "No one else :[");
                } else if (plugin.blueTeam.contains(p)) {
                    p.sendMessage(ChatColor.YELLOW + "You are on the blue team with...");
                    for (Player pl : plugin.blueTeam) {
                        if (pl != p) p.sendMessage(ChatColor.YELLOW + "-" + pl.getName());
                    }
                    if (plugin.blueTeam.size() == 1) p.sendMessage(ChatColor.YELLOW + "No one else :[");
                } else if (plugin.greenTeam.contains(p)) {
                    p.sendMessage(ChatColor.YELLOW + "You are on the green team with...");
                    for (Player pl : plugin.greenTeam) {
                        if (pl != p) p.sendMessage(ChatColor.YELLOW + "-" + pl.getName());
                    }
                    if (plugin.greenTeam.size() == 1) p.sendMessage(ChatColor.YELLOW + "No one else :[");
                } else if (plugin.orangeTeam.contains(p)) {
                    p.sendMessage(ChatColor.YELLOW + "You are on the orange team with...");
                    for (Player pl : plugin.orangeTeam) {
                        if (pl != p) p.sendMessage(ChatColor.YELLOW + "-" + pl.getName());
                    }
                    if (plugin.orangeTeam.size() == 1) p.sendMessage(ChatColor.YELLOW + "No one else :[");
                }
                return true;
            }
        }
        cmdSender.sendMessage(ChatColor.RED + "The console cannot join a team, silly!");
        return false;
    }
}