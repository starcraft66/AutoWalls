/*
 * AutoWalls by jkush321 is licensed under the
 * Creative Commons Attribution-NonCommercial 3.0 Unported License
 * 
 * You are fully allowed to modify the source code for your own network
 * of servers, but you may not distribute the modified code outside of
 * your servers.
 * 
 * AutoWalls was originally a personal project that was standalone for
 * my own private server, and it slowly accumulated into a giant plugin.
 * 
 * AutoWalls is for dedicated servers that are willing to run just Walls.
 * 
 * The license requires attribution and you have to give credit to jkush321
 * no matter how many changes were made to the code. In some clearly stated
 * way everyone who goes on the server must be able to easily see and be aware
 * of the fact that this code originated from jkush321 and was modified by
 * you or your team.
 * 
 * For more information visit http://bit.ly/AutoWalls
 * 
 */

package com.jkush321.autowalls;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class VoteResult implements Runnable {

    private AutoWalls plugin;

    public VoteResult(AutoWalls plugin) {
        this.plugin = plugin;
    }

	public void run()
	{
		try {
			AutoWalls.voting = false;
			Bukkit.broadcastMessage(ChatColor.DARK_RED + "The votes are in...");
            /* Old Code - Subject for removal
			Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "1 - " + AutoWalls.votedFor1.size());
			Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "2 - " + AutoWalls.votedFor2.size());*/
            Map<Integer, Integer> mostPopularMaps = new HashMap<Integer, Integer>();
            Integer freq = 0;
			for (Integer i : AutoWalls.votes.values()) {
                if (mostPopularMaps.get(i) != null) {
                    freq = mostPopularMaps.get(i) + 1;
                    mostPopularMaps.put(i, freq);
                } else {
                    mostPopularMaps.put(i, 1);
                }
            }

            ConfigurationHelper ch = ConfigurationHelper.getInstance();
            int maps = ch.getNumberOfArenas();
            for (int i = 1; i <= maps; i++) {
                Bukkit.broadcastMessage(ChatColor.DARK_AQUA + ch.getArenaName(i) + " - " + ((mostPopularMaps.get(i) !=  null) ? mostPopularMaps.get(i) : "0"));
            }

            Map.Entry<Integer, Integer> maxEntry = null;
            Boolean randomMap = false;
            for (Map.Entry<Integer, Integer> entry : mostPopularMaps.entrySet())
            {
                if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0)
                {
                    if (maxEntry == entry) {
                        randomMap = true;
                    }else {
                        maxEntry = entry;
                    }
                }
                if (randomMap) {
                    break;
                }
            }
            if (!randomMap) {
                if (maxEntry != null) {
                    AutoWalls.config.set("next-map", maxEntry.getKey());
                } else {
                    Random r = new Random();
                    AutoWalls.config.set("next-map", r.nextInt(ch.getNumberOfArenas()) + 1);
                }
            } else {
				Random r = new Random();
				AutoWalls.config.set("next-map", r.nextInt(ch.getNumberOfArenas()) + 1);
			}
			emptyFolder(new File("custom"));
			(new File("custom")).delete();
			plugin.saveConfig();
            AutoWalls.log("Copying " + new File("custom" + AutoWalls.config.getInt("next-map")).getName());
			copyFolder(new File("custom" + AutoWalls.config.getInt("next-map")), new File("custom"));
			
			for (Player p : Bukkit.getOnlinePlayers())
			{
				p.kickPlayer(ChatColor.YELLOW + "Next game: " + ChatColor.DARK_AQUA + ch.getArenaName(AutoWalls.config.getInt("next-map"))  + ChatColor.YELLOW + " Reconnect and type /join to get in a game.");
			}
            //Leave my hub kick plugin some time to send all players to the lobby!
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new BukkitRunnable() {
                @Override
                public void run() {
                    Bukkit.shutdown();
                }
            }, 40L);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	public static void copyFolder(File src, File dest) throws IOException
	{
	 
	    	if(src.isDirectory()){
	 
	    		if(!dest.exists()){
	    		   dest.mkdir();
	    		}
	    		String files[] = src.list();
	 
	    		for (String file : files) {
	    		   File srcFile = new File(src, file);
	    		   File destFile = new File(dest, file);
	    		   copyFolder(srcFile,destFile);
	    		}
	 
	    	}
	    	else
	    	{
	    		//if file, then copy it
	    		//Use bytes stream to support all file types
	    		InputStream in = new FileInputStream(src);
	    	        OutputStream out = new FileOutputStream(dest); 
	 
	    	        byte[] buffer = new byte[1024];
	 
	    	        int length;
	    	        //copy the file content in bytes 
	    	        while ((length = in.read(buffer)) > 0){
	    	    	   out.write(buffer, 0, length);
	    	        }
	 
	    	        in.close();
	    	        out.close();
	    	}
	    }
		public static void emptyFolder(File dest)
	    	throws IOException{
		 
		    	if(dest.isDirectory()){
		 
		    		//if directory exists, delete the files it contains
		    		if(dest.exists()){
		 
		    		//list all the directory contents
		    		String files[] = dest.list();
		 
		    		for (String file : files) {
		    		   //construct the dest file structure
		    		   File destFile = new File(dest, file);
		    		   //recursive delete
		    		   emptyFolder(destFile);
		    			}
				}
		 
		    	}else{
		    		//if file, then delete it
				dest.delete();
		    	        }
	 
	    }
}
