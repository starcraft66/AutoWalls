package com.jkush321.autowalls.commands;

import com.jkush321.autowalls.AutoWalls;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpHereCommand implements CommandExecutor {

    private AutoWalls plugin;

    public TpHereCommand(AutoWalls plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cmdSender, Command cmd, String label, String[] args) {

        if (cmdSender instanceof Player) {
            if (cmdSender.hasPermission("walls.op") || cmdSender.hasPermission("walls.mod") || cmdSender.isOp()) {
                if (args.length != 1) {
                    cmdSender.sendMessage(ChatColor.RED + "Invalid arguments");
                    return true;
                }
                Player pl = Bukkit.getPlayer(args[0]);
                if (pl != null && pl.isOnline()) {
                    pl.teleport((Player) cmdSender);
                    cmdSender.sendMessage(ChatColor.YELLOW + "Teleported " + pl.getDisplayName() + " to you.");
                } else cmdSender.sendMessage(ChatColor.RED + "Player is not online");
                return true;
            } else {
                cmdSender.sendMessage(ChatColor.RED + "No permission.");
                return false;

            }
        }

        cmdSender.sendMessage(ChatColor.DARK_AQUA + "Only Chuck Norris knows how to teleport players to the console!");
        return true;
    }
}