package info.bytecraft.commands;

import java.sql.Connection;
import java.sql.SQLException;

import org.bukkit.ChatColor;

import info.bytecraft.Bytecraft;
import info.bytecraft.api.BytecraftPlayer;
import info.bytecraft.api.Zone;
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
        Connection conn = null;//name, world 
        try{
            conn = ConnectionPool.getConnection();
            DBZoneDAO dbZone = new DBZoneDAO(conn);
            return dbZone.getZone(name) != null;
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
