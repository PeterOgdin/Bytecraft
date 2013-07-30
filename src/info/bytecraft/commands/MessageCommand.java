package info.bytecraft.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import info.bytecraft.Bytecraft;
import info.bytecraft.api.BytecraftPlayer;
import info.bytecraft.commands.AbstractCommand;

public class MessageCommand extends AbstractCommand
{

    public MessageCommand(Bytecraft instance)
    {
        super(instance, "message");
    }

    public boolean handlePlayer(BytecraftPlayer player, String[] args)
    {
        Player delegate = Bukkit.getPlayer(args[0]);
        BytecraftPlayer target = plugin.getPlayer(delegate);

        StringBuilder message = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            message.append(args[i] + " ");
        }

        target.sendMessage(ChatColor.GOLD + "<From>" + player.getDisplayName()
                + ": " + ChatColor.AQUA + message.toString().trim());
        if (!target.isInvisible()) {
            player.sendMessage(ChatColor.GOLD + "<To>"
                    + target.getDisplayName() + ": " + ChatColor.AQUA
                    + message.toString().trim());
        }
        target.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);

        return true;
    }

}
