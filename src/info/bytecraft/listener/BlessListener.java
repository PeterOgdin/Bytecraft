package info.bytecraft.listener;

import java.sql.Connection;
import java.sql.SQLException;

import info.bytecraft.Bytecraft;
import info.bytecraft.database.DBBlessDAO;
import info.tregmine.database.ConnectionPool;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class BlessListener implements Listener
{
    
    private Bytecraft plugin;
    
    public BlessListener(Bytecraft bytecraft)
    {
        this.plugin = bytecraft;
    }

    @EventHandler
    public void onClick(PlayerInteractEvent event)
    {
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_BLOCK){
            Connection conn = null;
            try{
                conn = ConnectionPool.getConnection();
                DBBlessDAO dbBless = new DBBlessDAO(conn);
                if(dbBless.isBlessed(event.getClickedBlock())){
                    event.getPlayer().sendMessage("Yes");
                }else{
                    event.getPlayer().sendMessage("no");
                }
            }catch(SQLException e){
                
            }finally{
                if(conn != null){
                    try {
                        conn.close();
                    } catch (SQLException e) {
                    }
                }
            }
        }
    }
}
