package info.bytecraft;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import info.bytecraft.api.BytecraftPlayer;
import info.bytecraft.api.PlayerBannedException;
import info.bytecraft.commands.*;
import info.bytecraft.database.DBPlayerDAO;
import info.bytecraft.listener.*;
import info.tregmine.database.ConnectionPool;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
            try {
                addPlayer(delegate);
            } catch (PlayerBannedException e) {}
        }

        registerEvents();
        
        getCommand("ban").setExecutor(new BanCommand(this));
        getCommand("bless").setExecutor(new BlessCommand(this));
        getCommand("creative").setExecutor(new GameModeCommand(this, "creative"));
        getCommand("channel").setExecutor(new ChannelCommand(this));
        getCommand("fill").setExecutor(new FillCommand(this));
        getCommand("gamemode").setExecutor(new GameModeCommand(this, "gamemode"));
        getCommand("give").setExecutor(new GiveCommand(this));
        getCommand("god").setExecutor(new SayCommand(this, "god"));
        getCommand("item").setExecutor(new ItemCommand(this));
        getCommand("kick").setExecutor(new KickCommand(this));
        getCommand("message").setExecutor(new MessageCommand(this));
        getCommand("say").setExecutor(new SayCommand(this, "say"));
        getCommand("summon").setExecutor(new SummonCommand(this));
        getCommand("survival").setExecutor(new GameModeCommand(this, "survival"));
        getCommand("teleport").setExecutor(new TeleportCommand(this));
        getCommand("user").setExecutor(new UserCommand(this));
        getCommand("vanish").setExecutor(new VanishCommand(this));
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
        pm.registerEvents(new FillListener(this), this);
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
            try {
                return addPlayer(Bukkit.getPlayer(name));
            } catch (PlayerBannedException e) {
            }
        }
        return null;
    }

    public BytecraftPlayer addPlayer(Player srcPlayer) throws PlayerBannedException
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
            
            if(playerDAO.isBanned(player)){
                throw new PlayerBannedException(ChatColor.RED + "You are not allowed on this server");
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
        Connection conn = null;
        DBPlayerDAO dbPlayer = null;
        
        try{
            conn = ConnectionPool.getConnection();
            dbPlayer = new DBPlayerDAO(conn);
            
            BytecraftPlayer bPlayer = getPlayer(player);
            dbPlayer.updatePlayerInfo(bPlayer);
        }catch(SQLException e){
            throw new RuntimeException(e);
        }finally{
            if(conn != null){
                try {
                    conn.close();
                } catch (SQLException e) {}
            }
        }
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
