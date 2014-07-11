package com.jkush321.autowalls.listeners;

import com.jkush321.autowalls.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.*;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.player.*;
import org.bukkit.metadata.FixedMetadataValue;
import sun.security.util.AuthResources_it;

import java.util.Random;
import java.util.logging.Level;

public class PlayerListener implements Listener {

    private AutoWalls plugin;

    public PlayerListener(AutoWalls plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onTp (PlayerTeleportEvent e)
    {
        for (Player p : Bukkit.getOnlinePlayers())
        {
            for (Player p2 : AutoWalls.playing)
            {
                if (p!=p2 && !AutoWalls.playing.contains(p))
                {
                    p2.hidePlayer(p);
                }
                else if (p!=p2 && AutoWalls.playing.contains(p))
                {
                    p2.showPlayer(p);
                }
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (!AutoWalls.gameInProgress) {
                e.setCancelled(true);
            } else if (!(AutoWalls.playing.contains(p))) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent e)
    {
        if (AutoWalls.playing.contains(e.getPlayer()) && Timer.time<=0 && AutoWalls.blockSneaking)
            if (e.isSneaking()==true) e.setCancelled(true);
    }
    @EventHandler
    public void onEat(EntityRegainHealthEvent e)
    {
        if (e.getEntity() instanceof Player)
        {
            if (AutoWalls.playing.contains((Player) e.getEntity()) && AutoWalls.disableHealing && Timer.time<=0) {
                Random r = new Random();
                e.setAmount(r.nextInt((int) (20 - ((Player)e.getEntity()).getHealth() / 2 )));
            }
        }
    }

    @EventHandler(priority= EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent e)
    {
        AutoWalls.setLastEventToNow(e.getPlayer());
        if (AutoWalls.voting)
        {
            ConfigurationHelper ch = ConfigurationHelper.getInstance();
            int i = ch.getNumberOfArenas();
            if (e.getMessage().trim().length() <= i)
            {
                if (AutoWalls.parseInt(e.getMessage().trim()) != null) {
                    int mn = AutoWalls.parseInt(e.getMessage().trim());
                    if (AutoWalls.votes.get(e.getPlayer()) != null) {
                        if (AutoWalls.votes.get(e.getPlayer()) == mn) {
                            e.getPlayer().sendMessage(ChatColor.GRAY + "You have already voted for that map!");
                            e.setCancelled(true);
                            return;
                        }
                        e.getPlayer().sendMessage(ChatColor.GRAY + "Your previous vote for has been deleted, you have now voted for map " + mn + "!");
                        AutoWalls.votes.put(e.getPlayer(), mn);
                        return;
                    }
                    if (ch.getArenaName(mn) != null) {
                        AutoWalls.votes.put(e.getPlayer(), mn);
                        e.getPlayer().sendMessage(ChatColor.GRAY + "You have successfully voted for map " + mn + "!");
                        e.setCancelled(true);
                    } else {
                        e.getPlayer().sendMessage(ChatColor.GRAY + "Invalid map number!");
                    }
                }
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
        if (!(e.getEntity() instanceof Player)) { if (e.getDamager() instanceof Player) { if (!AutoWalls.playing.contains((Player) e.getDamager())) e.setCancelled(true); return; } }

        //no arrows shot at spectators
        if (e.getEntity() instanceof Player) { if (!AutoWalls.playing.contains((Player) e.getEntity()) && e.getDamager().getType().equals(EntityType.ARROW)) { e.setCancelled(true); return; } }

        if (e.getDamager().getType().equals(EntityType.ARROW) && e.getEntity() instanceof Player)
        {
            if (AutoWalls.playing.contains((Player) e.getEntity()))
            {
                Arrow arrow = (Arrow) e.getDamager();
                if (arrow.getShooter() instanceof Player)
                {
                    Player d = (Player) arrow.getShooter();
                    if (AutoWalls.redTeam.contains((Player)e.getEntity()) && AutoWalls.redTeam.contains(d)) { d.sendMessage(ChatColor.RED + "You can not team kill!"); e.setCancelled(true); return; }
                    if (AutoWalls.blueTeam.contains((Player)e.getEntity()) && AutoWalls.blueTeam.contains(d)) { d.sendMessage(ChatColor.RED + "You can not team kill!"); e.setCancelled(true); return; }
                    if (AutoWalls.greenTeam.contains((Player)e.getEntity()) && AutoWalls.greenTeam.contains(d)) { d.sendMessage(ChatColor.RED + "You can not team kill!"); e.setCancelled(true); return; }
                    if (AutoWalls.orangeTeam.contains((Player)e.getEntity()) && AutoWalls.orangeTeam.contains(d)) { d.sendMessage(ChatColor.RED + "You can not team kill!"); e.setCancelled(true); return; }
                }
            }
        }
        if (!(e.getDamager() instanceof Player)) return;
        if (!(e.getEntity() instanceof Player)) return;

        Player p = (Player) e.getEntity();
        Player damager = (Player) e.getDamager();

        AutoWalls.setLastEventToNow(p);

        if (!AutoWalls.playing.contains(p) && AutoWalls.playing.contains(damager)) { damager.sendMessage(ChatColor.RED + "There is a spectator there, don't hurt it"); e.setCancelled(true); return; }
        if (!AutoWalls.playing.contains(damager) && AutoWalls.playing.contains(p)) { e.setCancelled(true); damager.sendMessage(ChatColor.RED + "You Are Not In This Fight!"); return; }

        if (AutoWalls.redTeam.contains(p) && AutoWalls.redTeam.contains(damager)) { e.setCancelled(true); damager.sendMessage(ChatColor.RED + "You Can Not Team Kill!"); return; }
        if (AutoWalls.blueTeam.contains(p) && AutoWalls.blueTeam.contains(damager)) { e.setCancelled(true); damager.sendMessage(ChatColor.RED + "You Can Not Team Kill!"); return; }
        if (AutoWalls.greenTeam.contains(p) && AutoWalls.greenTeam.contains(damager)) { e.setCancelled(true); damager.sendMessage(ChatColor.RED + "You Can Not Team Kill!"); return; }
        if (AutoWalls.orangeTeam.contains(p) && AutoWalls.orangeTeam.contains(damager)) { e.setCancelled(true); damager.sendMessage(ChatColor.RED + "You Can Not Team Kill!"); return; }
        if (Timer.time > 0 && AutoWalls.playing.contains(p) && AutoWalls.playing.contains(damager)) { damager.sendMessage(ChatColor.RED + "The walls haven't dropped yet! Why are you hitting " + p.getName() + "?"); e.setCancelled(true); return; }
    }
    @EventHandler
    public void onDroppedItem(PlayerDropItemEvent e)
    {
        AutoWalls.setLastEventToNow(e.getPlayer());
        if (!AutoWalls.playing.contains(e.getPlayer()) && !e.getPlayer().hasPermission("walls.op")) e.setCancelled(true);
    }
    @EventHandler
    public void onPickUp(PlayerPickupItemEvent e)
    {
        if (!AutoWalls.playing.contains(e.getPlayer())) e.setCancelled(true);
    }
    @EventHandler (priority = EventPriority.HIGHEST)
    public void onInteract(PlayerInteractEvent e)
    {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK)
        {
            if (e.getClickedBlock().getType() == Material.WALL_SIGN || e.getClickedBlock().getType() == Material.SIGN || e.getClickedBlock().getType() == Material.SIGN_POST)
            {
                Sign s = (Sign) e.getClickedBlock().getState();
                SignUI.onClick(e.getPlayer(), s.getLine(0), s.getLine(1), s.getLine(2), s.getLine(3));
            }
        }
        if (AutoWalls.playing.contains(e.getPlayer()) && (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK))
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
                            e.getPlayer().setMetadata("last-grenade", new FixedMetadataValue(plugin, "basic"));
                            System.out.println("Meep");
                        }
                    }
                    else
                    {
                        if (e.getPlayer().hasMetadata("last-grenade")) e.getPlayer().removeMetadata("last-grenade", plugin);
                    }
                }
                else if (e.getPlayer().getItemInHand().getType() == Material.ENDER_PEARL && Timer.time > 0)
                {
                    e.getPlayer().sendMessage(ChatColor.RED + "You can not do that until the walls fall!");
                    e.setCancelled(true);
                }
            }
        }
        if (e.getPlayer().hasPermission("walls.op")) { e.setCancelled(false); return; }
        if ((e.getPlayer().getLocation().getBlockY() > 139 && AutoWalls.mapNumber == 1) || (e.getPlayer().getLocation().getBlockY() > 125 && AutoWalls.mapNumber == 2))
        {
            e.setCancelled(false);
            return;
        }
        else
        {
            if (AutoWalls.playing.contains(e.getPlayer()))
            {
                AutoWalls.setLastEventToNow(e.getPlayer());
                if ((e.getPlayer().getItemInHand() != null && e.getAction() == Action.RIGHT_CLICK_BLOCK) || e.getAction() == Action.LEFT_CLICK_BLOCK)
                {
                    if (e.getPlayer().getItemInHand().getType() != Material.AIR)
                    {
                        for (Player p : Bukkit.getOnlinePlayers())
                        {
                            if (!AutoWalls.playing.contains(p))
                            {
                                if (p.getLocation().distance(e.getClickedBlock().getLocation()) <= 2)
                                {
                                    p.teleport(p.getLocation().add(new Location(p.getWorld(), 0, 2, 0)));
                                    p.sendMessage(ChatColor.YELLOW + "You have been moved over to allow " + e.getPlayer().getName() + " to place a block");
                                }
                            }
                        }
                    }
                    if (e.getPlayer().getItemInHand().getType() != Material.FLINT_AND_STEEL && e.getPlayer().getItemInHand().getType() == Material.FIREBALL && Timer.time > 0 && AutoWalls.preventFireBeforeWallsFall)
                    {
                        e.getPlayer().sendMessage(ChatColor.DARK_RED + "You can't place fire until the walls have fallen!");
                        e.setCancelled(true);
                    }
                }
            }
        }
        if (!AutoWalls.gameInProgress) e.setCancelled(true);
        if (!AutoWalls.playing.contains(e.getPlayer())) {e.setCancelled(true);}
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e)
    {
        try{
            if (!AutoWalls.playing.contains(e.getEntity())) {
                e.setDeathMessage("");
                if (e.getEntity().getInventory().getSize() > 0)
                {
                    while (e.getDrops().size()>0)
                        e.getDrops().remove(0);
                }
                return;
            }
            if (AutoWalls.gameInProgress && AutoWalls.playing.contains(e.getEntity()))
            {
                AutoWalls.playing.remove(e.getEntity());
                if (AutoWalls.redTeam.contains(e.getEntity())) AutoWalls.redTeam.remove(e.getEntity());
                if (AutoWalls.blueTeam.contains(e.getEntity())) AutoWalls.blueTeam.remove(e.getEntity());
                if (AutoWalls.greenTeam.contains(e.getEntity())) AutoWalls.greenTeam.remove(e.getEntity());
                if (AutoWalls.orangeTeam.contains(e.getEntity())) AutoWalls.orangeTeam.remove(e.getEntity());
                if (TeamChat.teamChatting.contains(e.getEntity())) TeamChat.teamChatting.remove(e.getEntity());
                if (AutoWalls.playing.size()>1)
                    e.setDeathMessage(ChatColor.YELLOW + e.getEntity().getName() + ChatColor.DARK_RED + " " + e.getDeathMessage().split(e.getEntity().getName() + " ")[1] + ChatColor.DARK_GREEN + " " + AutoWalls.playing.size() + " Players Remain");
                plugin.createGrave(e.getEntity().getLocation(), e.getEntity().getName());
                plugin.checkStats();
                Tags.refreshPlayer(e.getEntity());
                AutoWalls.addDeadPlayer(e.getEntity().getName());
                plugin.resetPlayer(e.getEntity(), true);
            }
        } catch (Exception ex) { ex.printStackTrace(); }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onRespawn(PlayerRespawnEvent e)
    {
        if (AutoWalls.gameInProgress) plugin.spectate(e.getPlayer());
        plugin.resetPlayer(e.getPlayer(), false);
    }

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent e) {
        if (!AutoWalls.gameInProgress) {
            e.setFoodLevel(20);
            e.setCancelled(true);
        } else if (!AutoWalls.playing.contains((Player) e.getEntity())) {
            e.setFoodLevel(20);
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void itemFrame(HangingBreakByEntityEvent e) {
    if (e.getEntity() instanceof ItemFrame) {
        if (e.getRemover() instanceof Player) {
            Player p = (Player) e.getRemover();
            if (!(p.hasPermission("walls.op") || p.isOp())) {
                e.setCancelled(true);
            }
        }
    }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e)
    {
        AutoWalls.setLastEventToNow(e.getPlayer());

        Player p = e.getPlayer();

        if (e.getTo().getY() < 0) {
            if (!AutoWalls.playing.contains(e.getPlayer())) {
                ConfigurationHelper ch = ConfigurationHelper.getInstance();
                e.getPlayer().teleport(ch.getLobbySpawn(plugin.getConfig().getInt("next-map")));
            } else if (AutoWalls.playing.contains(p) || !AutoWalls.gameInProgress) {
                ConfigurationHelper ch = ConfigurationHelper.getInstance();
                e.getPlayer().teleport(ch.getLobbySpawn(plugin.getConfig().getInt("next-map")));
            }
        }

        //FINALLY! Prevent pretty much all forms of cheating by not allowing players to leave their quadrants.

        if (AutoWalls.gameInProgress) {
            if (!Timer.Dropped) {
                //Game must be in progress
                if (AutoWalls.playing.contains(p)) {
                    //Only affect players
                    if(AutoWalls.redTeam.contains(p)) {
                        if (e.getTo().getX() > AutoWalls.arena.redQuadrant[0]
                                || e.getTo().getZ() > AutoWalls.arena.redQuadrant[1]
                                || e.getTo().getX() < AutoWalls.arena.redQuadrant[2]
                                || e.getTo().getZ() < AutoWalls.arena.redQuadrant[3]) {
                            e.setCancelled(true);
                            p.teleport(e.getFrom());
                        }

                    }
                    else if (AutoWalls.blueTeam.contains(p)) {
                        if (e.getTo().getX() > AutoWalls.arena.blueQuadrant[0]
                                || e.getTo().getZ() >  AutoWalls.arena.blueQuadrant[1]
                                || e.getTo().getX() < AutoWalls.arena.blueQuadrant[2]
                                || e.getTo().getZ() < AutoWalls.arena.blueQuadrant[3]) {
                            e.setCancelled(true);
                            p.teleport(e.getFrom());
                        }

                    }
                    else if (AutoWalls.greenTeam.contains(p)) {
                        if (e.getTo().getX() > AutoWalls.arena.greenQuadrant[0]
                                || e.getTo().getZ() > AutoWalls.arena.greenQuadrant[1]
                                || e.getTo().getX() < AutoWalls.arena.greenQuadrant[2]
                                || e.getTo().getZ() < AutoWalls.arena.greenQuadrant[3]) {
                            e.setCancelled(true);
                            p.teleport(e.getFrom());
                        }

                    }
                    else if (AutoWalls.orangeTeam.contains(p)) {

                        if (e.getTo().getX() > AutoWalls.arena.orangeQuadrant[0]
                                || e.getTo().getZ() > AutoWalls.arena.orangeQuadrant[1]
                                || e.getTo().getX() < AutoWalls.arena.orangeQuadrant[2]
                                || e.getTo().getZ() < AutoWalls.arena.orangeQuadrant[3]) {
                            e.setCancelled(true);
                            p.teleport(e.getFrom());
                        }

                    }
                }
            }
        }
    }

}
