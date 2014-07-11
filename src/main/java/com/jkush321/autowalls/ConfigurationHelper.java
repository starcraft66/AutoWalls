package com.jkush321.autowalls;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.*;

public class ConfigurationHelper {

    //Easy config access

    private static ConfigurationHelper instance = new ConfigurationHelper();
    private static Plugin p;
    private FileConfiguration arena;

    private File arenaFile; //arenas

    private static final int ARENA_VERSION = 0;


    private ConfigurationHelper() {

    }

    public static ConfigurationHelper getInstance() {
        return instance;
    }

    public void setup(Plugin p) {
        ConfigurationHelper.p = p;
        if (p.getConfig().getInt("version") == AutoWalls.CONFIG_VERSION) {
            AutoWalls.config_todate = true;
        }else{
            File config = new File(p.getDataFolder(), "config.yml");
            config.delete();
        }

        //p.getConfig().options().copyDefaults(true);
        p.saveDefaultConfig();

        arenaFile = new File(p.getDataFolder(), "arena.yml");
        arena = YamlConfiguration.loadConfiguration(arenaFile);

        try {
            if (!arenaFile.exists()){
                loadFile("arena.yml");
                AutoWalls.log("Copying arena.yml");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        reloadArena();
        saveArena();
    }

    public void set(String arg0, Object arg1) {
        p.getConfig().set(arg0, arg1);
    }

    public FileConfiguration getConfig() {
        return p.getConfig();
    }

    public FileConfiguration getArenaConfig() {
        return arena;
    }

    public void reloadConfig(){
        p.reloadConfig();
    }

    public boolean moveFile(File ff){
        AutoWalls.log("Moving outdated config file. " + arenaFile.getName());
        String name = ff.getName();
        File ff2 = new File(AutoWalls.getPluginDataFolder(), getNextName(name, 0));
        return ff.renameTo(ff2);
    }

    public String getNextName(String name, int n){
        File ff = new File(AutoWalls.getPluginDataFolder(), name + ".old" + n);
        if(!ff.exists()){
            return ff.getName();
        }
        else{
            return getNextName(name, n+1);
        }
    }

    public void reloadArena() {
        arena = YamlConfiguration.loadConfiguration(arenaFile);
        if(arena.getInt("version") != ARENA_VERSION){
            moveFile(arenaFile);
            reloadArena();
        }
        arena.set("version", ARENA_VERSION);
        saveArena();
    }

    public void saveArena() {
        try {
            arena.save(arenaFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Location getLobbySpawn(int arenaid) {
        try{
            return new Location(Bukkit.getWorlds().get(0),
                    arena.getInt("walls-arena." + arenaid + ".lobby.spawn.x"),
                    arena.getInt("walls-arena." + arenaid + ".lobby.spawn.y"),
                    arena.getInt("walls-arena." + arenaid + ".lobby.spawn.z"));
        }catch(Exception e){
            return null;
        }
    }

    public Location getSpawnPoint(int arenaid, String color) {
        return new Location(Bukkit.getWorlds().get(0),
                arena.getInt("walls-arena." + arenaid + "." + color + ".spawn.x"),
                arena.getInt("walls-arena." + arenaid + "." + color + ".spawn.y"),
                arena.getInt("walls-arena." + arenaid + "." + color + ".spawn.z"));
    }

    public Location getDeathmatchPoint(int arenaid) {
        return new Location(Bukkit.getWorlds().get(0),
                arena.getInt("walls-arena." + arenaid + ".deathmatch.x"),
                arena.getInt("walls-arena." + arenaid + ".deathmatch.y"),
                arena.getInt("walls-arena." + arenaid + ".deathmatch.z"));
    }

    public Location getRedstoneActivator(int arenaid) {
        return new Location(Bukkit.getWorlds().get(0),
                arena.getInt("walls-arena." + arenaid + ".redstone-activation-location.x"),
                arena.getInt("walls-arena." + arenaid + ".redstone-activation-location.y"),
                arena.getInt("walls-arena." + arenaid + ".redstone-activation-location.z"));
    }

    public Location getQuad1(int arenaid, String color) {
        return new Location(Bukkit.getWorlds().get(0),
                arena.getInt("walls-arena." + arenaid + "." + color + ".corner1.x"),0,
                arena.getInt("walls-arena." + arenaid + "." + color + ".corner1.z"));
    }

    public Location getQuad2(int arenaid, String color) {
        return new Location(Bukkit.getWorlds().get(0),
                arena.getInt("walls-arena." + arenaid + "." + color + ".corner2.x"),0,
                arena.getInt("walls-arena." + arenaid + "." + color + ".corner2.z"));
    }

    public Location getMapCorner1(int arenaid) {
        return new Location(Bukkit.getWorlds().get(0),
                arena.getInt("walls-arena." + arenaid + ".corner1.x"),0,
                arena.getInt("walls-arena." + arenaid + ".corner1.z"));
    }

    public Location getMapCorner2(int arenaid) {
        return new Location(Bukkit.getWorlds().get(0),
                arena.getInt("walls-arena." + arenaid + ".corner2.x"),0,
                arena.getInt("walls-arena." + arenaid + ".corner2.z"));
    }

    public int getMaxHeight(int arenaid) {
        return arena.getInt("walls-arena." + arenaid + ".maxheight");
    }

    public int getMinHeight(int arenaid) {
        return arena.getInt("walls-arena." + arenaid + ".minheight");
    }

    public String getArenaName(int arenaid){
        return arena.getString("walls-arena." + arenaid + ".name");
    }

    public String getArenaAuthor(int arenaid){
        return arena.getString("walls-arena." + arenaid + ".author");
    }

    public int getPlayersPerTeam(int arenaid) {
        return arena.getInt("walls-arena." + arenaid + ".players-per-team");
    }

    public int getNumberOfArenas() {
        ConfigurationSection configSection = arena.getConfigurationSection("walls-arena");
        int i = 0;
        for(String key : configSection.getKeys(false)){
            i++;
            //Bukkit.getLogger().info("Found configuration information for map " + key + " (" + getArenaName(Integer.parseInt(key)) + ")");
        }
        return i;
    }

    public int getGameLength(int arenaid) {
        return arena.getInt("walls-arena." + arenaid + ".game-length");
    }

    public void loadFile(String file){
        File newfile = new File(p.getDataFolder(), file);

        try {
            newfile.createNewFile();
            FileWriter out = new FileWriter(newfile);
            System.out.println(file);
            InputStream is = getClass().getResourceAsStream("/"+file);
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                out.write(line + "\n");
            }
            out.flush();
            is.close();
            isr.close();
            br.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
