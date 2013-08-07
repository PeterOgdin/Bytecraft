package info.bytecraft.commands;

import java.sql.Connection;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import info.bytecraft.Bytecraft;
import info.bytecraft.api.BytecraftPlayer;
import info.bytecraft.database.DBWarpDAO;
import info.tregmine.database.ConnectionPool;

public class WarpCommand extends AbstractCommand
{

    public WarpCommand(Bytecraft instance)
    {
        super(instance, "warp");
    }

    public class WarpTask implements Runnable
    {
        private BytecraftPlayer player;
        private Location loc;

        public WarpTask(BytecraftPlayer player, Location loc)
        {
            this.loc = loc;
            this.player = player;
        }

        public void run()
        {
            player.teleport(loc);
        }

    }

    public boolean handlePlayer(BytecraftPlayer player, String[] args)
    {
        if (args.length != 1)
            return false;
        if (!player.isTrusted())
            return true;
        String warp = args[0];
        Connection conn = null;
        try {
            conn = ConnectionPool.getConnection();
            DBWarpDAO dbWarp = new DBWarpDAO(conn);
            Location loc = dbWarp.getWarp(warp, plugin.getServer());
            if (loc != null) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new WarpTask(player, loc), 40L);
                player.sendMessage(ChatColor.AQUA + "Teleporting to " + warp + " please wait");
            }
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

        return true;
    }

}
