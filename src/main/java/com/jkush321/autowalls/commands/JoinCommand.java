package com.jkush321.autowalls.commands;

import com.jkush321.autowalls.AutoWalls;
import com.jkush321.autowalls.Timer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JoinCommand implements CommandExecutor {

    private AutoWalls plugin;

    public JoinCommand(AutoWalls plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cmdSender, Command cmd, String label, String[] args) {
        if (cmdSender instanceof Player)
        {
            Player p = (Player) cmdSender;
            boolean allowed = false;
            if (!plugin.gameInProgress && !plugin.gameOver && plugin.playing.contains(p) && (p.hasPermission("walls.donor")|| p.isOp() || p.hasPermission("walls.op"))){ allowed = true; }
            if (!allowed)
            {
                cmdSender.sendMessage(ChatColor.DARK_RED + "You must donate to switch teams!");
                return true;
            }
            if (args.length == 0) // Oops
            {
                p.sendMessage(ChatColor.RED + "You forgot to specify the team! /join <red|blue|green|orange>");
            }
            else if (args.length == 1) // Add to specified team
            {
                if (args[0].equalsIgnoreCase("red")){
                    plugin.leaveTeam(p);
                    plugin.joinTeam(p,"red");
                }
                else if (args[0].equalsIgnoreCase("blue")){
                    plugin.leaveTeam(p);
                    plugin.joinTeam(p,"blue");
                }
                else if (args[0].equalsIgnoreCase("green")){
                    plugin.leaveTeam(p);
                    plugin.joinTeam(p,"green");
                }
                else if (args[0].equalsIgnoreCase("orange")){
                    plugin.leaveTeam(p);
                    plugin.joinTeam(p,"orange");
                }
                else p.sendMessage(ChatColor.DARK_RED + "The Team " + args[0] + " is Invalid!");
            }
            else p.sendMessage(ChatColor.RED + "Too Many Arguments. /join <red|blue|green|orange>");
        }
        else cmdSender.sendMessage("You can't join a team, console :P");
        return false;
    }
}