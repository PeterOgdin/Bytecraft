package info.bytecraft.commands;

import java.sql.Connection;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import info.bytecraft.Bytecraft;
import info.bytecraft.api.BytecraftPlayer;
import info.bytecraft.api.Notification;
import info.bytecraft.api.Rank;
import info.bytecraft.database.DBPlayerDAO;
import info.tregmine.database.ConnectionPool;

public class UserCommand extends AbstractCommand
{

    public UserCommand(Bytecraft instance)
    {
        super(instance, "user");
    }

    public boolean handlePlayer(BytecraftPlayer player, String[] args)
    {
        if (!player.isMentor())
            return true;

        if (args.length == 3) {// user make settler player
            if ("make".equalsIgnoreCase(args[0])) {
                Player delegate = Bukkit.getPlayer(args[2]);
                if (delegate != null) {
                    BytecraftPlayer target = plugin.getPlayer(delegate);
                    DBPlayerDAO dbPlayer = null;
                    Connection conn = null;
                    try {
                        conn = ConnectionPool.getConnection();
                        dbPlayer = new DBPlayerDAO(conn);

                        String input = args[1].toLowerCase();
                        if (input.equalsIgnoreCase("settler")) {
                            target.setRank(Rank.SETTLER);
                            target.setDisplayName(target.getRank().getColor()
                                    + target.getName());
                            player.sendMessage(ChatColor.AQUA + "You made "
                                    + target.getDisplayName() + ChatColor.AQUA
                                    + " a " + target.getRank().toString());
                            target.sendMessage(ChatColor.AQUA + "You have been made a " + target.getRank().toString());
                        }
                        else if (input.equalsIgnoreCase("member")) {
                            target.setRank(Rank.MEMBER);
                            target.setDisplayName(target.getRank().getColor()
                                    + target.getName());
                            player.sendMessage(ChatColor.AQUA + "You made "
                                    + target.getDisplayName() + ChatColor.AQUA
                                    + " a " + target.getRank().toString());
                            target.sendMessage(ChatColor.AQUA + "You have been made a " + target.getRank().toString());
                        }else{
                            return true;
                        }

                        dbPlayer.updatePlayerInfo(target);
                        dbPlayer.updatePlayerPermissions(target);

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
                    target.setDisplayName(target.getRank().getColor()
                            + target.getName());
                }
                else {
                    player.sendMessage("No player found by the name of: "
                            + args[2]);
                    player.sendNotification(Notification.COMMAND_FAIL);
                }
            }
        }
        return true;
    }

    public boolean handleOther(Server server, String[] args)
    {
        if (args.length == 3) {
            if ("make".equalsIgnoreCase(args[0])) {
                Player delegate = Bukkit.getPlayer(args[2]);
                if (delegate != null) {
                    BytecraftPlayer target = plugin.getPlayer(delegate);
                    DBPlayerDAO dbPlayer = null;
                    Connection conn = null;
                    try {
                        conn = ConnectionPool.getConnection();
                        dbPlayer = new DBPlayerDAO(conn);

                        String input = args[1].toLowerCase();
                        if (input.equalsIgnoreCase("settler")) {
                            target.setRank(Rank.SETTLER);
                        }
                        else if (input.equalsIgnoreCase("member")) {
                            target.setRank(Rank.MEMBER);
                        }
                        target.setDisplayName(target.getRank().getColor()
                                + target.getName());
                        
                        target.sendMessage(ChatColor.AQUA + "You have been made a " + target.getRank().toString());
                        plugin.getLogger().info("You made " + target.getName() + " a " + target.getRank().toString());

                        dbPlayer.updatePlayerInfo(target);
                        dbPlayer.updatePlayerPermissions(target);

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
                    target.setDisplayName(target.getRank().getColor()
                            + target.getName());
                }
                else {
                    plugin.getLogger().info(
                            "No player found by the name of: " + args[2]);
                }
            }
        }
        return true;
    }

}
