package info.bytecraft.commands;

import java.sql.Connection;
import java.sql.SQLException;

import org.bukkit.ChatColor;
import org.bukkit.Location;

import info.bytecraft.Bytecraft;
import info.bytecraft.api.BytecraftPlayer;
import info.bytecraft.database.DBWarpDAO;
import info.tregmine.database.ConnectionPool;

public class WarpCommand extends AbstractCommand
{

    public WarpCommand(Bytecraft instance)
    {
        super(instance, "warp");
    }
    
    public boolean handlePlayer(BytecraftPlayer player, String[] args)
    {
        if(args.length != 1)return false;
        if(!player.isTrusted())return true;
        String warp = args[0];
        Connection conn = null;
        try{
            conn = ConnectionPool.getConnection();
            DBWarpDAO dbWarp = new DBWarpDAO(conn);
            Location loc = dbWarp.getWarp(warp, plugin.getServer());
            if(loc != null){
                player.teleport(loc);
                player.sendMessage(ChatColor.AQUA + "Teleporting to " + warp);
            }
        }catch(SQLException e){
            throw new RuntimeException(e);
        }finally{
            if(conn != null){
                try{
                    conn.close();
                }catch(SQLException e){}
            }
        }
        
        return true;
    }

}
