package info.bytecraft.database;

import info.bytecraft.api.BytecraftPlayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
                    "INSERT INTO player_chatlog (player_id, "
                            + "chatlog_channel, chatlog_message) ";
            sql += "VALUES (?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, player.getId());
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
}
