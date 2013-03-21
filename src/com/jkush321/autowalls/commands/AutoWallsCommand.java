package com.jkush321.autowalls.commands;

import com.jkush321.autowalls.AutoWalls;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class AutoWallsCommand implements CommandExecutor {

    private AutoWalls plugin;

    public AutoWallsCommand(AutoWalls plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cmdSender, Command cmd, String label, String[] args) {

        if (args.length == 0){
            cmdSender.sendMessage(ChatColor.AQUA + "AutoWalls v." + ChatColor.YELLOW + plugin.version);
            cmdSender.sendMessage(ChatColor.RED + "Usage : /autowalls <reload>");
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            if (cmdSender.isOp() || cmdSender.hasPermission("walls.op")){
                plugin.reloadConfig();
                plugin.saveConfig();
                cmdSender.sendMessage(ChatColor.GREEN + "Config reloaded!");
                return  true;
            }
        }


    return false;
    }
}