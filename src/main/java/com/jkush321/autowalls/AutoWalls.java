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
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.avaje.ebean.LogLevel;
import com.jkush321.autowalls.commands.*;
import com.jkush321.autowalls.listeners.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.jkush321.autowalls.kits.KitManager;
import org.bukkit.scheduler.BukkitRunnable;

public final class AutoWalls extends JavaPlugin {

	public static Plugin plugin = Bukkit.getPluginManager().getPlugin("AutoWalls");
	public static final Logger logger = Logger.getLogger("Minecraft");
	public static List<Player> playing = new CopyOnWriteArrayList<Player>();
	public static List<Player> redTeam = new CopyOnWriteArrayList<Player>();
	public static List<Player> blueTeam = new CopyOnWriteArrayList<Player>();
	public static List<Player> greenTeam = new CopyOnWriteArrayList<Player>();
	public static List<Player> orangeTeam = new CopyOnWriteArrayList<Player>();
	public static Map<Player, Integer> votes = new HashMap<Player, Integer>();
	public static boolean gameInProgress = false;
	public static boolean voting = false;
    public static boolean deathmatches = true;
	public static FileConfiguration config;
	public static boolean gameOver = false;
    public static int mapNumber;
    public static int timerTask;
	public static int joinTimerTask;
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
    public static int CONFIG_VERSION = 1;
    public static boolean config_todate;
	public static ArrayList<String> dead = new ArrayList<String>();
    private final PlayerListener PlayerListener = new PlayerListener(this);
    private final PlayerBlockListener PlayerBlockListener = new PlayerBlockListener(this);
    private final PlayerConnectionListener PlayerConnectionListener = new PlayerConnectionListener(this);
    private final ServerListener ServerListener = new ServerListener(this);
    private final WorldListener WorldListener = new WorldListener(this);
    private static File datafolder;
    public static Arena arena = Arena.getInstance();

    @Override
	public void onEnable()
	{
		plugin = this;
        datafolder = this.getDataFolder();

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

		/*config.addDefault("votes.players.jkush321", 500);
		config.addDefault("votes.players.example_player", 2);
		config.addDefault("priorities", true);
		config.addDefault("next-map", 1);
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
        config.addDefault("deathmatches", true);
        config.addDefault("time-until-deathmatch-in-minutes", 10);
		config.addDefault("vote-link", "my-vote-link.com");
		config.addDefault("priority-per-dollar", 5);
		config.addDefault("seconds-before-teleport", 3);
		config.addDefault("early-join-priority", 1);
		config.addDefault("late-join-priority", 25);
		config.addDefault("late-joins", true);
		config.addDefault("prevent-fire-before-walls-fall", true);
		config.addDefault("max-color-cycler-time", 120);
		config.addDefault("use-tab-api", true);
        config.addDefault("CONFIG_VERSION", 1);*/
		
		//config.options().copyDefaults(true);
	    saveConfig();
        ConfigurationHelper ch = ConfigurationHelper.getInstance();
        ch.setup(this);

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
        deathmatches = config.getBoolean("deathmatches");
	    Timer.time = ch.getGameLength(config.getInt("next-map"));
	    votelink = config.getString("vote-link");
	    priorityPerDollar=config.getInt("priority-per-dollar");
	    secondsBeforeTeleport=config.getInt("seconds-before-teleport");
	    earlyJoinPriority = config.getInt("early-join-priority");
	    lateJoinPriority = config.getInt("late-join-priority");
	    lateJoins = config.getBoolean("late-joins");
	    preventFireBeforeWallsFall = config.getBoolean("prevent-fire-before-walls-fall");
	    ColorCycler.MAX_COLOR_TIME = config.getInt("max-color-cycler-time");
	    useTabApi = config.getBoolean("use-tab-api");

        arena.loadArenaSettings(mapNumber);
	    
	    //teamSize = config.getInt("team-size");
	    
	    joinTimerTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new JoinTimer(), 0L, 20L);
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){
			public void run()
			{
				ColorCycler.tick();
			}
		}, 0L, 20L);
		
		Grenades.init();
		KitManager.fillKits();
        Tabs.updateAll();
		
		if (Bukkit.getPluginManager().getPlugin("TagAPI")!= null)
		{
			Bukkit.getPluginManager().registerEvents(new ColoredNames(), this);
			Tags.useTagAPI=true;
			logger.info("[AutoWalls] Successfully hooked into TagAPI!");
		}
		if (useTabApi)
		{
            if (Bukkit.getPluginManager().getPlugin("TabAPI")!=null) {
                useTabApi=true;
                log("[AutoWalls] Successfully hooked into TabAPI!");
            } else {
                log(Level.SEVERE, "[AutoWalls] Error! TabAPI is not installed but it was set to be used in the config! Disabling TabAPI features.");
                useTabApi = false;
            }

		}


        //Cancel weather cause no one likes rain ;-)

        new BukkitRunnable(){
            @Override
            public void run() {
                Bukkit.getWorlds().get(0).setStorm(false);
                Bukkit.getWorlds().get(0).setThundering(false);
            }
        }.runTaskLater(this, 100L);
	}

    @Override
	public void onDisable() {

	}

    public static void log(String m) {
        logger.info(m);
    }

    public static void log(Level l, String m) {
        logger.log(l, m);
    }

    public static File getPluginDataFolder() {
        return datafolder;
    }

    public void resetPlayer(final Player p) {
        if (AutoWalls.playing.contains(p)) {
            AutoWalls.addDeadPlayer(p.getName());
            new BukkitRunnable() {
                @Override
                public void run() {
                    p.teleport(new Location(Bukkit.getWorlds().get(0), arena.lobbySpawn[0], arena.lobbySpawn[1], arena.lobbySpawn[2]));
                }
            }.runTaskLater(this, 20L);
            if (AutoWalls.redTeam.contains(p)) AutoWalls.redTeam.remove(p);
            if (AutoWalls.blueTeam.contains(p)) AutoWalls.blueTeam.remove(p);
            if (AutoWalls.greenTeam.contains(p)) AutoWalls.greenTeam.remove(p);
            if (AutoWalls.orangeTeam.contains(p)) AutoWalls.orangeTeam.remove(p);
            if (TeamChat.teamChatting.contains(p)) TeamChat.teamChatting.remove(p);
            AutoWalls.playing.remove(p);
        }

        checkStats();
        Tags.refreshPlayer(p);
        Tabs.updateAll();
        p.getInventory().clear();
        p.getInventory().setArmorContents(null);
        p.setHealth(20);
        p.setFoodLevel(20);
        p.setFlying(false);
    }

	public void joinTeam(Player p, String team)
	{
		if (playing.contains(p)) {p.sendMessage(ChatColor.RED + "You are already on a team!"); }
		else
		{
			if (team == "red")
			{
				if (redTeam.size() == arena.teamSize)
				{
					p.sendMessage(ChatColor.RED + "That team is full!"); return;
				}
				redTeam.add(p);
			}
			if (team == "blue")
			{
				if (blueTeam.size() == arena.teamSize)
				{
					p.sendMessage(ChatColor.RED + "That team is full!"); return;
				}
				blueTeam.add(p);
			}
			if (team == "green")
			{
				if (greenTeam.size() == arena.teamSize)
				{
					p.sendMessage(ChatColor.RED + "That team is full!"); return;
				}
				greenTeam.add(p);
			}
			if (team == "orange")
			{
				if (orangeTeam.size() == arena.teamSize)
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
			int remaining = (arena.teamSize * 4) - playing.size();
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

					p.teleport(new Location(p.getWorld(), arena.redSpawn[0], arena.redSpawn[1], arena.redSpawn[2]));
                    p.setGameMode(GameMode.SURVIVAL);
                    for (Player pl : Bukkit.getOnlinePlayers())
                    {
                        if (p != pl && !playing.contains(p)) p.hidePlayer(pl);
                    }
				}
				else if (team.equals("blue"))
				{
					p.teleport(new Location(p.getWorld(), arena.blueSpawn[0], arena.blueSpawn[1], arena.blueSpawn[2]));
                    p.setGameMode(GameMode.SURVIVAL);
                    for (Player pl : Bukkit.getOnlinePlayers())
                    {
                        if (p != pl && !playing.contains(p)) p.hidePlayer(pl);
                    }
				}
				else if (team.equals("orange"))
				{
					p.teleport(new Location(p.getWorld(), arena.orangeSpawn[0], arena.orangeSpawn[1], arena.orangeSpawn[2]));
                    p.setGameMode(GameMode.SURVIVAL);
                    for (Player pl : Bukkit.getOnlinePlayers())
                    {
                        if (p != pl && !playing.contains(p)) p.hidePlayer(pl);
                    }
				}
				else if (team.equals("green"))
				{
					p.teleport(new Location(p.getWorld(), arena.greenSpawn[0], arena.greenSpawn[1], arena.greenSpawn[2]));
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
		if (Timer.time > 0 && gameInProgress && lateJoins) { Bukkit.broadcastMessage(ChatColor.DARK_GREEN + "A player with " + lateJoinPriority + "+ priority may " + ChatColor.YELLOW + "/join and take " + p.getName() + "'s place!"); }
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
				p.teleport(new Location(p.getWorld(),arena.redSpawn[0],arena.redSpawn[1],arena.redSpawn[2]));
			}
		if (!blueTeam.isEmpty())
			for (Player p : blueTeam)
			{
				p.teleport(new Location(p.getWorld(),arena.blueSpawn[0],arena.blueSpawn[1],arena.blueSpawn[2]));
			}
		if (!greenTeam.isEmpty())
			for (Player p : greenTeam)
			{
				p.teleport(new Location(p.getWorld(),arena.greenSpawn[0],arena.greenSpawn[1],arena.greenSpawn[2]));
			}
		if (!orangeTeam.isEmpty())
			for (Player p : orangeTeam)
			{
				p.teleport(new Location(p.getWorld(),arena.orangeSpawn[0],arena.orangeSpawn[1],arena.orangeSpawn[2]));
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

        timerTask = getServer().getScheduler().scheduleSyncRepeatingTask(this, new Timer(this),0L, 20L );

        gameInProgress=true;
	}
	public void endGame(String team, String players)
	{
		if (!gameInProgress) return;
		gameInProgress=false;
		gameOver=true;
		for (Player p : playing)
		{
			resetPlayer(p);
			Tags.refreshPlayer(p);
		}
		if (mapVotes)
		{
			Bukkit.broadcastMessage(ChatColor.DARK_RED + "The " + team + " team has won the game!");
			Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "Winning Players:  "+ ChatColor.DARK_GREEN + players);
			Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "It is time to vote for the next map!");
            ConfigurationHelper ch = ConfigurationHelper.getInstance();
            int maps = ch.getNumberOfArenas();
            for (Integer i = 1; i <= maps; i++) {
                Bukkit.broadcastMessage(ChatColor.YELLOW + i.toString() + " - " + ch.getArenaName(i.intValue()) + " - by " + ch.getArenaAuthor(i.intValue()) + ".");
            }
            /* Old Code - Subject for removal
			Bukkit.broadcastMessage(ChatColor.YELLOW + "1 - The Walls   - by Hypixel - Modified by staff team");
			Bukkit.broadcastMessage(ChatColor.YELLOW + "2 - The Walls 2 - by Hypixel - Modified by staff team");*/
			Bukkit.broadcastMessage(ChatColor.GRAY + "Type the number you want in chat. Vote will last 30 seconds");
			
			voting = true;
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(this, new VoteResult(this), 20L * 30L);
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
    public static Integer parseInt(String text) {
        try {
            return new Integer(text);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
