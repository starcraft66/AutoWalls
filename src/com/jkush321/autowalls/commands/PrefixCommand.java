package com.jkush321.autowalls.commands;

import com.jkush321.autowalls.AutoWalls;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class PrefixCommand implements CommandExecutor {

    private AutoWalls plugin;

    public PrefixCommand(AutoWalls plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cmdSender, Command cmd, String label, String[] args) {

        if (cmdSender.isOp() || cmdSender.hasPermission("walls.op")) {
            if (args.length < 2) {
                cmdSender.sendMessage(ChatColor.RED + "Usage : /prefix <set | remove> <name> <prefix>");
                return false;
            } else {


                String playerName = "";
                if (Bukkit.getPlayer(args[1]) == null || !Bukkit.getPlayer(args[1]).isOnline()) {
                    playerName = args[1];
                } else {
                    playerName = Bukkit.getPlayer(args[1]).getName();
                }
                String fullPrefix = "";
                for (int i = 2; i < args.length; i++) {
                    if (i > 0) {
                        fullPrefix += args[i] + " ";
                    }
                }
                fullPrefix = fullPrefix.trim();

                if (args[0].equalsIgnoreCase("set")) {
                    plugin.config.set("prefix." + playerName, fullPrefix);
                    plugin.saveConfig();
                    if (Bukkit.getPlayer(playerName) != null && Bukkit.getPlayer(playerName).isOnline()) {
                        if (plugin.config.isSet("prefix." + Bukkit.getPlayer(playerName).getName())) {
                            Bukkit.getPlayer(playerName).setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.config.getString("prefix." + Bukkit.getPlayer(playerName).getName()).replace("{pri}", plugin.config.getInt("votes.players." + Bukkit.getPlayer(playerName).getName()) + "") + Bukkit.getPlayer(playerName).getName() + ChatColor.WHITE));
                        }
                    }
                    cmdSender.sendMessage(ChatColor.YELLOW + "Set " + playerName + "'s prefix to " + ChatColor.WHITE + "\"" + fullPrefix + ChatColor.WHITE + "\"");
                    return true;
                }

                if (args[0].equalsIgnoreCase("remove")) {

                    if (plugin.config.getBoolean("priorities") == true) {

                        plugin.getConfig().set("prefix." + playerName, null);
                        plugin.saveConfig();
                        cmdSender.sendMessage(ChatColor.YELLOW + "Removed " + ChatColor.WHITE + playerName + ChatColor.YELLOW + "'s prefix");

                        if (Bukkit.getPlayer(playerName) != null && Bukkit.getPlayer(playerName).isOnline()) {
                            if (Bukkit.getPlayer(playerName).hasPermission("walls.op") || cmdSender.isOp()) {
                                Bukkit.getPlayer(playerName).setDisplayName(ChatColor.DARK_BLUE + "[" + ChatColor.DARK_GREEN + "Admin" + ChatColor.DARK_BLUE + "]" + ChatColor.DARK_RED + Bukkit.getPlayer(playerName).getName() + ChatColor.GRAY + ChatColor.WHITE);
                                return true;
                            }
                            if (plugin.config.isSet("prefix." + Bukkit.getPlayer(playerName).getName())) {
                                Bukkit.getPlayer(playerName).setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.config.getString("prefix." + Bukkit.getPlayer(playerName).getName())).replace("{pri}", plugin.config.getInt("votes.players." + Bukkit.getPlayer(playerName).getName()) + "") + Bukkit.getPlayer(playerName).getName() + ChatColor.WHITE);
                                return true;
                            }
                            //impossible
                            return true;
                        } else {
                            return true;
                        }
                    }
                }

                cmdSender.sendMessage(ChatColor.RED + "Usage : /prefix <set | remove> <name> <prefix>");
                return false;

            }
        } else {
            cmdSender.sendMessage(ChatColor.RED + "No permission!");
            return true;
        }
    }
}