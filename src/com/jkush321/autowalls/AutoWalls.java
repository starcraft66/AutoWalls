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
import com.jkush321.autowalls.listeners.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.jkush321.autowalls.kits.KitManager;

public final class AutoWalls extends JavaPlugin {

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
    public static int dropperTask;
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
	public static Map<Player, Long> lastEvent = new ConcurrentHashMap<>();
	public static int secondsBeforeTeleport;
	public final static String version = "1.1r1";
	public static int earlyJoinPriority, lateJoinPriority;
	public static boolean lateJoins;
	public static boolean preventFireBeforeWallsFall;
	public static boolean useTabApi;
	public static ArrayList<String> dead = new ArrayList<String>();
    private final PlayerListener PlayerListener = new PlayerListener(this);
    private final PlayerBlockListener PlayerBlockListener = new PlayerBlockListener(this);
    private final PlayerConnectionListener PlayerConnectionListener = new PlayerConnectionListener(this);
    private final ServerListener ServerListener = new ServerListener(this);
    private final WorldListener WorldListener = new WorldListener(this);

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

        getServer().getPluginManager().registerEvents(PlayerBlockListener, this);
        getServer().getPluginManager().registerEvents(PlayerConnectionListener, this);
        getServer().getPluginManager().registerEvents(PlayerListener, this);
        getServer().getPluginManager().registerEvents(WorldListener, this);
        getServer().getPluginManager().registerEvents(ServerListener, this);
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

        dropperTask = getServer().getScheduler().scheduleSyncRepeatingTask(this, new WallDropper(this),0L, 20L );

        gameInProgress=true;
	}
	public void endGame(String team, String players)
	{
		if (!gameInProgress) return;
		gameInProgress=false;
		gameOver=true;
		for (Player p : playing)
		{
			p.setHealth(0);
			Tags.refreshPlayer(p);
		}
		if (mapVotes)
		{
			Bukkit.broadcastMessage(ChatColor.DARK_RED + "The " + team + " team has won the game!");
			Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "Winning Players:  "+ ChatColor.DARK_GREEN + players);
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
