package com.jkush321.autowalls;

public class Arena {
    private static Arena instance = new Arena();

    public int ArenaId;
    public int teamSize;
    public String name;
    public String author;
    public int[] height = new int[2];
    public double[] redSpawn = new double[3];
    public double[] blueSpawn = new double[3];
    public double[] greenSpawn = new double[3];
    public double[] orangeSpawn = new double[3];
    public double[] redQuadrant = new double[4];
    public double[] blueQuadrant = new double[4];
    public double[] greenQuadrant = new double[4];
    public double[] orangeQuadrant = new double[4];
    public double[] lobbySpawn = new double[3];
    public double[] mapLimits = new double[4];
    public double[] redstoneCircuitActivator = new double[3];

    public static Arena getInstance() {
        return instance;
    }

    public void loadArenaSettings(int arenaid){
        ConfigurationHelper ch = ConfigurationHelper.getInstance();

        ArenaId = arenaid;

        //Spawns
        //X
        //Y
        //Z

        redSpawn[0] = ch.getSpawnPoint(arenaid, "red").getX();
        redSpawn[1] = ch.getSpawnPoint(arenaid, "red").getY();
        redSpawn[2] = ch.getSpawnPoint(arenaid, "red").getZ();

        blueSpawn[0] = ch.getSpawnPoint(arenaid, "blue").getX();
        blueSpawn[1] = ch.getSpawnPoint(arenaid, "blue").getY();
        blueSpawn[2] = ch.getSpawnPoint(arenaid, "blue").getZ();

        greenSpawn[0] = ch.getSpawnPoint(arenaid, "green").getX();
        greenSpawn[1] = ch.getSpawnPoint(arenaid, "green").getY();
        greenSpawn[2] = ch.getSpawnPoint(arenaid, "green").getZ();

        orangeSpawn[0] = ch.getSpawnPoint(arenaid, "orange").getX();
        orangeSpawn[1] = ch.getSpawnPoint(arenaid, "orange").getY();
        orangeSpawn[2] = ch.getSpawnPoint(arenaid, "orange").getZ();

        //Quadrants
        //xMax
        //zMax
        //xMin
        //zMin

        redQuadrant[0] = maxValue(ch.getQuad1(arenaid, "red").getX(), ch.getQuad2(arenaid, "red").getX());
        redQuadrant[1] = maxValue(ch.getQuad1(arenaid, "red").getZ(), ch.getQuad2(arenaid, "red").getZ());
        redQuadrant[2] = minValue(ch.getQuad1(arenaid, "red").getX(), ch.getQuad2(arenaid, "red").getX());
        redQuadrant[3] = minValue(ch.getQuad1(arenaid, "red").getZ(), ch.getQuad2(arenaid, "red").getZ());

        blueQuadrant[0] = maxValue(ch.getQuad1(arenaid, "blue").getX(), ch.getQuad2(arenaid, "blue").getX());
        blueQuadrant[1] = maxValue(ch.getQuad1(arenaid, "blue").getZ(), ch.getQuad2(arenaid, "blue").getZ());
        blueQuadrant[2] = minValue(ch.getQuad1(arenaid, "blue").getX(), ch.getQuad2(arenaid, "blue").getX());
        blueQuadrant[3] = minValue(ch.getQuad1(arenaid, "blue").getZ(), ch.getQuad2(arenaid, "blue").getZ());

        greenQuadrant[0] = maxValue(ch.getQuad1(arenaid, "green").getX(), ch.getQuad2(arenaid, "green").getX());
        greenQuadrant[1] = maxValue(ch.getQuad1(arenaid, "green").getZ(), ch.getQuad2(arenaid, "green").getZ());
        greenQuadrant[2] = minValue(ch.getQuad1(arenaid, "green").getX(), ch.getQuad2(arenaid, "green").getX());
        greenQuadrant[3] = minValue(ch.getQuad1(arenaid, "green").getZ(), ch.getQuad2(arenaid, "green").getZ());

        orangeQuadrant[0] = maxValue(ch.getQuad1(arenaid, "orange").getX(), ch.getQuad2(arenaid, "orange").getX());
        orangeQuadrant[1] = maxValue(ch.getQuad1(arenaid, "orange").getZ(), ch.getQuad2(arenaid, "orange").getZ());
        orangeQuadrant[2] = minValue(ch.getQuad1(arenaid, "orange").getX(), ch.getQuad2(arenaid, "orange").getX());
        orangeQuadrant[3] = minValue(ch.getQuad1(arenaid, "orange").getZ(), ch.getQuad2(arenaid, "orange").getZ());

        //Height
        //[0] = min
        //[1] = max

        height[0] = ch.getMinHeight(arenaid);
        height[1] = ch.getMaxHeight(arenaid);

        //Info

        name = ch.getArenaName(arenaid);
        author = ch.getArenaAuthor(arenaid);
        teamSize = ch.getPlayersPerTeam(arenaid);

        //Map boundaries
        //corner1 = top left - corner2 = bottom right
        //[0] = Lowest X
        //[1] = Lowest Z
        //[2] = Highest X
        //[3] = Highest Z

        mapLimits[0] = minValue(ch.getMapCorner1(arenaid).getX(), ch.getMapCorner2(arenaid).getX());
        mapLimits[1] = minValue(ch.getMapCorner1(arenaid).getZ(), ch.getMapCorner2(arenaid).getZ());
        mapLimits[2] = maxValue(ch.getMapCorner1(arenaid).getX(), ch.getMapCorner2(arenaid).getX());
        mapLimits[3] = maxValue(ch.getMapCorner1(arenaid).getZ(), ch.getMapCorner2(arenaid).getZ());

        //Redstone activator
        //Where a redstone torch will be placed and removed to make the walls fall

        redstoneCircuitActivator[0] = ch.getRedstoneActivator(arenaid).getX();
        redstoneCircuitActivator[1] = ch.getRedstoneActivator(arenaid).getY();
        redstoneCircuitActivator[2] = ch.getRedstoneActivator(arenaid).getZ();

        //Spawn

        lobbySpawn[0] = ch.getLobbySpawn(arenaid).getX();
        lobbySpawn[1] = ch.getLobbySpawn(arenaid).getY();
        lobbySpawn[2] = ch.getLobbySpawn(arenaid).getZ();

    }

    public double minValue(double val1, double val2){
        return Math.min(val1, val2);
    }

    public double maxValue(double val1, double val2) {
        return Math.max(val1, val2);
    }

}
