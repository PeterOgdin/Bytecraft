package info.bytecraft.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import info.bytecraft.Bytecraft;
import info.bytecraft.api.BytecraftPlayer;
import info.bytecraft.api.Notification;

public class BlessCommand extends AbstractCommand
{

    public BlessCommand(Bytecraft instance)
    {
        super(instance, "bless");
    }

    public boolean handlePlayer(BytecraftPlayer player, String[] args)
    {
        if(!player.isAdmin())return true;
        if(args.length != 1)return true;
        BytecraftPlayer target = plugin.getPlayer(Bukkit.getPlayer(args[0]));
        if(!target.isOnline()){
            player.sendNotification(Notification.COMMAND_FAIL);
            return true;
        }
        
        player.setBlessTarget(target);
        player.sendMessage(ChatColor.AQUA + "Preparing to bless a block for " + target.getDisplayName());
        return true;
    }
}
