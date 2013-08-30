package info.bytecraft.commands;

import java.sql.Connection;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import info.bytecraft.Bytecraft;
import info.bytecraft.api.BytecraftPlayer;
import info.bytecraft.api.Rank;
import info.bytecraft.database.ConnectionPool;
import info.bytecraft.database.DBPlayerDAO;

public class UserCommand extends AbstractCommand
{

    public UserCommand(Bytecraft instance)
    {
        super(instance, "user");
    }

    public boolean handlePlayer(BytecraftPlayer player, String[] args)
    {
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
                            if(!player.isMentor())return true;
                            target.setRank(Rank.SETTLER);
                            dbPlayer.promoteToSettler(target);
                            target.setDisplayName(target.getRank().getColor()
                                    + target.getName());
                            player.sendMessage(ChatColor.AQUA + "You made "
                                    + target.getDisplayName() + ChatColor.AQUA
                                    + " a " + target.getRank().toString());
                            target.sendMessage(ChatColor.AQUA
                                    + "You have been made a "
                                    + target.getRank().toString());
                        }
                        else if (input.equalsIgnoreCase("member")) {
                            if(!player.isMentor())return true;
                            target.setRank(Rank.MEMBER);
                            target.setDisplayName(target.getRank().getColor()
                                    + target.getName());
                            player.sendMessage(ChatColor.AQUA + "You made "
                                    + target.getDisplayName() + ChatColor.AQUA
                                    + " a " + target.getRank().toString());
                            target.sendMessage(ChatColor.AQUA
                                    + "You have been made a "
                                    + target.getRank().toString());
                        }
                        else {
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
                            dbPlayer.promoteToSettler(target);
                            target.setDisplayName(target.getRank().getColor()
                                    + target.getName());
                            target.sendMessage(ChatColor.AQUA
                                    + "You have been made a "
                                    + target.getRank().toString());
                            plugin.getLogger().info(
                                    "You made " + target.getName() + " a "
                                            + target.getRank().toString());
                        }
                        else if (input.equalsIgnoreCase("member")) {
                            target.setRank(Rank.MEMBER);
                            target.setDisplayName(target.getRank().getColor()
                                    + target.getName());
                            target.sendMessage(ChatColor.AQUA
                                    + "You have been made a "
                                    + target.getRank().toString());
                            plugin.getLogger().info(
                                    "You made " + target.getName() + " a "
                                            + target.getRank().toString());
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
                }
                
            }
        }
        return true;
    }

}
