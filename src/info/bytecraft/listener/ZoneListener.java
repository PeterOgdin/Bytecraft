package info.bytecraft.listener;

import info.bytecraft.Bytecraft;
import info.bytecraft.api.BytecraftPlayer;
import info.bytecraft.api.Zone;
import info.bytecraft.api.Zone.Permission;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.*;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class ZoneListener implements Listener
{

    private Bytecraft plugin;

    public ZoneListener(Bytecraft instance)
    {
        plugin = instance;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event)
    {
        BytecraftPlayer player = plugin.getPlayer(event.getPlayer());
        Location to = event.getTo();
        Location from = event.getFrom();
        for (Zone zone : plugin.getZones(player.getWorld().getName())) {
            if(zone.contains(to) && !zone.contains(from)){
                if(zone.isWhitelisted()){
                    Permission p = zone.getUser(player);
                    if(p != null){
                        if(p == Permission.BANNED){
                            if(!player.isAdmin()){
                                this.permissionsMessage(zone, player);
                                event.setCancelled(true);
                                return;
                            }
                        }else{
                            player.sendMessage(ChatColor.RED + "[" + zone.getName() + "] " + zone.getEnterMsg());
                            this.permissionsMessage(zone, player);
                            player.setCurrentZone(zone);
                            return;
                        }
                    }else{
                        player.sendMessage(ChatColor.RED + "[" + zone.getName() + "] You are not allowed in " + zone.getName());
                        return;
                    }
                }else{
                    player.sendMessage(ChatColor.RED + "[" + zone.getName() + "] " + zone.getEnterMsg());
                    this.permissionsMessage(zone, player);
                    player.setCurrentZone(zone);
                    return;
                }
            }else if(zone.contains(from) && !zone.contains(to)){
                player.sendMessage(ChatColor.RED + "[" + zone.getName() + "] " + zone.getExitMsg());
                player.setCurrentZone(null);
                return;
            }
        }
    }
    
    @EventHandler
    public void onBuild(BlockPlaceEvent event)
    {
        BytecraftPlayer player = plugin.getPlayer(event.getPlayer());
        if(player.getCurrentZone() != null){
            Zone zone = player.getCurrentZone();
            if(!zone.isBuildable()){
                Permission p = zone.getUser(player);
                if(p != null){
                    if(p == Permission.ALLOWED || p == Permission.BANNED){
                        if(!player.isAdmin()){
                            event.setBuild(false);
                            event.setCancelled(true);
                            player.sendMessage(ChatColor.RED + "You don't have permission to build here");
                            return;
                        }
                    }
                }else{
                    if(!player.isAdmin()){
                        event.setCancelled(true);
                        event.setBuild(false);
                        player.sendMessage(ChatColor.RED + "You don't have permission to build here");
                        return;
                    }
                }
            }
        }
    }
    
    @EventHandler
    public void onBreak(BlockBreakEvent event)
    {
        BytecraftPlayer player = plugin.getPlayer(event.getPlayer());
        if(player.getCurrentZone() != null){
            Zone zone = player.getCurrentZone();
            if(!zone.isBuildable()){
                Permission p = zone.getUser(player);
                if(p != null){
                    if(p == Permission.ALLOWED || p == Permission.BANNED){
                        if(!player.isAdmin()){
                            event.setCancelled(true);
                            player.sendMessage(ChatColor.RED + "You don't have permission to build here");
                            return;
                        }
                    }
                }else{
                    if(!player.isAdmin()){
                        event.setCancelled(true);
                        player.sendMessage(ChatColor.RED + "You don't have permission to build here");
                        return;
                    }
                }
            }
        }
    }
    
    public void permissionsMessage(Zone zone, BytecraftPlayer player)
    {
        String prefix = ChatColor.RED + "[" + zone.getName() + "] ";
        String message = null;
        Permission perm = zone.getUser(player);
        if(perm != null){
            switch(perm){
            case ALLOWED: message = prefix + "You are allowed in " + zone.getName();
                break;
            case BANNED: message = prefix + "You are banned from " + zone.getName();
                break;
            case OWNER: message = prefix + "You are an owner in " + zone.getName();
                break;
            case MAKER: message = prefix + "You are a maker in " + zone.getName();
                break;
            default: break;
            }
            if(message != null && !message.equalsIgnoreCase("")){
                player.sendMessage(message);
            }
        }
    }
    
    @EventHandler
    public void onSpawn(CreatureSpawnEvent event)
    {
        Entity ent = event.getEntity();
        for(Zone zone: plugin.getZones(ent.getWorld().getName())){
            if(zone.contains(ent.getLocation())){
                if(!zone.isHostile()){
                    event.setCancelled(true);
                    ent = null;
                    return;
                }
            }
        }
    }
}
