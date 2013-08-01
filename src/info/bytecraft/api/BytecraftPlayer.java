package info.bytecraft.api;

import info.bytecraft.database.DBPlayerDAO;
import info.tregmine.database.ConnectionPool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class BytecraftPlayer extends PlayerDelegate
{

    @SuppressWarnings("serial")
    private final static Map<String, ChatColor> COLORS =
            new HashMap<String, ChatColor>() {
                {
                    put("admin", ChatColor.RED);
                    put("child", ChatColor.AQUA);
                    put("donator", ChatColor.GOLD);
                    put("builder", ChatColor.YELLOW);
                    put("mentor", ChatColor.DARK_AQUA);
                    put("pink", ChatColor.LIGHT_PURPLE);
                    put("guard", ChatColor.BLUE);
                    put("coder", ChatColor.DARK_PURPLE);
                    put("settler", ChatColor.GREEN);
                    put("member", ChatColor.DARK_GREEN);
                    put("senior", ChatColor.DARK_RED);
                    put("warned", ChatColor.GRAY);
                    put("newcomer", ChatColor.WHITE);
                }
            };
            
    @SuppressWarnings("serial")
    private static final Map<String, ChatColor> GOD_COLORS = 
        new HashMap<String, ChatColor>(){
            {
                put("red", ChatColor.RED);
                put("aqua", ChatColor.AQUA);
                put("gold", ChatColor.GOLD);
                put("yellow", ChatColor.YELLOW);
                put("dark_aquar", ChatColor.DARK_AQUA);
                put("pink", ChatColor.LIGHT_PURPLE);
                put("purple", ChatColor.DARK_PURPLE);
                put("green", ChatColor.GREEN);
                put("dark_green", ChatColor.DARK_GREEN);
                put("dark_red", ChatColor.DARK_RED);
                put("gray", ChatColor.GRAY);
            }
    };

    private int id = 0;

    private boolean admin;
    private boolean builder;
    private boolean trusted;
    private boolean invisible;
    private boolean donator;
    private boolean member;
    private boolean tpblock;
    private boolean guard;
    private boolean settler;
    private boolean warned;
    private boolean hardwarned;

    private String color;
    private String godColor;
    private String chatChannel = "GLOBAL";
    
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

    public void setAdmin(boolean value)
    {
        this.admin = value;
    }

    public boolean isAdmin()
    {
        return admin;
    }

    public boolean isBuilder()
    {
        return this.builder;
    }

    public void setBuilder(boolean builder)
    {
        this.builder = builder;
    }

    public boolean isTrusted()
    {
        return trusted;
    }

    public void setTrusted(boolean trusted)
    {
        this.trusted = trusted;
    }

    public boolean isInvisible()
    {
        return invisible;
    }

    public void setInvisible(boolean invisible)
    {
        this.invisible = invisible;
    }

    public ChatColor getNameColor()
    {
        return COLORS.get(color) == null ? ChatColor.WHITE : COLORS.get(color);
    }

    public void setNameColor(String v)
    {
        this.color = v;
    }

    public boolean isDonator()
    {
        return this.donator;
    }

    public void setDonator(boolean donator)
    {
        this.donator = donator;
    }

    public boolean isMember()
    {
        return member;
    }

    public void setMember(boolean member)
    {
        this.member = member;
    }

    public boolean isTeleportBlock()
    {
        return tpblock;
    }

    public void setTeleportBlock(boolean tpblock)
    {
        this.tpblock = tpblock;
    }

    public boolean isGuard()
    {
        return guard;
    }

    public void setGuard(boolean guard)
    {
        this.guard = guard;
    }

    public String getColor()
    {
        return color;
    }

    public boolean isSettler()
    {
        return settler;
    }

    public void setSettler(boolean settler)
    {
        this.settler = settler;
    }

    public boolean isWarned()
    {
        return warned;
    }

    public void setWarned(boolean warned)
    {
        this.warned = warned;
    }

    public boolean isHardWarned()
    {
        return hardwarned;
    }

    public void setHardWarned(boolean hardwarned)
    {
        this.hardwarned = hardwarned;
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
        return GOD_COLORS.get(godColor);
    }

    public void setGodColor(String godColor)
    {
        this.godColor = godColor;
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
    
}
