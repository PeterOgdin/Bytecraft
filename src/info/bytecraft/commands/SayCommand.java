package info.bytecraft.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;

import info.bytecraft.Bytecraft;
import info.bytecraft.api.BytecraftPlayer;

public class SayCommand extends AbstractCommand
{

    public SayCommand(Bytecraft instance, String command)
    {
        super(instance, command);
    }
    
    private String argsToMessage(String[] args)
    {
        StringBuffer buf = new StringBuffer();
        buf.append(args[0]);
        for (int i = 1; i < args.length; ++i) {
            buf.append(" ");
            buf.append(args[i]);
        }

        return buf.toString();
    }

    public boolean handlePlayer(BytecraftPlayer player, String[] args)
    {
        if(!player.isAdmin())return true;
        if(args.length == 0)return true;
        if ("say".equalsIgnoreCase(getCommand())) {
            Bukkit.broadcastMessage(player.getGodColor() + "<GOD> " + ChatColor.LIGHT_PURPLE + argsToMessage(args));
        }
        else if ("god".equalsIgnoreCase(getCommand())) {
            Bukkit.broadcastMessage(ChatColor.RED + "<GOD> " + ChatColor.LIGHT_PURPLE + argsToMessage(args));
        }
        return true;
    }

    public boolean handleOther(Server server, String[] args)
    {
        if(args.length == 0)return true;
        if ("say".equalsIgnoreCase(getCommand())) {
            Bukkit.broadcastMessage(ChatColor.BLUE + "<GOD> " + ChatColor.LIGHT_PURPLE + argsToMessage(args));
        }
        return true;
    }
}
