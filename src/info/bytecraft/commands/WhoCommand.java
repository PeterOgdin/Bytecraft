package info.bytecraft.commands;

import static org.bukkit.ChatColor.*;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;

import info.bytecraft.Bytecraft;
import info.bytecraft.api.BytecraftPlayer;

public class WhoCommand extends AbstractCommand
{

    public WhoCommand(Bytecraft instance)
    {
        super(instance, "who");
    }

    public boolean handlePlayer(BytecraftPlayer player, String[] args)
    {
        if(args.length == 0){
            StringBuilder sb = new StringBuilder();
            String delim = "";
            int playerCounter = 0;
            for (BytecraftPlayer other : plugin.getOnlinePlayers()) {
                if (other.isInvisible()) {
                    continue;
                }
                sb.append(delim);
                sb.append(other.getDisplayName());
                delim = ChatColor.WHITE + ", ";
                playerCounter++;
            }
            player.sendMessage(GRAY + "************"
                    + DARK_PURPLE + "Player List" + GRAY
                    + "************");
            player.sendMessage(sb.toString().trim());
            player.sendMessage(GRAY + "************" + GOLD
                    + playerCounter + " player(s) online" + GRAY
                    + "*****");
        }else if(args.length == 1){
            if(!player.isAdmin())return true;
            BytecraftPlayer target = plugin.getPlayer(Bukkit.getPlayer(args[0]));
            if(target.isOnline()){
                double x = target.getLocation().getX();
                double y = target.getLocation().getY();
                double z = target.getLocation().getZ();
                
                player.sendMessage(DARK_GRAY + "******************** " + DARK_PURPLE +
                        "PLAYER INFO" + DARK_GRAY + " ********************");
                player.sendMessage(GOLD + "Player: " + target.getDisplayName());
                player.sendMessage(GOLD + "Id: " + GRAY + target.getId());
                player.sendMessage(GOLD + "World: " + GRAY + target.getWorld().getName());
                player.sendMessage(GOLD + "Location: " + GRAY + x + ", " + y + ", " + z);
                player.sendMessage(GOLD + "Channel: " + GRAY + target.getChatChannel());
                player.sendMessage(GOLD + "Wallet: " + target.getFormattedBalance());
                player.sendMessage(DARK_GRAY + "******************************************************");
            }
        }
        return true;
    }

    public boolean handleOther(Server server, String[] args)
    {
        ConsoleCommandSender sender = server.getConsoleSender();
        if(args.length == 0){
            StringBuilder sb = new StringBuilder();
            String delim = "";
            int playerCounter = 0;
            for (BytecraftPlayer other : plugin.getOnlinePlayers()) {
                sb.append(delim);
                sb.append(other.getDisplayName());
                delim = ChatColor.WHITE + ", ";
                playerCounter++;
            }
            sender.sendMessage(GRAY + "************"
                    + DARK_PURPLE + "Player List" + GRAY
                    + "************");
            sender.sendMessage(sb.toString().trim());
            sender.sendMessage(GRAY + "************" + GOLD
                    + playerCounter + " player(s) online" + GRAY
                    + "*****");
        }else if(args.length == 1){
            BytecraftPlayer target = plugin.getPlayer(Bukkit.getPlayer(args[0]));
            if(target.isOnline()){
                double x = target.getLocation().getX();
                double y = target.getLocation().getY();
                double z = target.getLocation().getZ();
                
                sender.sendMessage(DARK_GRAY + "******************** " + DARK_PURPLE +
                        "PLAYER INFO" + DARK_GRAY + " ********************");
                sender.sendMessage(GOLD + "sender: " + target.getDisplayName());
                sender.sendMessage(GOLD + "Id: " + GRAY + target.getId());
                sender.sendMessage(GOLD + "World: " + GRAY + target.getWorld().getName());
                sender.sendMessage(GOLD + "Location: " + GRAY + x + ", " + y + ", " + z);
                sender.sendMessage(GOLD + "Channel: " + GRAY + target.getChatChannel());
                sender.sendMessage(GOLD + "Wallet: " + target.getFormattedBalance());
                sender.sendMessage(DARK_GRAY + "******************************************************");
            }
        }
        return true;
    }

}
