package com.jkush321.autowalls.commands;

import com.jkush321.autowalls.AutoWalls;
import com.jkush321.autowalls.JoinTimer;
import com.jkush321.autowalls.kits.Kit;
import com.jkush321.autowalls.kits.KitManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KitCommand implements CommandExecutor {

    private AutoWalls plugin;

    public KitCommand(AutoWalls plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cmdSender, Command cmd, String label, String[] args) {

        if (cmdSender instanceof Player) {
            if (args.length == 1) {
                if (JoinTimer.timeleft > 0 || !plugin.gameInProgress) {

                    if (KitManager.findKit(args[0]) != null) {
                        Kit k = KitManager.findKit(args[0]);
                        int p = 0;
                        if (plugin.config.isSet("votes.players." + cmdSender.getName()))
                            p = plugin.config.getInt("votes.players." + cmdSender.getName());
                        if (k.getRequiredPriority() <= p) {
                            KitManager.setKit((Player) cmdSender, k);
                            cmdSender.sendMessage(ChatColor.DARK_AQUA + "Selected kit " + k.getName());
                        } else {
                            cmdSender
                                    .sendMessage(ChatColor.DARK_RED + "That kit is not available to you! You can get " + plugin.priorityPerDollar + " priority for every $1 donated");
                        }
                    } else {
                        cmdSender.sendMessage(ChatColor.DARK_RED + "That kit was not found.");
                    }
                } else
                    cmdSender.sendMessage(ChatColor.DARK_RED + "It is too late to choose a kit!");
            } else if (args.length == 0) {
                int p = 0;
                if (plugin.config.isSet("votes.players." + cmdSender.getName())) {
                    p = plugin.config.getInt("votes.players." + cmdSender.getName());
                }
                String m1 = (ChatColor.GRAY + "Available Kits: " + ChatColor.WHITE);
                for (Kit k : KitManager.kitList) {
                    if (k.getRequiredPriority() <= p)
                        m1 += "(" + k.getRequiredPriority() + ")" + k.getName() + ", ";
                }
                String m2 = (ChatColor.GRAY + "Unavailable Kits: " + ChatColor.WHITE);
                for (Kit k : KitManager.kitList) {
                    if (k.getRequiredPriority() > p)
                        m2 += "(" + k.getRequiredPriority() + ")" + k.getName() + ", ";
                }
                m1 = m1.substring(0, m1.length() - 2) + ".";
                m2 = m2.substring(0, m2.length() - 2) + ".";
                cmdSender.sendMessage(m1);
                cmdSender.sendMessage(m2);
                cmdSender.sendMessage(ChatColor.DARK_AQUA + "To unlock the unavaible kits you can donate for priority. You get " + plugin.priorityPerDollar + " priority for $1");
            } else {
                cmdSender.sendMessage(ChatColor.DARK_RED + "/kit [name]");
            }
            return true;
        }
        cmdSender.sendMessage(ChatColor.RED + "The console can not choose a kit!");
        return true;
    }
}