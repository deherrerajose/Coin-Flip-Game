package Server;

public class GameManager {
/*
        This class will handle the coin flip logic and leaderboard rankings. It will use the `DAO` to
        update the players balance accordingly.
 */
    private int randomCoinFlip() {
        return (int) Math.floor(Math.random() * 2);
    }

    private int randomDiceRoll() {
        return (int) Math.floor(Math.random() * 6);
    }
}
