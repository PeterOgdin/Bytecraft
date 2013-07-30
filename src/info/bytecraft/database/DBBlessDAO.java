package info.bytecraft.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.block.Block;

public class DBBlessDAO
{

    private Connection conn;

    public DBBlessDAO(Connection conn)
    {
        this.conn = conn;
    }
    
    public boolean isBlessed(Block block)
    {
        PreparedStatement stm = null;
        try{
            stm = conn.prepareStatement("SELECT * FROM bless WHERE x = ? AND y = ? AND z = ? AND world = ?");
            stm.setInt(1, block.getX());
            stm.setInt(2, block.getY());
            stm.setInt(3, block.getZ());
            stm.setString(4, block.getWorld().getName());
            
            stm.execute();
            ResultSet rs = stm.getResultSet();
            if(rs.next())return true;
        }catch(SQLException e){
            throw new RuntimeException(e);
        }finally{
            if(stm != null){
                try{
                    stm.close();
                }catch(SQLException e){}
            }
        }
        return false;
    }
    
    public boolean bless(Block block)
    {
        return true;
    }
}
