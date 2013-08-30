package info.bytecraft.commands;

import java.sql.Connection;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import info.bytecraft.Bytecraft;
import info.bytecraft.api.BytecraftPlayer;
import info.bytecraft.api.Zone;
import info.bytecraft.api.Zone.Flag;
import info.bytecraft.api.Zone.Permission;
import info.bytecraft.database.ConnectionPool;
import info.bytecraft.database.DBZoneDAO;

public class ZoneCommand extends AbstractCommand
{

    public ZoneCommand(Bytecraft instance)
    {
        super(instance, "zone");
    }
    
    public boolean handlePlayer(BytecraftPlayer player, String[] args)
    {
        if(args.length == 2)//zone create|delete name
        {
            if("create".equalsIgnoreCase(args[0])){
                if(player.isAdmin()){
                    if(!zoneExists(args[1])){
                        createZone(player, args[1]);
                        player.sendMessage(ChatColor.RED + "Created zone " + args[1]);
                    }else{
                        player.sendMessage(ChatColor.RED + "Zone " + args[1] + " already exists");
                    }
                }
            }else if("delete".equalsIgnoreCase(args[0])){
                if(player.isAdmin()){
                    if(!zoneExists(args[1])){
                        player.sendMessage(ChatColor.RED + "Zone " + args[1] + " doesn't exist");
                    }else{
                        deleteZone(args[1]);
                        player.sendMessage(ChatColor.RED + "Deleted zone " + args[1]);
                    }
                }
            }else if("deluser".equalsIgnoreCase(args[0])){
                Player delegate = Bukkit.getPlayer(args[1]);
                if(delegate != null){
                    BytecraftPlayer target = plugin.getPlayer(delegate);
                    //TODO: delete user
                }
            }
        }else if(args.length == 3){//zone [flag] [name] [true/false]
            if(!zoneExists(args[1])){
                player.sendMessage(ChatColor.RED + "Zone " + args[1] + " not found");
            }else{
                Zone zone = plugin.getZone(args[1]);
                Permission p = zone.getUser(player);
                if((p != null && p == Permission.OWNER) || player.isAdmin()){
                    if("pvp".equalsIgnoreCase(args[0])){
                        changeSetting(args[1], Flag.PVP, (args[2].equalsIgnoreCase("true")) ? "true" : "false");
                        player.sendMessage(ChatColor.RED + "["+ zone.getName() + "] Changed pvp to " + ((args[2].equalsIgnoreCase("true")) ? "true" : "false"));
                    }else if("whitelist".equalsIgnoreCase(args[0])){
                        changeSetting(args[1], Flag.WHITELIST, (args[2].equalsIgnoreCase("true")) ? "true" : "false");
                        player.sendMessage(ChatColor.RED + "["+ zone.getName() + "] Changed whitelist to " + ((args[2].equalsIgnoreCase("true")) ? "true" : "false"));
                    }else if("build".equalsIgnoreCase(args[0])){
                        changeSetting(args[1], Flag.BUILD, (args[2].equalsIgnoreCase("true")) ? "true" : "false");
                        player.sendMessage(ChatColor.RED + "["+ zone.getName() + "] Changed build settings to " + ((args[2].equalsIgnoreCase("true")) ? "true" : "false"));
                    }else if("hostile".equalsIgnoreCase(args[0])){
                        changeSetting(args[1], Flag.HOSTILE, (args[2].equalsIgnoreCase("true")) ? "true" : "false");
                        player.sendMessage(ChatColor.RED + "["+ zone.getName() + "] Changed hostile mob spawning to " + ((args[2].equalsIgnoreCase("true")) ? "true" : "false"));
                    }else if("adduser".equalsIgnoreCase(args[0])){
                        Player delegate = Bukkit.getPlayer(args[0]);
                        if(delegate != null){
                            BytecraftPlayer target = plugin.getPlayer(delegate);
                            //TODO: add user
                        }
                    }
                }
            }
        }else if(args.length >= 4){
            if(args[0].equalsIgnoreCase("entermsg") || args[0].equalsIgnoreCase("exitmsg")){
                if(!zoneExists(args[1])){
                    player.sendMessage(ChatColor.RED + "Zone " + args[1] + " does not exist");
                }else{
                    Permission p = plugin.getZone(args[1]).getUser(player);
                    if((p != null && p == Permission.OWNER) || player.isAdmin()){
                        StringBuilder sb = new StringBuilder();
                        for(int i = 2; i < args.length; i++){
                            sb.append(args[i] + " ");
                        }
                        Flag f = args[0].equalsIgnoreCase("entermsg") ? Flag.ENTERMSG: Flag.EXITMSG;
                        changeSetting(args[1], f, sb.toString().trim());
                        player.sendMessage(ChatColor.RED + "[" + args[1] + "] Changed " + f.name().toLowerCase() + " to: " + sb.toString().trim());
                    }
                }
            }
        }
        return true;
    }
    
    public boolean createZone(BytecraftPlayer player, String name)
    {
        Connection conn = null;//name, world 
        Zone zone = new Zone(name);
        zone.setWorld(player.getWorld().getName());
        try{
            conn = ConnectionPool.getConnection();
            DBZoneDAO dbZone = new DBZoneDAO(conn);
            dbZone.createZone(zone, player);
        }catch(SQLException e){
            throw new RuntimeException(e);
        }finally{
            if(conn != null){
                try {
                    conn.close();
                } catch (SQLException e) {}
            }
        }
        return true;
    }
    
    public boolean deleteZone(String name)
    {
        Connection conn = null;//name, world 
        try{
            conn = ConnectionPool.getConnection();
            DBZoneDAO dbZone = new DBZoneDAO(conn);
            dbZone.deleteZone(name);
        }catch(SQLException e){
            throw new RuntimeException(e);
        }finally{
            if(conn != null){
                try {
                    conn.close();
                } catch (SQLException e) {}
            }
        }
        return true;
    }
    
    public boolean zoneExists(String name)
    {
        return plugin.getZone(name) != null;
    }
    
    public void changeSetting(String zone, Flag flag, String value)
    {
        Connection conn = null;
        try{
            conn = ConnectionPool.getConnection();
            DBZoneDAO dbZone = new DBZoneDAO(conn);
            dbZone.updateFlag(zone, flag, value);
        }catch(SQLException e){
            throw new RuntimeException(e);
        }finally{
            if(conn != null){
                try {
                    conn.close();
                } catch (SQLException e) {}
            }
        }
    }
}
