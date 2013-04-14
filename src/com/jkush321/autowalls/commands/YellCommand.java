package com.jkush321.autowalls.commands;

import com.jkush321.autowalls.AutoWalls;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class YellCommand implements CommandExecutor {

    private AutoWalls plugin;

    public YellCommand(AutoWalls plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cmdSender, Command cmd, String label, String[] args) {

        if (cmdSender instanceof Player) {
            if (plugin.config.getInt("votes.players." + cmdSender.getName()) >= 20 || cmdSender.hasPermission("walls.op"))
            {
                String message = "";
                for (String s : args) {
                    message += s + " ";
                }
                message = message.trim();
                if (args.length != 0) {
                    Bukkit.broadcastMessage(ChatColor.DARK_GREEN + "[Yell] " + ChatColor.AQUA + cmdSender.getName() + ": " + ChatColor.WHITE + message);
                } else cmdSender.sendMessage(ChatColor.AQUA + "Usage : /yell [message]");
            } else cmdSender.sendMessage(ChatColor.AQUA + "You need at least 20 priority to do that.");
            return true;
        } cmdSender.sendMessage(ChatColor.RED + "The console cannot yell!");
        return true;
    }
}