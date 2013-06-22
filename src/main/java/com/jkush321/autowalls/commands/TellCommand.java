package com.jkush321.autowalls.commands;

import com.jkush321.autowalls.AutoWalls;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TellCommand implements CommandExecutor {

    private AutoWalls plugin;

    public TellCommand(AutoWalls plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cmdSender, Command cmd, String label, String[] args) {

        if (cmdSender instanceof Player)
        {
            Player p = (Player) cmdSender;
            if (args.length < 2) { cmdSender.sendMessage(ChatColor.RED + "Usage : /tell [name] [message]"); return false; }
            String msg="";
            boolean first = true;
            for (String s : args)
            {
                if (!first)
                    msg+=s+" ";
                else first = false;
            }
            msg=msg.trim();
            Player who = Bukkit.getPlayer(args[0]);
            if (plugin.playing.contains(who) && !plugin.playing.contains(p)) { p.sendMessage(ChatColor.GRAY + "You can not private message that person!"); }
            else { p.sendMessage(ChatColor.AQUA + "[" + ChatColor.RESET + p.getDisplayName() + ChatColor.AQUA + " -> " + ChatColor.RESET + who.getDisplayName() + "] "+ ChatColor.YELLOW + msg); who.sendMessage(ChatColor.AQUA + "[" + ChatColor.RESET + p.getDisplayName() + ChatColor.AQUA + " -> " + ChatColor.RESET + who.getDisplayName() + "] "+ ChatColor.YELLOW + msg); }
            return true;
        }
        else
        {
            if (args.length < 2) { cmdSender.sendMessage(ChatColor.GRAY + "Usage : /tell [name] [message]"); return false; }
            String msg="";
            boolean first = true;
            for (String s : args)
            {
                if (!first)
                    msg+=s+" ";
                else first = false;
            }
            msg=msg.trim();
            Player who = Bukkit.getPlayer(args[0]);
            if (!who.isOnline() || who==null) { cmdSender.sendMessage(ChatColor.DARK_RED + "Player not found"); return true;}
            cmdSender.sendMessage(ChatColor.AQUA + "[Private] " + ChatColor.YELLOW + msg); who.sendMessage(ChatColor.AQUA + "[Private] " + ChatColor.YELLOW + msg);
            return true;
        }
    }
}