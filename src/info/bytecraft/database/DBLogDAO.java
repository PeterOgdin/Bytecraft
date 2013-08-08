package info.bytecraft.database;

import info.bytecraft.api.BytecraftPlayer;
import info.bytecraft.blockfill.Fill;
import info.bytecraft.blockfill.Fill.Action;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bukkit.Location;
import org.bukkit.Material;

public class DBLogDAO
{

    private Connection conn;

    public DBLogDAO(Connection conn)
    {
        this.conn = conn;
    }

    public void insertChatMessage(BytecraftPlayer player, String channel,
            String message)
    {
        PreparedStatement stmt = null;
        try {
            String sql =
                    "INSERT INTO player_chatlog (player_name, "
                            + "chatlog_channel, chatlog_message) ";
            sql += "VALUES (?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, player.getName());
            stmt.setString(2, channel);
            stmt.setString(3, message);
            stmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                }
            }
        }
    }
    
    public void insertTransactionLog(BytecraftPlayer giver, BytecraftPlayer reciever, long amount)
    {
        PreparedStatement stm = null;
        try{
            stm = conn.prepareStatement("INSERT INTO transaction_log " +
            		"(sender_name, reciever_name, amount) VALUES (?, ?, ?)");
            stm.setString(1, giver.getName());
            stm.setString(2, reciever.getName());
            stm.setLong(3, amount);
            stm.execute();
        }catch(SQLException e){
            throw new RuntimeException(e);
        }finally{
            if(stm != null){
                try {
                    stm.close();
                } catch (SQLException e) {}
            }
        }
    }
    
    public void insertFillLog(BytecraftPlayer player, Fill fill, Material material, Action action)
    {
        PreparedStatement stm = null;
        try{
            stm = conn.prepareStatement("INSERT INTO fill_log (player_name, action, size," +
            		"material) VALUES (?, ?, ?, ?)");
            stm.setString(1, player.getName());
            stm.setString(2, action.toString().toLowerCase());
            stm.setInt(3, fill.getSize());
            stm.setString(4, material.name().toLowerCase());
            
            stm.execute();
        }catch(SQLException e){
            throw new RuntimeException(e);
        }finally{
            if(stm != null){
                try {
                    stm.close();
                } catch (SQLException e) {}
            }
        }
    }
    
    public void insertPaperLog(BytecraftPlayer player, Location loc, Material mat, String action)
    {
        PreparedStatement stm = null;
        try{
            stm = conn.prepareStatement("INSERT INTO paper_log (player_name, block_x, block_y, block_z, block_type, action) " +
            		"VALUES (?, ?, ?, ?, ?, ?)");
            stm.setString(1, player.getName());
            stm.setInt(2, loc.getBlockX());
            stm.setInt(3, loc.getBlockY());
            stm.setInt(4, loc.getBlockZ());
            stm.setString(5, mat.name().toLowerCase());
            stm.setString(6, action);
            
            stm.execute();
            
        }catch(SQLException e){
            throw new RuntimeException(e);
        }finally{
            if(stm != null){
                try {
                    stm.close();
                } catch (SQLException e) {}
            }
        }
    }
}
