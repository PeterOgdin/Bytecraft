package info.bytecraft.listener;

import info.bytecraft.Bytecraft;
import info.bytecraft.api.BytecraftPlayer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;

public class BytecraftPlayerListener implements Listener
{
    private Bytecraft plugin;

    public BytecraftPlayerListener(Bytecraft bytecraft)
    {
        this.plugin = bytecraft;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event)
    {
        event.setJoinMessage(null);
        BytecraftPlayer player = plugin.getPlayer(event.getPlayer());
        if (player.isAdmin() && player.isInvisible()) {
            for (BytecraftPlayer other : plugin.getOnlinePlayers()) {
                if (!other.isAdmin()) {
                    other.hidePlayer(player.getDelegate());
                }
                else {
                    other.sendMessage(player.getDisplayName() + ChatColor.RED
                            + " has joined invisible");
                }
            }
        }
        else {
            Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "Welcome "
                    + player.getDisplayName() + ChatColor.DARK_AQUA
                    + " to bytecraft!");
        }
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event)
    {
        BytecraftPlayer player = plugin.addPlayer(event.getPlayer());
        if (player == null) {
            event.disallow(Result.KICK_BANNED, "Please try again later");
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event)
    {
        event.setQuitMessage(plugin.getPlayer(event.getPlayer())
                .getDisplayName() + ChatColor.BLUE + " has left the game");
        plugin.removePlayer(event.getPlayer());
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event)
    {
        if (!(event.getEntity() instanceof Player))
            return;
        BytecraftPlayer player = plugin.getPlayer((Player) event.getEntity());
        if (player.isAdmin()) {
            event.setCancelled(true);
        }
    }
}