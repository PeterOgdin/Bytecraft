package info.bytecraft;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.bytecraft.api.BytecraftPlayer;
import info.bytecraft.commands.BlessCommand;
import info.bytecraft.commands.MessageCommand;
import info.bytecraft.commands.SayCommand;
import info.bytecraft.commands.UserCommand;
import info.bytecraft.commands.WalletCommand;
import info.bytecraft.commands.WarpCommand;
import info.bytecraft.commands.WhoCommand;
import info.bytecraft.database.DBPlayerDAO;
import info.bytecraft.listener.BlessListener;
import info.bytecraft.listener.BytecraftPlayerListener;
import info.bytecraft.listener.ChatListener;
import info.tregmine.database.ConnectionPool;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.Maps;

public class Bytecraft extends JavaPlugin
{

    private HashMap<String, BytecraftPlayer> players;

    public void onEnable()
    {
        players = Maps.newHashMap();

        for (Player delegate : Bukkit.getOnlinePlayers()) {
            addPlayer(delegate);
        }

        registerEvents();
        
        getCommand("bless").setExecutor(new BlessCommand(this));
        getCommand("god").setExecutor(new SayCommand(this, "god"));
        getCommand("message").setExecutor(new MessageCommand(this));
        getCommand("say").setExecutor(new SayCommand(this, "say"));
        getCommand("user").setExecutor(new UserCommand(this));
        getCommand("wallet").setExecutor(new WalletCommand(this));
        getCommand("warp").setExecutor(new WarpCommand(this));
        getCommand("who").setExecutor(new WhoCommand(this));
    }

    private void registerEvents()
    {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new ChatListener(this), this);
        pm.registerEvents(new BytecraftPlayerListener(this), this);
        pm.registerEvents(new BlessListener(this), this);
    }

    public BytecraftPlayer getPlayer(Player player)
    {
        return getPlayer(player.getName());
    }

    public BytecraftPlayer getPlayer(String name)
    {
        if(players.containsKey(name)){
            BytecraftPlayer player = players.get(name);
            player.setDisplayName(player.getNameColor() + player.getName());
            return player;
        }else{
            return addPlayer(Bukkit.getPlayer(name));
        }
    }

    public BytecraftPlayer addPlayer(Player srcPlayer)
    {
        if (players.containsKey(srcPlayer.getName())) {
            return players.get(srcPlayer.getName());
        }

        Connection conn = null;
        try {
            conn = ConnectionPool.getConnection();

            DBPlayerDAO playerDAO = new DBPlayerDAO(conn);
            BytecraftPlayer player = playerDAO.getPlayer(srcPlayer.getPlayer());

            if (player == null) {
                player = playerDAO.createPlayer(srcPlayer);
            }

            players.put(player.getName(), player);
            player.setDisplayName(player.getNameColor() + player.getName());
            return player;
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

    public void removePlayer(Player player)
    {
        this.players.remove(player.getName());
    }

    public List<BytecraftPlayer> getOnlinePlayers()
    {
        List<BytecraftPlayer> playersList = new ArrayList<BytecraftPlayer>();
        for (BytecraftPlayer player : players.values()) {
            playersList.add(player);
        }
        return playersList;
    }
}
