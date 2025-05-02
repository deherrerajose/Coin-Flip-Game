package Server.Model;

public class PlayerModel {
/*
    This data object will hold the player information.
 */
    private float balance;
    private String username;
    private String hashed_password;

    public PlayerModel() {
        this.balance = 1000;
    }

    public PlayerModel(String username, String password) {
        this.username = username;
        this.balance = 1000;
        this.hashed_password = password;
    }

    // Getters & Setters for `balance`
    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    // Getters & Setters for `username`
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Getters & Setters for `hashed_password`
    public String getHash() {
        return hashed_password;
    }

    public void setHash(String hashed_password) {
        this.hashed_password = hashed_password;
    }
}
