package com.jkush321.autowalls.commands;

import com.jkush321.autowalls.AutoWalls;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpAllCommand implements CommandExecutor {

    private AutoWalls plugin;

    public TpAllCommand(AutoWalls plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cmdSender, Command cmd, String label, String[] args) {

        if (cmdSender instanceof Player) {
            if (cmdSender.hasPermission("walls.op") || cmdSender.hasPermission("walls.mod") || cmdSender.isOp()) {


                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p != (Player) cmdSender)
                        p.teleport((Player) cmdSender);
                    return true;
                }
            } else {

                cmdSender.sendMessage(ChatColor.RED + "No permission.");
                return true;
            }

            cmdSender.sendMessage(ChatColor.RED+"Teleporting players via the console is not yet implemented!");
            return true;

        }
        return true;
    }


}
