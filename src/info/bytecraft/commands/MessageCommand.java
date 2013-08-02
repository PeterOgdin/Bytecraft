package info.bytecraft.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import info.bytecraft.Bytecraft;
import info.bytecraft.api.BytecraftPlayer;
import info.bytecraft.api.Notification;
import info.bytecraft.commands.AbstractCommand;

public class MessageCommand extends AbstractCommand
{

    public MessageCommand(Bytecraft instance)
    {
        super(instance, "message");
    }

    public boolean handlePlayer(BytecraftPlayer player, String[] args)
    {
        if (args.length >= 2) {
            Player delegate = Bukkit.getPlayer(args[0]);
            BytecraftPlayer target = plugin.getPlayer(delegate);
            if (!target.isOnline()) {
                player.sendNotification(Notification.COMMAND_FAIL);
                return true;
            }

            StringBuilder message = new StringBuilder();
            for (int i = 2; i < args.length; i++) {
                message.append(args[i] + " ");
            }

            target.sendMessage(ChatColor.GOLD + "<From> "
                    + player.getDisplayName() + ": " + ChatColor.GREEN
                    + message.toString().trim());
            if (!target.isInvisible()) {
                player.sendMessage(ChatColor.GOLD + "<To> "
                        + target.getDisplayName() + ": " + ChatColor.GREEN
                        + message.toString().trim());
            }
            target.sendNotification(Notification.MESSAGE);
        }
        return true;
    }

}
