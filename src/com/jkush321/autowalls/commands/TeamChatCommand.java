package com.jkush321.autowalls.commands;

import com.jkush321.autowalls.AutoWalls;
import com.jkush321.autowalls.TeamChat;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeamChatCommand implements CommandExecutor {

    private AutoWalls plugin;

    public TeamChatCommand(AutoWalls plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cmdSender, Command cmd, String label, String[] args) {

        if (cmdSender instanceof Player) {
            Player p = (Player) cmdSender;
            if (!plugin.playing.contains(p)) {
                cmdSender.sendMessage(ChatColor.RED + "You have to be on a team to teamchat!");
                return true;
            }
            if (!TeamChat.teamChatting.contains(p)) {
                TeamChat.teamChatting.add(p);
                cmdSender.sendMessage(ChatColor.YELLOW + "You are now team chatting!");
                return true;
            }
            if (TeamChat.teamChatting.contains(p)) {
                TeamChat.teamChatting.remove(p);
                cmdSender.sendMessage(ChatColor.YELLOW + "You have disabled team chatting!");
                return true;
            }
            //impossible
            return false;
        }
        cmdSender.sendMessage(ChatColor.RED + "The console is not in a team and cannot team chat!");
        return false;
    }
}