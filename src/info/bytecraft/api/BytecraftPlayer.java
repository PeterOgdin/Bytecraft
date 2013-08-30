package info.bytecraft.api;

import info.bytecraft.api.vector.Vector2D;
import info.bytecraft.blockfill.Fill;
import info.bytecraft.database.ConnectionPool;
import info.bytecraft.database.DBPlayerDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class BytecraftPlayer extends PlayerDelegate
{

    private int id = 0;
    private Rank rank;

    private boolean invisible;
    private boolean tpblock;

    private String chatChannel = "GLOBAL";
    
    private Block fillBlock1;
    private Block fillBlock2;
    private Block zoneBlock1;
    private Block zoneBlock2;
    private Block lotBlock1;
    private Block lotBlock2;
    
    private Fill lastFill;
    private Zone currZone = null;
    
    private BytecraftPlayer blessTarget;
    
    private Date loginTime;

    public BytecraftPlayer(Player player)
    {
        super(player);
        loginTime = new Date();
    }

    public BytecraftPlayer(String name)
    {
        super(Bukkit.getPlayer(name));
    }

    //Bytecraft stored values
    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }
    
    public Rank getRank()
    {
        return this.rank;
    }
    
    public void setRank(Rank rank)
    {
        this.rank = rank;
        setPlayerListName(rank.getColor() + getName());
    }
    public boolean isInvisible()
    {
        return invisible;
    }

    public void setInvisible(boolean invisible)
    {
        this.invisible = invisible;
    }

    public boolean isTeleportBlock()
    {
        return tpblock;
    }

    public void setTeleportBlock(boolean tpblock)
    {
        this.tpblock = tpblock;
    }
    
    public String getChatChannel()
    {
        return chatChannel.toUpperCase();
    }

    public void setChatChannel(String chatChannel)
    {
        this.chatChannel = chatChannel;
    }
    

    public long getBalance()
    {
        Connection conn = null;
        try {
            conn = ConnectionPool.getConnection();
            DBPlayerDAO dbPlayer = new DBPlayerDAO(conn);
            return dbPlayer.balance(this);
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
    
    public String getFormattedBalance()
    {
        Connection conn = null;
        try {
            conn = ConnectionPool.getConnection();
            DBPlayerDAO dbPlayer = new DBPlayerDAO(conn);
            return dbPlayer.formattedBalance(this);
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
    
    public ChatColor getGodColor(){
        Connection conn = null;
        DBPlayerDAO dbPlayer = null;
        try{
            conn = ConnectionPool.getConnection();
            dbPlayer = new DBPlayerDAO(conn);
            return dbPlayer.getGodColor(this);
        }catch (SQLException e) {
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
    
    //Zones
    public Zone getCurrentZone()
    {
        return currZone;
    }
    
    public void setCurrentZone(Zone zone)
    {
        this.currZone = zone;
    }
    
    //org.bukkit.entity.Player override
    public void setPlayerListName(String name)
    {
        super.setPlayerListName(name);
    }
    
    public String getDisplayName()
    {
        return getRank().getColor() + getName();
    }
    
    //Bytecraft non-persistent 
    public int getMaxTeleportDistance()
    {
        if(isAdmin())return Integer.MAX_VALUE;
        if(isDonator())return 10000;
        
        return 300;
    }
    
    public long getTeleportTimeout()
    {
        if(isAdmin()) return 20 * 2L;
        if(rank == Rank.GAURD) return 20 * 3L;
        
        if(rank == Rank.DONATOR) return 20 * 4L;
        
        return 20 * 5L;
    }
    
    public BytecraftPlayer getBlessTarget()
    {
        return blessTarget;
    }

    public void setBlessTarget(BytecraftPlayer blessTarget)
    {
        this.blessTarget = blessTarget;
    }
    
    public void sendNotification(Notification notif)
    {
        this.playSound(getLocation(), notif.getSound(), 2F, 1F);
    }

    public Block getFillBlock1()
    {
        return fillBlock1;
    }

    public void setFillBlock1(Block fillBlock1)
    {
        this.fillBlock1 = fillBlock1;
    }

    public Block getFillBlock2()
    {
        return fillBlock2;
    }

    public void setFillBlock2(Block fillBlock2)
    {
        this.fillBlock2 = fillBlock2;
    }

    public void setLastFill(Fill fill)
    {
        this.lastFill = fill;
    }
    
    public Block getZoneBlock1()
    {
        return zoneBlock1;
    }

    public void setZoneBlock1(Block zoneBlock1)
    {
        this.zoneBlock1 = zoneBlock1;
    }

    public Block getZoneBlock2()
    {
        return zoneBlock2;
    }

    public void setZoneBlock2(Block zoneBlock2)
    {
        this.zoneBlock2 = zoneBlock2;
    }

    public Block getLotBlock1()
    {
        return lotBlock1;
    }

    public void setLotBlock1(Block lotBlock1)
    {
        this.lotBlock1 = lotBlock1;
    }

    public Block getLotBlock2()
    {
        return lotBlock2;
    }

    public void setLotBlock2(Block lotBlock2)
    {
        this.lotBlock2 = lotBlock2;
    }

    public Fill getLastFill()
    {
        return this.lastFill;
    }

    //Bytecraft Rank-Inheritence 
    public boolean isAdmin()
    {
        return (this.rank == Rank.ADMIN || this.rank == Rank.SENIOR_ADMIN || isCoder());
    }
    
    public boolean isCoder()
    {
        return this.rank == Rank.CODER;
    }
    
    public boolean isModerator()
    {
        return (isAdmin() || this.rank == Rank.GAURD);
    }
    
    public boolean canFill()
    {
        return (isAdmin() || this.rank == Rank.BUILDER);
    }
    
    public boolean isDonator()
    {
        return (isModerator() || this.rank == Rank.DONATOR || this.rank == Rank.BUILDER);
    }
    
    public boolean isMentor()
    {
        return (isAdmin() || this.rank == Rank.MENTOR);
    }

    public int getOnlineTime()
    {
        return (int)((new Date().getTime() - loginTime.getTime())/1000L);
    }
    
    public int getPlayTime()
    {
        Connection conn = null;
        try{
            conn = ConnectionPool.getConnection();
            DBPlayerDAO dbPlayer = new DBPlayerDAO(conn);
            return dbPlayer.getPlayTime(this);
        }catch(SQLException e){
            throw new RuntimeException(e);
        }finally{
            if(conn != null){
                try{
                    conn.close();
                }catch(SQLException e){}
            }
        }
    }
    
    public long getPromotedTime()
    {
        Connection conn = null;
        try{
            conn = ConnectionPool.getConnection();
            DBPlayerDAO dbPlayer = new DBPlayerDAO(conn);
            return dbPlayer.getPromotedLong(this);
        }catch(SQLException e){
            throw new RuntimeException(e);
        }finally{
            if(conn != null){
                try{
                    conn.close();
                }catch(SQLException e){}
            }
        }
    }
    
    public Vector2D getVector()
    {
        Location loc = getLocation();
        return new Vector2D(loc.getBlockX(), loc.getBlockZ(), loc.getWorld());
    }
    
}
