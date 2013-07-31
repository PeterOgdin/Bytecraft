package info.bytecraft.database;

import info.bytecraft.api.BytecraftPlayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class DBPlayerDAO
{
    private Connection conn;

    public DBPlayerDAO(Connection conn)
    {
        this.conn = conn;
    }

    public BytecraftPlayer getPlayer(int id) throws SQLException
    {
        BytecraftPlayer player;

        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt =
                    conn.prepareStatement("SELECT * FROM player "
                            + "WHERE player_id = ?");
            stmt.setInt(1, id);
            stmt.execute();

            rs = stmt.getResultSet();
            if (!rs.next()) {
                return null;
            }

            player = new BytecraftPlayer(rs.getString("player_name"));
            player.setId(rs.getInt("player_id"));
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                }
            }
        }

        loadSettings(player);
        return player;
    }

    public BytecraftPlayer getPlayer(Player player) throws SQLException
    {
        return getPlayer(player.getName(), player);
    }

    public BytecraftPlayer getPlayer(String name) throws SQLException
    {
        return getPlayer(name, null);
    }

    public BytecraftPlayer getPlayer(String name, Player wrap)
            throws SQLException
    {
        BytecraftPlayer player;
        if (wrap != null) {
            player = new BytecraftPlayer(wrap);
        }
        else {
            player = new BytecraftPlayer(name);
        }

        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt =
                    conn.prepareStatement("SELECT * FROM player "
                            + "WHERE player_name = ?");
            stmt.setString(1, name);
            stmt.execute();

            rs = stmt.getResultSet();
            if (!rs.next()) {
                return null;
            }

            player.setId(rs.getInt("player_id"));
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                }
            }
        }

        loadSettings(player);

        return player;
    }

    private void loadSettings(BytecraftPlayer player) throws SQLException
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt =
                    conn.prepareStatement("SELECT * FROM player_property "
                            + "WHERE player_id = ?");
            stmt.setInt(1, player.getId());
            stmt.execute();

            rs = stmt.getResultSet();
            while (rs.next()) {
                String key = rs.getString("property_key");
                String value = rs.getString("property_value");
                if ("admin".equals(key)) {
                    player.setAdmin(Boolean.valueOf(value));
                }
                else if ("builder".equals(key)) {
                    player.setBuilder(Boolean.valueOf(value));
                }
                else if ("invisible".equals(key)) {
                    player.setInvisible(Boolean.valueOf(value));
                }
                else if ("donator".equals(key)) {
                    player.setDonator(Boolean.valueOf(value));
                }
                else if ("trusted".equals(key)) {
                    player.setTrusted(Boolean.valueOf(value));
                }
                else if ("member".equals(key)) {
                    player.setMember(Boolean.valueOf(value));
                }
                else if ("tpblock".equals(key)) {
                    player.setTeleportBlock(Boolean.valueOf(value));
                }
                else if ("guardian".equals(key)) {
                    player.setGuard(Boolean.valueOf(value));
                }
                else if ("color".equals(key)) {
                    player.setNameColor(value);
                }
                else if("god_color".equals(key)){
                    player.setGodColor(value);
                }
            }
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    public BytecraftPlayer createPlayer(Player wrap) throws SQLException
    {
        BytecraftPlayer player = new BytecraftPlayer(wrap);

        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "INSERT INTO player (player_name) VALUE (?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, player.getName());
            stmt.execute();

            stmt.executeQuery("SELECT LAST_INSERT_ID()");

            rs = stmt.getResultSet();
            if (!rs.next()) {
                throw new SQLException("Failed to get player id");
            }

            player.setId(rs.getInt(1));
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                }
            }
        }
        updateProperty(player, "color", "newcomer");
        updateProperty(player, "admin", false);
        updateProperty(player, "builder", false);
        updateProperty(player, "donator", false);
        updateProperty(player, "trusted", false);
        updateProperty(player, "member", false);
        updateProperty(player, "settler", false);
        updateProperty(player, "guard", false);
        updateProperty(player, "warned", false);
        updateProperty(player, "hard_warned", false);
        updateProperty(player, "tp_block", false);
        updateProperty(player, "invisible", false);
        updateProperty(player, "god_color", "red");
        return player;
    }

    public void updatePlayerPermissions(BytecraftPlayer player)
            throws SQLException
    {
        updateProperty(player, "admin", player.isAdmin());
        updateProperty(player, "builder", player.isBuilder());
        updateProperty(player, "donator", player.isDonator());
        updateProperty(player, "trusted", player.isTrusted());
        updateProperty(player, "member", player.isMember());
        updateProperty(player, "guard", player.isGuard());
        updateProperty(player, "settler", player.isSettler());
        updateProperty(player, "warned", player.isWarned());
        updateProperty(player, "hard_warned", player.isHardWarned());
    }

    public void updatePlayerInfo(BytecraftPlayer player) throws SQLException
    {
        updateProperty(player, "invisible", player.isInvisible());
        updateProperty(player, "tpblock", player.isTeleportBlock());
        updateProperty(player, "color", player.getColor());
    }

    public void updateProperty(BytecraftPlayer player, String key, boolean value)
            throws SQLException
    {
        updateProperty(player, key, String.valueOf(value));
    }

    public void updateProperty(BytecraftPlayer player, String key, String value)
            throws SQLException
    {
        if (value == null) {
            return;
        }

        PreparedStatement stmt = null;
        try {
            String sqlInsert =
                    "REPLACE INTO player_property (player_id, "
                            + "property_key, property_value) VALUE (?, ?, ?)";
            stmt = conn.prepareStatement(sqlInsert);
            stmt.setInt(1, player.getId());
            stmt.setString(2, key);
            stmt.setString(3, value);
            stmt.execute();

        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    public long balance(BytecraftPlayer player)
    {
        if (player == null) {
            throw new RuntimeException("Player can not be null");
        }

        PreparedStatement stm = null;
        try {
            stm =
                    conn.prepareStatement("SELECT * FROM player WHERE `player_id`=?");
            stm.setInt(1, player.getId());

            stm.execute();

            ResultSet rs = stm.getResultSet();

            if (rs.next()) {
                return rs.getInt("player_wallet");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return 0;
    }

    public boolean give(BytecraftPlayer player, long toAdd) throws SQLException
    {
        PreparedStatement stm = null;
        try {
            stm =
                    conn.prepareStatement("UPDATE player SET player_wallet = player_wallet + ? "
                            + "WHERE player_id = ?");
            stm.setLong(1, toAdd);
            stm.setInt(2, player.getId());

            stm.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    public boolean take(BytecraftPlayer player, long toTake)
            throws SQLException, IllegalArgumentException
    {
        if(balance(player) - toTake < 0){
            throw new IllegalArgumentException("Don't try to give more than you have");
        }
        PreparedStatement stm = null;
        try {
            stm =
                    conn.prepareStatement("UPDATE player SET player_wallet = player_wallet - ? WHERE player_id = ?");
            stm.setLong(1, toTake);
            stm.setInt(2, player.getId());
            stm.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    public String formattedBalance(BytecraftPlayer player)
    {
        NumberFormat nf = NumberFormat.getNumberInstance();
        return ChatColor.GOLD + nf.format(balance(player)) + ChatColor.AQUA
                + " bytes";
    }

}
