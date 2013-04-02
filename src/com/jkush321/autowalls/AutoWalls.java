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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

import com.jkush321.autowalls.commands.*;
import org.bukkit.block.Block;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.event.weather.LightningStrikeEvent;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.jkush321.autowalls.kits.Kit;
import com.jkush321.autowalls.kits.KitManager;
import sun.usagetracker.UsageTrackerClient;

public class AutoWalls extends JavaPlugin implements Listener {

	public static Plugin plugin = Bukkit.getPluginManager().getPlugin("AutoWalls");
	public static final Logger logger = Logger.getLogger("Minecraft");
	public static List<Player> playing = new CopyOnWriteArrayList<Player>();
	public static List<Player> redTeam = new CopyOnWriteArrayList<Player>();
	public static List<Player> blueTeam = new CopyOnWriteArrayList<Player>();
	public static List<Player> greenTeam = new CopyOnWriteArrayList<Player>();
	public static List<Player> orangeTeam = new CopyOnWriteArrayList<Player>();
	public static List<Player> votedFor1 = new ArrayList<Player>();
	public static List<Player> votedFor2 = new ArrayList<Player>();
	public static boolean gameInProgress = false;
	public static boolean voting = false;
	public static FileConfiguration config;
	public static boolean gameOver = false;
	public static int teamSize;
	public static int[] redSpawn = new int[3];
	public static int[] blueSpawn = new int[3];
	public static int[] greenSpawn = new int[3];
	public static int[] orangeSpawn = new int[3];
    public static int[] redQuadrant = new int[4];
    public static int[] blueQuadrant = new int[4];
    public static int[] greenQuadrant = new int[4];
    public static int[] orangeQuadrant = new int[4];
    public static int mapNumber;
    public static Boolean announcerState;
    public static Boolean heartbeatState;
	public static String announcerName;
	public static Thread beat;
	public static Thread announcer;
	public static Thread dropper;
	public static Thread joinTimer;
	public static boolean mapVotes;
	public static boolean blockSneaking;
	public static boolean disableHealing;
	public static boolean arrowLightning;
	public static int arrowLightningChance;
	public static boolean canJoin = false;
	public static List<Sign> graves = new ArrayList<Sign>();
	public static List<String> graveMessages;
	public static String fullKickMessage;
	public static String priorityKickMessage;
	public static boolean teamTeleports;
	public static String votelink = "";
	public static int priorityPerDollar;
	private static Map<Player, Long> lastEvent = new ConcurrentHashMap<>();
	public static int secondsBeforeTeleport;
	public final static String version = "1.1r1";
	public static int earlyJoinPriority, lateJoinPriority;
	public static boolean lateJoins;
	public static boolean preventFireBeforeWallsFall;
	public static boolean useTabApi;
	public static ArrayList<String> dead = new ArrayList<String>();

    @Override
	public void onEnable()
	{
		plugin = this;

        //Register commands

        getCommand("autowalls").setExecutor(new AutoWallsCommand(this));
        getCommand("day").setExecutor(new DayCommand(this));
        getCommand("fly").setExecutor(new FlyCommand(this));
        getCommand("forcedrop").setExecutor(new ForceDropCommand(this));
        getCommand("forceend").setExecutor(new ForceEndCommand(this));
        getCommand("forcestart").setExecutor(new ForceStartCommand(this));
        getCommand("join").setExecutor(new JoinCommand(this));
        getCommand("kit").setExecutor(new KitCommand(this));
        getCommand("leave").setExecutor(new LeaveCommand(this));
        getCommand("me").setExecutor(new MeCommand(this));
        getCommand("night").setExecutor(new NightCommand(this));
        getCommand("playing").setExecutor(new PlayingCommand(this));
        getCommand("prefix").setExecutor(new PrefixCommand(this));
        getCommand("priority").setExecutor(new PriorityCommand(this));
        getCommand("teamchat").setExecutor(new TeamChatCommand(this));
        getCommand("team").setExecutor(new TeamCommand(this));
        getCommand("tell").setExecutor(new TellCommand(this));
        getCommand("time").setExecutor(new TimeCommand(this));
        getCommand("tpall").setExecutor(new TpAllCommand(this));
        getCommand("tp").setExecutor(new TpCommand(this));
        getCommand("tphere").setExecutor(new TpHereCommand(this));
        getCommand("tpplayers").setExecutor(new TpPlayersCommand(this));
        getCommand("tpspecs").setExecutor(new TpSpecsCommand(this));
        getCommand("yell").setExecutor(new YellCommand(this));


		getServer().getPluginManager().registerEvents(this, this);
		config = getConfig();
		
		config.addDefault("votes.players.jkush321", 500);
		config.addDefault("votes.players.example_player", 2);
		config.addDefault("priorities", true);
		config.addDefault("team-size", 4);
		config.addDefault("next-map", 1);
        config.addDefault("enable-heartbeats", true);
        config.addDefault("enable-announcer", true);
		config.addDefault("announcer-name", "Announcer");
		config.addDefault("announcements", "Seperate Announements With SemiColons;You should have at least 2 messages;Your message here!");
		config.addDefault("map-votes", true);
		config.addDefault("prevent-sneaking-after-walls-fall", true);
		config.addDefault("disable-healing-after-walls-fall", true);
		config.addDefault("rare-lightning-strike-on-arrow-land", true);
		config.addDefault("one-in-blank-chance-of-lightning", 250);
		config.addDefault("seconds-before-can-join-team", 60);
		config.addDefault("grave-messages", Arrays.asList("He was loved","Loved by many","Will be missed","Died young","In our hearts","Has been lost","All gone now","Will be mourned","Had a good life","Withered away" ));
		config.addDefault("full-server-message", "The server is full and your priority is not high enough!");
		config.addDefault("priority-kick-message", "Someone with higher priority joined!");
		config.addDefault("team-teleports", true);
		config.addDefault("game-length-in-minutes", 15);
		config.addDefault("vote-link", "my-vote-link.com");
		config.addDefault("priority-per-dollar", 5);
		config.addDefault("seconds-before-teleport", 3);
		config.addDefault("early-join-priority", 1);
		config.addDefault("late-join-priority", 25);
		config.addDefault("late-joins", true);
		config.addDefault("prevent-fire-before-walls-fall", true);
		config.addDefault("max-color-cycler-time", 120);
		config.addDefault("use-tab-api", true);
		
		config.options().copyDefaults(true);
	    saveConfig();	    

        heartbeatState = config.getBoolean("enable-heartbeats");
        announcerState = config.getBoolean("enable-announcer");
	    announcerName = config.getString("announcer-name");
	    mapNumber = config.getInt("next-map");
	    mapVotes = config.getBoolean("map-votes");
	    blockSneaking = config.getBoolean("prevent-sneaking-after-walls-fall");
	    disableHealing = config.getBoolean("disable-healing-after-walls-fall");
	    arrowLightning = config.getBoolean("rare-lightning-strike-on-arrow-land");
	    arrowLightningChance = config.getInt("one-in-blank-chance-of-lightning");
	    graveMessages=config.getStringList("grave-messages");
	    fullKickMessage=config.getString("full-server-message");
	    priorityKickMessage=config.getString("priority-kick-message");
	    JoinTimer.timeleft = config.getInt("seconds-before-can-join-team");
	    teamTeleports = config.getBoolean("team-teleports");
	    WallDropper.time=config.getInt("game-length-in-minutes") * 60;
	    votelink = config.getString("vote-link");
	    priorityPerDollar=config.getInt("priority-per-dollar");
	    secondsBeforeTeleport=config.getInt("seconds-before-teleport");
	    earlyJoinPriority = config.getInt("early-join-priority");
	    lateJoinPriority = config.getInt("late-join-priority");
	    lateJoins = config.getBoolean("late-joins");
	    preventFireBeforeWallsFall = config.getBoolean("prevent-fire-before-walls-fall");
	    ColorCycler.MAX_COLOR_TIME = config.getInt("max-color-cycler-time");
	    useTabApi = config.getBoolean("use-tab-api");
	    
	    if (mapNumber == 1)
	    {
            //Spawns

			redSpawn[0] = 297;
			redSpawn[1] = 118;
			redSpawn[2] = -848;
			
			blueSpawn[0] = 403;
			blueSpawn[1] = 118;
			blueSpawn[2] = -848;
			
			greenSpawn[0] = 403;
			greenSpawn[1] = 118;
			greenSpawn[2] = -736;
			
			orangeSpawn[0] = 291;
			orangeSpawn[1] = 118;
			orangeSpawn[2] = -736;

            //Quadrants
            //xMax
            redQuadrant[0] = 346;
            //xMin
            redQuadrant[1] = 286;
            //zMax
            redQuadrant[2] = -794;
            //zMin
            redQuadrant[3] = -854;

            blueQuadrant[0] = 408;
            blueQuadrant[1] = 349;
            blueQuadrant[2] = -794;
            blueQuadrant[3] = -854;

            greenQuadrant[0] = 409;
            greenQuadrant[1] = 349;
            greenQuadrant[2] = -731;
            greenQuadrant[3] = -791;

            orangeQuadrant[0] = 346;
            orangeQuadrant[1] = 286;
            orangeQuadrant[2] = -731;
            orangeQuadrant[3] = -791;

        }
	    else if (mapNumber == 2)
	    {

            //Spawns

	    	redSpawn[0] = -857;
			redSpawn[1] = 74;
			redSpawn[2] = -204;
			
			blueSpawn[0] = -857;
			blueSpawn[1] = 74;
			blueSpawn[2] = -140;
			
			greenSpawn[0] = -729;
			greenSpawn[1] = 74;
			greenSpawn[2] = -140;
			
			orangeSpawn[0] = -729;
			orangeSpawn[1] = 74;
			orangeSpawn[2] = -204;

            //Quadrants

            redQuadrant[0] = -804;
            redQuadrant[1] = -863;
            redQuadrant[2] = -183;
            redQuadrant[3] = -242;

            blueQuadrant[0] = -804;
            blueQuadrant[1] = -863;
            blueQuadrant[2] = -103;
            blueQuadrant[3] = -162;

            greenQuadrant[0] = -724;
            greenQuadrant[1] = -783;
            greenQuadrant[2] = -103;
            greenQuadrant[3] = -162;

            orangeQuadrant[0] = -724;
            orangeQuadrant[1] = -783;
            orangeQuadrant[2] = -183;
            orangeQuadrant[3] = -242;
	    }
	    
	    teamSize = config.getInt("team-size");

        if (announcerState) {
	    Announcer a = new Announcer();
	    
	    //My CC3.0 Attribution license requires you to leave this in some way
	    //If you have forked it you can say...
	    //"This server runs MyFork by Me based on AutoWalls by Jkush321" or something similar
	    String[] announcements = config.getString("announcements").split(";");
	    Announcer.messages.add("This server runs AutoWalls by jkush321");
	    for (String s : announcements)
	    {
	    	Announcer.messages.add(s);
	    }
	        announcer = new Thread(a);
	        announcer.start();
        }

        if (heartbeatState) {
	    beat = new Thread(new Heartbeat());
	    beat.start();
        }
	    
	    joinTimer = new Thread(new JoinTimer());
	    joinTimer.start();

		dropper = new Thread(new WallDropper());
		dropper.start();
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){
			public void run()
			{
				ColorCycler.tick();
			}
		}, 0L, 20L);
		
		Grenades.init();
		KitManager.fillKits();
		
		if (Bukkit.getPluginManager().getPlugin("TagAPI")!= null)
		{
			Bukkit.getPluginManager().registerEvents(new ColoredNames(), this);
			Tags.useTagAPI=true;
			System.out.println("[AutoWalls] Successfully hooked into TagAPI!");
		}
		if (Bukkit.getPluginManager().getPlugin("TabAPI")!=null)
		{
			useTabApi=true;
			System.out.println("[AutoWalls] Successfully hooked into TabAPI!");
		}
		else if (useTabApi)
		{
			System.out.println("[AutoWalls] Error! TabAPI is not installed but it was set to be used in the config!");
			useTabApi = false;
		}
	}

    @Override
    @SuppressWarnings("Deprecated")
	public void onDisable() {
        if (announcerState){
		announcer.stop();
        }
        if (heartbeatState) {
            beat.stop();
        }
		dropper.stop();
	}

	
	public void joinTeam(Player p, String team)
	{
		if (playing.contains(p)) {p.sendMessage(ChatColor.RED + "You are already on a team!"); }
		else
		{
			if (team == "red")
			{
				if (redTeam.size() == teamSize)
				{
					p.sendMessage(ChatColor.RED + "That team is full!"); return;
				}
				redTeam.add(p);
			}
			if (team == "blue")
			{
				if (blueTeam.size() == teamSize)
				{
					p.sendMessage(ChatColor.RED + "That team is full!"); return;
				}
				blueTeam.add(p);
			}
			if (team == "green")
			{
				if (greenTeam.size() == teamSize)
				{
					p.sendMessage(ChatColor.RED + "That team is full!"); return;
				}
				greenTeam.add(p);
			}
			if (team == "orange")
			{
				if (orangeTeam.size() == teamSize)
				{
					p.sendMessage(ChatColor.RED + "That team is full!"); return;
				}
				orangeTeam.add(p);
			}
			playing.add(p);
			p.setAllowFlight(false);
			removeDeadPlayer(p.getName());
			Tabs.updateAll();
			Tags.refreshPlayer(p);
			Bukkit.broadcastMessage(ChatColor.RED + p.getName() + " has joined the " + team + " team!");
			int remaining = (teamSize * 4) - playing.size();
			String s = "s";
			if (remaining == 1) s = "";
			Bukkit.broadcastMessage(ChatColor.AQUA + "There is room for " + remaining + " more player" + s + "!");
			if (remaining == 0 && !gameInProgress)
			{
				Bukkit.broadcastMessage(ChatColor.GREEN + "It is time for the game to start! " + ChatColor.RED + "Go be the best you can be now!");
				startGame();
			}
			if (gameInProgress && lateJoins)
			{
				if (team.equals("red"))
				{
					p.teleport(new Location(p.getWorld(), redSpawn[0], redSpawn[1], redSpawn[2]));
                    p.setGameMode(GameMode.SURVIVAL);
                    for (Player pl : Bukkit.getOnlinePlayers())
                    {
                        if (p != pl && !playing.contains(p)) p.hidePlayer(pl);
                    }
				}
				else if (team.equals("blue"))
				{
					p.teleport(new Location(p.getWorld(), blueSpawn[0], blueSpawn[1], blueSpawn[2]));
                    p.setGameMode(GameMode.SURVIVAL);
                    for (Player pl : Bukkit.getOnlinePlayers())
                    {
                        if (p != pl && !playing.contains(p)) p.hidePlayer(pl);
                    }
				}
				else if (team.equals("orange"))
				{
					p.teleport(new Location(p.getWorld(), orangeSpawn[0], orangeSpawn[1], orangeSpawn[2]));
                    p.setGameMode(GameMode.SURVIVAL);
                    for (Player pl : Bukkit.getOnlinePlayers())
                    {
                        if (p != pl && !playing.contains(p)) p.hidePlayer(pl);
                    }
				}
				else if (team.equals("green"))
				{
					p.teleport(new Location(p.getWorld(), greenSpawn[0], greenSpawn[1], greenSpawn[2]));
                    p.setGameMode(GameMode.SURVIVAL);
                    for (Player pl : Bukkit.getOnlinePlayers())
                    {
                        if (p != pl && !playing.contains(p)) p.hidePlayer(pl);
                    }
				}
				p.sendMessage(ChatColor.YELLOW + "It is too late to receive a kit!");
				
				p.sendMessage(ChatColor.YELLOW + "Good Luck!");
			}
			p.setHealth(20);
			p.setFoodLevel(20);
			p.setExp(0);
			p.setLevel(0);
			p.setNoDamageTicks(60);
		}
	}
	public void leaveTeam(Player p)
	{
		if (playing.contains(p)) playing.remove(p);
		if (redTeam.contains(p)) redTeam.remove(p);
		if (blueTeam.contains(p)) blueTeam.remove(p);
		if (greenTeam.contains(p)) greenTeam.remove(p);
		if (orangeTeam.contains(p)) orangeTeam.remove(p);
		if (TeamChat.teamChatting.contains(p)) TeamChat.teamChatting.remove(p);
		if (WallDropper.time > 0 && gameInProgress && lateJoins) { Bukkit.broadcastMessage(ChatColor.DARK_GREEN + "A player with " + lateJoinPriority + "+ priority may " + ChatColor.YELLOW + "/join and take " + p.getName() + "'s place!"); }
		for (Player pl : Bukkit.getOnlinePlayers())
		{
			if (pl!=p)
			{
				if (!p.canSee(pl))
					p.showPlayer(pl);
			}
		}
		Tags.refreshPlayer(p);
		Tabs.updateAll();
		checkStats();
	}
	public void startGame()
	{
		if (gameInProgress) return;
		if (!redTeam.isEmpty())
			for (Player p : redTeam)
			{
				p.teleport(new Location(p.getWorld(),redSpawn[0],redSpawn[1],redSpawn[2]));
			}
		if (!blueTeam.isEmpty())
			for (Player p : blueTeam)
			{
				p.teleport(new Location(p.getWorld(),blueSpawn[0],blueSpawn[1],blueSpawn[2]));
			}
		if (!greenTeam.isEmpty())
			for (Player p : greenTeam)
			{
				p.teleport(new Location(p.getWorld(),greenSpawn[0],greenSpawn[1],greenSpawn[2]));
			}
		if (!orangeTeam.isEmpty())
			for (Player p : orangeTeam)
			{
				p.teleport(new Location(p.getWorld(),orangeSpawn[0],orangeSpawn[1],orangeSpawn[2]));
			}
		for (Player p : playing)
		{
            p.setGameMode(GameMode.SURVIVAL);
            for (Player pl : Bukkit.getOnlinePlayers())
            {
                if (p != pl && !playing.contains(p)) p.hidePlayer(pl);
            }
            p.sendMessage(ChatColor.DARK_AQUA + "To chat only with your teammates, enable team chatting with " + ChatColor.GREEN + "/tc" + ChatColor.DARK_AQUA + ".");
			p.sendMessage(ChatColor.YELLOW + "Good Luck!");
			if (KitManager.getKit(p) != null)
			{
				p.getInventory().addItem(KitManager.getKit(p).getItemStack());
			}
		}
		for (Player p : Bukkit.getOnlinePlayers())
		{
			if (!playing.contains(p))
			{
				spectate(p);
			}
		}
        gameInProgress=true;
	}
	public void endGame(String team, String players)
	{
		if (!gameInProgress) return;
		gameInProgress=false;
		gameOver=true;
		try {
			Thread.sleep(1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (Player p : playing)
		{
			p.setHealth(0);
			Tags.refreshPlayer(p);
		}
		if (mapVotes)
		{
			Bukkit.broadcastMessage(ChatColor.DARK_RED + "The " + team + " team has won the game!");
			Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "Winning Players:  "+ ChatColor.DARK_GREEN + players);
			try { Thread.sleep(1000); } catch (Exception e) { }
			Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "It is time to vote for the next map!");
			Bukkit.broadcastMessage(ChatColor.YELLOW + "1 - The Walls   - by Hypixel - Modified by staff team");
			Bukkit.broadcastMessage(ChatColor.YELLOW + "2 - The Walls 2 - by Hypixel - Modified by staff team");
			Bukkit.broadcastMessage(ChatColor.GRAY + "Type the number you want in chat. Vote will last 30 seconds");
			
			voting = true;
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(this, new VoteResult(), 20L * 30L);
		}
		else
		{
			for (Player p : Bukkit.getOnlinePlayers())
			{
				p.kickPlayer(ChatColor.RED + "The " + team + " team has won the game! " + ChatColor.DARK_AQUA + "Reconnect and type /join");
				Bukkit.shutdown();
			}
		}
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent e)
	{
		if (playing.contains(e.getPlayer()) && gameInProgress) e.getPlayer().setHealth(0);
		else if (playing.contains(e.getPlayer()) && !gameInProgress) leaveTeam(e.getPlayer());
		if (getLastEvent(e.getPlayer()) != 0) lastEvent.remove(e.getPlayer());
		checkStats();
		Tags.refreshPlayer(e.getPlayer());
		Tabs.removePlayer(e.getPlayer());
		e.setQuitMessage(ChatColor.AQUA + "- " + ChatColor.DARK_AQUA + e.getPlayer().getName() + ChatColor.GRAY + " has left");
	}
	@EventHandler
	public void onDeath(PlayerDeathEvent e)
	{
		try{ 
		if (!playing.contains(e.getEntity())) {
			e.setDeathMessage(""); 
			if (e.getEntity().getInventory().getSize() > 0)
			{
				while (e.getDrops().size()>0)
					e.getDrops().remove(0);
			}
			return;
		}
		if (gameInProgress && playing.contains(e.getEntity()))
		{
			playing.remove(e.getEntity());
			if (redTeam.contains(e.getEntity())) redTeam.remove(e.getEntity());
			if (blueTeam.contains(e.getEntity())) blueTeam.remove(e.getEntity());
			if (greenTeam.contains(e.getEntity())) greenTeam.remove(e.getEntity());
			if (orangeTeam.contains(e.getEntity())) orangeTeam.remove(e.getEntity());
			if (TeamChat.teamChatting.contains(e.getEntity())) TeamChat.teamChatting.remove(e.getEntity());
			if (playing.size()>1)
				e.setDeathMessage(ChatColor.YELLOW + e.getEntity().getName() + ChatColor.DARK_RED + " " + e.getDeathMessage().split(e.getEntity().getName() + " ")[1] + ChatColor.DARK_GREEN + " " + playing.size() + " Players Remain");
			createGrave(e.getEntity().getLocation(), e.getEntity().getName());
			checkStats();
			Tags.refreshPlayer(e.getEntity());
			addDeadPlayer(e.getEntity().getName());
			Tabs.updateAll();
		}
		} catch (Exception ex) { ex.printStackTrace(); }
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onLogin(PlayerLoginEvent e)
	{	
		if (config.isSet("votes.players." + e.getPlayer().getName()) && config.getInt("votes.players." + e.getPlayer().getName()) >= 20) { e.getPlayer().setDisplayName(ChatColor.DARK_AQUA + e.getPlayer().getName() + ChatColor.WHITE); }
		if (config.isSet("votes.players." + e.getPlayer().getName()) && config.getInt("votes.players." + e.getPlayer().getName()) >= 250) { e.getPlayer().setDisplayName(ChatColor.DARK_RED + e.getPlayer().getName() + ChatColor.WHITE); }
		
		if (config.getBoolean("priorities") == true)
		{
			if (config.isSet("votes.players." + e.getPlayer().getName())) { e.getPlayer().setDisplayName(ChatColor.YELLOW + "[" + config.getInt("votes.players." + e.getPlayer().getName()) + "]" + ChatColor.GRAY + e.getPlayer().getDisplayName() + ChatColor.WHITE); }
			else e.getPlayer().setDisplayName(ChatColor.YELLOW + "[0]" + ChatColor.GRAY + e.getPlayer().getDisplayName() + ChatColor.WHITE);
		}
		if (e.getPlayer().hasPermission("walls.op")) e.getPlayer().setDisplayName(ChatColor.DARK_BLUE + "[" + ChatColor.DARK_GREEN + "Admin" + ChatColor.DARK_BLUE + "]" + ChatColor.DARK_RED + e.getPlayer().getName() + ChatColor.GRAY + ChatColor.WHITE);
		if (config.isSet("prefix." + e.getPlayer().getName())) e.getPlayer().setDisplayName(        ChatColor.translateAlternateColorCodes('&', config.getString("prefix." + e.getPlayer().getName())).replace("{pri}", config.getInt("votes.players." + e.getPlayer().getName())+"") + e.getPlayer().getName() + ChatColor.WHITE);

		if (Bukkit.getOnlinePlayers().length == Bukkit.getMaxPlayers())
		{
			if (config.isSet("votes.players." + e.getPlayer().getName()) && (config.getBoolean("priorities") || config.getInt("votes.players." + e.getPlayer().getName()) > 5))
			{
				int pl = config.getInt("votes.players." + e.getPlayer().getName());
				int l = 999999;
				Player low = null;
				for (int i = Bukkit.getOnlinePlayers().length -  1; i > -1; i--)
				{
					Player p = Bukkit.getOnlinePlayers()[i];
					if (!playing.contains(p))
					{
						if (!config.isSet("votes.players." + p.getName()))
						{
							p.kickPlayer(priorityKickMessage);
							if (!e.getPlayer().isBanned()) 
								if ((Bukkit.hasWhitelist() && e.getPlayer().isWhitelisted()) || !Bukkit.hasWhitelist())
									e.allow();
							return;
						}
						if (config.getInt("votes.players." + p.getName()) < l)
						{
							low = p;
							l = config.getInt("votes.players." + p.getName());
						}
					}
				}
				if (pl > l) { low.kickPlayer("Someone with higher priority joined!"); /*e.allow();*/ return; }
				
			}
			e.disallow(Result.KICK_FULL, fullKickMessage);
		}
				
		e.getPlayer().getInventory().clear();
		e.getPlayer().getInventory().setArmorContents(new ItemStack[]{new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.AIR)});
		
	}
	@EventHandler
	public void onJoin(PlayerJoinEvent e)
	{
		e.setJoinMessage(ChatColor.AQUA + "+ " + ChatColor.DARK_AQUA + e.getPlayer().getName() + ChatColor.GRAY + " is now online");
		if (gameInProgress) {
			spectate(e.getPlayer());
			for (Player p : playing)
			{
				p.hidePlayer(e.getPlayer());
			}
		}
        e.getPlayer().setGameMode(GameMode.ADVENTURE);
		if (e.getPlayer().hasPermission("walls.op"))
		{
			UpdateChecker.checkAndSendMessage(e.getPlayer());
		}
		Tabs.addPlayer(e.getPlayer());
	}
	public void checkStats()
	{
		if (!gameInProgress) return;
		
		if (redTeam.size()==playing.size())
		{
			String s = "";
			for (Player p : redTeam)
			{
				s += (ChatColor.GRAY + p.getDisplayName() + ChatColor.GRAY + ", ");
			}
			s=s.substring(0, s.length() - 4);
			endGame("red", s);
		}
		else if (blueTeam.size()==playing.size())
		{
			String s = "";
			for (Player p : blueTeam)
			{
				s += (ChatColor.GRAY + p.getDisplayName() + ChatColor.GRAY + ", ");
			}
			s=s.substring(0, s.length() - 4);
			endGame("blue", s);
		}
		else if (greenTeam.size()==playing.size())
		{
			String s = "";
			for (Player p : greenTeam)
			{
				s += (ChatColor.GRAY + p.getDisplayName() + ChatColor.GRAY + ", ");
			}
			s=s.substring(0, s.length() - 4);
			endGame("green", s);
		}
		else if (orangeTeam.size()==playing.size())
		{
			String s = "";
			for (Player p : orangeTeam)
			{
				s += (ChatColor.GRAY + p.getDisplayName() + ChatColor.GRAY + ", ");
			}
			s=s.substring(0, s.length() - 4);
			endGame("orange", s);
		}
	}
	public void spectate(Player p)
	{
		p.setAllowFlight(true);
		p.sendMessage(ChatColor.YELLOW + "You are now spectating!");
		p.sendMessage(ChatColor.YELLOW + "You can enable flying with /fly");
		p.setGameMode(GameMode.ADVENTURE);
	}
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e)
	{
        Player p = e.getPlayer();
		if (e.getPlayer().hasPermission("walls.op")) return;
		if (!playing.contains(e.getPlayer())) e.setCancelled(true);
		if (!gameInProgress) e.setCancelled(true);
		if (mapNumber==1)
		{
			if (e.getBlock().getX()==347) e.setCancelled(true);
			if (e.getBlock().getZ()==-793) e.setCancelled(true);
			if (e.getBlock().getX()>408) e.setCancelled(true);
			if (e.getBlock().getZ()<-853) e.setCancelled(true);
			if (e.getBlock().getX()<286) e.setCancelled(true);
			if (e.getBlock().getZ()>-731) e.setCancelled(true);
			if (e.getBlock().getY() > 137) {e.setCancelled(true); e.getPlayer().sendMessage(ChatColor.RED + "You can't build over the height limit. This prevents getting over walls."); }
		}
		else if (mapNumber ==2)
		{
			if (e.getBlock().getZ()==-182) e.setCancelled(true);
			if (e.getBlock().getZ()==-164) e.setCancelled(true);
			if (e.getBlock().getX()==-785) e.setCancelled(true);
			if (e.getBlock().getX()==-803) e.setCancelled(true);
			if (e.getBlock().getZ()>-103) e.setCancelled(true);
			if (e.getBlock().getX()<-863) e.setCancelled(true);
			if (e.getBlock().getX()>-725) e.setCancelled(true);
			if (e.getBlock().getZ()<-243) e.setCancelled(true);
			if (e.getBlock().getY() > 95) {e.setCancelled(true); e.getPlayer().sendMessage(ChatColor.RED + "You can't build over the heigt limit. This prevents getting over walls."); }
		}
		if (e.getBlock() instanceof Sign)
		{
			if (graves.contains((Sign) e.getBlock()))
			{
				e.setCancelled(true);
				e.getPlayer().sendMessage(ChatColor.AQUA + "You can not touch this grave!");
			}
		}
    }
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e)
	{
		if (e.getPlayer().hasPermission("walls.op")) return;
		if (!playing.contains(e.getPlayer())) e.setCancelled(true);
		if (!gameInProgress) e.setCancelled(true);
		if (mapNumber==1)
		{
			if (e.getBlock().getX()==347) e.setCancelled(true);
			if (e.getBlock().getZ()==-793) e.setCancelled(true);
			if (e.getBlock().getX()>408) e.setCancelled(true);
			if (e.getBlock().getZ()<-853) e.setCancelled(true);
			if (e.getBlock().getX()<286) e.setCancelled(true);
			if (e.getBlock().getZ()>-731) e.setCancelled(true);
			if (e.getBlock().getY() > 138) {e.setCancelled(true); e.getPlayer().sendMessage(ChatColor.RED + "You can't build over the heigt limit. This prevents getting over walls."); }
		}
		else if (mapNumber ==2)
		{
			if (e.getBlock().getZ()==-182) e.setCancelled(true);
			if (e.getBlock().getZ()==-164) e.setCancelled(true);
			if (e.getBlock().getX()==-785) e.setCancelled(true);
			if (e.getBlock().getX()==-803) e.setCancelled(true);
			if (e.getBlock().getZ()>-103) e.setCancelled(true);
			if (e.getBlock().getX()<-863) e.setCancelled(true);
			if (e.getBlock().getX()>-725) e.setCancelled(true);
			if (e.getBlock().getZ()<-243) e.setCancelled(true);
			if (e.getBlock().getY() > 94) {e.setCancelled(true); e.getPlayer().sendMessage(ChatColor.RED + "You can't build over the heigt limit. This prevents getting over walls."); }
		}
	}
	@EventHandler
	public void onRespawn(PlayerRespawnEvent e)
	{
		if (gameInProgress) spectate(e.getPlayer());
	}

	@EventHandler(priority=EventPriority.HIGHEST)
	public void onChat(AsyncPlayerChatEvent e)
	{
		setLastEventToNow(e.getPlayer());
		if (WallDropper.timeContinued < 0 && WallDropper.timeContinued >= -30 && (e.getMessage().toLowerCase().contains(" lag") || e.getMessage().toLowerCase().startsWith("lag")))
		{
			e.setCancelled(true);
			e.getPlayer().sendMessage(ChatColor.DARK_GREEN + "Please do not send messages about lag while the walls are falling ;)");
		}
		else if (voting)
		{
			if (e.getMessage().trim().length() == 1)
			{
				if (e.getMessage().trim().equals("1"))
				{
					if (votedFor1.contains(e.getPlayer())) { e.getPlayer().sendMessage(ChatColor.GRAY + "You have already voted for that map!"); e.setCancelled(true); return; }
					if (votedFor2.contains(e.getPlayer())) { e.getPlayer().sendMessage(ChatColor.GRAY + "Your vote for map 2 has been deleted!"); votedFor2.remove(e.getPlayer()); }
					votedFor1.add(e.getPlayer());
					e.getPlayer().sendMessage(ChatColor.GRAY + "You have successfully voted for map 1!");
					e.setCancelled(true);
				}
				else if (e.getMessage().trim().equals("2"))
				{
					if (votedFor2.contains(e.getPlayer())) { e.getPlayer().sendMessage(ChatColor.GRAY + "You have already voted for that map!"); e.setCancelled(true); return; }
					if (votedFor1.contains(e.getPlayer())) { e.getPlayer().sendMessage(ChatColor.GRAY + "Your vote for map 1 has been deleted!"); votedFor1.remove(e.getPlayer()); }
					votedFor2.add(e.getPlayer());
					e.getPlayer().sendMessage(ChatColor.GRAY + "You have successfully voted for map 2!");
					e.setCancelled(true);
				}
				else e.getPlayer().sendMessage(ChatColor.GRAY + "Invalid input, type a 1 or a 2.");
			}
		}
		
		if (!e.isCancelled())
		{
			e.setMessage(e.getPlayer().getDisplayName() + ": " + e.getMessage());
			e.setCancelled(TeamChat.say(e.getPlayer(), e.getMessage()));
		}
	}
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onDamage(EntityDamageByEntityEvent e)
	{
		if (e.isCancelled()) return;		
		
		//no spectators hitting animals
		if (!(e.getEntity() instanceof Player)) { if (e.getDamager() instanceof Player) { if (!playing.contains((Player) e.getDamager())) e.setCancelled(true); return; } } 
		
		//no arrows shot at spectators
		if (e.getEntity() instanceof Player) { if (!playing.contains((Player) e.getEntity()) && e.getDamager().getType().equals(EntityType.ARROW)) { e.setCancelled(true); return; } }
		
		if (e.getDamager().getType().equals(EntityType.ARROW) && e.getEntity() instanceof Player)
		{
			if (playing.contains((Player) e.getEntity()))
			{
				Arrow arrow = (Arrow) e.getDamager();
				if (arrow.getShooter() instanceof Player)
				{
					Player d = (Player) arrow.getShooter();
					if (redTeam.contains((Player)e.getEntity()) && redTeam.contains(d)) { d.sendMessage(ChatColor.RED + "You can not team kill!"); e.setCancelled(true); return; }
					if (blueTeam.contains((Player)e.getEntity()) && blueTeam.contains(d)) { d.sendMessage(ChatColor.RED + "You can not team kill!"); e.setCancelled(true); return; }
					if (greenTeam.contains((Player)e.getEntity()) && greenTeam.contains(d)) { d.sendMessage(ChatColor.RED + "You can not team kill!"); e.setCancelled(true); return; }
					if (orangeTeam.contains((Player)e.getEntity()) && orangeTeam.contains(d)) { d.sendMessage(ChatColor.RED + "You can not team kill!"); e.setCancelled(true); return; }
				}
			}
		}
		if (!(e.getDamager() instanceof Player)) return;
		if (!(e.getEntity() instanceof Player)) return;
		
		Player p = (Player) e.getEntity();
		Player damager = (Player) e.getDamager();
		
		setLastEventToNow(p);
		
		if (!playing.contains(p) && playing.contains(damager)) { damager.sendMessage(ChatColor.RED + "There is a spectator there, don't hurt it"); e.setCancelled(true); return; } 
		if (!playing.contains(damager) && playing.contains(p)) { e.setCancelled(true); damager.sendMessage(ChatColor.RED + "You Are Not In This Fight!"); return; }
		
		if (!playing.contains(p) && !playing.contains(damager))
		{
			/*if (p.getLocation().getBlockX() <= 357 && p.getLocation().getBlockX() >= 337 && p.getLocation().getBlockZ() >= -804 && p.getLocation().getBlockZ() <= -782 && p.getLocation().getBlockY() >= 152 && p.getLocation().getBlockY() <= 155)
			{
				if (damager.getLocation().getBlockX() <= 357 && damager.getLocation().getBlockX() >= 337 && damager.getLocation().getBlockZ() >= -804 && damager.getLocation().getBlockZ() <= -782 && damager.getLocation().getBlockY() >= 152 && damager.getLocation().getBlockY() <= 155)
				{
					return;
				}
			}*/
			e.setCancelled(true); //damager.sendMessage("�cIf you want to fight do it in the area above spawn");
		}
		
		if (redTeam.contains(p) && redTeam.contains(damager)) { e.setCancelled(true); damager.sendMessage(ChatColor.RED + "You Can Not Team Kill!"); return; }
		if (blueTeam.contains(p) && blueTeam.contains(damager)) { e.setCancelled(true); damager.sendMessage(ChatColor.RED + "You Can Not Team Kill!"); return; }
		if (greenTeam.contains(p) && greenTeam.contains(damager)) { e.setCancelled(true); damager.sendMessage(ChatColor.RED + "You Can Not Team Kill!"); return; }
		if (orangeTeam.contains(p) && orangeTeam.contains(damager)) { e.setCancelled(true); damager.sendMessage(ChatColor.RED + "You Can Not Team Kill!"); return; }
		if (WallDropper.time > 0 && playing.contains(p) && playing.contains(damager)) { damager.sendMessage(ChatColor.RED + "The walls haven't dropped yet! Why are you hitting " + p.getName() + "?"); e.setCancelled(true); return; }
	}
	@EventHandler
	public void onDroppedItem(PlayerDropItemEvent e)
	{
		setLastEventToNow(e.getPlayer());
		if (!playing.contains(e.getPlayer()) && !e.getPlayer().hasPermission("walls.op")) e.setCancelled(true);
	}
	@EventHandler 
	public void onPickUp(PlayerPickupItemEvent e)
	{
		if (!playing.contains(e.getPlayer())) e.setCancelled(true);
	}
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onInteract(PlayerInteractEvent e)
	{
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK)
		{
			if (e.getClickedBlock().getType() == Material.SIGN || e.getClickedBlock().getType() == Material.SIGN_POST)
			{
				Sign s = (Sign) e.getClickedBlock().getState();
				SignUI.onClick(e.getPlayer(), s.getLine(0), s.getLine(1), s.getLine(2), s.getLine(3));
			}
		}
		if (playing.contains(e.getPlayer()) && (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK))
		{
			if (e.getPlayer().getItemInHand() != null)
			{
				if (e.getPlayer().getItemInHand().getType() == Material.NETHER_STAR)
				{
					if (ColorCycler.colorTime.containsKey(e.getPlayer()))
					{
						if (ColorCycler.colorTime.get(e.getPlayer()) == 0)
						{
							e.getPlayer().sendMessage(ChatColor.RED + "Your ability to do that has worn off!");
						}
						else
						{
							ColorCycler.cycle(e.getPlayer());
						}
					}
					else
					{
						ColorCycler.cycle(e.getPlayer());
					}
				}
				else if (e.getPlayer().getItemInHand().getType() == Material.SNOW_BALL)
				{
					if (e.getPlayer().getItemInHand().getItemMeta().hasDisplayName())
					{
						if (e.getPlayer().getItemInHand().getItemMeta().getDisplayName().contains("Basic"))
						{
							e.getPlayer().setMetadata("last-grenade", new FixedMetadataValue(this, "basic"));
							System.out.println("Meep");
						}
					}
					else
					{
						if (e.getPlayer().hasMetadata("last-grenade")) e.getPlayer().removeMetadata("last-grenade", this);
					}
				}
				else if (e.getPlayer().getItemInHand().getType() == Material.ENDER_PEARL && WallDropper.time > 0)
				{
					e.getPlayer().sendMessage(ChatColor.RED + "You can not do that until the walls fall!");
					e.setCancelled(true);
				}
			}
		}
		if (e.getPlayer().hasPermission("walls.op")) { e.setCancelled(false); return; }
		if ((e.getPlayer().getLocation().getBlockY() > 139 && mapNumber == 1) || (e.getPlayer().getLocation().getBlockY() > 125 && mapNumber == 2))
		{
			e.setCancelled(false);
			return;
		}
		else
		{
			if (playing.contains(e.getPlayer()))
			{
				setLastEventToNow(e.getPlayer());
				if ((e.getPlayer().getItemInHand() != null && e.getAction() == Action.RIGHT_CLICK_BLOCK) || e.getAction() == Action.LEFT_CLICK_BLOCK)
				{
					if (e.getPlayer().getItemInHand().getType() != Material.AIR)
					{
						for (Player p : Bukkit.getOnlinePlayers())
						{
							if (!playing.contains(p))
							{
								if (p.getLocation().distance(e.getClickedBlock().getLocation()) <= 2)
								{
									p.teleport(p.getLocation().add(new Location(p.getWorld(), 0, 2, 0)));
									p.sendMessage(ChatColor.YELLOW + "You have been moved over to allow " + e.getPlayer().getName() + " to place a block");
								}
							}
						}
					}
					if (e.getPlayer().getItemInHand().getType() != Material.FLINT_AND_STEEL && e.getPlayer().getItemInHand().getType() == Material.FIREBALL && WallDropper.time > 0 && preventFireBeforeWallsFall)
					{
						e.getPlayer().sendMessage(ChatColor.DARK_RED + "You can't place fire until the walls have fallen!");
						e.setCancelled(true);
					}
				}
			}
		}
		if (!gameInProgress) e.setCancelled(true);
		if (!playing.contains(e.getPlayer())) {e.setCancelled(true);}
	}
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onEntitySpawn(CreatureSpawnEvent e)
	{
		if (e.getEntity().getType().equals(EntityType.CREEPER) || e.getEntity().getType().equals(EntityType.ENDERMAN) || e.getEntity().getType().equals(EntityType.SLIME) || e.getEntity().getType().equals(EntityType.SKELETON) || e.getEntity().getType().equals(EntityType.SPIDER) || e.getEntity().getType().equals(EntityType.ZOMBIE)) e.setCancelled(true);
	}
	@EventHandler
	public void onPing(ServerListPingEvent e)
	{
		String message = "AutoWalls Server";
		if (!gameInProgress && !gameOver)
		{
			message=(ChatColor.DARK_GREEN + "Getting ready to start!");
		}
		else if (gameInProgress && WallDropper.time > 0)
		{
			int mins = WallDropper.time / 60;
			int secs = WallDropper.time % 60;
			message=(ChatColor.DARK_GREEN + "Walls drop in "+ ChatColor.YELLOW + mins + ChatColor.DARK_RED + " mins, " + ChatColor.YELLOW + secs + ChatColor.DARK_RED + " secs!");
		}
		else if (gameInProgress)
		{
			message=(ChatColor.YELLOW + "" + playing.size() + ChatColor.DARK_RED + " players alive!");
		}
		else if (gameOver && !voting)
		{
			message=ChatColor.DARK_GREEN + "Game has ended!";
		}
		else {
			message=ChatColor.DARK_AQUA + "Voting for the next map!";
		}
		e.setMotd(message);
	}
    @EventHandler( priority = EventPriority.HIGHEST, ignoreCancelled = true )
    public void onWeatherChange( WeatherChangeEvent event )
    {
        if( event.toWeatherState( ) )
        {
            event.setCancelled( true );
        }
    }

    @EventHandler( priority = EventPriority.HIGHEST, ignoreCancelled = true )
    public void onThunderChange( ThunderChangeEvent event )
    {
        if( event.toThunderState( ) )
        {
            event.setCancelled( true );
        }
    }

    @EventHandler( priority = EventPriority.HIGHEST, ignoreCancelled = true )
    public void onLightningStrike( LightningStrikeEvent event )
    {
            event.setCancelled( true );
    }
	@EventHandler
	public void onSneak(PlayerToggleSneakEvent e)
	{
		if (playing.contains(e.getPlayer()) && WallDropper.time<=0 && blockSneaking)
			if (e.isSneaking()==true) e.setCancelled(true);
	}
	@EventHandler
	public void onEat(EntityRegainHealthEvent e)
	{
		if (e.getEntity() instanceof Player)
		{
			if (playing.contains((Player) e.getEntity()) && disableHealing && WallDropper.time<=0) { 
				Random r = new Random();
				e.setAmount(r.nextInt( (20 - ((Player)e.getEntity()).getHealth()) / 2 )); 
			} 
		}
	}
	@EventHandler
	public void onProjectileLand(ProjectileHitEvent e)
	{
		if (e.getEntityType() == EntityType.ARROW && arrowLightning)
		{
			if (e.getEntity().getShooter() != null)
			{
				if (e.getEntity().getShooter() instanceof Player)
				{
					Player shooter = (Player) e.getEntity().getShooter();
					if (WallDropper.time <= 0)
					{
						Random r = new Random();
						int rand = r.nextInt(arrowLightningChance);
						if (rand==0)
						{
							Bukkit.broadcastMessage(ChatColor.DARK_RED + shooter.getName() + ChatColor.RED + " Has Shot A Rare Lightning Arrow!");
							e.getEntity().getWorld().strikeLightning(e.getEntity().getLocation());
						}
					}
				}
			}
		}
		else if (e.getEntity().getType() == EntityType.SNOWBALL)
		{
			if (e.getEntity().hasMetadata("grenade-type"))
			{
				Grenades.handleLanding(e, e.getEntity());
			}
			//e.getEntity().getWorld().createExplosion(e.getEntity().getLocation(), .8F, true);
		}
	}
	public void createGrave(Location l, String playername)
	{
		Random r = new Random();
		l.getBlock().setType(Material.SIGN_POST);
		l.getBlock().setData((byte) r.nextInt(16));
		Sign s = (Sign) l.getBlock().getState();
		s.setLine(0, "R.I.P.");
		s.setLine(1, playername);
		int i = r.nextInt(graveMessages.size());
		s.setLine(3, graveMessages.get(i));
		s.update();
		graves.add(s);
	}
	@EventHandler
	public void onPistonRetract (BlockPistonRetractEvent e)
	{
		if (e.getRetractLocation().getBlock().getType() == Material.SAND || e.getRetractLocation().getBlock().getType() == Material.GRAVEL) e.setCancelled(true);
	}
	@EventHandler
	public void onPistonExtend (BlockPistonExtendEvent e)
	{
		for (Block b : e.getBlocks())
		{
			if (b.getType()==Material.SAND || b.getType()==Material.GRAVEL) e.setCancelled(true);
		}
	}
	@EventHandler
	public void onExplode (EntityExplodeEvent e)
	{
		List<Block> newList = new ArrayList<Block>();
		newList.addAll(e.blockList());
		
		for (Block b : newList)
		{
			if (b.getType() == Material.SAND || b.getType() == Material.GRAVEL) { e.blockList().remove(b); }
		}
	}
	@EventHandler
	public void onTp (PlayerTeleportEvent e)
	{
		for (Player p : Bukkit.getOnlinePlayers())
		{
			for (Player p2 : playing)
			{
				if (p!=p2 && !playing.contains(p))
				{
					p2.hidePlayer(p);
				}
				else if (p!=p2 && playing.contains(p))
				{
					p2.showPlayer(p);
				}
			}
		}
	}
	public static void setLastEvent(Player p, long millis)
	{
		if (lastEvent.containsKey(p)) lastEvent.remove(p);
		lastEvent.put(p, millis);
	}
	public static void setLastEventToNow(Player p)
	{
		if (lastEvent.containsKey(p)) lastEvent.remove(p);
		lastEvent.put(p, System.currentTimeMillis());
	}
	public static long getLastEvent(Player p)
	{
		if (lastEvent.containsKey(p)) return lastEvent.get(p);
		return 0;
	}
	public static int getTicksFromLastEvent(Player p)
	{
		if (lastEvent.containsKey(p)) return (int)Math.floor((double)((System.currentTimeMillis() - lastEvent.get(p)) / 50));
		return Integer.MAX_VALUE;
	}
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e)
	{
		setLastEventToNow(e.getPlayer());

        Player p = e.getPlayer();

        //FINALLY! Prevent pretty much all forms of cheating by not allowing players to leave their quadrants.

        if (gameInProgress) {
            if (WallDropper.time > 0) {
                //Game must be in progress
                if (playing.contains(p)) {
                    //Only affect players
                    if(redTeam.contains(p)) {
                            if (e.getTo().getX() > redQuadrant[0] || e.getTo().getX() < redQuadrant[1] || e.getTo().getZ() > redQuadrant[2] || e.getTo().getZ() < redQuadrant[3]) {
                                p.sendMessage(ChatColor.RED + "You cannot leave your quadrant now!");
                                p.teleport(new Location(e.getFrom().getWorld(),e.getFrom().getX(),e.getFrom().getY(),e.getFrom().getZ(),e.getFrom().getYaw(),e.getFrom().getPitch()));
                            }

                    }
                    else if (blueTeam.contains(p)) {
                            if (e.getTo().getX() > blueQuadrant[0] || e.getTo().getX() <  blueQuadrant[1] || e.getTo().getZ() > blueQuadrant[2] || e.getTo().getZ() < blueQuadrant[3]) {
                                p.sendMessage(ChatColor.RED + "You cannot leave your quadrant now!");
                                p.teleport(new Location(e.getFrom().getWorld(),e.getFrom().getX(),e.getFrom().getY(),e.getFrom().getZ(),e.getFrom().getYaw(),e.getFrom().getPitch()));
                            }

                    }
                    else if (greenTeam.contains(p)) {
                            if (e.getTo().getX() > greenQuadrant[0] || e.getTo().getX() < greenQuadrant[1] || e.getTo().getZ() > greenQuadrant[2] || e.getTo().getZ() < greenQuadrant[3]) {
                                p.sendMessage(ChatColor.RED + "You cannot leave your quadrant now!");
                                p.teleport(new Location(e.getFrom().getWorld(),e.getFrom().getX(),e.getFrom().getY(),e.getFrom().getZ(),e.getFrom().getYaw(),e.getFrom().getPitch()));
                            }

                    }
                    else if (orangeTeam.contains(p)) {

                            if (e.getTo().getX() > orangeQuadrant[0] || e.getTo().getX() < orangeQuadrant[1] || e.getTo().getZ() > orangeQuadrant[2] || e.getTo().getZ() < orangeQuadrant[3]) {
                                p.sendMessage(ChatColor.RED + "You cannot leave your quadrant now!");
                                p.teleport(new Location(e.getFrom().getWorld(),e.getFrom().getX(),e.getFrom().getY(),e.getFrom().getZ(),e.getFrom().getYaw(),e.getFrom().getPitch()));
                            }

                    }
                }
            }
        }
	}

	@EventHandler
	public void onSignUpdate(SignChangeEvent e)
	{
		if (ChatColor.stripColor(e.getLine(0).trim()).equalsIgnoreCase("[Join]") && !e.getPlayer().hasPermission("walls.op"))
		{
			e.setCancelled(true);
			e.getPlayer().sendMessage(ChatColor.RED + "No placing special signs!");
		}
		if (ChatColor.stripColor(e.getLine(0).trim()).equalsIgnoreCase("[Kit]") && !e.getPlayer().hasPermission("walls.op"))
		{
			e.setCancelled(true);
			e.getPlayer().sendMessage(ChatColor.RED + "No placing special signs!");
		}
	}
	@EventHandler
	public void onProjLaunch(ProjectileLaunchEvent e)
	{
		if (e.getEntity().getShooter().hasMetadata("last-grenade"))
		{
			e.getEntity().setMetadata("grenade-type", new FixedMetadataValue(this, e.getEntity().getShooter().getMetadata("last-grenade").get(0).asString()));
		}
	}
	public static void addDeadPlayer(String name)
	{
		if (!dead.contains(name)) dead.add(name);
		Tabs.updateAll();
	}
	public static void removeDeadPlayer(String name)
	{
		if (dead.contains(name)) dead.remove(name);
		Tabs.updateAll();
	}
}
