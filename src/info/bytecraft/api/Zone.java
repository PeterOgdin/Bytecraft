package info.bytecraft.api;

import java.sql.Connection;
import java.sql.SQLException;

import org.bukkit.Location;

import info.bytecraft.api.math.Point;
import info.bytecraft.api.math.Rectangle;
import info.bytecraft.database.ConnectionPool;
import info.bytecraft.database.DBZoneDAO;

public class Zone
{
    public static enum Permission {
        OWNER("%s is now an owner of %s.", "You are now an owner of %s.",
                "%s is no longer an owner of %s.",
                "You are no longer an owner of %s.",
                "You are an owner in this zone."),
        // can build in the zone
        MAKER("%s is now a maker in %s.", "You are now a maker in %s.",
                "%s is no longer a maker in %s.",
                "You are no longer a maker in %s.",
                "You are a maker in this zone."),
        // is allowed in the zone, if this isn't the default
        ALLOWED("%s is now allowed in %s.", "You are now allowed in %s.",
                "%s is no longer allowed in %s.",
                "You are no longer allowd in %s.",
                "You are allowed in this zone."),
        // banned from the zone
        BANNED("%s is now banned from %s.", "You have been banned from %s.",
                "%s is no longer banned in %s.",
                "You are no longer banned in %s.",
                "You are banned from this zone.");

        private String addedConfirm;
        private String addedNotif;
        private String delConfirm;
        private String delNotif;
        private String permNotification;

        private Permission(String addedConfirmation, String addedNotification,
                String delConfirmation, String delNotification,
                String permNotification)
        {
            this.addedConfirm = addedConfirmation;
            this.addedNotif = addedNotification;
            this.delConfirm = delConfirmation;
            this.delNotif = delNotification;
            this.setPermNotification(permNotification);
        }

        public String getAddedConfirm()
        {
            return addedConfirm;
        }

        public void setAddedConfirm(String addedConfirm)
        {
            this.addedConfirm = addedConfirm;
        }

        public String getAddedNotif()
        {
            return addedNotif;
        }

        public void setAddedNotif(String addedNotif)
        {
            this.addedNotif = addedNotif;
        }

        public String getDelConfirm()
        {
            return delConfirm;
        }

        public void setDelConfirm(String delConfirm)
        {
            this.delConfirm = delConfirm;
        }

        public String getDelNotif()
        {
            return delNotif;
        }

        public void setDelNotif(String delNotif)
        {
            this.delNotif = delNotif;
        }

        public String getPermNotification()
        {
            return permNotification;
        }

        public void setPermNotification(String permNotification)
        {
            this.permNotification = permNotification;
        }
    }

    private int id;
    private String name;
    private String world;

    private Rectangle rect;

    private boolean whitelisted;
    private boolean buildable;
    private boolean pvp;
    private boolean hostile;

    private String enterMsg;
    private String exitMsg;

    public Zone(String name)
    {
        this.name = name;
    }
    
    public Zone()
    {
        this("");
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Rectangle getRect()
    {
        return rect;
    }

    public void setRect(Rectangle rect)
    {
        this.rect = rect;
    }

    public boolean contains(Point p)
    {
        return rect.contains(p);
    }
    
    public boolean contains(Location loc)
    {
        return this.contains(new Point(loc.getBlockX(), loc.getBlockZ()));
    }

    public boolean isWhitelisted()
    {
        return whitelisted;
    }

    public void setWhitelisted(boolean whitelisted)
    {
        this.whitelisted = whitelisted;
    }

    public boolean isBuildable()
    {
        return buildable;
    }

    public void setBuildable(boolean buildable)
    {
        this.buildable = buildable;
    }

    public boolean isPvp()
    {
        return pvp;
    }

    public void setPvp(boolean pvp)
    {
        this.pvp = pvp;
    }

    public boolean isHostile()
    {
        return hostile;
    }

    public void setHostile(boolean hostile)
    {
        this.hostile = hostile;
    }

    public String getEnterMsg()
    {
        return enterMsg;
    }

    public void setEnterMsg(String enterMsg)
    {
        this.enterMsg = enterMsg;
    }

    public String getExitMsg()
    {
        return exitMsg;
    }

    public void setExitMsg(String exitMsg)
    {
        this.exitMsg = exitMsg;
    }

    public String getWorld()
    {
        return world;
    }

    public void setWorld(String world)
    {
        this.world = world;
    }

    public Permission getUser(BytecraftPlayer player)
    {
        Connection conn = null;
        try{
            conn = ConnectionPool.getConnection();
            return (new DBZoneDAO(conn)).getUser(this, player);
        }catch(SQLException e){
            throw new RuntimeException(e);
        }finally{
            if(conn != null){
                try {
                    conn.close();
                } catch (SQLException e) {}
            }
        }
    }
}
