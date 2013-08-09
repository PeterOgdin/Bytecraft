package info.bytecraft.api;

import info.bytecraft.blockfill.Fill;
import info.bytecraft.database.DBPlayerDAO;
import info.tregmine.database.ConnectionPool;

import java.sql.Connection;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
    private Fill lastFill;
    
    private BytecraftPlayer blessTarget;

    public BytecraftPlayer(Player player)
    {
        super(player);
    }

    public BytecraftPlayer(String name)
    {
        super(Bukkit.getPlayer(name));
    }

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

    public void setDisplayName(String name)
    {
        super.setDisplayName(name);
        super.setPlayerListName(name);
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
    
    public Fill getLastFill()
    {
        return this.lastFill;
    }

    public boolean isAdmin()
    {
        return (this.rank == Rank.ADMIN || this.rank == Rank.SENIOR_ADMIN);
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
        return (isModerator() || this.rank == Rank.DONATOR);
    }
    
    public boolean isMentor()
    {
        return (isModerator() || this.rank == Rank.MENTOR);
    }
    
}
