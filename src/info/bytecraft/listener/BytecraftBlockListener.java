package info.bytecraft.listener;

import java.sql.Connection;
import java.sql.SQLException;

import info.bytecraft.Bytecraft;
import info.bytecraft.api.BytecraftPlayer;
import info.bytecraft.database.DBLogDAO;
import info.tregmine.database.ConnectionPool;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BytecraftBlockListener implements Listener
{
    private Bytecraft plugin;

    public BytecraftBlockListener(Bytecraft plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event)
    {
        BytecraftPlayer player = plugin.getPlayer(event.getPlayer());
        Location loc = event.getBlock().getLocation();
        Connection conn = null;
        DBLogDAO dbLog = null;
        try {
            conn = ConnectionPool.getConnection();
            dbLog = new DBLogDAO(conn);
            dbLog.insertPaperLog(player, loc, event.getBlock().getType(), "break");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
    }
    
    @EventHandler
    public void onPlace(BlockPlaceEvent event)
    {
        BytecraftPlayer player = plugin.getPlayer(event.getPlayer());
        Location loc = event.getBlock().getLocation();
        Connection conn = null;
        DBLogDAO dbLog = null;
        try {
            conn = ConnectionPool.getConnection();
            dbLog = new DBLogDAO(conn);
            dbLog.insertPaperLog(player, loc, event.getBlock().getType(), "place");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
    }

}
