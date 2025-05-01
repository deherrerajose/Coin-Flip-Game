package Server;
import Server.Model.PlayerModel;
import java.sql.*;

public class DAO {
/*
        This is the Data Access Object. It will be used to collect and store the data objects in the
        database. It will manage a single connection that is opened and destroyed when needed.
 */
    private final String URL = "jdbc:sqlite:database.db";
    private Connection connection;

    public DAO() {
        try {
            connection = getConnection();
            String cmd = "CREATE TABLE IF NOT EXISTS players (" +
                    "id INTEGER PRIMARY KEY," +
                    "balance TEXT," +
                    "username INTEGER," +
                    "hashed_password TEXT);";
            connection.createStatement().executeUpdate(cmd);
            closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Database Access
    private Connection getConnection() throws SQLException
    {
        if (connection == null || connection.isClosed())
            connection = DriverManager.getConnection(URL);

        return connection;
    }

    private void closeConnection() throws SQLException {
        connection.close();
    }

    // CREATE NEW PLAYER
    public void createNewPlayer(PlayerModel player) {
        String cmd = "INSERT INTO players (balance, username, hashed_password) VALUES (?, ?, ?);";
        try {
            connection = getConnection();
            // No bueno... sql injections
//            connection.createStatement().executeUpdate(String.format("INSERT INTO students (name, age) VALUES ('%s',%d);", name, age));
            PreparedStatement stmt = connection.prepareStatement(cmd);
            stmt.setFloat(1, player.getBalance());
            stmt.setString(2, player.getUsername());
            stmt.setString(3, player.getHash());
            stmt.executeUpdate();

            closeConnection();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // GRAB A SPECIFIC PLAYER
    public PlayerModel getPlayer(String username) {
        try {
            connection = getConnection();
            PlayerModel player = new PlayerModel();
            String cmd = "SELECT * FROM students WHERE username = ?;";
            ResultSet rs = connection.createStatement().executeQuery(cmd);
            while(rs.next()){
                player.setPlayerID(rs.getInt("playerID"));
                player.setBalance(rs.getFloat("balance"));
                player.setUsername(rs.getString("username"));
                player.setHash(rs.getString("hashed_password"));
            }
            closeConnection();
            return player;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    // UPDATE PLAYER BALANCE
    public void updatePlayerBalance(PlayerModel player, float newBalance) {
        String cmd = "UPDATE players SET balance = ? WHERE playerID = ?;";
        try {
            connection = getConnection();
            PreparedStatement stmt = connection.prepareStatement(cmd);

            stmt.setFloat(1, newBalance);
            stmt.setInt(2, player.getPlayerID());
            stmt.executeUpdate();

            closeConnection();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
