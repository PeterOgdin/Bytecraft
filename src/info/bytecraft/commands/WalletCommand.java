package info.bytecraft.commands;

import java.sql.Connection;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import info.bytecraft.Bytecraft;
import info.bytecraft.api.BytecraftPlayer;
import info.bytecraft.database.DBPlayerDAO;
import info.tregmine.database.ConnectionPool;

public class WalletCommand extends AbstractCommand
{

    public WalletCommand(Bytecraft instance)
    {
        super(instance, "wallet");
    }

    public boolean handlePlayer(BytecraftPlayer player, String[] args)
    {
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("balance")) {
                player.sendMessage(ChatColor.AQUA + "You have "
                        + player.getFormattedBalance());
            }
            else if (args[0].equalsIgnoreCase("tell")) {
                Bukkit.broadcastMessage(player.getDisplayName()
                        + ChatColor.AQUA + " has "
                        + player.getFormattedBalance());
            }
        }
        else if (args.length == 3 && "give".equalsIgnoreCase(args[0])) {
            long amount = 0;
            BytecraftPlayer target =
                    plugin.getPlayer(Bukkit.getPlayer(args[1]));
            if (target.isOnline()) {
                try {
                    amount = Long.parseLong(args[2]);
                } catch (NumberFormatException e) {
                    player.sendMessage(ChatColor.RED + "Are you sure "
                            + ChatColor.BLUE + args[2] + ChatColor.RED
                            + " is a number?");
                }
                Connection conn = null;
                try {
                    conn = ConnectionPool.getConnection();
                    DBPlayerDAO dbPlayer = new DBPlayerDAO(conn);
                    if(amount > 0){
                        dbPlayer.take(player, amount);
                        dbPlayer.give(target, amount);
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (IllegalArgumentException e) {
                    player.sendMessage(ChatColor.RED + e.getMessage());
                } finally {
                    if (conn != null) {
                        try {
                            conn.close();
                        } catch (SQLException e) {
                        }
                    }
                }
            }
        }
        return true;
    }

}
