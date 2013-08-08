package info.bytecraft.commands;

import java.sql.Connection;
import java.sql.SQLException;

import org.bukkit.ChatColor;

import info.bytecraft.Bytecraft;
import info.bytecraft.api.BytecraftPlayer;
import info.bytecraft.database.DBPlayerDAO;
import info.tregmine.database.ConnectionPool;

public class VanishCommand extends AbstractCommand
{

    public VanishCommand(Bytecraft instance)
    {
        super(instance, "vanish");
    }
    
    public boolean handlePlayer(BytecraftPlayer player, String[] args)
    {
        if(player.isAdmin() && player.getColor().equalsIgnoreCase("senior")){//seniors only
            if(args.length == 0){
                Connection conn = null;
                DBPlayerDAO dbPlayer = null;
                try{
                    conn = ConnectionPool.getConnection();
                    dbPlayer = new DBPlayerDAO(conn);
                    
                    if(player.isInvisible()){
                        player.setInvisible(false);
                        for(BytecraftPlayer other: plugin.getOnlinePlayers()){
                            other.showPlayer(player.getDelegate());
                        }
                        player.sendMessage(ChatColor.AQUA + "You have re-appeared");
                    }else{
                        player.setInvisible(true);
                        for(BytecraftPlayer other: plugin.getOnlinePlayers()){
                            if(other.isAdmin() && other.getColor().equalsIgnoreCase("senior")){
                                continue;
                            }
                            other.hidePlayer(player.getDelegate());
                        }
                        player.sendMessage(ChatColor.AQUA + "You have disappeared");
                    }
                    
                    dbPlayer.updatePlayerInfo(player);
                }catch(SQLException e){
                    throw new RuntimeException(e);
                }
            }
        }
        return true;
    }

}