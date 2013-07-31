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
        if (!player.isAdmin() || !player.isGuard())
            return true;

        if (args.length == 3) {
            if ("make".equalsIgnoreCase(args[0])) {
                Player delegate = Bukkit.getPlayer(args[2]);
                if(delegate == null){
                    player.sendNotification(Notification.COMMAND_FAIL);
                    return true;
                }
                BytecraftPlayer target =
                        plugin.getPlayer(delegate);
                if (target.isOnline()) {
                    DBPlayerDAO dbPlayer = null;
                    Connection conn = null;
                    try {
                        conn = ConnectionPool.getConnection();
                        dbPlayer = new DBPlayerDAO(conn);

                        switch (args[1]) {
                        case "settler":
                            target.setSettler(true);
                            target.setTrusted(true);
                            target.setNameColor("settler");
                            plugin.getLogger().info(
                                    ChatColor.AQUA + "You have made "
                                            + target.getDisplayName()
                                            + " a settler");
                            target.sendMessage(ChatColor.AQUA
                                    + "You have been made a settler");
                            break;
                        case "warned":
                            target.setWarned(true);
                            player.sendMessage(ChatColor.RED
                                    + "You have warned "
                                    + target.getDisplayName());
                            target.sendMessage(ChatColor.RED
                                    + "You have been warned.");
                            target.setNameColor("warned");
                            break;
                        case "hardwarned":
                            target.setHardWarned(true);
                            player.sendMessage(ChatColor.RED
                                    + "You have warned "
                                    + target.getDisplayName());
                            target.sendMessage(ChatColor.RED
                                    + "You have been warned.");
                            target.setNameColor("warned");
                        }

                        dbPlayer.updatePlayerInfo(target);

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
                    target.setDisplayName(target.getNameColor()
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
                BytecraftPlayer target =
                        plugin.getPlayer(Bukkit.getPlayer(args[2]));
                if (target.isOnline()) {
                    DBPlayerDAO dbPlayer = null;
                    Connection conn = null;
                    try {
                        conn = ConnectionPool.getConnection();
                        dbPlayer = new DBPlayerDAO(conn);

                        switch (args[1]) {
                        case "settler":
                            target.setSettler(true);
                            target.setNameColor("settler");
                            plugin.getLogger().info(
                                    ChatColor.AQUA + "You have made "
                                            + target.getDisplayName()
                                            + " a settler");
                            target.sendMessage(ChatColor.AQUA
                                    + "You have been made a settler");
                            break;
                        case "warned":
                            target.setWarned(true);
                            server.getConsoleSender().sendMessage(
                                    ChatColor.RED + "You have warned "
                                            + target.getDisplayName());
                            target.sendMessage(ChatColor.RED
                                    + "You have been warned.");
                            target.setNameColor("warned");
                            break;
                        case "hardwarned":
                            target.setHardWarned(true);
                            server.getConsoleSender().sendMessage(
                                    ChatColor.RED + "You have warned "
                                            + target.getDisplayName());
                            target.sendMessage(ChatColor.RED
                                    + "You have been warned.");
                            target.setNameColor("warned");
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
                    target.setDisplayName(target.getNameColor()
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
