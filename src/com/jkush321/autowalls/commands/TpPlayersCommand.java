package com.jkush321.autowalls.commands;

import com.jkush321.autowalls.AutoWalls;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpPlayersCommand implements CommandExecutor {

    private AutoWalls plugin;

    public TpPlayersCommand(AutoWalls plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cmdSender, Command cmd, String label, String[] args) {

        if (cmdSender instanceof Player) {
            if (!cmdSender.hasPermission("walls.op") || !cmdSender.hasPermission("walls.mod") || !cmdSender.isOp()) {
                for (Player p : plugin.playing) {
                    if (p != (Player) cmdSender)
                        p.teleport((Player) cmdSender);
                }
                return true;
            } else {

                cmdSender.sendMessage(ChatColor.RED + "No permision.");
                return true;
            }
        }
        cmdSender.sendMessage(ChatColor.RED + "Teleporting players via the console is not yet implemented!");
        return true;
    }
}