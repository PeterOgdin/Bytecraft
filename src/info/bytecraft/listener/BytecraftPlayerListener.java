package info.bytecraft.listener;

import java.util.HashMap;

import info.bytecraft.Bytecraft;
import info.bytecraft.api.BytecraftPlayer;
import info.bytecraft.api.PlayerBannedException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Maps;

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
        try {
            plugin.addPlayer(event.getPlayer());
        } catch (PlayerBannedException e) {
            event.disallow(Result.KICK_BANNED, e.getMessage());
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
        event.setCancelled(player.isAdmin());
    }

    @EventHandler
    public void onKick(PlayerKickEvent event)
    {
        event.setLeaveMessage(null);
    }

    private HashMap<Item, BytecraftPlayer> droppedItems = Maps.newHashMap();

    @EventHandler
    public void onDrop(PlayerDropItemEvent event)
    {
        BytecraftPlayer player = plugin.getPlayer(event.getPlayer());
        droppedItems.put(event.getItemDrop(), player);
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent event)
    {
        if (!droppedItems.containsKey(event.getItem()))
            return;
        BytecraftPlayer player = plugin.getPlayer(event.getPlayer());
        BytecraftPlayer from = droppedItems.get(event.getItem());
        ItemStack stack = event.getItem().getItemStack();

        if (from != null && (from != player)) {
            player.sendMessage(ChatColor.YELLOW + "You got " + ChatColor.GOLD
                    + stack.getAmount() + " " + stack.getType()
                    + ChatColor.YELLOW + " from " + from.getDisplayName() + ".");
            from.sendMessage(ChatColor.YELLOW + "You gave "
                    + player.getDisplayName() + ChatColor.GOLD + " "
                    + stack.getAmount() + " "
                    + stack.getType().name().toLowerCase().replace("_", " "));
            droppedItems.remove(event.getItem());
        }
    }
}