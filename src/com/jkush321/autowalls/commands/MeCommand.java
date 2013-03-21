package com.jkush321.autowalls.commands;

import com.jkush321.autowalls.AutoWalls;
import com.jkush321.autowalls.TeamChat;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MeCommand implements CommandExecutor {

    private AutoWalls plugin;

    public MeCommand(AutoWalls plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cmdSender, Command cmd, String label, String[] args) {

        if (cmdSender instanceof Player) {
            if (args.length == 0) {
                cmdSender.sendMessage(ChatColor.GRAY + "Invalid arguments... /me [message]");
                return true;
            }
            Player p = (Player) cmdSender;
            String msg = "";
            for (String s : args) {
                msg += s + " ";
            }
            msg = msg.trim();
            TeamChat.say(p, p.getDisplayName() + " " + ChatColor.GRAY + "* " + msg);
            return true;
        }
        cmdSender.sendMessage(ChatColor.RED + "Last time I checked, consoles couldn't emote!");
        return true;
    }
}