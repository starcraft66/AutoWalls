package com.jkush321.autowalls.commands;

import com.jkush321.autowalls.AutoWalls;
import com.jkush321.autowalls.TeleportManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpCommand implements CommandExecutor {

    private AutoWalls plugin;

    public TpCommand(AutoWalls plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cmdSender, Command cmd, String label, String[] args) {

        if (cmdSender instanceof Player) {
            if (args.length == 1) {
                Player p = (Player) cmdSender;
                Player p2 = Bukkit.getPlayer(args[0]);
                if (p2 != null && p2.isOnline()) {
                    if (!plugin.playing.contains(p) || p.hasPermission("walls.op")) p.teleport(p2);
                    else {
                        if (plugin.teamTeleports && plugin.secondsBeforeTeleport > 0) {
                            if (plugin.redTeam.contains(p) && plugin.redTeam.contains(p2))
                                TeleportManager.createTpRunnable(p, p2);
                            else if (plugin.blueTeam.contains(p) && plugin.blueTeam.contains(p2))
                                TeleportManager.createTpRunnable(p, p2);
                            else if (plugin.greenTeam.contains(p) && plugin.greenTeam.contains(p2))
                                TeleportManager.createTpRunnable(p, p2);
                            else if (plugin.orangeTeam.contains(p) && plugin.orangeTeam.contains(p2))
                                TeleportManager.createTpRunnable(p, p2);
                            else {
                                p.sendMessage(ChatColor.YELLOW + p2.getName() + " is not on your team!");
                                return true;
                            }
                            p.sendMessage(ChatColor.YELLOW + "You will be teleported to " + ChatColor.DARK_GREEN + p2.getName() + ChatColor.YELLOW + "if you do not move for " + ChatColor.YELLOW + plugin.secondsBeforeTeleport + ChatColor.YELLOW + " seconds");
                        } else if (plugin.teamTeleports) {
                            if (plugin.redTeam.contains(p) && plugin.redTeam.contains(p2)) p.teleport(p2);
                            else if (plugin.blueTeam.contains(p) && plugin.blueTeam.contains(p2)) p.teleport(p2);
                            else if (plugin.greenTeam.contains(p) && plugin.greenTeam.contains(p2)) p.teleport(p2);
                            else if (plugin.orangeTeam.contains(p) && plugin.orangeTeam.contains(p2)) p.teleport(p2);
                            else p.sendMessage(ChatColor.YELLOW + p2.getName() + " is not on your team!");
                        } else p.sendMessage(ChatColor.DARK_AQUA + "This server has team teleporting disabled!");
                    }
                } else p.sendMessage(ChatColor.GRAY + "That player is not online!");
            } else if (args.length == 4) {
                Player p = Bukkit.getPlayer(args[0]);
                if (p.isOnline() && p != null) {
                    double x, y, z;
                    try {
                        x = Double.parseDouble(args[1]);
                        y = Double.parseDouble(args[2]);
                        z = Double.parseDouble(args[3]);
                    } catch (Exception e) {
                        cmdSender.sendMessage(ChatColor.DARK_RED + "Invalid coordinates");
                        return true;
                    }
                    p.teleport(new Location(p.getWorld(), x, y, z));
                } else
                    cmdSender.sendMessage(ChatColor.DARK_RED + "Player not found.");
            } else cmdSender.sendMessage("Invalid Arguments. /tp playername");
            return true;
        }
        cmdSender.sendMessage(ChatColor.RED + "Teleporting players via the console is not yet implemented!");
        return true;
    }
}