package Server;
import Server.Model.PlayerModel;
import java.sql.*;
import java.util.ArrayList;

public class DAO {
/*
        This is the Data Access Object. It will be used to collect and store the data objects in the
        database. It will manage a single connection that is opened and destroyed when needed.
 */
    private static final String URL = "jdbc:sqlite:database.db";
    private static Connection connection;

    private static void initializeDatabase(Connection connection) {
        try {
            String cmd = "CREATE TABLE IF NOT EXISTS players (" +
                    "username TEXT PRIMARY KEY," +
                    "hashed_password TEXT," +
                    "balance REAL);";
            connection.createStatement().executeUpdate(cmd);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Database Access
    private static Connection getConnection() throws SQLException
    {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL);

            initializeDatabase(connection);
        }

        return connection;
    }

    private static void closeConnection() throws SQLException {
        connection.close();
    }

    // CREATE NEW PLAYER
    public static void createNewPlayer(PlayerModel player) {
        String cmd = "INSERT INTO players (balance, username, hashed_password) VALUES (?, ?, ?);";
        try {
            connection = getConnection();
            PreparedStatement stmt = connection.prepareStatement(cmd);
            stmt.setFloat(1, player.getBalance());
            stmt.setString(2, player.getUsername());
            stmt.setString(3, player.getHash());
            stmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // GRAB A SPECIFIC PLAYER
    public static PlayerModel getPlayer(String username) {
        try {
            connection = getConnection();
            PlayerModel player = new PlayerModel();
            String cmd = "SELECT * FROM players WHERE username = ?;";
            PreparedStatement stmt = connection.prepareStatement(cmd);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

//            System.out.println(rs.toString());

            if (!rs.next())
                return null;

            player.setBalance(rs.getFloat("balance"));
            player.setUsername(rs.getString("username"));
            player.setHash(rs.getString("hashed_password"));

//            System.out.println(player.getUsername());
//            System.out.println(player.getHash());

            return player;
        } catch (SQLException ex) {
            return null;
        }
    }

    // GRAB TOP 3 LEADERBOARD
    public static ArrayList<PlayerModel> getTopThree() {
        try {
            connection = getConnection();
            ArrayList<PlayerModel> leaderboard = new ArrayList<>();

            String cmd = "SELECT username, balance FROM players ORDER BY balance DESC LIMIT 3;";
            PreparedStatement stmt = connection.prepareStatement(cmd);
            ResultSet rs = stmt.executeQuery();

//            System.out.println(rs.toString());

            while (rs.next()) {
                PlayerModel player = new PlayerModel();
                player.setUsername(rs.getString("username"));
                player.setBalance(Float.parseFloat(rs.getString("balance")));
                leaderboard.add(player);
            }

            return leaderboard;
        } catch (SQLException ex) {
            return null;
        }
    }

    // UPDATE PLAYER BALANCE
    public static void updatePlayerBalance(PlayerModel player, float newBalance) {
        String cmd = "UPDATE players SET balance = ? WHERE username = ?;";
        try {
            connection = getConnection();
            PreparedStatement stmt = connection.prepareStatement(cmd);

            stmt.setFloat(1, newBalance);
            stmt.setString(2, player.getUsername());
            stmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
