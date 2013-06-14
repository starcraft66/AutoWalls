package com.jkush321.autowalls.commands;

import com.jkush321.autowalls.AutoWalls;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PriorityCommand implements CommandExecutor {

    private AutoWalls plugin;

    public PriorityCommand(AutoWalls plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cmdSender, Command cmd, String label, String[] args) {

        if (cmdSender.hasPermission("walls.op") || cmdSender.isOp()) {

            if (args.length == 0 || args.length == 2 || args.length > 3) {
                cmdSender.sendMessage(ChatColor.RED + "Usage : /priority <name> " + ChatColor.YELLOW + "Get a player's priority");
                cmdSender.sendMessage(ChatColor.RED + "Usage : /priority <name> set <amount> " + ChatColor.YELLOW + "Set a player's priority");
                cmdSender.sendMessage(ChatColor.RED + "Usage : /priority <name> add <amount> " + ChatColor.YELLOW + "Add to a player's priority");
                return false;
            }

            if (args.length == 1) {

                if (plugin.config.isSet("votes.players." + args[0])) {

                    cmdSender.sendMessage(ChatColor.YELLOW + args[0] + "'s priority is " + plugin.config.getInt("votes.players." + args[0]) + ".");
                    cmdSender.sendMessage(ChatColor.YELLOW + "Use '/priority " + args[0] + " set <amount>' to alter " + args[0] + "'s priority.");
                    cmdSender.sendMessage(ChatColor.YELLOW + "Use '/priority " + args[0] + " add <amount>' to add to " + args[0] + "'s priority.");

                    return true;
                }

                cmdSender.sendMessage(ChatColor.YELLOW + args[0] + "'s priority is 0.");
                cmdSender.sendMessage(ChatColor.YELLOW + "Use '/priority " + args[0] + " set <amount>' to alter " + args[0] + "'s priority.");
                cmdSender.sendMessage(ChatColor.YELLOW + "Use '/priority " + args[0] + " add <amount>' to add to " + args[0] + "'s priority.");

                return true;

            }

            if (args.length == 3 && args[1].equalsIgnoreCase("set")) {

                int prioritySet = Integer.parseInt(args[2]);

                if (Bukkit.getPlayer(args[0]) != null) {

                    Player pl = Bukkit.getPlayer(args[0]);


                    plugin.config.set("votes.players." + pl.getName(), prioritySet);
                    plugin.saveConfig();
                    if (pl.isOnline()) {
                        Bukkit.getPlayer(pl.getName()).sendMessage(ChatColor.YELLOW + "Your priority is now " + plugin.config.getInt("votes.players." + pl.getName()));
                    }
                    cmdSender.sendMessage(ChatColor.YELLOW + pl.getName() + "'s priority is now " + plugin.config.getInt("votes.players." + pl.getName()));
                    if (Bukkit.getPlayer(pl.getName()).hasPermission("walls.op") || pl.isOp()) {
                        Bukkit.getPlayer(pl.getName()).setDisplayName(ChatColor.DARK_BLUE + "[" + ChatColor.DARK_GREEN + "Admin" + ChatColor.DARK_BLUE + "]" + ChatColor.DARK_RED + Bukkit.getPlayer(pl.getName()).getName() + ChatColor.GRAY + ChatColor.WHITE);
                        return true;
                    }
                    if (plugin.config.isSet("votes.players." + pl.getName()) && plugin.config.getInt("votes.players." + pl.getName()) >= 20) {
                        pl.setDisplayName(ChatColor.DARK_AQUA + pl.getName() + ChatColor.WHITE);
                    }
                    if (plugin.config.isSet("votes.players." + pl.getName()) && plugin.config.getInt("votes.players." + pl.getName()) >= 250) {
                        pl.setDisplayName(ChatColor.DARK_RED + pl.getName() + ChatColor.WHITE);
                    }
                    if (plugin.config.getBoolean("priorities") == true) {
                        if (plugin.config.isSet("votes.players." + pl.getName())) {
                            pl.setDisplayName(ChatColor.YELLOW + "[" + plugin.config.getInt("votes.players." + pl.getName()) + "]" + ChatColor.GRAY + pl.getDisplayName() + ChatColor.WHITE);
                        } else pl.setDisplayName(ChatColor.GRAY + "[0]" + pl.getDisplayName() + ChatColor.WHITE);
                    }
                    return true;
                } else {

                    String pl = args[0];

                    plugin.config.set("votes.players." + pl, prioritySet);
                    plugin.saveConfig();
                    cmdSender.sendMessage(ChatColor.YELLOW + pl + "'s priority is now " + plugin.config.getInt("votes.players." + pl));

                    return true;
                }
            } else if (args.length == 3 && args[1].equalsIgnoreCase("add")) {

                int priorityAdd = Integer.parseInt(args[2]);

                if (Bukkit.getPlayer(args[0]) != null) {

                    Player pl = Bukkit.getPlayer(args[0]);


                    plugin.config.set("votes.players." + pl.getName(), (plugin.config.getInt("votes.players." + pl.getName()) + priorityAdd));
                    plugin.saveConfig();
                    if (pl.isOnline()) {
                        Bukkit.getPlayer(pl.getName()).sendMessage(ChatColor.YELLOW + "Your priority is now " + plugin.config.getInt("votes.players." + pl.getName()));
                    }
                    cmdSender.sendMessage(ChatColor.YELLOW + pl.getName() + "'s priority is now " + plugin.config.getInt("votes.players." + pl.getName()));
                    if (Bukkit.getPlayer(pl.getName()).hasPermission("walls.op") || pl.isOp()) {
                        Bukkit.getPlayer(pl.getName()).setDisplayName(ChatColor.DARK_BLUE + "[" + ChatColor.DARK_GREEN + "Admin" + ChatColor.DARK_BLUE + "]" + ChatColor.DARK_RED + Bukkit.getPlayer(pl.getName()).getName() + ChatColor.GRAY + ChatColor.WHITE);
                        return true;
                    }
                    if (plugin.config.isSet("votes.players." + pl.getName()) && plugin.config.getInt("votes.players." + pl.getName()) >= 20) {
                        pl.setDisplayName(ChatColor.DARK_AQUA + pl.getName() + ChatColor.WHITE);
                    }
                    if (plugin.config.isSet("votes.players." + pl.getName()) && plugin.config.getInt("votes.players." + pl.getName()) >= 250) {
                        pl.setDisplayName(ChatColor.DARK_RED + pl.getName() + ChatColor.WHITE);
                    }
                    if (plugin.config.getBoolean("priorities") == true) {
                        if (plugin.config.isSet("votes.players." + pl.getName())) {
                            pl.setDisplayName(ChatColor.YELLOW + "[" + plugin.config.getInt("votes.players." + pl.getName()) + "]" + ChatColor.GRAY + pl.getDisplayName() + ChatColor.WHITE);
                        } else pl.setDisplayName(ChatColor.GRAY + "[0]" + pl.getDisplayName() + ChatColor.WHITE);
                    }
                    return true;
                } else {

                    String pl = args[0];

                    plugin.config.set("votes.players." + pl, (plugin.config.getInt("votes.players." + pl) + priorityAdd));
                    plugin.saveConfig();
                    cmdSender.sendMessage(ChatColor.YELLOW + pl + "'s priority is now " + plugin.config.getInt("votes.players." + pl));

                    return true;
                }
            } else {
                cmdSender.sendMessage(ChatColor.RED + "Usage : /priority <name> " + ChatColor.YELLOW + "Get a player's priority");
                cmdSender.sendMessage(ChatColor.RED + "Usage : /priority <name> set <amount> " + ChatColor.YELLOW + "Set a player's priority");
                cmdSender.sendMessage(ChatColor.RED + "Usage : /priority <name> add <amount> " + ChatColor.YELLOW + "Add to a player's priority");
                return false;
            }
        }
        cmdSender.sendMessage(ChatColor.RED + "No permission!");
        return true;
    }
}